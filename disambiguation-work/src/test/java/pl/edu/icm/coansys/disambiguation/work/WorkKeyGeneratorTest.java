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
