package pl.edu.icm.coansys.similarity.test.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.hadoop.io.IOUtils;

public class MacroExtractor {

    public static LinkedList<String> extract(String filename, String name) throws FileNotFoundException, IOException {

        LinkedList<String> script = new LinkedList<String>();
        boolean isMacroFound = false;
        boolean isMacroEnded = false;

        FileReader fr = null;
        BufferedReader in = null;

        try {
            fr = new FileReader(filename);
            in = new BufferedReader(fr);
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("DEFINE " + name + "(")) {
                    isMacroFound = true;
                } else if (line.equals("};") && isMacroFound) {
                    isMacroEnded = true;
                    break;
                } else if (isMacroFound) {
                    script.add(line);
                }
            }
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(fr);
        }

        if (!(isMacroFound && isMacroEnded)) {
            throw new RuntimeException("Cannot find the begining and the end of the macro: " + name);
        }
        return script;
    }

    public static LinkedList<String> extract(String filename, String name, Boolean includeHeader) throws FileNotFoundException, IOException {

        LinkedList<String> script = new LinkedList<String>();
        boolean isMacroFound = false;
        boolean isMacroEnded = false;

        FileReader fr = null;
        BufferedReader in = null;

        try {
            fr = new FileReader(filename);
            in = new BufferedReader(fr);
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("DEFINE " + name + "(")) {
                    if (includeHeader) {
                        script.add(line);
                    }
                    isMacroFound = true;
                } else if (line.equals("};") && isMacroFound) {
                    if (includeHeader) {
                        script.add(line);
                    }
                    isMacroEnded = true;
                    break;
                } else if (isMacroFound) {
                    script.add(line);
                }
            }
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(fr);
        }

        if (!(isMacroFound && isMacroEnded)) {
            throw new RuntimeException("Cannot find the begining and the end of the macro: " + name);
        }
        return script;
    }

    public static LinkedList<String> extract(String filename) throws FileNotFoundException, IOException {
        LinkedList<String> script = new LinkedList<String>();
        FileReader fr = null;
        BufferedReader in = null;
        try {
            fr = new FileReader(filename);
            in = new BufferedReader(fr);
            String line;
            while ((line = in.readLine()) != null) {
                script.add(line);
            }
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(fr);
        }
        return script;
    }
}
