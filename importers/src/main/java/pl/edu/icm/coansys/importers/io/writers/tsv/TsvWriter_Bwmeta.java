package pl.edu.icm.coansys.importers.io.writers.tsv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.transformers.DocumentDTO2TSVLine;

public class TsvWriter_Bwmeta {
	public static void main(String[] args) throws IOException{
		
		if(args.length != 3){
			usage();
			System.exit(1);
		}
		
		String in = args[0];
		String col = args[1];
		String out = args[2];
		
		File inf = new File(in);
		if(!inf.exists()){
			System.out.println("Input dir does not exist");
			System.exit(2);
		}
		if(!inf.isDirectory()){
			System.out.println("<Input dir> is not a directory");
			System.exit(3);
		}
		
		if(col.length() != col.replaceAll("[^a-zA-Z0-9]", "").length()){
			System.out.println("Only alphanumeric signs (a space sign is also excluded) are allowed for a collection name.");
			System.exit(4);
		}
		
		System.out.println(out);
		File outf = new File(out);
		if(!outf.getParentFile().exists()){
			outf.getParentFile().mkdirs();
		}
		
        ZipDirToDocumentDTOIterator zdtp = new ZipDirToDocumentDTOIterator(in, col);
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
        
        for (DocumentDTO doc : zdtp) {
        	bw.write(DocumentDTO2TSVLine.translate(doc));
        }
        bw.flush();
        bw.close();
	}

	private static void usage() {
		String usage = "Usage: \n" +
		"java -jar importers-*-with-deps.jar pl.edu.icm.coansys.importers.io.writers.tsv.TsvWriter_Bwmeta <in_dir> <collectionName> <out_file>";
		System.out.println(usage);
	}
}
