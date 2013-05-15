package pl.edu.icm.coansys.classification.documents.auxil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

public class CurrentTime {
	public static void main(String[] args) throws Exception{
		if(args.length<1){
	         File file = new File(System.getProperty("oozie.action.output.properties"));
	         Properties props = new Properties();
	         
	         props.setProperty(args[0], (new Date()).toString());

	         OutputStream os = new FileOutputStream(file);
	         props.store(os, "");
	         os.close();
		}else throw new Exception("When using class " +
				"pl.edu.icm.coansys.classification.documents.auxil." +
				"CurrentTime one parameter (property name) has to be passed");
	}
}
