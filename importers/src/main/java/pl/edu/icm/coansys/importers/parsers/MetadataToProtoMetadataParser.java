/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.importers.ZipArchive;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.KeyValue;
import pl.edu.icm.coansys.importers.models.DocumentProtos.ReferenceMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.TextWithLanguage;
import pl.edu.icm.model.bwmeta.*;
import pl.edu.icm.model.bwmeta.constants.YConstants;
import pl.edu.icm.model.bwmeta.constants.YaddaIdConstants;
import pl.edu.icm.model.bwmeta.transformers.BwmetaTransformerConstants;
import pl.edu.icm.model.general.MetadataTransformers;
import pl.edu.icm.model.transformers.MetadataFormat;
import pl.edu.icm.model.transformers.MetadataReader;
import pl.edu.icm.model.transformers.TransformationException;

/**
 *
 * @author piotrw
 * @author pdendek
 * @author acz
 */
public class MetadataToProtoMetadataParser {

    public enum MetadataType {

        BWMETA, OAI_DC, DMF
    }
    private static final Logger log = LoggerFactory.getLogger(MetadataToProtoMetadataParser.class);

    private MetadataToProtoMetadataParser() {
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        InputStreamReader input = new InputStreamReader(is, "UTF-8");
        final int CHARS_PER_PAGE = 50000; //counting spaces
        final char[] buffer = new char[CHARS_PER_PAGE];
        StringBuilder output = new StringBuilder(CHARS_PER_PAGE);
        try {
            for (int read = input.read(buffer, 0, buffer.length);
                    read != -1;
                    read = input.read(buffer, 0, buffer.length)) {
                output.append(buffer, 0, read);
            }
        } catch (IOException ignore) {
        }

        return output.toString();
    }

    private static Map<String, MetadataFormat> getSupportedBwmetaTypes() {
        // Supported bwmeta formats
        Map<String, MetadataFormat> result = new HashMap<String, MetadataFormat>();
        result.put("http://yadda.icm.edu.pl/bwmeta-1.2.0.xsd", BwmetaTransformerConstants.BWMETA_1_2);
        result.put("http://yadda.icm.edu.pl/bwmeta-2.0.0.xsd", BwmetaTransformerConstants.BWMETA_2_0);
        result.put("http://yadda.icm.edu.pl/bwmeta-2.1.0.xsd", BwmetaTransformerConstants.BWMETA_2_1);

        return result;
    }

    public static List<YExportable> streamToYExportable(InputStream stream, MetadataType type) throws TransformationException, IOException {
        return stringToYExportable(convertStreamToString(stream), type);
    }

    private static List<YExportable> stringToYExportable(String data, MetadataType type) throws TransformationException, IOException {
        List<YExportable> result = null;

        switch (type) {
            case BWMETA:
                for (Map.Entry<String, MetadataFormat> bwtype : getSupportedBwmetaTypes().entrySet()) {
                    if (data.contains(bwtype.getKey())) {
                        result = stringToYExportable(data, bwtype.getValue());
                        if (result != null) {
                            break;
                        }
                    }
                }
                break;
            case OAI_DC:
                result = stringToYExportable(data, BwmetaTransformerConstants.OAI_DUBLIN_CORE_2_0);
                break;
            case DMF:
                result = stringToYExportable(data, BwmetaTransformerConstants.DMF);
                break;

        }

        return result;
    }

    private static List<YExportable> stringToYExportable(String data, MetadataFormat format) throws TransformationException {
        MetadataReader<YExportable> reader = MetadataTransformers.BTF.getReader(format,
                BwmetaTransformerConstants.Y);
        List<YExportable> inputElements = reader.read(data);
        return inputElements;

    }

