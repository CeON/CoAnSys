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

package pl.edu.icm.coansys.deduplication.document;

import java.util.Date;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import pl.edu.icm.coansys.deduplication.document.tool.MockDocumentMetadataFactory;
import com.google.common.collect.Lists;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import pl.edu.icm.coansys.models.DocumentProtos;


@Test
@ContextConfiguration(locations="classpath:spring/applicationContext.xml")
public class DuplicateWorkDetectReduceServiceTest extends AbstractTestNGSpringContextTests {

    private static Logger log = Logger.getLogger(DuplicateWorkDetectReduceServiceTest.class);
    
    @Autowired
    private DuplicateWorkDetectReduceService duplicateWorkDetectReduceService;
    
    private List<BytesWritable> documentsBW = Lists.newArrayList();
    //private List<DocumentProtos.DocumentMetadata> documents = Lists.newArrayList();
    @SuppressWarnings("unchecked")
    private Reducer<Text, BytesWritable, Text, Text>.Context context = Mockito.mock(Context.class);

    @BeforeTest
    public void setUp() throws Exception {

        for (int i = 0; i < 2000; i++) {
            DocumentProtos.DocumentMetadata dm = MockDocumentMetadataFactory.createDocumentMetadata("A brief story of time. From the Big Bang to Black Holes " + i);
            documentsBW.add(MockDocumentMetadataFactory.createDocumentWrapperBytesWritable(dm));
        }
        
        for (int i = 0; i <= 300; i++) {
            DocumentProtos.DocumentMetadata dm = MockDocumentMetadataFactory.createDocumentMetadata("The news in brief");
            documentsBW.add(MockDocumentMetadataFactory.createDocumentWrapperBytesWritable(dm));
        }
    }

    @Test
    public void testProcess() throws Exception {
        long startTime = new Date().getTime();
        duplicateWorkDetectReduceService.process(new Text(""), context, documentsBW, 100);
        long endTime = new Date().getTime();
        log.info("time[ms]: " + (endTime-startTime));
        
    }    
}