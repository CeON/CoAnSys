package pl.edu.icm.coansys.similarity.pig.script;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.pig.tools.parameters.ParseException;
import pl.edu.icm.coansys.similarity.test.utils.MacroExtractor;

/**
 *
 * @author akawa
 */
public class TestPairwiseSimilarity extends AbstractPigUnitTest {

    private String[] params = {
        "in_relation=in_relation",
        "doc_field=docId",
        "term_field=term",
        "tfidf_field=tfidf",
        "out_relation=out_relation",
        "outputPath=null",
         "CC=::"
    };

    @org.testng.annotations.Test(groups = {"fast"})
    public void testSingle() throws IOException, ParseException {

        String[] input = {
            "d1\tt1\t5",
            "d1\tt2\t2",
            "d2\tt1\t4",
            "d2\tt2\t8"
        };

        String[] inRelation2 = {
            "(d1,t1,5.0)",
            "(d1,t2,2.0)",
            "(d2,t1,4.0)",
            "(d2,t2,8.0)"
        };
        //test.assertOutput("in_relation", input, "in_relation2", inRelation2);

        String[] filtered = {
            "(t1,d1,d2,5.0,4.0)",
            "(t2,d1,d2,2.0,8.0)"
        };
        //test.assertOutput("in_relation", input, "filtered", filtered);

        String[] docsSimilarity = {
            "(d1,d2,160.0)",
            "(d2,d1,160.0)"
        };
        //test.assertOutput("in_relation", input, "similarity", docsSimilarity);
    }

    @org.testng.annotations.Test(groups = {"medium"})
    public void testMultiple() throws IOException, ParseException {

        String[] input = {
            "d1\tt1\td2\t5",
            "d1\tt2\td2\t2",
            "d2\tt1\td2\t4",
            "d2\tt2\td2\t8"
        };

        String[] termDocSimilarity = {
            "(t1,d1,d2,5.0,4.0,20.0)",
            "(t2,d1,d2,2.0,8.0,16.0)"
        };
        
        //test.assertOutput("in_relation", input, "term_doc_similarity", termDocSimilarity);

        String[] docsSimilarity = {
            "(d1,d2,160.0)",
            "(d2,d1,160.0)"
        };
        //test.assertOutput("in_relation", input, "similarity", docsSimilarity);
    }

    @Override
    public String[] getScriptToTest() throws FileNotFoundException, IOException {
        LinkedList<String> macro = MacroExtractor.extract(PIG_SCRIPT_DIR + "macros.pig", "calculate_pairwise_similarity");
        macro.addFirst(""
                + "REGISTER target/document-similarity-1.0-SNAPSHOT.jar;"
                + "DEFINE KeywordSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dks');"
                + "DEFINE DocsCombinedSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dkcs');"
                + "in_relation = LOAD 'ommited' AS (docId: chararray, term: chararray, tfidf: double);");
        macro.addLast("similarity = ORDER out_relation BY docId1, docId2, similarity;");
        return macro.toArray(new String[]{});
    }

    @Override
    public String[] getScriptParams() {
        return params;
    }
}
