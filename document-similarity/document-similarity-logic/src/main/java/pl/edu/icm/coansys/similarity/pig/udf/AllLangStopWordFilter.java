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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class AllLangStopWordFilter extends EvalFunc<Boolean> {

	private Set<String> stopwords = new HashSet<String>();
	
	/**
	 * The example of the very ugly constructor
	 *  
	 * @throws IOException
	 */
	public AllLangStopWordFilter() throws IOException{
		String root = "stopwords/stopwords_";
		String[] langs = new String[]{"de","dk","en","es","fi","fr","hu","it","nl","no","pl","pt","ru","se","tr"};
		for(String ln : langs){
			InputStream stopwordsStream;
	        InputStreamReader isr;
	        BufferedReader br = null;
	        stopwordsStream = AllLangStopWordFilter.class.getClassLoader().getResourceAsStream(root+ln+".txt");
			
	        try {
	            isr = new InputStreamReader(stopwordsStream, Charset.forName("UTF-8"));
	            br = new BufferedReader(isr);

	            String stopword = br.readLine();
	            while (stopword != null) {
	                stopword = stopword.trim();
	                if (!stopword.isEmpty()) {
	                    stopwords.add(stopword);
	                }
	                stopword = br.readLine();
	            }
	        }
	        finally {
	            IOUtils.closeQuietly(br);
	        }	
	    }
	}
	
    public Boolean isInAllStopwords(String input) throws IOException {
            return stopwords.contains(input);
    }
	
    @Override
    public Boolean exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return false;
        }
        try {
            String word = (String) input.get(0);
            return !stopwords.contains(word);
                
        } catch (Exception e) {
            throw new IOException("Caught exception processing input row ", e);
        }
    }
}
