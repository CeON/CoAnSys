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

package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

import pl.edu.icm.coansys.disambiguation.idgenerators.IdGenerator;
import pl.edu.icm.coansys.disambiguation.idgenerators.UuIdGenerator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author pdendek
 * @author mwos
 */
public class GenUUID extends EvalFunc< String> {

    private IdGenerator idgenerator = new UuIdGenerator();

    /**
     * @param Tuple input with DataBag with Strings - contributors' names, 
     * for whom we want to find unique id
     * @returns String UUID
     */		
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
