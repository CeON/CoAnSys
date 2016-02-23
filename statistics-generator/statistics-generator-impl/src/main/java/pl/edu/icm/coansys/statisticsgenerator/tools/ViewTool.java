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
package pl.edu.icm.coansys.statisticsgenerator.tools;

import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;
import pl.edu.icm.coansys.models.StatisticsProtos;

/**
 * This tool views protocol buffers serialized data stored in the hadoop sequence file. It tries to guess the protocol
 * buffers class from those assiciated with statistics generator module.
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public final class ViewTool {

    private ViewTool() {
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Syntax: ViewTool <path_to_sq_file>");
        }

        Configuration conf = new Configuration();
        
        for (String ptStr : args) {
            Path pt = new Path(ptStr);
            processFileOrDirectory(pt, conf);
        }
    }
    
    private static void processFileOrDirectory(Path pt, Configuration conf) throws IOException {
        FileSystem fs = pt.getFileSystem(conf);
        if (fs.isDirectory(pt)) {
            for (FileStatus fstat : fs.listStatus(pt)) {
                processFileOrDirectory(fstat.getPath(), conf);
            }
        } else if (fs.isFile(pt)) {
            viewFile(pt, conf);
        } else {
            //zaloguj błąd
        }
    }

    private static void viewFile(Path pt, Configuration conf) throws IOException {
        
        System.err.println("================ Viewing file " + pt.toString());
        
        Reader sfReader = new Reader(conf, Reader.file(pt));

        WritableComparable key = (WritableComparable) ReflectionUtils.newInstance(sfReader.getKeyClass(), conf);
        BytesWritable value = (BytesWritable) ReflectionUtils.newInstance(sfReader.getValueClass(), conf);

        while (sfReader.next(key, value)) {
            try {
                StatisticsProtos.InputEntry statistics = StatisticsProtos.InputEntry.parseFrom(value.copyBytes());
                System.out.println("======= Viewing InputEntry...");
                System.out.println(statistics.toString()); // NOPMD
                continue;
            } catch (InvalidProtocolBufferException ex) {
            }

            try {
                StatisticsProtos.Statistics statistics = StatisticsProtos.Statistics.parseFrom(value.copyBytes());
                System.out.println("======== Viewing Statistics...");
                System.out.println(statistics.toString()); // NOPMD
                continue;
            } catch (InvalidProtocolBufferException ex) {
            }

            try {
                StatisticsProtos.SelectedStatistics statistics = StatisticsProtos.SelectedStatistics.parseFrom(value.copyBytes());
                System.out.println("======== Viewing SelectedStatistics...");
                System.out.println(statistics.toString()); // NOPMD
                continue;
            } catch (InvalidProtocolBufferException ex) {
            }

            throw new IllegalArgumentException("Cannot guess the protocol buffers class");
        }
        sfReader.close();
    }
}
