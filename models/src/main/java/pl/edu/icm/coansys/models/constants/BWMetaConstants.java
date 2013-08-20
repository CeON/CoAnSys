/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author pdendek
 */
public final class BWMetaConstants {

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
