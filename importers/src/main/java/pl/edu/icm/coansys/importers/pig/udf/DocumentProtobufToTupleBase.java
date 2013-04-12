/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.util.*;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.logicalLayer.schema.Schema.FieldSchema;
import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.KeyValue;
import pl.edu.icm.coansys.importers.models.DocumentProtos.MediaContainer;
import pl.edu.icm.coansys.importers.models.DocumentProtos.ReferenceMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.TextWithLanguage;

/**
 * 
 * @deprecated Replaced by {@link ProtobufToTuple}
 * @author akawa
 * @author acz
 */
@Deprecated
public abstract class DocumentProtobufToTupleBase extends EvalFunc<Tuple> {

    private static final Map<String, Integer> documentMetadataFieldNumberMap = new HashMap<String, Integer>() {

        {
            put("key", 0);
            put("basicMetadata", 1);
            put("documentAbstract", 2);
            put("keyword", 3);
            put("extId", 4);
            put("auxiliarInfo", 5);
            put("reference", 6);
            put("collection", 7);
            put("sourcePath", 8);
        }
    };
    private static final Map<String, Integer> basicMetadataFieldNumberMap = new HashMap<String, Integer>() {

        {
            put("title", 0);
            put("author", 1);
            put("doi", 2);
            put("journal", 3);
            put("isbn", 4);
            put("issn", 5);
            put("year", 6);
            put("issue", 7);
            put("volume", 8);
            put("pages", 9);
            put("classifCode", 10);
        }
    };
    private static final Map<String, Integer> authorFieldNumberMap = new HashMap<String, Integer>() {

        {
            put("key", 0);
            put("forenames", 1);
            put("surname", 2);
            put("name", 3);
            put("email", 4);
            put("affiliationRef", 5);
            put("docId", 6);
            put("positionNumber", 7);
            put("extId", 8);
            put("auxiliarInfo", 9);
        }
    };
    private static final Map<String, Integer> referenceMetadataFieldNumberMap = new HashMap<String, Integer>() {

        {
            put("basicMetadata", 0);
            put("sourceDocKey", 1);
            put("position", 2);
            put("rawCitationText", 3);
        }
    };

    static {
        for (int i = 0; i < documentMetadataFieldNumberMap.size(); i++) {
            assert (documentMetadataFieldNumberMap.containsValue(i));
        }
        for (int i = 0; i < basicMetadataFieldNumberMap.size(); i++) {
            assert (basicMetadataFieldNumberMap.containsValue(i));
        }
        for (int i = 0; i < authorFieldNumberMap.size(); i++) {
            assert (authorFieldNumberMap.containsValue(i));
        }
        for (int i = 0; i < referenceMetadataFieldNumberMap.size(); i++) {
            assert (referenceMetadataFieldNumberMap.containsValue(i));
        }
    }

