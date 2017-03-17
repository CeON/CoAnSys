package pl.edu.icm.coansys.document.deduplication.merge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

import pl.edu.icm.coansys.commons.java.DiacriticsRemover;
import pl.edu.icm.coansys.commons.java.Pair;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.KeyValue;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;

/**
 * Class which merge list of DocumentWrapper to one single DocumentWrapper
 * object
 *
 * @author acz
 */
public class AdvancedDuplicatesMerger implements DuplicatesMerger {
  public static final String MERGED_ID_SEPARATOR = "+";  
    private Map<String, Integer> collectionPreferences;

    @Override
    public void setup(String collectionPriorities) {
        collectionPreferences = new HashMap<String, Integer>();

        for (String coll : collectionPriorities.split("," )) {
            coll = coll.trim();
            
            Pattern prefPattern = Pattern.compile("^(-?\\d+):(.+)");
            Matcher prefMatcher = prefPattern.matcher(coll);
            if (prefMatcher.matches()) {
                String priority = prefMatcher.group(1);
                String collectionName = prefMatcher.group(2);
                collectionPreferences.put(collectionName, Integer.parseInt(priority));
                
            }
        }
    }

    /**
     * Chooses the best DocumentWrapper, updates keys in DocumentWrapper,
     * DocumentMetadata and authors, gets extIds, auxiliarInfos from all
     * DocumentWrappers, matches authors, gets extIds from matched authors
     *
     * @param duplicates
     * @return
     */
    @Override
    public DocumentWrapper merge(List<DocumentWrapper> duplicates) {

        if (duplicates == null || duplicates.isEmpty()) {
            throw new RuntimeException("Nothing to merge");
        } else if (duplicates.size() == 1) {
            return duplicates.get(0);
        }

        int theBestDocumentWrapperIndex = chooseTheBestIndex(duplicates);

        // Collect information from all items
        List<String> identifiers = new ArrayList<String>(duplicates.size());
        List<KeyValue> allExtIds = new ArrayList<KeyValue>();
        List<KeyValue> allAuxiliarInfos = new ArrayList<KeyValue>();
        SortedSet<String> sortedCollections = new TreeSet<String>();
        List<KeywordsList> allKeywords = new ArrayList<KeywordsList>();

        for (DocumentWrapper dw : duplicates) {
            DocumentMetadata dm = dw.getDocumentMetadata();
            identifiers.add(dw.getRowId());
            List<String> collectionList = new ArrayList<String>(dm.getCollectionList());
            if (collectionList.isEmpty()) {
                collectionList.add("unknown"); //TODO move to constants
            }
            for (String collection : collectionList) {
                sortedCollections.add(collection);
            }
            allExtIds.addAll(dm.getExtIdList());
            allAuxiliarInfos.addAll(dm.getAuxiliarInfoList());
            allKeywords.addAll(dm.getKeywordsList());
        }
        Collections.sort(identifiers);
        String joinedIds = StringUtils.join(identifiers, MERGED_ID_SEPARATOR);
        String newIdentifier = UUID.nameUUIDFromBytes(joinedIds.getBytes()).toString();

        // Create new DocumentWrapper.Builder
        DocumentWrapper.Builder resultBuilder = DocumentWrapper
                .newBuilder(duplicates.get(theBestDocumentWrapperIndex));

        // Modify fields of DocumentWrapper.Builder
        resultBuilder.setRowId(newIdentifier);

        DocumentMetadata.Builder documentMetadataBuilder = resultBuilder
                .getDocumentMetadataBuilder();

        BasicMetadata.Builder basicMetadataBuilder = documentMetadataBuilder
                .getBasicMetadataBuilder();
        documentMetadataBuilder.setKey(newIdentifier);
        documentMetadataBuilder.addAllOrigKey(identifiers);
        documentMetadataBuilder.addAllCollection(sortedCollections);

        List<Author.Builder> finalAuthorBuilderList = basicMetadataBuilder
                .getAuthorBuilderList();
        for (Author.Builder authorBuilder : finalAuthorBuilderList) {
            String positionSuffix = authorBuilder.getKey().replaceAll(".*(#c\\d+)", "$1");
            authorBuilder.setDocId(newIdentifier);
            authorBuilder.setKey(newIdentifier + positionSuffix);
        }

        List<List<Author>> authorListsToMerge = new ArrayList<List<Author>>();

        for (int i = 0; i < duplicates.size(); i++) {
            if (i != theBestDocumentWrapperIndex) {
                List<Author> unmatchedList = duplicates.get(i)
                        .getDocumentMetadata().getBasicMetadata()
                        .getAuthorList();
                List<Author> matchedList = matchAuthors(finalAuthorBuilderList,
                        unmatchedList);
                if (matchedList != null) {
                    authorListsToMerge.add(matchedList);
                }
            }
        }

        mergeAuthors(finalAuthorBuilderList, authorListsToMerge);

        documentMetadataBuilder.clearExtId();
        documentMetadataBuilder.addAllExtId(mergeKeyValues(allExtIds));
        documentMetadataBuilder
                .addAllAuxiliarInfo(mergeKeyValues(allAuxiliarInfos));
        documentMetadataBuilder.addAllKeywords(mergeKeywords(allKeywords));

        // Build and return DocumentWrapper
        return resultBuilder.build();
    }

