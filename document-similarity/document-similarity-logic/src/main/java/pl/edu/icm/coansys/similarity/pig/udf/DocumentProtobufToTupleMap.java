/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;
import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;

import com.google.common.base.Joiner;

/**
 *
 * @author akawa
 * @author pdendek
 */
public class DocumentProtobufToTupleMap extends EvalFunc<Tuple> {

    @SuppressWarnings({ "boxing", "serial" })
	Map<String, Integer> fieldNumberMap = new HashMap<String, Integer>() {
        {
            put("key", 0);
            put("title", 1);
            put("abstract", 2);
            put("keywords", 3);
            put("contributors", 4);
        }
    };

    @Override
    public Schema outputSchema(Schema input) {
        try {

            Schema keywordSchema = new Schema(new Schema.FieldSchema("keyword",
                    new Schema(new Schema.FieldSchema("value", DataType.CHARARRAY)),
                    DataType.TUPLE));
            Schema contributorSchema = new Schema(new Schema.FieldSchema("contributor",
                    new Schema(Arrays.asList(
                    new Schema.FieldSchema("key", DataType.CHARARRAY),
                    new Schema.FieldSchema("name", DataType.CHARARRAY))),
                    DataType.TUPLE));

            Schema tupleSchema = new Schema();
            tupleSchema.add(new Schema.FieldSchema("key", DataType.CHARARRAY));
            tupleSchema.add(new Schema.FieldSchema("title", DataType.CHARARRAY));
            tupleSchema.add(new Schema.FieldSchema("abstract", DataType.CHARARRAY));
            tupleSchema.add(new Schema.FieldSchema("keywords", keywordSchema, DataType.BAG));
            tupleSchema.add(new Schema.FieldSchema("contributors", contributorSchema, DataType.BAG));

            return new Schema(new Schema.FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input),
                    tupleSchema, DataType.TUPLE));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Tuple exec(Tuple input) throws IOException {
        DataByteArray dba = (DataByteArray) input.get(0);
        DocumentMetadata metadata = DocumentWrapper.parseFrom(dba.get()).getDocumentMetadata();
        
        Tuple output = TupleFactory.getInstance().newTuple(fieldNumberMap.size());
        output = addDocumentMetatdataFields(metadata, output);
        return output;
    }
    
    private Tuple addDocumentMetatdataFields(DocumentMetadata metadata, Tuple output) throws ExecException {

        output.set(fieldNumberMap.get("key"), metadata.getKey());        
        appendToOutput(output,"title",metadata.getBasicMetadata().getTitleList());
        appendToOutput(output,"abstract",metadata.getDocumentAbstractList());
        
        List<String> al = new ArrayList<String>(); 
        for(KeywordsList kl : metadata.getKeywordsList()){
        	for(String s : kl.getKeywordsList()){
        		al.add(s);
        	}
        }
        output.set(fieldNumberMap.get("keywords"), listToDataBag(al));

        List<String> authorKeys = new ArrayList<String>();
        List<String> authorNames = new ArrayList<String>();
        for (Author author : metadata.getBasicMetadata().getAuthorList()) {
            authorKeys.add(author.getKey());
            authorNames.add(author.getName());
        }

        output.set(fieldNumberMap.get("contributors"), listToDataBag(authorKeys, authorNames));

        return output;
    }

    private void appendToOutput(Tuple output, String field, List<TextWithLanguage> someList) throws ExecException {
    	ArrayList<String> al = new ArrayList<String>();
        for(TextWithLanguage twl : someList){
        	al.add(twl.getText());
        }
        output.set(fieldNumberMap.get(field), Joiner.on(" ").join(al));
	}

    
    private <T> DataBag listToDataBag(List<T> list) {
    	DataBag output = BagFactory.getInstance().newDefaultBag();
        for (T l : list) {
            output.add(TupleFactory.getInstance().newTuple(l));
        }
        return output;
    }

    private <T1, T2> DataBag listToDataBag(List<T1> list1, List<T2> list2) throws ExecException {
        DataBag output = BagFactory.getInstance().newDefaultBag();
        for (int i = 0; i < Math.min(list1.size(), list2.size()); i++) {
            Tuple t = TupleFactory.getInstance().newTuple(2);
            t.set(0, list1.get(i));
            t.set(1, list2.get(i));
            output.add(t);
        }
        return output;
    }
}