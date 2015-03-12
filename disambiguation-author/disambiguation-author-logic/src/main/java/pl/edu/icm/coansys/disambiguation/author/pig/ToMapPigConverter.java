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

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mwos
 */
public class ToMapPigConverter extends EvalFunc< Map<String, Object> > {

	@Override
	public Map<String, Object> exec( Tuple input ) throws ExecException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for( int i = 0; i < input.size(); i+=2 ){
			String key = (String) input.get(i);
			Object val = input.get(i + 1);
			map.put(key, val);
		}
		
		return map;
		
	}

}
