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

package pl.edu.icm.coansys.statisticsgenerator.operationcomponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import javax.xml.bind.DatatypeConverter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class DateRangesPartitioner implements Partitioner {
    
    private TimeZone timezone = TimeZone.getTimeZone("Europe/Warsaw");
    private List<TimeBucket> timeBuckets;

    @Override
    public String partition(String inputField) {
        DateTime dt = new DateTime(DatatypeConverter.parseTime(inputField));
        for (TimeBucket bucket : timeBuckets) {
            if (bucket.inThisBucket(dt)) {
                return bucket.getLabel();
            }
        }
        return "none";
    }

    @Override
    public void setup(String... params) {
        List<Integer> numbers = new ArrayList<Integer>();
 
        DateTime end = new DateTime(DateTimeZone.forTimeZone(timezone)).withMillisOfDay(0).plusDays(1);

        for (String param : params) {
            if (param.contains("=")) {
                int eqIndex = param.indexOf('=');
                String paramName = param.substring(0, eqIndex);
                String paramValue = param.substring(eqIndex + 1);
                if (paramName.equals("timezone")) {
                    timezone = TimeZone.getTimeZone(paramValue);
                    TimeZone.setDefault(timezone);
                } else if (paramName.equals("logsEnd")) {
                    end = new DateTime(DatatypeConverter.parseTime(paramValue)).withMillisOfDay(0).plusDays(1);
                } else {
                    throw new IllegalArgumentException("Unknown param name: " + paramName);
                }
            } else {
               numbers.add(Integer.parseInt(param));
            }
        }
        
        Collections.sort(numbers);
        timeBuckets = new ArrayList<TimeBucket>();
        
        DateTime previousStart = end;
        String previousNumber = "0";
        
        for (Integer period : numbers) {
            DateTime newStart = end.minusDays(period).withMillisOfDay(0);
            String label = "[" + period + ", " + previousNumber + ")";
            timeBuckets.add(new TimeBucket(newStart, previousStart, label));
            previousStart = newStart;
            previousNumber = period.toString();
        }
        timeBuckets.add(new TimeBucket(previousStart, "(-inf, " + previousNumber + ")"));
    }
    
    private static class TimeBucket {
        boolean startInInfinity;
        private DateTime start;
        private DateTime end;
        private String label;
        
        public TimeBucket(DateTime start, DateTime end, String label) {
            this.startInInfinity = false;
            this.start = start;
            this.end = end;
            this.label = label;

        }
        
        public TimeBucket(DateTime end, String label) {
            this.startInInfinity = true;
            this.end = end;
            this.label = label;
        }


        public String getLabel() {
            return label;
        }
        
        public boolean inThisBucket(DateTime time) {
            return time.isBefore(this.end) && (startInInfinity || !time.isBefore(this.start));
        }
    }
}
