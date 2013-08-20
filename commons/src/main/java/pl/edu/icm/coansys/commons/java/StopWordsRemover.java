/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.commons.java;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pdendek
 *
 */
public final class StopWordsRemover {

    private static volatile List<String> stopwords;

    static {
        String sw = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,"
                + "be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,"
                + "for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,"
                + "just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,"
                + "of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,"
                + "than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,"
                + "wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";
        stopwords = Arrays.asList(sw.split(","));

    }

    private StopWordsRemover() {
    }

    public static boolean isAnEnglishStopWords(String input) throws IOException {
        return stopwords.contains(input);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Is 'by' a stop word?: " + isAnEnglishStopWords("by"));
        System.out.println("Is 'eclipse' a stop word?: " + isAnEnglishStopWords("eclipse"));
    }
}
