package pl.edu.icm.coansys.disambiguation.work.tool;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class StringUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetPositionOfTrailingInteger() {
        Assert.assertEquals(20, StringUtils.getPositionOfTrailingInteger("Alice has got a cat 12"));
        Assert.assertEquals(19, StringUtils.getPositionOfTrailingInteger("Alice has got a cat12234"));
        Assert.assertEquals(-1, StringUtils.getPositionOfTrailingInteger("Alice has got a cat12234 sdsd"));
    }
    
    
    @Test
    public void testGetTrailingInteger() {
        Assert.assertEquals("12", StringUtils.getTrailingInteger("Alice has got a cat 12"));
        Assert.assertEquals("12234", StringUtils.getTrailingInteger("Alice has got a cat12234"));
        Assert.assertNull(StringUtils.getTrailingInteger("Alice has got a cat12234 sdsd"));
    }

    
    @Test
    public void testIsRomanNumber() {
        Assert.assertTrue(StringUtils.isRomanNumber("XXX"));
        Assert.assertFalse(StringUtils.isRomanNumber("XAX"));
        Assert.assertTrue(StringUtils.isRomanNumber("cXX"));
        Assert.assertFalse(StringUtils.isRomanNumber("tXX"));
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidRomanToDecimal() {
        String romanNumber = "XXAX";
        StringUtils.romanToDecimal(romanNumber);
    }
    
    @Test
    public void testRomanToDecimal() {
        String romanNumber = "XXX";
        Assert.assertEquals(30, StringUtils.romanToDecimal(romanNumber));
        
        romanNumber = "MMCCX";
        Assert.assertEquals(2210, StringUtils.romanToDecimal(romanNumber));
    }
    
    @Test
    public void testReplaceLastRomanNumberToDecimal() {
        String value = "Aloha scooby doo! part I";
        Assert.assertEquals("Aloha scooby doo! part 1", StringUtils.replaceLastRomanNumberToDecimal(value));
        
        value = "Aloha II scooby doo! part XIX";
        Assert.assertEquals("Aloha II scooby doo! part 19", StringUtils.replaceLastRomanNumberToDecimal(value));
        
        Assert.assertEquals("", StringUtils.replaceLastRomanNumberToDecimal(""));
     }
    
    @Test
    public void testReplaceLastWordNumberToDecimal() {
        String value = "Aloha scooby doo! part One";
        Assert.assertEquals("Aloha scooby doo! part 1", StringUtils.replaceLastWordNumberToDecimal(value));
        
        value = "Aloha II scooby doo! part ten";
        Assert.assertEquals("Aloha II scooby doo! part 10", StringUtils.replaceLastWordNumberToDecimal(value));
        
        Assert.assertEquals("", StringUtils.replaceLastWordNumberToDecimal(""));
     }
    
    
    @Test
    public void testNormalize() {
        String value = "Aloha II scooby doo! part XIX";
        Assert.assertEquals("aloha ii scooby doo part xix", StringUtils.normalize(value));
    }
    
    @Test
    public void testNormalizeRussian() {
        String value = "Квантовый размерный эффект в трехмерных микрокристаллах полупроводников";
        Assert.assertEquals("квантовыи размерныи эффект в трехмерных микрокристаллах полупроводников", StringUtils.normalize(value));
    }
    
}