    /**
     * Converts the YContributor object coming from a YElement
     *
     * @param yContributor from YElement
     * @return protocol buffers builder for Author
     */
    private static Author.Builder ycontributorToAuthorMetadata(YContributor yContributor) {
        Author.Builder authorBuilder = Author.newBuilder();

        //identifier of relating YPerson object
        String identity = yContributor.getIdentity();
        if (identity != null && !identity.trim().isEmpty()) {
            KeyValue.Builder extId = KeyValue.newBuilder();
            extId.setKey(ProtoConstants.authorExtIdBwmeta);
            extId.setValue(identity);
            authorBuilder.addExtId(extId);
        }

        List<YName> names = yContributor.getNames();
        for (YName yName : names) {
            String type = yName.getType();
            if ("canonical".equals(type)) {
                authorBuilder.setName(yName.getText());
            } else if ("forenames".equals(type)) {
                authorBuilder.setForenames(yName.getText());
            } else if ("surname".equals(type)) {
                authorBuilder.setSurname(yName.getText());
            } else { //type not set
                authorBuilder.setName(yName.getText());
            }
        }

        /*
         * Contributors' attributes could be contact informations like e-mail,
         * phone number etc, identifiers from external databases...
         */
        List<YAttribute> attrs = yContributor.getAttributes();
        for (YAttribute yAttribute : attrs) {
            String key = yAttribute.getKey();
            if (key.equals(YConstants.AT_CONTACT_EMAIL)) {
                if (yAttribute.getValue() != null) {
                    authorBuilder.setEmail(yAttribute.getValue());
                }
            } else if (key.equals(YConstants.AT_ZBL_AUTHOR_FINGERPRINT)) {
                if (yAttribute.getValue() != null) {
                    KeyValue.Builder extId = KeyValue.newBuilder();
                    extId.setKey(ProtoConstants.authorExtIdZbl);
                    extId.setValue(yAttribute.getValue());
                    authorBuilder.addExtId(extId.build());
                }
                // else if ... are there another external identifiers?
            } else {
                String authorIdentity = yAttribute.getValue();
                if (authorIdentity != null && !authorIdentity.trim().isEmpty()) {
                    KeyValue.Builder aux = KeyValue.newBuilder();
                    aux.setKey(key);
                    aux.setValue(authorIdentity);
                    authorBuilder.addAuxiliarInfo(aux);
                }
            }
        }
//        authorBuilder.setType(HBaseConstants.T_AUTHOR_COPY);
        return authorBuilder;
    }

    /**
     * Author from parsed reference
     *
     * @param yAttribute coming from parsed reference
     * @return protocol buffers builder for Author
     */
    private static Author.Builder yattributeToAuthorMetadata(YAttribute yAttribute) {
        Author.Builder author = Author.newBuilder();
//      author.setType(HBaseConstants.T_AUTHOR_COPY);
        String content;
        if ((content = yAttribute.getValue()) != null) {
            author.setName(content);
        }
        if ((content = yAttribute.getOneAttributeSimpleValue("reference-parsed-author-forenames")) != null) {
            author.setForenames(content);
        }
        if ((content = yAttribute.getOneAttributeSimpleValue("reference-parsed-author-surname")) != null) {
            author.setSurname(content);
        }
        if ((content = yAttribute.getOneAttributeSimpleValue("zbl.author-fingerprint")) != null) {
            KeyValue.Builder extId = KeyValue.newBuilder();
            extId.setKey(ProtoConstants.authorExtIdZbl);
            extId.setValue(content);
            author.addExtId(extId.build());
        }
        return author;
    }

