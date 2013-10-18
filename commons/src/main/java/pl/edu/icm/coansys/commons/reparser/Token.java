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

/**
 * @author Lukasz Bolikowski (bolo@icm.edu.pl)
 *
 */
public class Token {
	
	private String content;
	private int start = -1;
	private int end = -1;
	
	private Token previous;
	private Token next;
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}

	public Token getNext() {
		return next;
	}

	public void setNext(Token next) {
		this.next = next;
	}

	public Token getPrevious() {
		return previous;
	}

	public void setPrevious(Token previous) {
		this.previous = previous;
	}
	
	@Override
	public String toString() {
		return content;
	}
}
