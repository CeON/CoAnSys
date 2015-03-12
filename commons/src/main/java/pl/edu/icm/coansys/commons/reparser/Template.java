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
package pl.edu.icm.coansys.commons.reparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lukasz Bolikowski (bolo@icm.edu.pl)
 *
 */
public class Template {

	private List<NodeCategory> fields = new ArrayList<NodeCategory>();
	private String type;
	
	private Pattern patternCache;
	
	private void invalidatePatternCache() {
		patternCache = null;
	}
	
	public void addField(NodeCategory nc) {
		if (nc == null)
			return;
		invalidatePatternCache();
		fields.add(nc);
	}

	public List<NodeCategory> getFields() {
		return fields;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		boolean first = true;
		for (NodeCategory nc : fields) {
			ret.append((first ? "" : " ")).append(nc.toString());
			first = false;
		}
		return "{" + ret.toString() + "}";
	}

	public String getRegexp() {
		StringBuilder regexp = new StringBuilder();
		for (NodeCategory nc : fields) {
			regexp.append(nc.getRegexp());
		}
		return "^" + regexp.toString() + "$";
	}

	private Pattern getPattern() {
		if (patternCache == null)
			patternCache = Pattern.compile(getRegexp());
		return patternCache;
	}
	
	private String spaces(int count) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < count; i++) {
			ret.append(" ");
                }
		return ret.toString();
	}
	
	private String findMatchedText(List<Token> tokens, int start, int end) {
		StringBuilder res = new StringBuilder();
		int curStart = 0;
		for (Token t : tokens) {
			final int curLen = t.getContent().length();
			final int curEnd = curStart + curLen;
			if (start < curEnd && end > curStart) {
				//TODO: concatenate only the relevant (matched) part of the token
				res.append(t.getContent());
				//TODO: implement without using getNext()/getPrevious()
				if (t.getNext() != null)
					res.append(spaces(t.getNext().getStart() - t.getEnd() - 1));
			}
			curStart += curLen + 1;
		}
		return res.toString();
	}

	public Node match(List<Token> tokens) {
		StringBuilder text = new StringBuilder();
		for (Token t : tokens) {
			text.append(t.getContent()).append(" ");
		}

		Node n = new Node();
		n.setType(getType());
		
		Pattern p = getPattern();
		Matcher m = p.matcher(text.toString());
		if (!m.matches())
			return null;
		
		int index = 1;
		for (NodeCategory nc : getFields()) {
			if (nc.getName() == null)
				continue;
			String matchedText = findMatchedText(tokens, m.start(index), m.end(index));
			Node nn = nc.match(matchedText);
			if (nn == null)
				return null;
			n.addField(nc.getName(), nn);
			index++;
				
		}
		return n;
	}
}
