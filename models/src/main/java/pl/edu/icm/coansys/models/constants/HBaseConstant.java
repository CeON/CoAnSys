/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
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
