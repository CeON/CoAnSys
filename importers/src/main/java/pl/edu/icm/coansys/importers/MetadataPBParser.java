/*
 * (C) 2010-2012 ICM UW. All righst reserved.
 */

package pl.edu.icm.coansys.importers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.importers.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.DocumentProtos.DocumentMetadata;
import pl.edu.icm.synat.application.commons.transformers.MetadataFormat;
import pl.edu.icm.synat.application.commons.transformers.MetadataReader;
import pl.edu.icm.synat.application.commons.transformers.TransformationException;
import pl.edu.icm.synat.application.model.bwmeta.*;
import pl.edu.icm.synat.application.model.bwmeta.constants.YaddaIdConstants;
import pl.edu.icm.synat.application.model.bwmeta.transformers.BwmetaTransformerConstants;
import pl.edu.icm.synat.application.model.general.MetadataTransformers;

/**
 *
 * @author piotrw
 * @author pdendek
 */
public class MetadataPBParser {

    public enum MetadataType {

        BWMETA, OAI_DC, DMF
    }
    private static final Logger log = LoggerFactory.getLogger(MetadataPBParser.class);

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

    private static Author.Builder ycontributorToAuthorMetadata(YContributor yContributor) {
    	Author.Builder authorBuilder = DocumentProtos.Author.newBuilder(); 
        
    	List<YName> names = yContributor.getNames();
        for (YName yName : names) {
            String type = yName.getType();
            if ("canonical".equals(type)) {
            	authorBuilder.setName(yName.getText());
            } else if ("forenames".equals(type)) {
            	authorBuilder.setForenames(yName.getText());
            } else if ("surname".equals(type)) {
            	authorBuilder.setSurname(yName.getText());
            }
        }

        List<YAttribute> attrs = yContributor.getAttributes();
        for (YAttribute yAttribute : attrs) {
            String key = yAttribute.getKey();
            if (key.equals("contact-email")) {
                if (yAttribute.getValue() != null) {
                	authorBuilder.setEmail(yAttribute.getValue());
                }
            } else if (key.equals("zbl.author-fingerprint")) {
                if (yAttribute.getValue() != null) {
                	authorBuilder.setZbl(yAttribute.getValue());
                }
            } else if (key.equals("identity")) {
                String authorIdentity = yAttribute.getValue();
                if (authorIdentity.length() >= 36) {
                    authorIdentity = authorIdentity.substring(authorIdentity.length() - 36);
                }
                try {
                	authorBuilder.setKey(UUID.fromString(authorIdentity).toString());
                } catch (IllegalArgumentException e) {
                	authorBuilder.setKey(UUID.randomUUID().toString());
                }
            }
        }
        if (authorBuilder.getKey() == null || authorBuilder.getKey().length() == 0) {
        	authorBuilder.setKey(UUID.randomUUID().toString());
        }
//        authorBuilder.setType(HBaseConstants.T_AUTHOR_COPY);
        return authorBuilder;
    }

    private static Author.Builder yattributeToAuthorMetadata(YAttribute node) {
    	Author.Builder author = DocumentProtos.Author.newBuilder();
        author.setKey(UUID.randomUUID().toString());
//        author.setType(HBaseConstants.T_AUTHOR_COPY);
        String content;
        if((content = node.getValue())!=null)
        	author.setName(content);
        if((content = node.getOneAttributeSimpleValue("reference-parsed-author-forenames"))!=null)
        	author.setForenames(content);
        if((content = node.getOneAttributeSimpleValue("reference-parsed-author-surname"))!=null)
        	author.setSurname(content);
        if((content = node.getOneAttributeSimpleValue("zbl.author-fingerprint"))!=null)
        	author.setZbl(content);
        return author;
    }

