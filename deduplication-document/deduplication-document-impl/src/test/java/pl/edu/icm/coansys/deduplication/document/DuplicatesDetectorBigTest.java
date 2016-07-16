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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.commons.hadoop.SequenceFileUtils;
import pl.edu.icm.coansys.commons.java.Pair;
import pl.edu.icm.coansys.deduplication.document.tool.DuplicateGenerator;

public class DuplicatesDetectorBigTest {
    
    private static Logger log = LoggerFactory.getLogger(DuplicatesDetectorBigTest.class);
    
    private URL baseOutputUrl = this.getClass().getResource("/");
    private String outputDir = baseOutputUrl.getPath() + "/bigTestOut";
    
    
    @BeforeTest
    public void before() throws Exception{
        URL inputSeqFileUrl = this.getClass().getResource("/test_input_yadda_2000.seq");
        ToolRunner.run(new Configuration(), new DuplicateGenerator(), new String[]{inputSeqFileUrl.getFile(), this.getClass().getResource("/").getFile()});
        FileUtils.deleteDirectory(new File(outputDir));
        URL inputFileUrl = this.getClass().getResource("/generated/ambiguous-publications.seq");
        ToolRunner.run(new Configuration(), new DuplicateWorkDetector(), new String[]{inputFileUrl.getPath(), outputDir});
    }
    
    @AfterTest
    public void after() throws Exception{
        FileUtils.deleteDirectory(new File(outputDir));
    }
    
    
    public void addPairToHashMap(HashMap<String,Set<String>> map, Pair<String,String> p) {
        Set <String>s =map.get(p.getX());
        if (s==null) {
            s=new HashSet<>();
            map.put(p.getX(), s);
        }
        s.add(p.getY());
    }
    
    
    public List<Pair<String,String>> getDupicatesPairsFromMap( HashMap<String,Set<String>> map ) {
        List<Pair<String,String>> li=new ArrayList<>();
        for (Set<String> s:map.values()){
            for (String a:s) {
                for (String b:s){
                    if (a!=b) {
                        li.add(new Pair<>(a,b));
                    }
                }
            }
        }
        return li;
    }
    
    
    public List<Pair<String,String>> readResultInput(InputStream is) throws IOException{
        List<Pair<String,String>> ret=new ArrayList<>();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String s=null;
        while ((s=br.readLine())!=null) {
            String[] sp=s.split(",");
            ret.add(new Pair<>(sp[0].trim(),sp[1].trim()));
        }
        br.close();
        return ret;
    }
    
    @Test(enabled=true)
    public void test() throws Exception {
        List<Pair<String,String>> expectedPositive=readResultInput(this.getClass().getResourceAsStream("/real_duplicates_yadda_2000"));
        List<Pair<String,String>> expectedNegative=readResultInput(this.getClass().getResourceAsStream("/false_duplicates_yadda_2000"));
        HashMap<String,Set<String>> map=new HashMap<>();
        for (File f :FileUtils.listFiles(new File(outputDir), new RegexFileFilter("part-r-\\d{5}"), FalseFileFilter.FALSE) ) {
            List<Pair<String, String>> docIds = SequenceFileUtils.readTextPairs(outputDir + "/"+f.getName());
            for (Pair<String, String> p : docIds) {
                addPairToHashMap(map, p);
            }
        }
        List<Pair<String,String>> results=getDupicatesPairsFromMap(map);
        
        int truePositive=0;
        int falsePositive=0;
        int trueNegative=0;
        int falseNegative=0;
        
        
        for (Pair<String,String> s:expectedPositive) {
            if (results.contains(s)) {
                truePositive++;
            } else {
                falseNegative++;
                System.out.println("False negative pair: "+s.getX()+","+s.getY());
            }
            
        }
        for (Pair<String,String> s:expectedNegative) {
            if (results.contains(s)) {
                falsePositive++;
                System.out.println("False positive pair: "+s.getX()+","+s.getY());
            } else {
                trueNegative++;
            }
            
        }
        System.out.println("true positive: "+truePositive);
        System.out.println("false positive: "+falsePositive);
        System.out.println("false negative: "+falseNegative);
        System.out.println("true negative: "+trueNegative);
        
        System.err.println("Precission: "+(truePositive/((double) truePositive+falsePositive))); 
        System.err.println("Recall: "+(truePositive/((double) truePositive+falseNegative))); 
        System.err.println("true negative rate: "+(trueNegative/((double) trueNegative+falsePositive))); 
        System.err.println("Accuacy: "+((truePositive+trueNegative)/((double) truePositive+falsePositive+trueNegative+falseNegative))); 
        
        System.out.println(outputDir);
       
    }
}