    /**
     * ReferenceMetadata from YRelation
     *
     * @param item
     * @return protocol buffers builder for ReferenceMetadata
     */
    private static ReferenceMetadata.Builder yrelationToReferenceMetadata(YRelation item) {
        ReferenceMetadata.Builder reference = ReferenceMetadata.newBuilder();
        BasicMetadata.Builder basicMetadata = BasicMetadata.newBuilder();

        ////// Parse YAttributes

        //pareparation -- initialisation of some objects to be filled while parsing YAttributes:
        List<Author.Builder> authorBuilders = new ArrayList<Author.Builder>();
        int authorNumber = 0;
        ClassifCode.Builder mscCodes = ClassifCode.newBuilder();
        mscCodes.setSource(ProtoConstants.documentClassifCodeMsc);
        ClassifCode.Builder pacsCodes = ClassifCode.newBuilder();
        pacsCodes.setSource(ProtoConstants.documentClassifCodePacs);

        //parsing:
        for (YAttribute yAttr : item.getAttributes()) {
            if (yAttr != null) {
                if (yAttr.getKey().equals("reference-text")) {
                    reference.setRawCitationText(yAttr.getValue());
                } else if (yAttr.getKey().equals("reference-number")) {
                    Double refPos;
                    try {
                        refPos = Double.parseDouble(yAttr.getValue());
                        reference.setPosition((int) (double) refPos);
                    } catch (NumberFormatException ex) {
                        //don't set anything
                    }
                } else if (yAttr.getKey().equals("reference-parsed-title")) {
                    TextWithLanguage.Builder title = TextWithLanguage.newBuilder();
                    title.setText(yAttr.getValue());
                    basicMetadata.addTitle(title);
                } else if (yAttr.getKey().equals("reference-parsed-journal")) {
                    basicMetadata.setJournal(yAttr.getValue());
                } else if (yAttr.getKey().equals("reference-parsed-volume")) {
                    basicMetadata.setVolume(yAttr.getValue());
                } else if (yAttr.getKey().equals("reference-parsed-issue")) {
                    basicMetadata.setIssue(yAttr.getValue());
                } else if (yAttr.getKey().equals("reference-parsed-pages")) {
                    basicMetadata.setPages(yAttr.getValue());
                } else if (yAttr.getKey().equals(YaddaIdConstants.CATEGORY_CLASS_MSC)) {
                    mscCodes.addValue(yAttr.getValue());
                    //TODO czesc kodow MSC mylnie trafia do kwordow - mozna je stamtad wyciagnac porownujac z wzorcem kodu
                } else if (yAttr.getKey().equals(YaddaIdConstants.CATEGORY_CLASS_PACS)) {
                    pacsCodes.addValue(yAttr.getValue());
                } else if (yAttr.getKey().equals("reference-parsed-author")) {
                    Author.Builder refAuthor = yattributeToAuthorMetadata(yAttr);
                    refAuthor.setPositionNumber(++authorNumber);
                    authorBuilders.add(refAuthor);
                }
            }
        }

        //finishing
        if (mscCodes.getValueCount() > 0) {
            basicMetadata.addClassifCode(mscCodes);
        }
        if (pacsCodes.getValueCount() > 0) {
            basicMetadata.addClassifCode(pacsCodes);
        }

        //docKey depends on authors sorted by name
        Collections.sort(authorBuilders, new AuthorsComparatorByName());
        String docKey = calculateDocKey(basicMetadata, authorBuilders, null);
        //In BasicMetadata authors are sorted by position, so re-sorting...
        Collections.sort(authorBuilders, new AuthorsComparatorByPosition());

        //some data in authorBuilders depends on docKey...
        for (int i = 0; i < authorBuilders.size(); i++) {
            Author.Builder authorBuilder = authorBuilders.get(i);
            authorBuilder.setDocId(docKey);
            setAuthorKey(authorBuilder);

            basicMetadata.addAuthor(authorBuilder);
        }

        reference.setBasicMetadata(basicMetadata);
        return reference;
    }

