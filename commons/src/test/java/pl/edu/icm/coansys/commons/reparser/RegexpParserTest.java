/**
 * (C)2007 ICM, University of Warsaw
 *
 * package: pl.edu.icm.ceon.tools.reparser
 * file:    RegexpParserTest.java
 * date:    2007-07-03
 * svnid:   $Id$
 */
package pl.edu.icm.coansys.commons.reparser;

import static org.testng.Assert.*;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Lukasz Bolikowski (bolo@icm.edu.pl)
 *
 */
public class RegexpParserTest {
	RegexpParser rp;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeMethod
	public void setUp() throws Exception {
		rp = new RegexpParser("pl/edu/icm/coansys/commons/reparser/parserTest.properties", "testing");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterMethod
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link pl.edu.icm.ceon.tools.reparser.RegexpParser#parse(java.lang.String)}.
	 */
	@Test
	public void testParse() {
		Node n;

		n = rp.parse("123 idziesz ty");
		assertNotNull(n);
		assertEquals("Testing", n.getName());
		assertEquals("t2", n.getType());
		assertNull(n.getNextAlternative());

		n = rp.parse("123 idziesz ty");
		assertNotNull(n);
		assertNull(n.getNextAlternative());
		assertTrue(n.getFieldNames().contains("Digits"));
		assertNotNull(n.getFirstField("Digits"));
		assertEquals("123", n.getFirstField("Digits").getValue());
		assertNotNull(n.getFields("Alpha"));

		n = rp.parse("6000000 bicycles 443");
		/* check first alternative */
		assertNotNull(n);
		assertNotNull(n.getFields("Digits"));
		assertEquals(2, n.getFields("Digits").size());
		assertNotNull(n.getFields("Letters"));
		assertEquals(1, n.getFields("Letters").size());
		assertNull(n.getFields("Alpha"));
		assertNotNull(n.getFields("Digits").get(1));
		assertEquals("443", n.getFields("Digits").get(1).getValue());
		/* check second alternative */
		assertNotNull(n.getNextAlternative());
		assertNotNull(n.getNextAlternative().getFields("Digits"));
		assertEquals(1, n.getNextAlternative().getFields("Digits").size());
		assertNotNull(n.getNextAlternative().getFields("Letters"));
		assertEquals(1, n.getNextAlternative().getFields("Letters").size());
		assertNotNull(n.getNextAlternative().getFields("Alpha"));
		assertEquals(1, n.getNextAlternative().getFields("Alpha").size());
		assertNotNull(n.getNextAlternative().getFields("Alpha").get(0));
		assertEquals("443", n.getNextAlternative().getFields("Alpha").get(0).getValue());
		
		n = rp.parse("Hey no name spox");
		assertNotNull(n);
		assertNotNull(n.getFieldNames());
		assertEquals(1, n.getFieldNames().size());

		n = rp.parse("Who is this");
		assertNull(n);

		n = rp.parse("60 % of 80");
		assertNotNull(n);

		n = rp.parse("100 % of U");
		assertNull(n);
        System.out.println("test bola ok ");
	}
    
    @Test
	public void testParseSurname() {
        RegexpParser authorParser = new RegexpParser("authorParser.properties", "author");
        Node authorNode = authorParser.parse("Czeczko, Artur");
        assertNotNull(authorNode);
        authorNode = authorParser.parse("Czeczko, A");
        assertNotNull(authorNode);
        authorNode = authorParser.parse("Czeczko, A.");
        assertNotNull(authorNode);
    }
    
}
