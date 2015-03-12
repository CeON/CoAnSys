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
package pl.edu.icm.coansys.statisticsgenerator.jobs;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.models.StatisticsProtos;
import pl.edu.icm.coansys.statisticsgenerator.conf.StatGeneratorConfiguration;
import pl.edu.icm.coansys.statisticsgenerator.filters.Filter;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class FilterInput implements Tool {

    private static Logger logger = LoggerFactory.getLogger(FilterInput.class);
    private Configuration conf;

    public static class FilterInputMap extends Mapper<Text, BytesWritable, Text, BytesWritable> {

        private StatGeneratorConfiguration statGenConfiguration;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            statGenConfiguration = new StatGeneratorConfiguration(conf);
        }

        @Override
        protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {

            StatisticsProtos.InputEntry inputEntry = StatisticsProtos.InputEntry.parseFrom(value.copyBytes());
            Filter inputFilter = statGenConfiguration.getInputFilter();

            if (inputFilter.filter(inputEntry)) {
                
                context.write(key, value);
            }
        }
    }


    @Override
    public int run(String[] args) throws Exception {
        Job job = new Job(conf);
        job.setJarByClass(FilterInput.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setMapperClass(FilterInputMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setNumReduceTasks(0);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        long startTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        boolean success = job.waitForCompletion(true);
        long endTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        double duration = (endTime - startTime) / Math.pow(10, 9);
        logger.info("=== Job Finished in " + duration + " seconds " + (success ? "(success)" : "(failure)"));
        return success ? 0 : 1;
    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("syntax: StatisticsGenerator <input_path> <output_path>");
        }
        ToolRunner.run(new FilterInput(), args);
    }
}
