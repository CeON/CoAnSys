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

package pl.edu.icm.coansys.statisticsgenerator.operationcomponents;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class LastCharsPartitioner implements Partitioner {
    
    int numberOfCharacters;

    @Override
    public String[] partition(String inputField) {
        String[] result = { inputField.substring(Math.max(inputField.length() - numberOfCharacters, 0)) };
        return result;
    }

    @Override
    public void setup(String... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("setup requires number of characters");
        }
        numberOfCharacters = Integer.parseInt(params[0]);
    }
}
