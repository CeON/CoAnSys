/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

import java.util.Date;

/**
 * @author pdendek
 * @author mwos
 */

public class GenUUID extends EvalFunc< String > {

    /**
     * @param Tuple input with one String - contributor name, 
     * for whom we want find unique id
     * @returns String UUID
     */		
	//TODO: na wejsciu DateBag nie pojedyncza Tupla, na wykscoi DateBag z String
	@Override
	public String exec( Tuple input ) throws IOException {	
		return "" + input.get(0) + (new Date()).getTime();
	}
}