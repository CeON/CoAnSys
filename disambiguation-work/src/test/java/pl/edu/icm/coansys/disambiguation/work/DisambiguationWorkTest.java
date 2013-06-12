package pl.edu.icm.coansys.disambiguation.work;

import org.apache.commons.codec.language.Soundex;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;

public class DisambiguationWorkTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
        Soundex soundex = new Soundex();
        System.out.println(new Soundex().soundex("ALA"));
        System.out.println(new Soundex().soundex("ala"));
        System.out.println(new Soundex().soundex("kora"));
        System.out.println(new Soundex().soundex("kota"));
        System.out.println(new Soundex().soundex(DiacriticsRemover.removeDiacritics("Ała")));
        System.out.println(new Soundex().soundex("ola ma Kta Mia"));
        System.out.println(new Soundex().soundex("Ako ma psa Mia ala ma"));
    }

}
