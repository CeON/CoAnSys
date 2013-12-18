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
package pl.edu.icm.coansys.statisticsgenerator.tools;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;
import pl.edu.icm.coansys.models.StatisticsProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public final class ViewResults {
    
    private ViewResults() {}

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Syntax: ViewResults <path_to_sq_file>");
        }

        Configuration conf = new Configuration();
        String ptStr = args[0];
        Path pt = new Path(ptStr);
        Reader sfReader = new Reader(conf, Reader.file(pt));

        WritableComparable key = (WritableComparable) ReflectionUtils.newInstance(sfReader.getKeyClass(), conf);
        BytesWritable value = (BytesWritable) ReflectionUtils.newInstance(sfReader.getValueClass(), conf);
        
        while(sfReader.next(key, value)) {
            StatisticsProtos.Statistics statistics = StatisticsProtos.Statistics.parseFrom(value.copyBytes());
            System.out.println(statistics.toString()); // NOPMD
        }
    }
}