    private static DocumentMetadata.Builder yrelationToDocumentMetadata(YRelation item) {
        DocumentMetadata.Builder doc = DocumentProtos.DocumentMetadata.newBuilder();

        doc.setKey(UUID.randomUUID().toString());
//        doc.setType(HBaseConstants.T_REFERENCE);

        String attr = item.getOneAttributeSimpleValue("reference-number");
        if (attr != null) {
            Double refPos;
            try {
                refPos = Double.parseDouble(attr);
            } catch (NumberFormatException ex) {
                refPos = null;
            }
            if (refPos != null) {
                double doubleRefPos = refPos;
                int intRefPos = (int) doubleRefPos;
                doc.setBibRefPosition(intRefPos);
            }
        }

        doc.setBibRefText(item.getOneAttributeSimpleValue("reference-text"));

        ArrayList<Author> authors = new ArrayList<Author>();
        List<YAttribute> refAuthorsNodes = item.getAttributes("reference-parsed-author");
        for (int i = 0; i < refAuthorsNodes.size(); i++) {
            Author.Builder refAuthor = yattributeToAuthorMetadata(refAuthorsNodes.get(i));
            refAuthor.setDocId(doc.getKey().toString());
            refAuthor.setPositionNumber(i);
            authors.add(refAuthor.build());
        }
        doc.addAllAuthor(authors);
        
        String content = null;
        //References may not contain a title or any other then bibreftext filed
        if((content = item.getOneAttributeSimpleValue("reference-parsed-title"))!=null) 
        	doc.setTitle(content);
       	if((content = item.getOneAttributeSimpleValue("reference-parsed-journal"))!=null) 
       		doc.setJournal(content);
      	if((content = item.getOneAttributeSimpleValue("reference-parsed-volume"))!=null) 
      		doc.setVolume(content);
      	if((content = item.getOneAttributeSimpleValue("reference-parsed-issue"))!=null) 
      		doc.setIssue(content);
        if((content = item.getOneAttributeSimpleValue("reference-parsed-pages"))!=null) 
        	doc.setPages(content);
        
        //TODO czesc kodow MSC mylnie trafia do kwordow - mozna je stamtad wyciagnac porownujac z wzorcem kodu
        List<String> refMscCodes = new ArrayList<String>();
        List<YAttribute> refMscCodesNodes = item.getAttributes(YaddaIdConstants.CATEGORY_CLASS_MSC);
        for (int i = 0; i < refMscCodesNodes.size(); i++) {
            refMscCodes.add(refMscCodesNodes.get(i).getValue());
        }
        if (!refMscCodes.isEmpty()) {
            doc.addAllMscCode(refMscCodes);
        }

        List<String> refPacsCodes = new ArrayList<String>();
        List<YAttribute> refPacsCodesNodes = item.getAttributes(YaddaIdConstants.CATEGORY_CLASS_PACS);
        for (int i = 0; i < refPacsCodesNodes.size(); i++) {
            refPacsCodes.add(refPacsCodesNodes.get(i).getValue());
        }
        if (!refPacsCodes.isEmpty()) {
            doc.addAllPacsCode(refPacsCodes);
        }

        return doc;
    }

