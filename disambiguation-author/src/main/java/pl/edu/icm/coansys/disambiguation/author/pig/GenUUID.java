/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

import pl.edu.icm.coansys.disambiguation.idgenerators.IdGenerator;
import pl.edu.icm.coansys.disambiguation.idgenerators.UuIdGenerator;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author pdendek
 * @author mwos
 */

public class GenUUID extends EvalFunc< String > {

	IdGenerator idgenerator = new UuIdGenerator();
	
    /**
     * @param Tuple input with one String - contributor name, 
     * for whom we want find unique id
     * @returns String UUID
     */		
	//TODO: na wejsciu DateBag nie pojedyncza Tupla, na wykscoi DateBag z String
	@Override
	public String exec( Tuple input ) throws IOException {
		DataBag db = (DataBag) input.get(0);
		List<String> l = new LinkedList<String>();
		Iterator<Tuple> it = db.iterator();
		while(it.hasNext()){
			l.add((String) it.next().get(0));
		}
		return idgenerator.genetareId(l);
	}
}