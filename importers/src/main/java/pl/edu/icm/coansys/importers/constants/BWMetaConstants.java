/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.constants;

import java.util.ArrayList;

/**
 * @author pdendek
 */
public class BWMetaConstants {
	
	public static final String mimePdfOneApplicationPdf = "application/pdf";
	public static final String mimePdfOneApplicationAcrobat = "application/acrobat";
	public static final String mimePdfOneApplicationXPdf = "application/x-pdf";
	public static final String mimePdfOneTextPdf = "text/pdf";
	public static final String mimePdfOneTextXPdf = "text/x-pdf";
	
	public static final ArrayList<String> mimePdfListExtension = new ArrayList<String>();
	static{
		mimePdfListExtension.add(BWMetaConstants.mimePdfOneApplicationAcrobat);
		mimePdfListExtension.add(BWMetaConstants.mimePdfOneApplicationPdf);
		mimePdfListExtension.add(BWMetaConstants.mimePdfOneApplicationXPdf);
		mimePdfListExtension.add(BWMetaConstants.mimePdfOneTextPdf);
		mimePdfListExtension.add(BWMetaConstants.mimePdfOneTextXPdf);
	}
	
}