    /**
     * DocumentMetadata from YElement
     *
     * @param yElement
     * @param zipArchive
     * @param currentXmlPath
     * @param collection
     * @return
     */
    public static DocumentMetadata yelementToDocumentMetadata(YElement yElement, ZipArchive zipArchive, String sourcePath, String collection) {
        YStructure struct = yElement.getStructure(YaddaIdConstants.ID_HIERARACHY_JOURNAL);
        if (struct != null && !YaddaIdConstants.ID_LEVEL_JOURNAL_ARTICLE.equals(struct.getCurrent().getLevel())) {
            return null;
        }

        DocumentMetadata.Builder docBuilder = DocumentMetadata.newBuilder();
        BasicMetadata.Builder basicMetadataBuilder = BasicMetadata.newBuilder();

        if (sourcePath != null && !sourcePath.isEmpty()) {
            docBuilder.setSourcePath(sourcePath);
        }

        //Keywords in YElement are grupped by language;
        //in protobuf message they are stored in pairs <keyword, language>
        for (YTagList tagList : yElement.getTagLists()) {
            String lang = yLangAsString(tagList.getLanguage());
            for (String keyword : tagList.getValues()) {
                TextWithLanguage.Builder kwdBuilder = TextWithLanguage.newBuilder();
                kwdBuilder.setText(keyword);
                if (lang != null) {
                    kwdBuilder.setLanguage(lang);
                }
                docBuilder.addKeyword(kwdBuilder);
            }
        }

        // Documents' abstracts
        List<YDescription> yDescriptions = yElement.getDescriptions();
        for (YDescription abs : yDescriptions) {
            if (abs != null) {
                TextWithLanguage.Builder docAbstract = TextWithLanguage.newBuilder();
                docAbstract.setText(abs.getText());
                String lang = yLangAsString(abs.getLanguage());
                if (lang != null) {
                    docAbstract.setLanguage(lang);
                }
                docBuilder.addDocumentAbstract(docAbstract);
            }
        }

        if (struct != null) {
            YAncestor issue = struct.getAncestor("bwmeta1.level.hierarchy_Journal_Issue");
            if (issue != null && issue.getOneName() != null) {
                basicMetadataBuilder.setIssue(issue.getOneName().getText());
            }
            YAncestor volume = struct.getAncestor(YaddaIdConstants.ID_LEVEL_JOURNAL_VOLUME);
            if (volume != null) {
                basicMetadataBuilder.setVolume(volume.getOneName().getText());
            }
            YAncestor journal = struct.getAncestor(YaddaIdConstants.ID_LEVEL_JOURNAL_JOURNAL);
            if (journal != null) {
                basicMetadataBuilder.setJournal(journal.getOneName().getText());
            }

            YAncestor pages = struct.getAncestor(YaddaIdConstants.ID_LEVEL_JOURNAL_ARTICLE);
            if (pages != null) {
                basicMetadataBuilder.setPages(pages.getPosition());
            }
        }

        String content;
        if ((content = yElement.getId(YaddaIdConstants.IDENTIFIER_CLASS_DOI)) != null) {
            basicMetadataBuilder.setDoi(content);
        }
        if ((content = yElement.getId(YaddaIdConstants.IDENTIFIER_CLASS_ISSN)) != null) {
            basicMetadataBuilder.setIssn(content);
        }
        if ((content = yElement.getId(YaddaIdConstants.IDENTIFIER_CLASS_ISBN)) != null) {
            basicMetadataBuilder.setIsbn(content);
        }
        if ((content = yElement.getId("bwmeta1.id-class.MR")) != null) {
            KeyValue.Builder extId = KeyValue.newBuilder();
            extId.setKey(ProtoConstants.documentExtIdMr);
            extId.setValue(content);
            docBuilder.addExtId(extId);
        }
        if ((content = yElement.getId("oai")) != null) {
            KeyValue.Builder extId = KeyValue.newBuilder();
            extId.setKey(ProtoConstants.documentExtIdOai);
            extId.setValue(content);
            docBuilder.addExtId(extId);
        }
        if ((content = yElement.getId()) != null) {
            KeyValue.Builder extId = KeyValue.newBuilder();
            extId.setKey(ProtoConstants.documentExtIdBwmeta);
            extId.setValue(content);
            docBuilder.addExtId(extId);
        }
        if ((content = yElement.getId("bwmeta1.id-class.Zbl")) != null) {
            KeyValue.Builder eib = KeyValue.newBuilder();
            eib.setKey(ProtoConstants.documentExtIdZbl);
            eib.setValue(content);
            docBuilder.addExtId(eib.build());
        }

        List<YCategoryRef> catRefs = yElement.getCategoryRefs();

        //TODO czesc kodow MSC mylnie trafia do kwordow - mozna je stamtad wyciagnac porownujac z wzorcem kodu
        if (catRefs != null && catRefs.size() > 0) {

            ClassifCode.Builder ccodeMSC = ClassifCode.newBuilder();
            ccodeMSC.setSource(ProtoConstants.documentClassifCodeMsc);

            ClassifCode.Builder ccodePACS = ClassifCode.newBuilder();
            ccodePACS.setSource(ProtoConstants.documentClassifCodePacs);

            for (YCategoryRef yCategoryRef : catRefs) {
                if (yCategoryRef != null && yCategoryRef.getClassification().equals(YaddaIdConstants.CATEGORY_CLASS_MSC)) {
                    ccodeMSC.addValue(yCategoryRef.getCode());
                } else if (yCategoryRef != null && yCategoryRef.getClassification().equals(YaddaIdConstants.CATEGORY_CLASS_PACS)) {
                    ccodePACS.addValue(yCategoryRef.getCode());
                }
            }
            if (ccodeMSC.getValueCount() > 0) {
                basicMetadataBuilder.addClassifCode(ccodeMSC.build());
            }
            if (ccodePACS.getValueCount() > 0) {
                basicMetadataBuilder.addClassifCode(ccodePACS.build());
            }
        }

        //        docBuilder.setType(HBaseConstants.T_DOCUMENT_COPY);

        //Documents' titles
        for (YName yName : yElement.getNames()) {
            TextWithLanguage.Builder title = TextWithLanguage.newBuilder();
            String lang = yLangAsString(yName.getLanguage());
            if (lang != null) {
                title.setLanguage(lang);
            }
            title.setText(yName.getText());
            basicMetadataBuilder.addTitle(title);
        }

        docBuilder.setCollection(collection);

        List<YContributor> authorNodeList = yElement.getContributors();
        List<Author.Builder> authorBuilders = new ArrayList<Author.Builder>();
        for (int i = 0; i < authorNodeList.size(); i++) {
            YContributor currentNode = authorNodeList.get(i);
            if (currentNode != null && currentNode.isPerson() && "author".equals(currentNode.getRole())) {
                Author.Builder author = MetadataToProtoMetadataParser.ycontributorToAuthorMetadata(currentNode);
                author.setPositionNumber(i + 1);
                authorBuilders.add(author);
            }
        }

        //docKey depends on authors sorted by name
        Collections.sort(authorBuilders, new AuthorsComparatorByName());
        String docKey = calculateDocKey(basicMetadataBuilder, authorBuilders, docBuilder.getDocumentAbstractList());
        docBuilder.setKey(docKey);
        //In DocumentMetadata authors are sorted by position
        Collections.sort(authorBuilders, new AuthorsComparatorByPosition());

        //some data in authors metadata depend on docKey
        for (int i = 0; i < authorBuilders.size(); i++) {
            Author.Builder authorBuilder = authorBuilders.get(i);
            authorBuilder.setDocId(docKey);
            setAuthorKey(authorBuilder);
            basicMetadataBuilder.addAuthor(authorBuilder);
        }

        List<YRelation> refNodes = yElement.getRelations("reference-to");
        List<ReferenceMetadata> references = new ArrayList<ReferenceMetadata>();
        if (refNodes != null && refNodes.size() > 0) {
            for (int i = 0; i < refNodes.size(); i++) {
                ReferenceMetadata.Builder refMetadata = yrelationToReferenceMetadata(refNodes.get(i));
                refMetadata.setSourceDocKey(docKey);
                if (refMetadata != null) {
                    // quick dirty fix
                    refMetadata.setPosition(i);
                    references.add(refMetadata.build());
                }
            }
        }
        docBuilder.addAllReference(references);

        docBuilder.setBasicMetadata(basicMetadataBuilder);
        return docBuilder.build();
    }

