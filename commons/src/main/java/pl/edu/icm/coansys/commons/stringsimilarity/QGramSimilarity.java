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
package pl.edu.icm.coansys.commons.stringsimilarity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This code is derived from Fine-Grained Record Integration and Linkage Tool
 * written by Pawel Jurczyk (http://fril.sourceforge.net/)
 * 
 * @author acz
 */
public class QGramSimilarity extends SimilarityCalculator {

    public static final float APPROVE = 0.2f;
    public static final float DISAPPROVE = 0.4f;
    private int qgram = 3;
    private ThreadLocal gramsS1 = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new HashMap();
        }
    };
    private ThreadLocal gramsS2 = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new HashMap();
        }
    };

    private class Counter {
        int count = 1;
    }

    @Override
    public float doCalculate(String s1, String s2) {
        float distance = distanceInt(s1, s2);
        float approve = (int) Math.round((((Map) gramsS1.get()).size() + ((Map) gramsS2.get()).size()) * APPROVE);
        float disapprove = (int) Math.round((((Map) gramsS1.get()).size() + ((Map) gramsS2.get()).size()) * DISAPPROVE);
        ((Map) gramsS1.get()).clear();
        ((Map) gramsS2.get()).clear();
        if (distance > disapprove) {
            return 0f;
        } else if (distance < approve) {
            return 1.0f;
        } else {
            if (disapprove == approve) {
                return 0f;
            } else {
                return (1f - 1f / (disapprove - approve) * (distance - approve));
            }
        }
    }

    private int distanceInt(String s1, String s2) {

        int qgramLoc;
        if (this.qgram == -1) {
            qgramLoc = findGram(s1.length() + s2.length());
        } else {
            qgramLoc = this.qgram;
        }

        findGrams(s1, ((Map) gramsS1.get()), qgramLoc);
        findGrams(s2, ((Map) gramsS2.get()), qgramLoc);

        int distance = measureDistance(((Map) gramsS1.get()), ((Map) gramsS2.get()));

        return distance;
    }

    private void findGrams(String string, Map gramsS1, int q) {
        for (int i = 0; i < string.length(); i++) {
            String qgramStr = string.substring(i, (i + q) > string.length() ? string.length() : (i + q));
            Counter counter = (Counter) gramsS1.get(qgramStr);
            if (counter == null) {
                Counter c = new Counter();
                gramsS1.put(qgramStr, c);
            } else {
                counter.count++;
            }
        }
    }

    private int findGram(int size) {
        int q = 1;
        if (size > 60) {
            q = 4;
        } else if (size > 40) {
            q = 3;
        } else if (size > 20) {
            q = 2;
        }
        return q;
    }

    private int measureDistance(Map gramsS1, Map gramsS2) {
        int diff = 0;
        for (Map.Entry entry : (Set<Map.Entry>) gramsS1.entrySet()) {
            String gramS1 = (String) entry.getKey();
            Counter counterS1 = (Counter) entry.getValue();
            Counter counterS2 = (Counter) gramsS2.get(gramS1);
            if (counterS2 == null) {
                diff += counterS1.count;
            } else {
                diff += Math.abs(counterS1.count - counterS2.count);
            }
        }

        for (Map.Entry entry : (Set<Map.Entry>) gramsS2.entrySet()) {
            String gramS2 = (String) entry.getKey();
            if (!gramsS1.containsKey(gramS2)) {
                Counter counterS2 = (Counter) entry.getValue();
                diff += counterS2.count;
            }
        }
        return diff;
    }
}
