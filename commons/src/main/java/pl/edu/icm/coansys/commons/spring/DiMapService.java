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

package pl.edu.icm.coansys.commons.spring;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

/**
 * A contract for classes performing map business logic in {@link DiMapper}
 * @author lukdumi
 *
 */
public interface DiMapService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

    void map(KEYIN key, VALUEIN value, Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>.Context context) throws IOException, InterruptedException;
}
