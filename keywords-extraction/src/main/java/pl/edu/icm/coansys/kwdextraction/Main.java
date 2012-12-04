/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction;

import java.io.IOException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.hadoop.fs.Path;
import pl.edu.icm.coansys.kwdextraction.rake.RakeExtracion;
import pl.edu.icm.coansys.kwdextraction.stat.StatExtraction;

public class Main {

    //private static String pathToStopwords = "stopwords/stopwords_en.txt";
    private static String pathToStopwords = Main.class.getClassLoader().getResource("stopwords/stopwords_en.txt").getPath();
    private static boolean r = true;
    private static boolean s = false;
    private static String inputPath;
    private static String outputPath;
    private static int length = 3;

    /**
     * Parses a given list of options.
     */
    private static void setOptions(String[] args) {
        OptionParser parser = new OptionParser();
        parser.accepts("stop").withRequiredArg();
        parser.accepts("d").withRequiredArg();
        parser.accepts("o").withRequiredArg();
        parser.accepts("s");
        parser.accepts("r");
        parser.accepts("l").withRequiredArg().ofType(Integer.class);
        OptionSet options = parser.parse(args);
        if (options.has("stop")) {
            pathToStopwords = (String) options.valueOf("stop");
        }
        if (options.has("d")) {
            inputPath = (String) options.valueOf("d");
        }
        if (options.has("o")) {
            outputPath = (String) options.valueOf("o");
        }
        if (options.has("s")) {
            s = true;
        }
        if (options.has("l")) {
            length = (Integer) options.valueOf("l");
        }
    }

    /**
     * main function <p> Valid options are:<p> -s <br> extracting keywords using
     * statistic algorithm <p> -r <br> extracting keywords using RAKE algorithm
     * <p> <p> (If none of above options are given RAKE algorithm is run <p>
     * These options can not be used together) <p>
     *
     * -stop "file name" <br> the path to the file containing stopwords, the
     * default is "stopwords/stopwords_en.txt" <p>
     *
     * -l "length of keyword" <br> maximum length of keyword (length = how many
     * words it consists of), the default is 3 <p>
     *
     * -d "path to input directory" <br> the default is "in/" <p>
     *
     * -o "path to output directory" <br> the default is "keywords/"
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        setOptions(args);
        if (s == r) {
            System.err.println("Choose only one algorithm");
        }

        if (s) {
            StatExtraction stat = new StatExtraction();
            stat.addInputPath(new Path(inputPath));
            stat.maxLength = length;
            stat.pathToStopwords = pathToStopwords;
            stat.outputPath = outputPath;
            stat.run();
        } else {
            RakeExtracion rake = new RakeExtracion();
            rake.addInputPath(new Path(inputPath));
            rake.maxLength = length;
            rake.pathToStopwords = pathToStopwords;
            rake.outputPath = outputPath;
            rake.run();
        }
    }
}
