/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.commons.hbase;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import pl.edu.icm.coansys.commons.oozie.OozieWorkflowUtils;

/**
 *
 * @author akawa
 */
public class IncrementalLoadConfigurator {

    private IncrementalLoadConfigurator() {
    }

    public static void main(String[] args) throws IOException {

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
                    props.setProperty("mapred.cache.file.symlinked", cacheUri.toString());
                    props.setProperty("mapred.cache.file", cacheUri.toString().split("#")[0]);
                    break;
                }
            }
        }

        OozieWorkflowUtils.captureOutput(props);
    }
}