    /**
     * Generates an unique key in the UUID format, which is a hash from some
     * subset of document's metadata
     *
     * @param docBuilder
     * @param authors
     * @return document's key in the UUID format
     */
    private static String calculateDocKey(BasicMetadata.Builder basicMetadata, List<Author.Builder> authors, List<TextWithLanguage> documentAbstracts) {

        StringBuilder sb = new StringBuilder();
        for (TextWithLanguage title : basicMetadata.getTitleList()) {
            sb.append(title.getText()).append("#");
        }
        for (Author.Builder authorBuilder : authors) {
            sb.append(authorBuilder.getSurname()).append(" ").append(authorBuilder.getForenames()).append("#");
        }
        sb.append(basicMetadata.getYear()).append("#");
        if (documentAbstracts != null) {
            for (TextWithLanguage abs : documentAbstracts) {
                sb.append(abs.getText()).append("#");
            }
        }
        sb.append(basicMetadata.getDoi()).append("#").append(basicMetadata.getIssn()).append("#");
        sb.append(basicMetadata.getIsbn()).append("#").append(basicMetadata.getIssue()).append("#");
        sb.append(basicMetadata.getJournal()).append("#").append(basicMetadata.getPages()).append("#");
        sb.append(basicMetadata.getVolume()).append("#");

        String key = UUID.nameUUIDFromBytes(sb.toString().getBytes()).toString();
        return key;
    }