    @Override
    public Schema outputSchema(Schema input) {
        try {
            //partial schemas:

            //KeyValue schema
            Schema keyValueSchema = new Schema(Arrays.asList(
                    new FieldSchema("key", DataType.CHARARRAY),
                    new FieldSchema("value", DataType.CHARARRAY),
                    new FieldSchema("comment", DataType.CHARARRAY)));

            //TextWithLanguage schema
            Schema textWithLanguageSchema = new Schema(Arrays.asList(
                    new FieldSchema("text", DataType.CHARARRAY),
                    new FieldSchema("language", DataType.CHARARRAY),
                    new FieldSchema("comment", DataType.CHARARRAY)));

            //keywords
            Schema keywordSchema = new Schema(new FieldSchema("keyword", textWithLanguageSchema, DataType.TUPLE));

            //titles
            Schema titleSchema = new Schema(new FieldSchema("title", textWithLanguageSchema, DataType.TUPLE));

            //abstracts
            Schema documentAbstractSchema = new Schema(new FieldSchema("documentAbstract", textWithLanguageSchema, DataType.TUPLE));

            //extIds
            Schema extIdSchema = new Schema(new FieldSchema("extId", keyValueSchema, DataType.TUPLE));

            //classifCodes
            Schema valueInnerSchema = new Schema(
                    new FieldSchema("value", DataType.CHARARRAY));
            Schema valueSchema = new Schema(
                    new FieldSchema("value", valueInnerSchema, DataType.TUPLE));
            Schema classifCodeInnerSchema = new Schema(Arrays.asList(
                    new FieldSchema("source", DataType.CHARARRAY),
                    new FieldSchema("value", valueSchema, DataType.BAG)));
            Schema classifCodeSchema = new Schema(new FieldSchema("classifCode", classifCodeInnerSchema, DataType.TUPLE));

            //auxiliarInfos
            Schema auxiliarSchema = new Schema(new FieldSchema("auxiliarInfo", keyValueSchema, DataType.TUPLE));

            //affiliationRefs
            Schema affiliationRefTupleSchema = new Schema(Arrays.asList(
                    new FieldSchema("key", DataType.CHARARRAY),
                    new FieldSchema("affiliationId", DataType.CHARARRAY)));
            Schema affiliationRefSchema = new Schema(new FieldSchema("affiliationRef", affiliationRefTupleSchema, DataType.TUPLE));


            //authors
            Schema authorInnerSchema = new Schema(Arrays.asList(
                    new FieldSchema("key", DataType.CHARARRAY),
                    new FieldSchema("forenames", DataType.CHARARRAY),
                    new FieldSchema("surname", DataType.CHARARRAY),
                    new FieldSchema("name", DataType.CHARARRAY),
                    new FieldSchema("email", DataType.CHARARRAY),
                    new FieldSchema("affiliationRef", affiliationRefSchema, DataType.BAG),
                    new FieldSchema("docId", DataType.CHARARRAY),
                    new FieldSchema("positionNumber", DataType.INTEGER),
                    new FieldSchema("extId", extIdSchema, DataType.BAG),
                    new FieldSchema("auxiliarInfo", auxiliarSchema, DataType.BAG)));
            Schema authorSchema = new Schema(new FieldSchema("author", authorInnerSchema, DataType.TUPLE));


            //basic metadata
            Schema basicMetadataSchema = new Schema(Arrays.asList(
                    new FieldSchema("title", titleSchema, DataType.BAG),
                    new FieldSchema("author", authorSchema, DataType.BAG),
                    new FieldSchema("doi", DataType.CHARARRAY),
                    new FieldSchema("journal", DataType.CHARARRAY),
                    new FieldSchema("isbn", DataType.CHARARRAY),
                    new FieldSchema("issn", DataType.CHARARRAY),
                    new FieldSchema("year", DataType.CHARARRAY),
                    new FieldSchema("issue", DataType.CHARARRAY),
                    new FieldSchema("volume", DataType.CHARARRAY),
                    new FieldSchema("pages", DataType.CHARARRAY),
                    new FieldSchema("classifCode", classifCodeSchema, DataType.BAG)));

            //references
            Schema referenceInnerSchema = new Schema(Arrays.asList(
                    new FieldSchema("basicMetadata", basicMetadataSchema, DataType.TUPLE),
                    new FieldSchema("sourceDocKey", DataType.CHARARRAY),
                    new FieldSchema("position", DataType.INTEGER),
                    new FieldSchema("rawCitationText", DataType.CHARARRAY)));
            Schema referenceSchema = new Schema(new FieldSchema("reference", referenceInnerSchema, DataType.TUPLE));

            //DocumentMetadata inner schema
            Schema documentMetadataSchema = new Schema(Arrays.asList(
                    new FieldSchema("key", DataType.CHARARRAY),
                    new FieldSchema("basicMetadata", basicMetadataSchema, DataType.TUPLE),
                    new FieldSchema("documentAbstract", documentAbstractSchema, DataType.BAG),
                    new FieldSchema("keyword", keywordSchema, DataType.BAG),
                    new FieldSchema("extId", extIdSchema, DataType.BAG),
                    new FieldSchema("auxiliarInfo", auxiliarSchema, DataType.BAG),
                    new FieldSchema("reference", referenceSchema, DataType.BAG),
                    new FieldSchema("collection", DataType.CHARARRAY),
                    new FieldSchema("sourcePath", DataType.CHARARRAY)));

            //main schema (DocumentMetadata) with single field of type TUPLE
            return new Schema(new FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input),
                    documentMetadataSchema, DataType.TUPLE));
        } catch (Exception e) {
            return null;
        }
    }

    private static DataBag listsToDataBag(List... lists) throws ExecException {
        return listsTableToDataBag(lists, lists.length);
    }

    //private <T> DataBag listsToDataBag(List<T>... lists) throws ExecException {
    private static DataBag listsTableToDataBag(List[] lists, int length) throws ExecException {
        DataBag output = null;

        if (length > 0) {
            int minListLength = Integer.MAX_VALUE;
            for (int l = 0; l < length; l++) {
                if (lists[l] == null) {
                    minListLength = 0;
                    break;
                } else {
                    minListLength = Math.min(minListLength, lists[l].size());
                }
            }

            if (minListLength > 0) {
                output = BagFactory.getInstance().newDefaultBag();
                for (int i = 0; i < minListLength; i++) {
                    Tuple t = TupleFactory.getInstance().newTuple(length);
                    for (int l = 0; l < length; l++) {
                        t.set(l, lists[l].get(i));
                    }
                    output.add(t);
                }
            }
        }

        return output;
    }
    
    private static Tuple basicMetadataToTuple(BasicMetadata bm) throws ExecException {
        final int HELPER_LISTS = 20;

        Tuple bmTuple = TupleFactory.getInstance().newTuple(basicMetadataFieldNumberMap.size());

        //simple fields of bmTuple
        bmTuple.set(basicMetadataFieldNumberMap.get("doi"), bm.hasDoi() ? bm.getDoi() : null);
        bmTuple.set(basicMetadataFieldNumberMap.get("journal"), bm.hasJournal() ? bm.getJournal() : null);
        bmTuple.set(basicMetadataFieldNumberMap.get("isbn"), bm.hasIsbn() ? bm.getIsbn() : null);
        bmTuple.set(basicMetadataFieldNumberMap.get("issn"), bm.hasIssn() ? bm.getIssn() : null);
        bmTuple.set(basicMetadataFieldNumberMap.get("year"), bm.hasYear() ? bm.getYear() : null);
        bmTuple.set(basicMetadataFieldNumberMap.get("issue"), bm.hasIssue() ? bm.getIssue() : null);
        bmTuple.set(basicMetadataFieldNumberMap.get("volume"), bm.hasVolume() ? bm.getVolume() : null);
        bmTuple.set(basicMetadataFieldNumberMap.get("pages"), bm.hasPages() ? bm.getPages() : null);

        //complex fields
        List[] helperLists = new List[HELPER_LISTS];
        int helperIndex1 = authorFieldNumberMap.size() + 1;
        int helperIndex2 = helperIndex1 + 1;
        int helperIndex3 = helperIndex2 + 1;

        for (int i = 0; i < HELPER_LISTS; i++) {
            helperLists[i] = new ArrayList<Object>();
        }

        //titles
        for (int i = 0; i < HELPER_LISTS; i++) {
            helperLists[i].clear();
        }
        for (TextWithLanguage title : bm.getTitleList()) {
            helperLists[0].add(title.getText());
            helperLists[1].add(title.hasLanguage() ? title.getLanguage() : null);
            helperLists[2].add(title.hasComment() ? title.getComment() : null);
        }
        bmTuple.set(basicMetadataFieldNumberMap.get("title"), listsTableToDataBag(helperLists, 3));

        //authors
        for (int i = 0; i < HELPER_LISTS; i++) {
            helperLists[i].clear();
        }

        for (Author author : bm.getAuthorList()) {

            //simple fields
            helperLists[authorFieldNumberMap.get("key")].add(author.getKey());
            helperLists[authorFieldNumberMap.get("forenames")].add(author.hasForenames() ? author.getForenames() : null);
            helperLists[authorFieldNumberMap.get("surname")].add(author.hasSurname() ? author.getSurname() : null);
            helperLists[authorFieldNumberMap.get("name")].add(author.hasName() ? author.getName() : null);
            helperLists[authorFieldNumberMap.get("email")].add(author.hasEmail() ? author.getEmail() : null);
            helperLists[authorFieldNumberMap.get("docId")].add(author.hasDocId() ? author.getDocId() : null);
            helperLists[authorFieldNumberMap.get("positionNumber")].add(author.hasPositionNumber() ? author.getPositionNumber() : null);

            //affiliationRefs
            helperLists[helperIndex1].clear();
            helperLists[helperIndex2].clear();
            helperLists[helperIndex3].clear();
            for (KeyValue affiliationRef : author.getAffiliationRefList()) {
                helperLists[helperIndex1].add(affiliationRef.getKey());
                helperLists[helperIndex2].add(affiliationRef.getValue());
                helperLists[helperIndex3].add(affiliationRef.hasComment() ? affiliationRef.getComment() : null);
            }
            helperLists[authorFieldNumberMap.get("affiliationRef")].add(listsToDataBag(helperLists[helperIndex1],
                    helperLists[helperIndex2], helperLists[helperIndex3]));

            //extIds
            helperLists[helperIndex1].clear();
            helperLists[helperIndex2].clear();
            helperLists[helperIndex3].clear();
            for (KeyValue extId : author.getExtIdList()) {
                helperLists[helperIndex1].add(extId.getKey());
                helperLists[helperIndex2].add(extId.getValue());
                helperLists[helperIndex3].add(extId.hasComment() ? extId.getComment() : null);
            }
            helperLists[authorFieldNumberMap.get("extId")].add(listsToDataBag(helperLists[helperIndex1],
                    helperLists[helperIndex2], helperLists[helperIndex3]));

            //auxiliarInfos
            helperLists[helperIndex1].clear();
            helperLists[helperIndex2].clear();
            helperLists[helperIndex3].clear();
            for (KeyValue aux : author.getAuxiliarInfoList()) {
                helperLists[helperIndex1].add(aux.getKey());
                helperLists[helperIndex2].add(aux.getValue());
                helperLists[helperIndex3].add(aux.hasComment() ? aux.getComment() : null);
            }
            helperLists[authorFieldNumberMap.get("auxiliarInfo")].add(listsToDataBag(helperLists[helperIndex1],
                    helperLists[helperIndex2], helperLists[helperIndex3]));
        }
        bmTuple.set(basicMetadataFieldNumberMap.get("author"), listsTableToDataBag(helperLists, authorFieldNumberMap.size()));
        //authors end

        //classifCodes
        for (int i = 0; i < HELPER_LISTS; i++) {
            helperLists[i].clear();
        }
        for (DocumentProtos.ClassifCode classifCode : bm.getClassifCodeList()) {
            helperLists[0].add(classifCode.getSource());

            helperLists[helperIndex1].clear();
            for (String code : classifCode.getValueList()) {
                helperLists[helperIndex1].add(code);
            }
            helperLists[1].add(listsToDataBag(helperLists[helperIndex1]));
        }
        bmTuple.set(basicMetadataFieldNumberMap.get("classifCode"), listsTableToDataBag(helperLists, 2));

        return bmTuple;
    }

    private static Tuple addDocumentMetatdataFields(DocumentMetadata metadata, Tuple output) throws ExecException {

        //simple fields
        output.set(documentMetadataFieldNumberMap.get("key"), metadata.getKey());
        output.set(documentMetadataFieldNumberMap.get("collection"), metadata.hasCollection() ? metadata.getCollection() : null);
        output.set(documentMetadataFieldNumberMap.get("sourcePath"), metadata.hasSourcePath() ? metadata.getSourcePath() : null);

        //complex fields

        List helperList1 = new ArrayList<Object>();
        List helperList2 = new ArrayList<Object>();
        List helperList3 = new ArrayList<Object>();
        List helperList4 = new ArrayList<Object>();

        //basicMetadata
        output.set(documentMetadataFieldNumberMap.get("basicMetadata"), basicMetadataToTuple(metadata.getBasicMetadata()));

        //abstracts
        helperList1.clear();
        helperList2.clear();
        helperList3.clear();
        for (TextWithLanguage docAbstract : metadata.getDocumentAbstractList()) {
            helperList1.add(docAbstract.getText());
            helperList2.add(docAbstract.hasLanguage() ? docAbstract.getLanguage() : null);
            helperList3.add(docAbstract.hasComment() ? docAbstract.getComment() : null);
        }
        output.set(documentMetadataFieldNumberMap.get("documentAbstract"), listsToDataBag(helperList1, helperList2, helperList3));

        //keywords
        helperList1.clear();
        helperList2.clear();
        helperList3.clear();
        for (TextWithLanguage keyword : metadata.getKeywordList()) {
            helperList1.add(keyword.getText());
            helperList2.add(keyword.hasLanguage() ? keyword.getLanguage() : null);
            helperList3.add(keyword.hasComment() ? keyword.getComment() : null);
        }
        output.set(documentMetadataFieldNumberMap.get("keyword"), listsToDataBag(helperList1, helperList2, helperList3));

        //extIds
        helperList1.clear();
        helperList2.clear();
        helperList3.clear();
        for (KeyValue extId : metadata.getExtIdList()) {
            helperList1.add(extId.getKey());
            helperList2.add(extId.getValue());
            helperList3.add(extId.hasComment() ? extId.getComment() : null);
        }
        output.set(documentMetadataFieldNumberMap.get("extId"), listsToDataBag(helperList1, helperList2, helperList3));

        //auxiliarInfos
        helperList1.clear();
        helperList2.clear();
        helperList3.clear();
        for (KeyValue auxiliar : metadata.getAuxiliarInfoList()) {
            helperList1.add(auxiliar.getKey());
            helperList2.add(auxiliar.getValue());
            helperList3.add(auxiliar.hasComment() ? auxiliar.getComment() : null);
        }
        output.set(documentMetadataFieldNumberMap.get("auxiliarInfo"), listsToDataBag(helperList1, helperList2, helperList3));

        //references
        helperList1.clear();
        helperList2.clear();
        helperList3.clear();
        helperList4.clear();
        for (ReferenceMetadata reference : metadata.getReferenceList()) {
            helperList1.add(basicMetadataToTuple(reference.getBasicMetadata()));
            helperList2.add(reference.hasSourceDocKey() ? reference.getSourceDocKey() : null);
            helperList3.add(reference.hasPosition() ? reference.getPosition() : null);
            helperList4.add(reference.hasRawCitationText() ? reference.getRawCitationText() : null);
        }
        output.set(documentMetadataFieldNumberMap.get("reference"), listsToDataBag(helperList1, helperList2, helperList3, helperList4));


        return output;
    }

    public abstract DocumentMetadata getDocumentMetadata(Tuple input) throws ExecException, InvalidProtocolBufferException;

    public abstract MediaContainer getDocumentMedia(Tuple input) throws ExecException, InvalidProtocolBufferException;

    @Override
    public Tuple exec(Tuple input) throws IOException {

        Tuple output = TupleFactory.getInstance().newTuple(documentMetadataFieldNumberMap.size());
        DocumentMetadata metadata = getDocumentMetadata(input);
        output = addDocumentMetatdataFields(metadata, output);
        //if (input.size() > 1) {
//            MediaContainer media = getDocumentMedia(input);
        //}
        return output;
    }
}