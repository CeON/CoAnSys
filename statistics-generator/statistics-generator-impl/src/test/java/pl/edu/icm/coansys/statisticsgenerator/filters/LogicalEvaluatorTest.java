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
package pl.edu.icm.coansys.statisticsgenerator.filters;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import pl.edu.icm.coansys.models.StatisticsProtos;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterComponent;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterContains;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterEq;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterNotEmpty;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.FilterTwoFieldsEqual;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class LogicalEvaluatorTest {

    @Test
    public void testEvaluator() throws InstantiationException, IllegalAccessException {

        StatisticsProtos.InputEntry.Builder ieb = StatisticsProtos.InputEntry.newBuilder();
        ieb.setLabel("testInputEntry");
        addKV(ieb, "bookId", "book-03426");
        addKV(ieb, "elId", "book-03426");
        addKV(ieb, "authorId", "author-571411");
        StatisticsProtos.InputEntry ie = ieb.build();

        Map<String, FilterComponent> components = new HashMap<String, FilterComponent>();
        addFilterComponent(components, FilterNotEmpty.class, "book", "bookId");
        addFilterComponent(components, FilterEq.class, "book26", "bookId", "book-03426");
        addFilterComponent(components, FilterEq.class, "book27", "bookId", "book-03427");
        addFilterComponent(components, FilterContains.class, "auth1411", "authorId", "1411");
        addFilterComponent(components, FilterTwoFieldsEqual.class, "elbook", "elId", "bookId");

        checkFilter("book", components, ie, true);
        checkFilter("book and book26", components, ie, true);
        checkFilter("book and book27", components, ie, false);
        checkFilter("not book or not book27", components, ie, true);
        checkFilter("(book27 and book26) or book", components, ie, true);
        checkFilter("not book or (auth1411 and elbook)", components, ie, true);
    }

    private static void addKV(StatisticsProtos.InputEntry.Builder ieb, String key, String value) {
        StatisticsProtos.KeyValue.Builder kvb = StatisticsProtos.KeyValue.newBuilder();
        kvb.setKey(key);
        kvb.setValue(value);
        ieb.addField(kvb);
    }

    private static void addFilterComponent(Map<String, FilterComponent> components, Class<? extends FilterComponent> componentCls,
            String label, String... parameters) throws InstantiationException, IllegalAccessException {
        FilterComponent fc = componentCls.newInstance();
        fc.setup(parameters);
        components.put(label, fc);
    }

    private static void checkFilter(String formula, Map<String, FilterComponent> components, 
            StatisticsProtos.InputEntry ie, boolean expectedResult) {
        InputFilter ifilter = new InputFilter(formula, components);
        if (expectedResult) {
            assertTrue(ifilter.filter(ie));
        } else {
            assertFalse(ifilter.filter(ie));
        }
    }
}
