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

package pl.edu.icm.coansys.disambiguation.work;

import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.coansys.disambiguation.work.tool.MockDocumentMetadataFactory;
import pl.edu.icm.coansys.models.DocumentProtos.Author;

import com.google.common.collect.Lists;
import pl.edu.icm.coansys.models.DocumentProtos;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext.xml")
public class DuplicateWorkServiceTest {
    
    private static Logger log = LoggerFactory.getLogger(DuplicateWorkServiceTest.class);

    @Autowired
    private DuplicateWorkService duplicateWorkService;
    
    
    private List<DocumentProtos.DocumentMetadata> documentWrappers = Lists.newArrayList();
    
    private DocumentProtos.DocumentMetadata documentWrapper1;
    private DocumentProtos.DocumentMetadata documentWrapper5;
    
    @Before
    public void setUp() throws Exception {
        Author janKowalski = MockDocumentMetadataFactory.createAuthor("Jan", "Kowalski", 1);
        Author adamNowak = MockDocumentMetadataFactory.createAuthor("Adam", "Nowak", 2);
        
        documentWrapper1 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota a", 2012, janKowalski, adamNowak);
        DocumentProtos.DocumentMetadata documentWrapper2 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota b", 2012, janKowalski, adamNowak);
        DocumentProtos.DocumentMetadata documentWrapper3 = MockDocumentMetadataFactory.createDocumentMetadata("Ala ma kota g", 2012, janKowalski, adamNowak);
        DocumentProtos.DocumentMetadata documentWrapper4 = MockDocumentMetadataFactory.createDocumentMetadata("Ala mna kota f", 2012, janKowalski, adamNowak);
        documentWrapper5 = MockDocumentMetadataFactory.createDocumentMetadata("Ola ma fajnego psiaka 300", 2012, janKowalski, adamNowak);
        DocumentProtos.DocumentMetadata documentWrapper6 = MockDocumentMetadataFactory.createDocumentMetadata("Ola mma fajnego pisaka 300", 2012, janKowalski, adamNowak);
        
        documentWrappers.add(documentWrapper1);
        documentWrappers.add(documentWrapper2);
        documentWrappers.add(documentWrapper3);
        documentWrappers.add(documentWrapper4);
        documentWrappers.add(documentWrapper5);
        documentWrappers.add(documentWrapper6);
    }

    @Test
    public void testFindDuplicates() {
        Map<Integer, Set<DocumentProtos.DocumentMetadata>> duplicates = duplicateWorkService.findDuplicates(documentWrappers);
        for (Map.Entry<Integer, Set<DocumentProtos.DocumentMetadata>> entry : duplicates.entrySet()) {
            log.info("key   : {}", ""+entry.getKey());
            for (DocumentProtos.DocumentMetadata documentMetadata: entry.getValue()) {
                log.info("------ title0: {}", DocumentWrapperUtils.getMainTitle(documentMetadata));
            }
        }
        Assert.assertEquals(2, duplicates.size());
        for (Map.Entry<Integer, Set<DocumentProtos.DocumentMetadata>> entry : duplicates.entrySet()) {
            if (entry.getValue().contains(documentWrapper1)) {
                Assert.assertEquals(4, entry.getValue().size());
            }
            else if (entry.getValue().contains(documentWrapper5)) {
                Assert.assertEquals(2, entry.getValue().size());
            } else {
                Assert.fail();
            }
        }
    }

}
