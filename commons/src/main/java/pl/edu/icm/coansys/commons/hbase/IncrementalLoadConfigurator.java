///*
// * This file is part of CoAnSys project.
// * Copyright (c) 2012-2015 ICM-UW
// * 
// * CoAnSys is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Affero General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
//
// * CoAnSys is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU Affero General Public License for more details.
// * 
// * You should have received a copy of the GNU Affero General Public License
// * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
// */
//
//package pl.edu.icm.coansys.commons.hbase;
//
//import java.io.IOException;
//import java.net.URI;
//import java.util.Properties;
//import org.apache.hadoop.filecache.DistributedCache;
//import org.apache.hadoop.hbase.client.HTable;
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
//import org.apache.hadoop.mapreduce.Job;
//import pl.edu.icm.coansys.commons.oozie.OozieWorkflowUtils;
//
///**
// *
// * @author akawa
// */
//public final class IncrementalLoadConfigurator {
//
//    private IncrementalLoadConfigurator() {
//    }
//
//    public static void main(String[] args) throws IOException {
//
//        String tableName = args[0];
//
//        Job job = new Job();
//        job.setMapOutputValueClass(Put.class);
//        HTable table = new HTable(tableName);
//        HFileOutputFormat.configureIncrementalLoad(job, table);
//
//        Properties props = new Properties();
//        props.setProperty("mapred.reduce.tasks", Integer.valueOf(job.getNumReduceTasks()).toString());
//        URI[] cacheUris = DistributedCache.getCacheFiles(job.getConfiguration());
//        if (cacheUris != null) {
//            for (URI cacheUri : cacheUris) {
//                if (cacheUri.toString().endsWith("#_partition.lst")) {
//                    props.setProperty("mapred.cache.file.symlinked", cacheUri.toString());
//                    props.setProperty("mapred.cache.file", cacheUri.toString().split("#")[0]);
//                    break;
//                }
//            }
//        }
//
//        OozieWorkflowUtils.captureOutput(props);
//    }
//}
