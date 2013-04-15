/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.logicalLayer.schema.Schema.FieldSchema;
import static org.testng.Assert.*;
import pl.edu.icm.coansys.importers.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.TextWithLanguage;

/**
 *
 * @author acz
 */
public class DocumentMetadataToTupleTest {

    private static final Map<String, String> titlesMap = new HashMap<String, String>();

    static {
        titlesMap.put("en", "Title of test article");
        titlesMap.put("fr", "Le titre de l'article");
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void basicTest() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {

        //prepare some test data
        DocumentMetadata inputDM;
        
        DocumentMetadata.Builder dmBuilder = DocumentMetadata.newBuilder();
        dmBuilder.setKey("key123");
        BasicMetadata.Builder basic = BasicMetadata.newBuilder();

        TextWithLanguage.Builder title;
        for (String lang : titlesMap.keySet()) {
            title = TextWithLanguage.newBuilder();
            title.setText(titlesMap.get(lang));
            title.setLanguage(lang);
            basic.addTitle(title);
        }

        dmBuilder.setBasicMetadata(basic);
        
        inputDM = dmBuilder.build();

        //convert by DocumentMetadataToTuple UDF
        //  UDF input argument must be a Tuple, so we have to prepare a tuple with 
        //  one field of type bytearray
        DataByteArray dbarr = new DataByteArray(inputDM.toByteArray());
        Tuple metadataTuple = TupleFactory.getInstance().newTuple(dbarr);
        //  instantiate a UDF and convert
        DocumentMetadataToTuple dm2tpl = new DocumentMetadataToTuple();
        Tuple pigTuple = dm2tpl.exec(metadataTuple);

        //asserts

        assertTrue(pigTuple.size() > 0);
        Schema oneFieldSchema = dm2tpl.outputSchema(null);
        assertEquals(oneFieldSchema.size(), 1);
        assertEquals(oneFieldSchema.getField(0).type, DataType.TUPLE);
        Schema tupleSchema = oneFieldSchema.getField(0).schema;
        assertEquals(tupleSchema.size(), pigTuple.size());

        boolean keyFound = false;
        // go through a data
        for (int i = 0; i < pigTuple.size(); i++) {
            FieldSchema fieldSchema = tupleSchema.getField(i);
            if (pigTuple.getType(i) != DataType.NULL) {
                assertTrue(pigTuple.getType(i) == fieldSchema.type);
                if (fieldSchema.alias.equals("key")) {
                    assertEquals(fieldSchema.type, DataType.CHARARRAY);
                    assertEquals((String) pigTuple.get(i), inputDM.getKey());
                    keyFound = true;
                } else if (fieldSchema.alias.equals("basicmetadata")) {
                    assertEquals(fieldSchema.type, DataType.TUPLE);
                    Tuple bmTuple = (Tuple) pigTuple.get(i);
                    Schema bmSchema = fieldSchema.schema;
                    checkBasicMetadata(bmTuple, bmSchema);
                }
            }
        }
        assertTrue(keyFound, "Key not found in tuple");
        
        //convert to DocumentMetadata by TupleToProtoBytearray UDF
        TupleToProtoBytearray tpl2dm = new TupleToProtoBytearray(DocumentMetadata.class);
        DataByteArray finalDMdba = tpl2dm.exec(pigTuple);
        DocumentMetadata finalDM = DocumentMetadata.parseFrom(finalDMdba.get());
        
        assertEquals(finalDM, inputDM);
    }

    private void checkBasicMetadata(Tuple bmTuple, Schema bmSchema) throws FrontendException, ExecException {
        int titlesFound = 0;
        
        assertTrue(bmSchema.size() > 1);
        assertEquals(bmSchema.size(), bmTuple.size());

        for (int j = 0; j < bmTuple.size(); j++) {
            FieldSchema bmFieldSchema = bmSchema.getField(j);
            if (bmFieldSchema.alias.equals("title")) {
                assert (bmFieldSchema.type == DataType.BAG);
                DataBag bag = (DataBag) bmTuple.get(j);
                
                Schema titleSchema = bmFieldSchema.schema.getField(0).schema;

                Map<String, String> extractedTitles = new HashMap<String, String>();
                for (Tuple titleTuple : bag) {
                    String txt = "";
                    String lang = "";
                    for (int k = 0; k < titleTuple.size(); k++) {
                        if (titleSchema.getField(k).alias.equals("text")) {
                            txt = (String) titleTuple.get(k);
                        } else if (titleSchema.getField(k).alias.equals("language")) {
                            lang = (String) titleTuple.get(k);
                        }
                    }
                    extractedTitles.put(lang, txt);
                    titlesFound++;
                }
                assertEquals(extractedTitles, titlesMap);
            } // else - other fields - maybe in the future...
        }
        assertEquals(titlesFound, titlesMap.size());
    }
}