    /**
     * Moves some informations from author lists in listsToMerge to base list
     *
     * @param base
     * @param listsToMerge
     */
    protected void mergeAuthors(List<Author.Builder> base,
            List<List<Author>> listsToMerge) {

        for (int i = 0; i < base.size(); i++) {
            Author.Builder baseBuilder = base.get(i);
            List<KeyValue> allExtIds = new ArrayList<KeyValue>();
            allExtIds.addAll(baseBuilder.getExtIdList());
            for (List<Author> authorsToMerge : listsToMerge) {
                Author author = authorsToMerge.get(i);
                if (author != null) {
                    allExtIds.addAll(author.getExtIdList());
                }
            }

            baseBuilder.clearExtId();
            baseBuilder.addAllExtId(mergeKeyValues(allExtIds));
        }
    }

    /**
     * Checks if tho author lists contain the same authors. Returns second list
     * in order as in base list.
     *
     * @param base
     * @param second
     * @return
     */
    protected List<Author> matchAuthors(List<Author.Builder> base,
            List<Author> second) {
        List<Author> result = new ArrayList<Author>(base.size());
        List<Author> secondCopy = new ArrayList<Author>(second);

        for (Author.Builder author : base) {
            Author foundAuthor = null;
            for (Author secondAuthor : secondCopy) {

                if (equalsIgnoreCaseIgnoreDiacritics(
                        author.getName(), secondAuthor.getName())
                        || equalsIgnoreCaseIgnoreDiacritics(
                                author.getForenames(), secondAuthor.getForenames())
                        && equalsIgnoreCaseIgnoreDiacritics(
                                author.getSurname(), secondAuthor.getSurname())) {
                    foundAuthor = secondAuthor;
                    break;
                }
            }
            if (foundAuthor != null) {
                result.add(foundAuthor);
                secondCopy.remove(foundAuthor);
            } else {
                result.add(null);
            }
        }

        if (result.size() == base.size()) {
            return result;
        } else {
            return null;
        }
    }

    private boolean equalsIgnoreCaseIgnoreDiacritics(String firstName,
            String secondName) {
        if (firstName.isEmpty() || secondName.isEmpty()) {
            return false;
        }
        return DiacriticsRemover.removeDiacritics(firstName).equalsIgnoreCase(
                DiacriticsRemover.removeDiacritics(secondName));
    }

