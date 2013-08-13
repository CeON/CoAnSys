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
import java.io.File;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.Bw2ProtoFileUtils;
import pl.edu.icm.coansys.disambiguation.work.tool.DuplicateGenerator;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;


public class DuplicateWorkDetectorTest {
    
    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectorTest.class);
    
    private URL baseOutputUrl = this.getClass().getResource("/");
    private String outputDir = baseOutputUrl.getPath() + "/testOut";
    
    
    @Before
    public void before() throws Exception{
        URL inputSeqFileUrl = this.getClass().getResource("/publications.seq");
        ToolRunner.run(new Configuration(), new DuplicateGenerator(), new String[]{inputSeqFileUrl.getFile(), this.getClass().getResource("/").getFile()});
        FileUtils.deleteDirectory(new File(outputDir));
        URL inputFileUrl = this.getClass().getResource("/generated/ambiguous-publications.seq");
        ToolRunner.run(new Configuration(), new DuplicateWorkDetector(), new String[]{inputFileUrl.getPath(), outputDir});
    }
    
    @After
    public void after() throws Exception{
        FileUtils.deleteDirectory(new File(outputDir));
    }
    
    @Test
    public void test() throws Exception {
        List<DocumentWrapper> docWrappers = Bw2ProtoFileUtils.readDocWrappers(outputDir+"/part-r-00000");
        for (DocumentWrapper doc : docWrappers) {
            log.info(DocumentWrapperUtils.getMainTitle(doc));
        }
        Assert.assertEquals(6, docWrappers.size());
       
    }

    
}
