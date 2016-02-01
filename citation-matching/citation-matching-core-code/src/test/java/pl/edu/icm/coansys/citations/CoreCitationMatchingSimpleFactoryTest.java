package pl.edu.icm.coansys.citations;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.spark.api.java.JavaSparkContext;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import pl.edu.icm.coansys.citations.hashers.CitationNameYearHashGenerator;
import pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesHashGenerator;
import pl.edu.icm.coansys.citations.hashers.DocumentNameYearHashGenerator;
import pl.edu.icm.coansys.citations.hashers.DocumentNameYearNumNumHashGenerator;

/**
 * @author madryk
 */
public class CoreCitationMatchingSimpleFactoryTest {

    private CoreCitationMatchingSimpleFactory coreCitationMatchingFactory = new CoreCitationMatchingSimpleFactory();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void createCoreCitationMatchingService() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        // given
        
        JavaSparkContext sparkContext = mock(JavaSparkContext.class);
        
        String hashersPair1 = CitationNameYearHashGenerator.class.getName() + ":" + DocumentNameYearHashGenerator.class.getName();
        String hashersPair2 = CitationNameYearPagesHashGenerator.class.getName() + ":" + DocumentNameYearNumNumHashGenerator.class.getName();
        
        
        // execute
        
        CoreCitationMatchingService citationMatchingService = coreCitationMatchingFactory.createCoreCitationMatchingService(sparkContext, 1000, Lists.newArrayList(hashersPair1, hashersPair2));
        
        
        // assert
        
        Object retSparkContext = Whitebox.getInternalState(citationMatchingService, "sparkContext");
        assertTrue(retSparkContext == sparkContext);
        
        Object retMaxHashBucketSize = Whitebox.getInternalState(citationMatchingService, "maxHashBucketSize");
        assertEquals(retMaxHashBucketSize, 1000l);
        
        
        @SuppressWarnings("unchecked")
        List<Pair<MatchableEntityHasher, MatchableEntityHasher>> retHashers = (List<Pair<MatchableEntityHasher, MatchableEntityHasher>>) Whitebox.getInternalState(citationMatchingService, "matchableEntityHashers");
        
        assertEquals(retHashers.size(), 2);
        
        Object citHashGenerator1 = Whitebox.getInternalState(retHashers.get(0).getLeft(), "hashGenerator");
        assertTrue(citHashGenerator1 instanceof CitationNameYearHashGenerator);
        
        Object docHashGenerator1 = Whitebox.getInternalState(retHashers.get(0).getRight(), "hashGenerator");
        assertTrue(docHashGenerator1 instanceof DocumentNameYearHashGenerator);
        
        Object citHashGenerator2 = Whitebox.getInternalState(retHashers.get(1).getLeft(), "hashGenerator");
        assertTrue(citHashGenerator2 instanceof CitationNameYearPagesHashGenerator);
        
        Object docHashGenerator2 = Whitebox.getInternalState(retHashers.get(1).getRight(), "hashGenerator");
        assertTrue(docHashGenerator2 instanceof DocumentNameYearNumNumHashGenerator);
        
        
        Object retDocumentAttacher = Whitebox.getInternalState(citationMatchingService, "documentAttacher");
        assertNotNull(retDocumentAttacher);
        
        Object retCitationAttacher = Whitebox.getInternalState(citationMatchingService, "citationAttacher");
        assertNotNull(retCitationAttacher);
        
        Object retBestPicker = Whitebox.getInternalState(citationMatchingService, "bestMatchedCitationPicker");
        assertNotNull(retBestPicker);
        
        Object retHashCitationMatcherFactory = Whitebox.getInternalState(citationMatchingService, "heuristicHashCitationMatcherFactory");
        assertNotNull(retHashCitationMatcherFactory);
        
        
    }
    
}