    /**
     * Sets key in Author.Builder protobuf message, constructed from document id
     * and author's position on authors' list
     *
     * @param authorBuilder
     */
    private static void setAuthorKey(Author.Builder authorBuilder) {
        // docId + "#c" + positionNumber
        authorBuilder.setKey(authorBuilder.getDocId() + "#c"
                + authorBuilder.getPositionNumber());
    }

    private static class AuthorsComparatorByName implements Comparator<Author.Builder> {

        @Override
        public int compare(Author.Builder o1, Author.Builder o2) {

            int compareSurnames = o1.getSurname().compareTo(o2.getSurname());
            int compareForenames = o1.getForenames().compareTo(o2.getForenames());

            if (compareSurnames != 0) {
                return compareSurnames;
            } else {
                return compareForenames;
            }
        }
    }

    private static class AuthorsComparatorByPosition implements Comparator<Author.Builder> {

        @Override
        public int compare(Author.Builder o1, Author.Builder o2) {
            Integer pos1 = o1.getPositionNumber();
            Integer pos2 = o2.getPositionNumber();
            return pos1.compareTo(pos2);
        }
    }

    /**
     * Returns a 2-letters language code (like 'en', 'pl' etc.). For some
     * languages this code isn't defined, in this case the method returns a
     * bibliographic code, usually a little bit longer.
     *
     * @param yLang
     * @return Short code of language
     */
    private static String yLangAsString(YLanguage yLang) {
        if (yLang == null || yLang.equals(YLanguage.Undetermined)) {
            return null;
        }
        String lang = yLang.getShortCode();
        if (lang != null && !lang.isEmpty()) {
            return lang;
        }
        return yLang.getBibliographicCode();
    }

    public static List<DocumentMetadata> parseStream(InputStream stream, MetadataType type, String collection) {
        List<DocumentMetadata> results = new ArrayList<DocumentMetadata>();

        try {
            List<YExportable> elem = MetadataToProtoMetadataParser.streamToYExportable(stream, type);
            if (elem != null) {
                for (YExportable yExportable : elem) {
                    if (yExportable instanceof YElement) {
                        DocumentMetadata doc = yelementToDocumentMetadata((YElement) yExportable, null, null, collection);
                        if (doc != null) {
                            results.add(doc);
                        }
                    }
                }
            } else {
                log.error("Cannot parse bwmeta");
            }

        } catch (TransformationException e) {
            log.error("Cannot configure parser");
        } catch (IOException e) {
            log.warn("Cannot process record");
        }

        return results;
    }
}
