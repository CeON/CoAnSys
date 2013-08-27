package pl.edu.icm.coansys.disambiguation.clustering.strategies;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

public class CompleteLinkageHACStrategy_StateOfTheArtTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CompleteLinkageHACStrategy_StateOfTheArtTest.class);
	
	@Test
	public void smallExampleTest(){
        float[][] in = {{}, {15}, {-46, -3}, {-2, -18, -20}, {-100, -100, -3, -200}};
        int[] out = null;
        try{
        	out = new CompleteLinkageHACStrategy_StateOfTheArt().clusterize(in);
        }catch(Exception e){
        	logger.error(StackTraceExtractor.getStackTrace(e));
        	Assert.fail();
        }
         
        StringBuilder sb = new StringBuilder("");
        for (int i : out) {
            sb.append(i).append(" ");
        }
        Assert.assertEquals("Elements are not correctly assigned","1 1 2 3 4 ",sb.toString());
	}

	//@Test
	public void oneCluster(){
        float[][] in = {{}, {15}, {15, 15}};
        int[] out = null;
        try{
        	out = new CompleteLinkageHACStrategy_StateOfTheArt().clusterize(in);
        }catch(Exception e){
        	logger.error(StackTraceExtractor.getStackTrace(e));
        	Assert.fail();
        }
         
        StringBuilder sb = new StringBuilder("");
        for (int i : out) {
            sb.append(i).append(" ");
        }
        Assert.assertEquals("Elements are not correctly assigned","1 1 1 ",sb.toString());
	}

	//@Test
	public void oneCluster2(){
        float[][] in = {{}, {-1}, {-1, -1}};
        int[] out = null;
        try{
        	out = new CompleteLinkageHACStrategy_StateOfTheArt().clusterize(in);
        }catch(Exception e){
        	logger.error(StackTraceExtractor.getStackTrace(e));
        	Assert.fail();
        }
         
        StringBuilder sb = new StringBuilder("");
        for (int i : out) {
            sb.append(i).append(" ");
        }
        Assert.assertEquals("Elements are not correctly assigned","0 1 2 ",sb.toString());
	}
	
}
