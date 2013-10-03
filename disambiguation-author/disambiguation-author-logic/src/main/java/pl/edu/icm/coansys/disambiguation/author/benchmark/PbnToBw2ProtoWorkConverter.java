//package pl.edu.icm.coansys.disambiguation.author.benchmark;
//
//import java.util.List;
//
//import org.apache.commons.lang.StringUtils;
//
//import pl.edu.icm.coansys.models.DocumentProtos.Author;
//import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
//import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
//import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
//import pl.edu.icm.coansys.models.DocumentProtos.KeyValue;
//import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;
//import pl.edu.icm.sedno.common.model.SednoDate;
//import pl.edu.icm.sedno.model.Article;
//import pl.edu.icm.sedno.model.Book;
//import pl.edu.icm.sedno.model.Contribution;
//import pl.edu.icm.sedno.model.Journal;
//import pl.edu.icm.sedno.model.Work;
//
//import com.google.common.collect.Lists;
//
///**
// * Pbn {@link Work} to Bw2Proto {@link DocumentWrapper} converter
// *
// * @author lukdumi
// *
// */
//public class PbnToBw2ProtoWorkConverter {
//
//
//    public static final String PERSON_ID_KEY_NAME = "personPbnId";
//
//
//    public DocumentWrapper convertWork(Work work) {
//        DocumentWrapper doc = DocumentWrapper.newBuilder().setRowId(createDocId(work)).setDocumentMetadata(createDocumentMetadata(work)).build();
//        return doc;
//    }
//
//
//
//
//    //******************** PRIVATE ********************
//
//    private DocumentMetadata createDocumentMetadata(Work work) {
//        DocumentMetadata docMetadata = DocumentMetadata.newBuilder().setKey(createDocId(work)).setBasicMetadata(createBasicMetadata(work)).build();
//        return docMetadata;
//    }
//
//
//    private BasicMetadata createBasicMetadata(Work work) {
//        BasicMetadata.Builder basicMetadataBuilder = BasicMetadata.newBuilder();
//        //title
//        basicMetadataBuilder.addTitle(0, createTitle(work));
//        //authors
//        basicMetadataBuilder.addAllAuthor(createAuthors(work));
//        //IF article GET journal,issn
//        if (work.getExt().isArticle()) {
//            Article article = (Article)work;
//            Journal journal = article.getJournal();
//            if (journal!=null) {
//                basicMetadataBuilder.setJournal(journal.getTitle());
//                basicMetadataBuilder.setIssn(StringUtils.defaultIfBlank(journal.getIssnOrEissn(),""));
//            }
//        }
//        //IF book GET isbn
//        if (work.getExt().isBook()) {
//            Book book = (Book)work;
//            basicMetadataBuilder.setIsbn(StringUtils.defaultIfBlank(book.getIsbn(), ""));
//        }
//        //publication date
//        SednoDate publicationDate = work.getPublicationDate();
//        if (publicationDate !=null) {
//        	basicMetadataBuilder.setYear(""+work.getPublicationDate().getYear());
//        }
//        //doi
//        basicMetadataBuilder.setDoi(StringUtils.defaultIfBlank(work.getExt().getDoiIdentifier(),""));
// 		//foreach lang in getKeywordsLang() do DocumentMetadata.keywords.add(getKeywords(lang))  (#2)
//        //TODO
// 		//foreach codeType in getClassifCodeTypes() do DocumentMetadata.BasicMetadata.classifCode.add(getClassificationCodes(codeType))  (#1)
//        //TODO
//        //foreach lang in getAbstractsLang() do DocumentMetadata.documentAbstract.add(getAbstract(lang))  (#3)
//        //TODO
//        //@repeated DocumentMetadata.affiliations.add(getAffiliations()) (#4)
//        //TODO
//        //@repeated DocumentMetadata.reference.add(getReferences()) (#5)
//        BasicMetadata basicMetadata = basicMetadataBuilder.build();
//        return basicMetadata;
//    }
//
//
//    private TextWithLanguage createTitle(Work work) {
//        String lang = "";
//        if (work.getMetadata().getOriginalLanguage()!=null) {
//            lang = work.getMetadata().getOriginalLanguage().getItem();
//        }
//        TextWithLanguage textWithLanguage = TextWithLanguage.newBuilder().setLanguage(lang).setText(work.getOriginalTitle()).build();
//        return textWithLanguage;
//    }
//
//
//    private List<Author> createAuthors(Work work) {
//        List<Author> authors = Lists.newArrayList();
//        for (Contribution contribution : work.getContributions()) {
//            Author author = createAuthor(contribution, createDocId(work));
//            authors.add(author);
//        }
//        return authors;
//    }
//
//
//    private Author createAuthor(Contribution contribution, String docId) {
//        String personId = "";
//        if (contribution.isAssignedToPerson()) {
//            personId = ""+contribution.getPerson().getIdPerson();
//        }
//        Author.Builder authorBuilder = Author.newBuilder();
//        if (!StringUtils.isEmpty(personId)) {
//            KeyValue personKeyValue = KeyValue.newBuilder().setKey(PERSON_ID_KEY_NAME).setValue(personId).build();
//            authorBuilder.addExtId(personKeyValue);
//        }
//        authorBuilder.setKey(""+contribution.getIdContribution());
//        authorBuilder.setPositionNumber(contribution.getContribOrder());
//        authorBuilder.setForenames(StringUtils.defaultIfBlank(contribution.getContributorFirstName(),""));
//        authorBuilder.setSurname(StringUtils.defaultIfBlank(contribution.getContributorLastName(),""));
//        authorBuilder.setName(contribution.getExt().getContributorFullName());
//        authorBuilder.setDocId(docId);
//        Author author = authorBuilder.build();
//        return author;
//    }
//
//
//    private String createDocId(Work work) {
//        return "pbn_"+work.getIdWork();
//    }
//
//
//
//
//
//}