package pl.edu.icm.coansys.disambiguation.work.tool;

import pl.edu.icm.coansys.commons.java.StringTools;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class StringToolsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetPositionOfTrailingInteger() {
        Assert.assertEquals(20, StringTools.getPositionOfTrailingInteger("Alice has got a cat 12"));
        Assert.assertEquals(19, StringTools.getPositionOfTrailingInteger("Alice has got a cat12234"));
        Assert.assertEquals(-1, StringTools.getPositionOfTrailingInteger("Alice has got a cat12234 sdsd"));
    }
    
    
    @Test
    public void testGetTrailingInteger() {
        Assert.assertEquals("12", StringTools.getTrailingInteger("Alice has got a cat 12"));
        Assert.assertEquals("12234", StringTools.getTrailingInteger("Alice has got a cat12234"));
        Assert.assertNull(StringTools.getTrailingInteger("Alice has got a cat12234 sdsd"));
    }

    
    @Test
    public void testIsRomanNumber() {
        Assert.assertTrue(StringTools.isRomanNumber("XXX"));
        Assert.assertFalse(StringTools.isRomanNumber("XAX"));
        Assert.assertTrue(StringTools.isRomanNumber("cXX"));
        Assert.assertFalse(StringTools.isRomanNumber("tXX"));
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidRomanToDecimal() {
        String romanNumber = "XXAX";
        StringTools.romanToDecimal(romanNumber);
    }
    
    @Test
    public void testRomanToDecimal() {
        String romanNumber = "XXX";
        Assert.assertEquals(30, StringTools.romanToDecimal(romanNumber));
        
        romanNumber = "MMCCX";
        Assert.assertEquals(2210, StringTools.romanToDecimal(romanNumber));
    }
    
    @Test
    public void testReplaceLastRomanNumberToDecimal() {
        String value = "Aloha scooby doo! part I";
        Assert.assertEquals("Aloha scooby doo! part 1", StringTools.replaceLastRomanNumberToDecimal(value));
        
        value = "Aloha II scooby doo! part XIX";
        Assert.assertEquals("Aloha II scooby doo! part 19", StringTools.replaceLastRomanNumberToDecimal(value));
        
        Assert.assertEquals("", StringTools.replaceLastRomanNumberToDecimal(""));
     }
    
    @Test
    public void testReplaceLastWordNumberToDecimal() {
        String value = "Aloha scooby doo! part One";
        Assert.assertEquals("Aloha scooby doo! part 1", StringTools.replaceLastWordNumberToDecimal(value));
        
        value = "Aloha II scooby doo! part ten";
        Assert.assertEquals("Aloha II scooby doo! part 10", StringTools.replaceLastWordNumberToDecimal(value));
        
        Assert.assertEquals("", StringTools.replaceLastWordNumberToDecimal(""));
     }
    
    
    @Test
    public void testNormalize() {
        String value = "Aloha II scooby doo! part XIX";
        Assert.assertEquals("aloha ii scooby doo part xix", StringTools.normalize(value));
    }
    
    @Test
    public void testNormalizeRussian() {
        String value = "Квантовый размерный эффект в трехмерных микрокристаллах полупроводников";
        Assert.assertEquals("квантовыи размерныи эффект в трехмерных микрокристаллах полупроводников", StringTools.normalize(value));
    }
    
    @Test
    public void testRemoveArticles() {
        Assert.assertEquals(" comparison ...", StringTools.removeStopWords("The comparison of ..."));
        Assert.assertEquals(" comparison ...", StringTools.removeStopWords("A comparison of ..."));
    }
}
