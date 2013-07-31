/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.models.constants;

/**
 * @author pdendek
 */
public final class HBaseConstant {

    private HBaseConstant() {
    }

    // DocumentProtos Constants
    public static final String FAMILY_METADATA = "m";
    public static final String FAMILY_CONTENT = "c";
    public static final String FAMILY_METADATA_QUALIFIER_PROTO = "mproto";
    public static final String FAMILY_CONTENT_QUALIFIER_PROTO = "cproto";
    public static final String ROW_ID_MEDIA_TYPE_PDF = "PDF";
    // PIC Constants
    public static final String FAMILY_PIC_RESULT = "picres";
    public static final String FAMILY_PIC_RESULT_QUALIFIER_PROTO = "picresproto";
    //Auxiliar variables
    public static final String ROW_ID_SEPARATOR = "_";
}
