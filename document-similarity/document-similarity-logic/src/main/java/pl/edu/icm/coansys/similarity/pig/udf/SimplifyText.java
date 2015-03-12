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

package pl.edu.icm.coansys.similarity.pig.udf;

/**
 *
 * @author pdendek
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.python.google.common.base.Joiner;

public class SimplifyText extends EvalFunc<Tuple> {

	@Override
    public Tuple exec(Tuple input) throws ExecException{
            String title = (String) input.get(0);
            DataBag kwds = (DataBag) input.get(1);
            
            List<String> l = new ArrayList<String>();
            for(Tuple kwd : kwds){
            	l.add((String) kwd.get(0));
            }
            Collections.sort(l);
            
            Tuple t = TupleFactory.getInstance().newTuple();
            t.append(title + " " + Joiner.on(" ").join(l));
            
            return t;
    }
}