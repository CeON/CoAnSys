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

package pl.edu.icm.coansys.disambiguation.work;

import org.junit.Assert;
import org.junit.Test;

import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentWrapperFactory;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

public class WorkKeyGeneratorTest {

    
    @Test
    public void testGenerateKey() {
        DocumentWrapper doc = MockDocumentWrapperFactory.createDocumentWrapper("A comparison of associated dsd sd");
        
        Assert.assertEquals("compa", WorkKeyGenerator.generateKey(doc, 0));
        Assert.assertEquals("comparison", WorkKeyGenerator.generateKey(doc, 1));
        Assert.assertEquals("comparisonassoc", WorkKeyGenerator.generateKey(doc, 2));
    }
}
