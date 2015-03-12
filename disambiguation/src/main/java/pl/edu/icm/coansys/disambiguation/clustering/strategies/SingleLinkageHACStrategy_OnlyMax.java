/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.clustering.strategies;


/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class SingleLinkageHACStrategy_OnlyMax extends SingleLinkageHACStrategy implements Cloneable {

    @Override
    protected float SIM(float a, float b) {
        return Math.max(a, b);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return new SingleLinkageHACStrategy_OnlyMax();
    }
}