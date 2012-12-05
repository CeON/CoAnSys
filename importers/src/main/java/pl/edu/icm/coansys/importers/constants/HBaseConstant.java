/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.importers.constants;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author pdendek
 */
public class HBaseConstant {

    private HBaseConstant() {
    }

    public static final String FAMILY_METADATA = "m";
    public static final String FAMILY_CONTENT = "c";
    public static final String FAMILY_METADATA_QUALIFIER_PROTO = "mproto";
    public static final String FAMILY_CONTENT_QUALIFIER_PROTO = "cproto";
    public static final byte[] FAMILY_METADATA_BYTES = Bytes.toBytes(FAMILY_METADATA);
    public static final byte[] FAMILY_CONTENT_BYTES = Bytes.toBytes(FAMILY_CONTENT);
    public static final byte[] FAMILY_METADATA_QUALIFIER_PROTO_BYTES = Bytes.toBytes(FAMILY_METADATA_QUALIFIER_PROTO);
    public static final byte[] FAMILY_CONTENT_QUALIFIER_PROTO_BYTES = Bytes.toBytes(FAMILY_CONTENT_QUALIFIER_PROTO);

    // PIC Constants
    public static final String FAMILY_PIC_RESULT = "picres";
    public static final String FAMILY_PIC_RESULT_QUALIFIER_PROTO = "picresproto";
    public static final byte[] FAMILY_PIC_RESULT_BYTES = Bytes.toBytes(FAMILY_PIC_RESULT);
    public static final byte[] FAMILY_PIC_RESULT_QUALIFIER_PROTO_BYTES = Bytes.toBytes(FAMILY_PIC_RESULT_QUALIFIER_PROTO);

    public static final String ROW_ID_SEPARATOR = "_";
    public static final String ROW_ID_MEDIA_TYPE_PDF = "PDF";
}
