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

package pl.edu.icm.coansys.statisticsgenerator.filters;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import pl.edu.icm.coansys.models.StatisticsProtos;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterComponent;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterEq;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterNotEmpty;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class LogicalEvaluatorTest {

    @Test
    public void testEvaluator() {
        Map<String, FilterComponent> components = new HashMap<String, FilterComponent>();
        FilterComponent fc1 = new FilterNotEmpty();
        fc1.setup("bookId");
        components.put("book", fc1);
        FilterComponent fc2 = new FilterEq();
        fc2.setup("bookId", "book-03426");
        components.put("book26", fc2);
        FilterComponent fc3 = new FilterEq();
        fc3.setup("bookId", "book-03427");
        components.put("book27", fc3);

        
        InputFilter ifilter1 = new InputFilter("book", components);
        InputFilter ifilter2 = new InputFilter("book and book26", components);
        InputFilter ifilter3 = new InputFilter("book and book27", components);
        InputFilter ifilter4 = new InputFilter("not book or not book27", components);
        InputFilter ifilter5 = new InputFilter("(book27 and book26) or book", components);
        
        StatisticsProtos.InputEntry.Builder ieb = StatisticsProtos.InputEntry.newBuilder();
        ieb.setLabel("testInputEntry");
        StatisticsProtos.KeyValue.Builder kvb = StatisticsProtos.KeyValue.newBuilder();
        kvb.setKey("bookId");
        kvb.setValue("book-03426");
        ieb.addField(kvb);
        StatisticsProtos.InputEntry ie = ieb.build();

        
        assertTrue(ifilter1.filter(ie));
        assertTrue(ifilter2.filter(ie));
        assertFalse(ifilter3.filter(ie));
        assertTrue(ifilter4.filter(ie));
        assertTrue(ifilter5.filter(ie));
    }

}
