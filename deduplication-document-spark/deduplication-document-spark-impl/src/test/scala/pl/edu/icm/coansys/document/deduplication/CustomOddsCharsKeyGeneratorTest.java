/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.document.deduplication;

import java.util.Arrays;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Aleksander Nowinski <aleksander.nowinski@gmail.com>
 */
public class CustomOddsCharsKeyGeneratorTest {

    public CustomOddsCharsKeyGeneratorTest() {
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    /**
     * Test of generateKey method, of class CustomOddsCharsKeyGenerator.
     */
    @Test
    public void testGenerateKey() {
        System.out.println("generateKey");
        CustomOddsCharsKeyGenerator instance = new CustomOddsCharsKeyGenerator();

        assertEquals(instance.getKeySize(), CustomOddsCharsKeyGenerator.DEFAULT_KEY_SIZE);
        assertEquals(instance.generateKey(""), "");
        assertEquals(instance.generateKey("Short legged cat"), "soteg");
        assertEquals(instance.generateKey("a \t\t12 domino titles"), "1dmnt");
        instance.setKeySize(2);
        assertEquals(instance.generateKey("The eleven elves"), "ee");
        assertEquals(instance.generateKey("404"), "44");
        instance.setKeySize(10);
        assertEquals(instance.generateKey("\t\tbabsbdbsbd  bdbdbdb   1b2bcbadsfads"), "bbbbbbbbbb");
    }

    @Test
    public void testSetKeySizes() {
        System.out.println("setKeySizes");

        CustomOddsCharsKeyGenerator instance = new CustomOddsCharsKeyGenerator();
        try {
            instance.setKeySizes(null);
            fail("Permitted null.");
        } catch (IllegalArgumentException iae) {
            //this is ok.
        }
        try {
            instance.setKeySizes(new int[0]);
            fail("Permitted empty array.");
        } catch (IllegalArgumentException iae) {
            //this is ok.
        }
        try {
            instance.setKeySizes(new int[]{1, 2, 3, 3});
            fail("Permitted two equal params");
        } catch (IllegalArgumentException iae) {
            //this is ok.
        }
        try {
            instance.setKeySizes(new int[]{1, 2, 4, 3});
            fail("Permitted unsorted array");
        } catch (IllegalArgumentException iae) {
            //this is ok.
        }
        int[] a = new int[] {1,3,6,7,8,9};
        instance.setKeySizes(a);
        assertTrue(Arrays.equals(a, instance.getKeySizes()));

    }

    @Test
    public void testGenerateKeys() {
        System.out.println("generateKeys");
        CustomOddsCharsKeyGenerator instance = new CustomOddsCharsKeyGenerator();
        instance.setKeySizes(new int[]{2, 3, 5});
        assertTrue(Arrays.equals(new String[]{"", "", ""}, instance.generateKeys("")));
        assertTrue(Arrays.equals(new String[]{"so", "sot", "soteg"}, instance.generateKeys("Short legged cat")));
        assertTrue(Arrays.equals(new String[]{"44", "44", "44"}, instance.generateKeys("404")));
        assertTrue(Arrays.equals(new String[]{"bb", "bbb", "bbbbb"}, instance.generateKeys("\t\tbabsbdbsbd  bdbdbdb   1b2bcbadsfads")));
    }

    /**
     * Test of cleanUpString method, of class CustomOddsCharsKeyGenerator.
     */
    @Test
    public void testCleanUpString() {
        System.out.println("cleanUpString");
        CustomOddsCharsKeyGenerator instance = new CustomOddsCharsKeyGenerator();
        assertEquals(instance.cleanUpString("test"), "test");
        assertEquals(instance.cleanUpString(".-- test  --:+"), "test");
        assertEquals(instance.cleanUpString(".-- test  -ab-:+"), "testab");
        assertEquals(instance.cleanUpString(".-- test  2-ab-:+"), "test2ab");
        assertEquals(instance.cleanUpString("\t\n test   \t\ttest  "), "testtest");
        assertEquals(instance.cleanUpString("test of cat"), "testcat");
        assertEquals(instance.cleanUpString("TeSt oF caT\t\t\n"), "testcat");
        assertEquals(instance.cleanUpString("Koń jak koń"), "konjakkon");
        assertEquals(instance.cleanUpString("  Litera β"), "literabeta");
    }

}
