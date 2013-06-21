package pl.edu.icm.coansys.disambiguation.work.tool;

import static java.lang.System.out;

import java.io.IOException;

/** 
 * Prints a Bw2Proto sequence file to the console
 * */
public class Bw2ProtoFilePrinter {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            out.println("Usage:  Bw2ProtoFilePrinter bw2ProtoFileUri");
            return;
        }
        
        Bw2ProtoFileUtils.formatAndPrintToConsole(args[0]);
        

    }

}
