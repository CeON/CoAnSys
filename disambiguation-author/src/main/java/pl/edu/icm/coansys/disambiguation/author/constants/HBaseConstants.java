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

package pl.edu.icm.coansys.disambiguation.author.constants;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public final class HBaseConstants {

    private HBaseConstants() {
    }
    
    //separator in a row id between a type prefix and an id postfix 
    public static final String INTRASEPARATOR = "_";
    //type prefix
    public static final String T_CONTRIBUTIONS_CLUSTER = "authCluster";
    public static final String T_CONTRIBUTOR = "contrib";
    //column family
    public static final String F_RESULT = "r";
    //column qualifier
    public static final String Q_CONTRIBS = "cbs";
    public static final String Q_CLUSTER_ID = "cls";
}