    /**
     * Merges KeyValue messages. Removes repetitions, concatenates comments.
     *
     * @param listWithRepetitions
     * @return
     */
    protected static List<KeyValue> mergeKeyValues(List<KeyValue> listWithRepetitions) {

        Map<Pair<String, String>, String> map = new HashMap<Pair<String, String>, String>();
        for (KeyValue extId : listWithRepetitions) {
            Pair<String, String> keyValue = new Pair<String, String>(
                    extId.getKey(), extId.getValue());
            String comment = extId.getComment();
            if (!map.containsKey(keyValue)) {
                map.put(keyValue, comment);
            } else if (!comment.isEmpty()) {
                String oldComment = map.get(keyValue);
                if (oldComment.isEmpty()) {
                    map.put(keyValue, comment);
                } else {
                    map.put(keyValue, oldComment + "\t" + comment);
                }
            }
        }

        List<KeyValue> result = new ArrayList<KeyValue>();

        for (Map.Entry<Pair<String, String>, String> mapEntry : map.entrySet()) {
            KeyValue.Builder kvBuilder = KeyValue.newBuilder();
            kvBuilder.setKey(mapEntry.getKey().getX());
            kvBuilder.setValue(mapEntry.getKey().getY());
            String comment = mapEntry.getValue();
            if (!comment.isEmpty()) {
                kvBuilder.setComment(comment);
            }
            result.add(kvBuilder.build());
        }

        return result;
    }

    /**
     * Chooses index of item which will be the base for merged result.
     *
     * @param duplicates
     * @return
     */
    protected int chooseTheBestIndex(List<DocumentWrapper> duplicates) {
        if (collectionPreferences == null || collectionPreferences.isEmpty()) {
            return 0;
        }

        int bestDuplicateIdx = 0;
        int bestPref = Integer.MIN_VALUE;
        
        for (int i = 0; i < duplicates.size(); i++) {
            DocumentWrapper dw = duplicates.get(i);
            for (String collection : dw.getDocumentMetadata().getCollectionList()) {

                int pref = 0;
                if (collectionPreferences.containsKey(collection)) {
                    pref = collectionPreferences.get(collection);
                }

                if (pref > bestPref) {
                    bestPref = pref;
                    bestDuplicateIdx = i;
                }
            }
        }
        return bestDuplicateIdx;
    }

    private List<KeywordsList> mergeKeywords(List<KeywordsList> allKeywords) {
        Map<Pair<String, String>, Pair<Set<String>, String>> keywordsMap = new HashMap<Pair<String, String>, Pair<Set<String>, String>>();
        // type, lang, keywords, comment

        for (KeywordsList kwdList : allKeywords) {
            Pair<String, String> typeAndLang = new Pair(kwdList.getType(),
                    kwdList.getLanguage());
            Pair<Set<String>, String> keywordsAndComment;
            String comment = kwdList.getComment();
            if (!keywordsMap.containsKey(typeAndLang)) {
                keywordsAndComment = new Pair<Set<String>, String>(
                        new HashSet<String>(), comment);
                keywordsMap.put(typeAndLang, keywordsAndComment);
            } else {
                keywordsAndComment = keywordsMap.get(typeAndLang);
                if (!comment.isEmpty()) {
                    String oldComment = keywordsAndComment.getY();
                    if (oldComment.isEmpty()) {
                        keywordsAndComment.setY(comment);
                    } else {
                        keywordsAndComment.setY(oldComment + "\t" + comment);
                    }
                }
            }
            keywordsAndComment.getX().addAll(kwdList.getKeywordsList());
        }

        List<KeywordsList> result = new ArrayList<KeywordsList>();
        for (Map.Entry<Pair<String, String>, Pair<Set<String>, String>> entry : keywordsMap
                .entrySet()) {
            KeywordsList.Builder kwdlBuilder = KeywordsList.newBuilder();
            String type = entry.getKey().getX();
            String lang = entry.getKey().getY();
            Set<String> keywords = entry.getValue().getX();
            String comment = entry.getValue().getY();

            if (type != null && !type.isEmpty()) {
                kwdlBuilder.setType(type);
            }
            if (lang != null && !lang.isEmpty()) {
                kwdlBuilder.setLanguage(lang);
            }
            if (comment != null && !comment.isEmpty()) {
                kwdlBuilder.setComment(comment);
            }
            kwdlBuilder.addAllKeywords(keywords);

            result.add(kwdlBuilder.build());
        }
        return result;
    }
}
