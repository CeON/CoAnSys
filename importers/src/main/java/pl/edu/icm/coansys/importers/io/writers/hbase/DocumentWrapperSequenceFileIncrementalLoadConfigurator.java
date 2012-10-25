/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.io.writers.hbase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Properties;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

/**
 *
 * @author akawa
 */
public class DocumentWrapperSequenceFileIncrementalLoadConfigurator {

    public static void main(String[] args) throws Exception {

        String tableName = args[0];

        Job job = new Job();
        job.setMapOutputValueClass(Put.class);
        HTable table = new HTable(tableName);
        HFileOutputFormat.configureIncrementalLoad(job, table);

        Properties props = new Properties();
        props.setProperty("mapred.reduce.tasks", Integer.valueOf(job.getNumReduceTasks()).toString());
        URI[] cacheUris = DistributedCache.getCacheFiles(job.getConfiguration());
        if (cacheUris != null) {
            for (URI cacheUri : cacheUris) {
                if (cacheUri.toString().endsWith("#_partition.lst")) {
                    props.setProperty("mapred.cache.file", cacheUri.toString());
                    break;
                }
            }
        }

        //Passing the variable to WF that could be referred by subsequent actions
        File file = new File(System.getProperty("oozie.action.output.properties"));
        OutputStream os = new FileOutputStream(file);
        props.store(os, "");
        os.close();
    }

    /*
     * private static void usage(String info) { System.out.println(info);
     * System.out.println("Exemplary command: "); String command = "";
     * System.out.println(command);
    }
     */
}