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
package pl.edu.icm.coansys.commons.reparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * @author Lukasz Bolikowski (bolo@icm.edu.pl)
 *
 */
public class Node {
	private String type;
	private String name;
	private String value;
	private Map<String, List<Node>> fields;
	private Node nextAlternative;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void addField(String key, Node value) {
		if (fields == null)
			fields = new HashMap<String, List<Node>>();
		List<Node> values;
		if (fields.containsKey(key))
			values = fields.get(key);
		else {
			values = new ArrayList<Node>(1);
			fields.put(key, values);
		}
		values.add(value);
	}
	
	public List<Node> getFields(String key) {
		return fields.get(key);
	}
	
	public Node getFirstField(String key) {
		if (fields == null)
			return null;
		List<Node> values = fields.get(key);
		if (values == null)
			return null;
		return values.get(0);
	}

	public Set<String> getFieldNames() {
		return fields.keySet();
	}
	
	@Override
	public String toString() {
		return "Node(" + getName() + ", '" + getValue() + "')";
	}

	public Node getNextAlternative() {
		return nextAlternative;
	}

	public void setNextAlternative(Node nextAlternative) {
		this.nextAlternative = nextAlternative;
	}
}
