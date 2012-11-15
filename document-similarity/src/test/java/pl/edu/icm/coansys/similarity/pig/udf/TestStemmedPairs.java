/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.pig.tools.parameters.ParseException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

/**
 *
 * @author akawa
 */
public class TestStemmedPairs {

    private StemmedPairs sp;

    @BeforeClass
    public static void beforeClass() throws Exception {
    }

    @AfterClass
    public static void afterClass() throws Exception {
    }

    @BeforeTest
    public void init() throws FileNotFoundException, IOException {
        sp = new StemmedPairs();
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemmingAbstract() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("Orientation: Stress is a social reality which does not exist in isolation, but in many social situations,\n"
                + "especially work-related environments. Police officers in particular suffer from highly negative stress\n"
                + "related outcomes.\n"
                + "Research purpose: The purpose of the study was to determine how Moos's hypothesised stress and\n"
                + "coping model (1994) fitted a sample of police officers.\n"
                + "Motivation for the study: The study was an attempt to understand police officers' unique needs and\n"
                + "how the frequency and/or intensity of perceived stress could be reduced so that they would be able\n"
                + "to cope more effectively with stress.\n"
                + "Research design, approach and method: A non-experimental survey design, following the\n"
                + "quantitative tradition, was used in pursuit of the research objectives. A random sample of 505\n"
                + "participants was extracted from a population of serving male and female police officers reflecting\n"
                + "the typical South African ethnic groups. Structural equation modelling (SEM) was used to establish\n"
                + "the adequacy of between the hypothesised Moos model and the sample.\n"
                + "Main findings: The hypothesised theoretical framework was disproved. A respecified model\n"
                + "and inter-correlations confirm that some officers experience burnout, while, paradoxically, others\n"
                + "continue to be unaffected because of the buffering effect of social support, personality factors and\n"
                + "other resilience factors not revealed in this study.\n"
                + "Practical/managerial implications: The study calls on police management for awareness of the negative\n"
                + "health consequences of prolonged stressors. Simultaneously, employee assistance programmes\n"
                + "could be directed to problem-solving strategies, perceived self-efficacy and learned resourcefulness\n"
                + "to improve control over prolonged negative stress consequences among members.\n"
                + "Contribution/value-add: This research provides a theoretical framework to understand, describe\n"
                + "and assess individual well-being in the police work context.");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemmingTitle() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("An evAluAtion of A psychosociAl stress And coping model in the police work context");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemmingKeywords() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("police stress_coping strategies_vigour_burnout_resources");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemmingKeywords2() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("Carbohydrate metabolism_Pyruvate_SDH_MDH_Cestode parasites_Gallus gallus domesticus,contributorNames#Waghmare S.B._Chavan R.J.");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemmingKeywords3() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("RFID (radio frequency identification) technology_Bar Coding_Retailing_Security_Inventory Control_Business Intelligence");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemming() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("The big fat cat, said 'your funniest guy i know' to the kangaroo... ¿Escribiste la carta esta mañana?");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemming2() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("The two-year old child when to the shop in Łódź to buy some beer");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemming3() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("WHere to GO to buy some cool stuff?");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemming4() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("Poland failed to quaily to 1/4-final in Euro2012");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemming5() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("Guns N' Roses and rock 'n' roll");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemmingPL1() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("Poszła Ola do przedszkola, zapomniała parasola! A parasol był zepsuty, połamane wszystkie druty");
        println(strings);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStemmingPL2() throws IOException, ParseException {
        List<String[]> strings = sp.getStemmedPairs("Z tego co wiem, to wiem, że nic nie wiem o tym i tamtym i owantym.");
        println(strings);
    }

    private void println(List<String[]> strings) {
        System.out.println("Stemmer output:");
        for (String[] sa : strings) {
            for (String s : sa) {
                System.out.println(s);
            }
            System.out.println("---");
        }
    }
}
