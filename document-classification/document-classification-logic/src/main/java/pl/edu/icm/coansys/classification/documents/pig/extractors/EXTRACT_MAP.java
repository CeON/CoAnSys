/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.classification.documents.pig.extractors;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;
import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;

/**
 *
 * @author pdendek
 */
@SuppressWarnings("rawtypes")
public class EXTRACT_MAP extends EvalFunc<Map> {
    
    private static final Logger logger = LoggerFactory.getLogger(EXTRACT_MAP.class);

    @Override
    public Map exec(Tuple input) throws IOException {
        try {
            DataByteArray protoMetadata = (DataByteArray) input.get(0);
            DocumentMetadata metadata = DocumentMetadata.parseFrom(protoMetadata.get());

            String titles;
            String abstracts;

            List<String> titleList = new ArrayList<String>();
            for (TextWithLanguage title : metadata.getBasicMetadata().getTitleList()) {
                titleList.add(title.getText());
            }
            titles = Joiner.on(" ").join(titleList);

            List<String> abstractsList = new ArrayList<String>();
            for (TextWithLanguage documentAbstract : metadata.getBasicMetadata().getTitleList()) {
                abstractsList.add(documentAbstract.getText());
            }
            abstracts = Joiner.on(" ").join(abstractsList);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key", metadata.getKey());
            map.put("title", titles);
            map.put("keywords", getConcatenated(metadata.getKeywordsList()));
            map.put("abstract", abstracts);
            map.put("categories", getCategories(metadata.getBasicMetadata().getClassifCodeList()));

            return map;
        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }

    private static DataBag getCategories(List<ClassifCode> classifCodeList) {
        DataBag db = new DefaultDataBag();
        for (ClassifCode code : classifCodeList) {
            for (String co_str : code.getValueList()) {
                db.add(TupleFactory.getInstance().newTuple(co_str));
            }
        }
        return db;
    }

    private String getConcatenated(List<KeywordsList> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<String> allKeywords = new ArrayList<String>();
        if (allKeywords.isEmpty()) {
            return null;
        } else {
            return Joiner.on(" ").join(allKeywords);
        }
    }
}
