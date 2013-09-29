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

package pl.edu.icm.coansys.classification.documents.pig.extractors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

/**
 *
 * @author pdendek
 */
public class EXTRACT_BAG_FROM_MAP extends EvalFunc<DataBag> {

    private static final Logger logger = LoggerFactory.getLogger(EXTRACT_KEY_TI_ABS_KW.class);

    @Override
    public DataBag exec(Tuple input) throws IOException {
        try {
            Map<String, Object> map;
            String key;
            Object raw;
            try {
                map = (Map<String, Object>) input.get(0);
                key = (String) input.get(1);
                raw = map.get(key);
            } catch (ExecException e) {
                logger.error("No map or key/The key does not occure in the given map:", e);
                return null;
            }

            if (raw != null) {
                DataBag ret = new DefaultDataBag();
                String vals = raw.toString();
                if (vals.length() <= 2) {
                    return null;
                }
                String[] valsA = vals.substring(1, vals.length() - 1).split(",");
                for (final String v : valsA) {
                    if (v.length() <= 2) {
                        continue;
                    }
                    ret.add(TupleFactory.getInstance().newTuple(new ArrayList<String>() {
                        {
                            add(v.substring(1, v.length() - 1));
                        }
                    }));
                }
                return ret;
            }
            return null;
        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}