    public static DocumentMetadata.Builder yelementToDocumentMetadata(YElement yElement) {
        YStructure struct = yElement.getStructure(YaddaIdConstants.ID_HIERARACHY_JOURNAL);
        if (struct == null || !YaddaIdConstants.ID_LEVEL_JOURNAL_ARTICLE.equals(struct.getCurrent().getLevel())) {
            return null;
        }

        DocumentMetadata.Builder doc = DocumentProtos.DocumentMetadata.newBuilder();

        UUID uuId;

        String uuIdStr = yElement.getId();
        if (uuIdStr.length() >= 36) {
            uuIdStr = uuIdStr.substring(uuIdStr.length() - 36);
        }
        try {
            uuId = UUID.fromString(uuIdStr);
        } catch (IllegalArgumentException e) {
            log.warn("Error reading UUID from file: {}", e.toString());
            uuId = UUID.randomUUID();
        }
        doc.setKey(uuId.toString());
//        doc.setType(HBaseConstants.T_DOCUMENT_COPY);
        doc.setTitle(yElement.getOneName().getText());

        List<YContributor> authorNodeList = yElement.getContributors();
        List<Author> authors = new ArrayList<Author>();
        for (int i = 0; i < authorNodeList.size(); i++) {
            YContributor currentNode = authorNodeList.get(i);
            if (currentNode != null && currentNode.isPerson() && "author".equals(currentNode.getRole())) {
                Author.Builder author = MetadataPBParser.ycontributorToAuthorMetadata(currentNode);
                author.setDocId(uuId.toString());
                author.setPositionNumber(i);
                authors.add(author.build());
            }
        }
        doc.addAllAuthor(authors);

        List<String> keywords = Collections.emptyList();
        YTagList tagList = yElement.getTagList("keyword");
        if (tagList != null) {
            keywords = tagList.getValues();
        }
        doc.addAllKeyword(keywords);
        
        
        List<YDescription> abst = yElement.getDescriptions();
        if (abst != null && abst.size() > 0 && abst.get(0) != null) {
            doc.setAbstrakt(abst.get(0).getText());
        }

        YAncestor issue = yElement.getStructure(YaddaIdConstants.ID_HIERARACHY_JOURNAL).getAncestor("bwmeta1.level.hierarchy_Journal_Issue");
        if (issue != null && issue.getOneName() != null) {
            doc.setIssue(issue.getOneName().getText());
        }

        YAncestor volume = yElement.getStructure(YaddaIdConstants.ID_HIERARACHY_JOURNAL).getAncestor(YaddaIdConstants.ID_LEVEL_JOURNAL_VOLUME);
        if (volume != null) {
            doc.setVolume(volume.getOneName().getText());
        }

        String content;
        if((content = yElement.getId(YaddaIdConstants.IDENTIFIER_CLASS_DOI))!=null)
        	doc.setDoi(content);
        if((content = yElement.getId(YaddaIdConstants.IDENTIFIER_CLASS_ISSN))!=null)
        	doc.setIssn(content);
        if((content = yElement.getId(YaddaIdConstants.IDENTIFIER_CLASS_ISBN))!=null)
        	doc.setIsbn(content);
        if((content = yElement.getId("bwmeta1.id-class.MR"))!=null)
        	doc.setMrId(content);
        if((content = yElement.getId("bwmeta1.id-class.Zbl"))!=null)
        	doc.setZblId(content);

        List<YCategoryRef> catRefs = yElement.getCategoryRefs();
        List<String> bwMscCodes = new ArrayList<String>();
        List<String> bwPacsCodes = new ArrayList<String>();

        //TODO czesc kodow MSC mylnie trafia do kwordow - mozna je stamtad wyciagnac porownujac z wzorcem kodu
        if (catRefs != null && catRefs.size() > 0) {
            for (YCategoryRef yCategoryRef : catRefs) {
                if (yCategoryRef != null && yCategoryRef.getClassification().equals(YaddaIdConstants.CATEGORY_CLASS_MSC)) {
                    bwMscCodes.add(yCategoryRef.getCode());
                } else if (yCategoryRef != null && yCategoryRef.getClassification().equals(YaddaIdConstants.CATEGORY_CLASS_PACS)) {
                    bwPacsCodes.add(yCategoryRef.getCode());
                }
            }
            if (bwMscCodes.size() > 0) {
                doc.addAllMscCode(bwMscCodes);
            }
            if (bwPacsCodes.size() > 0) {
                doc.addAllPacsCode(bwPacsCodes);
            }
        }

        YAncestor journal = yElement.getStructure(YaddaIdConstants.ID_HIERARACHY_JOURNAL).getAncestor(YaddaIdConstants.ID_LEVEL_JOURNAL_JOURNAL);
        if (journal != null) {
            doc.setJournal(journal.getOneName().getText());
        }

        YAncestor pages = yElement.getStructure(YaddaIdConstants.ID_HIERARACHY_JOURNAL).getAncestor(YaddaIdConstants.ID_LEVEL_JOURNAL_ARTICLE);
        if (pages != null) {
            doc.setPages(pages.getPosition());
        }

        List<YRelation> refNodes = yElement.getRelations("reference-to");
        List<DocumentMetadata> references = new ArrayList<DocumentMetadata>();
        if (refNodes != null && refNodes.size() > 0) {
            for (int i = 0; i < refNodes.size(); i++) {
                DocumentMetadata.Builder refMetadata = MetadataPBParser.yrelationToDocumentMetadata(refNodes.get(i));
                refMetadata.setBibRefSource(uuId.toString());
                if (refMetadata != null) {
                    // quick dirty fix
                    refMetadata.setBibRefPosition(i);
                    references.add(refMetadata.build());
                }
            }
        }

        doc.addAllReference(references);

        return doc;
    }

    public static List<DocumentMetadata.Builder> parseStream(InputStream stream, MetadataType type) {
        List<DocumentMetadata.Builder> results = new ArrayList<DocumentMetadata.Builder>();

        try {
            List<YExportable> elem = MetadataPBParser.streamToYExportable(stream, type);
            if (elem != null) {
                for (YExportable yExportable : elem) {
                    if (yExportable instanceof YElement) {
                        DocumentMetadata.Builder doc = yelementToDocumentMetadata((YElement) yExportable);
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
