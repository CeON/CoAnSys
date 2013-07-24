package pl.edu.icm.coansys.disambiguation.author;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.hadoop.io.IOUtils;

public class PigScriptExtractor {

    private static String[] skipPrefixes = {"REGISTER", "register", "IMPORT", "import", "SET","set","DESCRIBE","describe","--"};

    public static LinkedList<String> extract(String filename) throws FileNotFoundException, IOException {

        LinkedList<String> script = new LinkedList<String>();

        FileReader fr = null;
        BufferedReader in = null;

        try {
            fr = new FileReader(filename);
            in = new BufferedReader(fr);
            String line;
            linesLoop:
            while ((line = in.readLine()) != null) {
            	if(line.length()==0) continue;
                for (String prefix : skipPrefixes) {
                    if (line.startsWith(prefix)) {
                        continue linesLoop;
                    }
                }

                script.add(line);
            }
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(fr);
        }

        return script;
    }
}
