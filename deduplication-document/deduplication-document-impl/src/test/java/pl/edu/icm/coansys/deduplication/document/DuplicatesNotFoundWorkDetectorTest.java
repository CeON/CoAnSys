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

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.commons.hadoop.SequenceFileUtils;
import pl.edu.icm.coansys.deduplication.document.tool.DuplicateGenerator;

public class DuplicatesNotFoundWorkDetectorTest {
    
    private static Logger log = LoggerFactory.getLogger(DuplicatesNotFoundWorkDetectorTest.class);
    
    private URL baseOutputUrl = this.getClass().getResource("/");
    private String outputDir = baseOutputUrl.getPath() + "/testOut";
    
    
    @BeforeTest
    public void before() throws Exception{
        URL inputSeqFileUrl = this.getClass().getResource("/not_deduplicated.seq");
        ToolRunner.run(new Configuration(), new DuplicateGenerator(), new String[]{inputSeqFileUrl.getFile(), this.getClass().getResource("/").getFile()});
        FileUtils.deleteDirectory(new File(outputDir));
        URL inputFileUrl = this.getClass().getResource("/generated/ambiguous-publications.seq");
        ToolRunner.run(new Configuration(), new DuplicateWorkDetector(), new String[]{inputFileUrl.getPath(), outputDir});
    }
    
    @AfterTest
    public void after() throws Exception{
        FileUtils.deleteDirectory(new File(outputDir));
    }
    
    @Test(enabled=true)
    public void test() throws Exception {
        List<String> docIds = SequenceFileUtils.readTexts(outputDir+"/part-r-00000");
        System.out.println(outputDir);
        Assert.assertEquals(docIds.size(),2);
    }
}