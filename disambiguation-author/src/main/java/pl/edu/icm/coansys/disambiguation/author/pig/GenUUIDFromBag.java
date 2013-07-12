/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import java.util.Arrays;
import java.util.Date;

/**
 * @author pdendek
 * @author mwos
 */

public class GenUUIDFromBag extends EvalFunc< DataBag > {

    /**
     * @param Tuple input with one String - contributor name, 
     * for whom we want find unique id
     * @returns String UUID
     */		
	@Override
	public DataBag exec( Tuple input ) throws IOException {	
		
		DataBag ret = new DefaultDataBag();
		DataBag tuples = (DataBag) input.get(0);
		
		for ( Tuple tpl : tuples )
		{
			String sname = (String) tpl.get(0);
			String uuid = sname + (new Date()).getTime();
			Object[] to = new Object[]{uuid};
			Tuple t = TupleFactory.getInstance().newTuple( Arrays.asList(to) );
			ret.add(t);
		}
		
		return ret;
	}
}