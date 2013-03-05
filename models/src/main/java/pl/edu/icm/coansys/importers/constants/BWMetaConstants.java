/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pdendek
 */
public class BWMetaConstants {

    private BWMetaConstants() {
    }
    
    public static final String mimePdfOneApplicationPdf = "application/pdf";
    public static final String mimePdfOneApplicationAcrobat = "application/acrobat";
    public static final String mimePdfOneApplicationXPdf = "application/x-pdf";
    public static final String mimePdfOneTextPdf = "text/pdf";
    public static final String mimePdfOneTextXPdf = "text/x-pdf";
    public static final List<String> mimePdfListExtension = new ArrayList<String>();

    static {
        mimePdfListExtension.add(BWMetaConstants.mimePdfOneApplicationAcrobat);
        mimePdfListExtension.add(BWMetaConstants.mimePdfOneApplicationPdf);
        mimePdfListExtension.add(BWMetaConstants.mimePdfOneApplicationXPdf);
        mimePdfListExtension.add(BWMetaConstants.mimePdfOneTextPdf);
        mimePdfListExtension.add(BWMetaConstants.mimePdfOneTextXPdf);
    }
    
    public static final String mimeTextPlain = "text/plain";
    public static final List<String> mimeTxtListExtension = new ArrayList<String>();
    
    static {
        mimeTxtListExtension.add(BWMetaConstants.mimeTextPlain);
    }
}
