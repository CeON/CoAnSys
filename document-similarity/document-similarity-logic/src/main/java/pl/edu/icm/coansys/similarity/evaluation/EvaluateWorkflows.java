package pl.edu.icm.coansys.similarity.evaluation;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.WorkflowJob;

public class EvaluateWorkflows {

	static final String masterNode = "hadoop-master.vls.icm.edu.pl";
	static final String nameNode = "hdfs://" + masterNode + ":8020";
	static final String jobTracker = "" + masterNode + ":8021";
	static final String queueName = "default";
	static final String project = "coansys";
	static final String subproject = "document-similarity";
	static final String oozielaunchermapredfairschedulerpool = "bigjobs";
	static final String pool = oozielaunchermapredfairschedulerpool;;
	static final String ooziewfapplicationpath = "" + nameNode
			+ "/user/pdendek/docsim-on-oap/docsim-ranked-wf/";
	static final String workflowPath = ooziewfapplicationpath;;
	static final String commonJarsPath = "/usr/lib/hbase/lib/zookeeper.jar";
	static final String ds_bwndataMetadataInputPath = "" + nameNode
			+ "/user/mhorst/documentssimilarity/input_protobuf/2014-01-30";
	static final String ds_similarityOutputPath = workflowPath
			+ "/../results_oap_eval";
	static final String ds_scheduler = "bigjobs";
	static final String ds_tmpCompressionCodec = "gz";

	public static void main(String[] args) throws Exception {

		String[] ds_removal_least_used_arr = new String[] { "10", "0", "1",
				"2", "3", "5", "20", "40", "60" };
		String[] ds_removal_rate_arr = new String[] { "0.99", "0.97", "0.95",
				"0.9", "0.8", "0.6", "0.4", };
		String[] ds_tfidfTopnTermPerDocument_arr = new String[] { "20", "10",
				"5", "60", "80", "100", };
		String[] ds_sample_arr = new String[] { "0.122", "0.7", "0.4", "1.0",
				"0.05", "0.01", };
		String[] ds_mapredChildJavaOpts_arr = new String[] { "10", "8", "6",
				"4", "2", };
		String[] ds_parallel_arr = new String[] { "40", "20", "10", "5", "2", };

		for (String ds_removal_least_used : ds_removal_least_used_arr) {
			executeWorkflow(ds_removal_least_used,
					ds_removal_rate_arr[0], ds_tfidfTopnTermPerDocument_arr[0],
					ds_sample_arr[0], ds_mapredChildJavaOpts_arr[0],
					ds_parallel_arr[0]);
		}

		for (String ds_removal_rate : ds_removal_rate_arr) {
			executeWorkflow(ds_removal_least_used_arr[0],
					ds_removal_rate, ds_tfidfTopnTermPerDocument_arr[0],
					ds_sample_arr[0], ds_mapredChildJavaOpts_arr[0],
					ds_parallel_arr[0]);
		}
		for (String ds_tfidfTopnTermPerDocument : ds_tfidfTopnTermPerDocument_arr) {
			executeWorkflow(ds_removal_least_used_arr[0],
					ds_removal_rate_arr[0], ds_tfidfTopnTermPerDocument,
					ds_sample_arr[0], ds_mapredChildJavaOpts_arr[0],
					ds_parallel_arr[0]);
		}
		for (String ds_sample : ds_sample_arr) {
			executeWorkflow(ds_removal_least_used_arr[0],
					ds_removal_rate_arr[0], ds_tfidfTopnTermPerDocument_arr[0],
					ds_sample, ds_mapredChildJavaOpts_arr[0],
					ds_parallel_arr[0]);
		}
		for (String ds_mapredChildJavaOpts : ds_mapredChildJavaOpts_arr) {
			executeWorkflow(ds_removal_least_used_arr[0],
					ds_removal_rate_arr[0], ds_tfidfTopnTermPerDocument_arr[0],
					ds_sample_arr[0], ds_mapredChildJavaOpts,
					ds_parallel_arr[0]);
		}
		for (String ds_parallel : ds_parallel_arr) {
			executeWorkflow(ds_removal_least_used_arr[0],
					ds_removal_rate_arr[0], ds_tfidfTopnTermPerDocument_arr[0],
					ds_sample_arr[0], ds_mapredChildJavaOpts_arr[0],
					ds_parallel);
		}

	}

	private static void executeWorkflow(String ds_removal_least_used,
			String ds_removal_rate, String ds_tfidfTopnTermPerDocument,
			String ds_sample, String ds_mapredChildJavaOpts, String ds_parallel) throws Exception {

		String params = StringUtils.join(new String[] { ds_removal_least_used,
				ds_removal_rate, ds_tfidfTopnTermPerDocument, ds_sample,
				ds_mapredChildJavaOpts, ds_parallel },"*");
		params = params.replaceAll("\\.", "_");

		OozieClient wc = new OozieClient(
				"http://hadoop-master.vls.icm.edu.pl:11000/oozie");
		Properties conf = wc.createConfiguration();

		conf.setProperty("masterNode", masterNode);
		conf.setProperty("ds_sample", ds_sample); 
		conf.setProperty("ds_removal_least_used", ds_removal_least_used);
		conf.setProperty("ds_removal_rate", ds_removal_rate);
		conf.setProperty("ds_similarityTopnDocumentPerDocument", "20");
		conf.setProperty("ds_mapredChildJavaOpts", "-Xmx"+ds_mapredChildJavaOpts+"g"); 
		conf.setProperty("ds_parallel", ds_parallel);
		conf.setProperty("ds_tfidfTopnTermPerDocument", ds_tfidfTopnTermPerDocument);
		conf.setProperty("commonJarsPath", commonJarsPath);
		conf.setProperty("nameNode", nameNode);
		conf.setProperty("jobTracker", jobTracker);
		conf.setProperty("queueName", queueName);
		conf.setProperty("oozie.libpath", "${nameNode}/user/oozie/share/lib");
		conf.setProperty("oozie.use.system.libpath", "true");
		conf.setProperty("oozie.wf.rerun.failnodes", "false");

		conf.setProperty("project", project);
		conf.setProperty("subproject", "eval_" + params);
		conf.setProperty("oozie.launcher.mapred.fairscheduler.pool",
				oozielaunchermapredfairschedulerpool);
		conf.setProperty("pool", pool);
		conf.setProperty("oozie.wf.application.path", ooziewfapplicationpath);
		conf.setProperty(OozieClient.APP_PATH, ooziewfapplicationpath);
		conf.setProperty("workflowPath", workflowPath);
		conf.setProperty("commonJarPath", commonJarsPath);
		conf.setProperty("ds_bwndataMetadataInputPath",
				ds_bwndataMetadataInputPath);
		conf.setProperty("ds_similarityOutputPath", ds_similarityOutputPath+"/"+params);
		conf.setProperty("ds_scheduler", ds_scheduler);
		conf.setProperty("ds_tmpCompressionCodec", ds_tmpCompressionCodec);

		try {
			String jobId = wc.run(conf);
			System.out.println("Workflow job, " + jobId + " submitted");

			while (wc.getJobInfo(jobId).getStatus() == WorkflowJob.Status.RUNNING) {
				System.out.println("Workflow job running ...");
				// check if you need to go to the next example
				// once per 15minutes
				Thread.sleep(15 * 60 * 1000);
			}
			System.out.println("Workflow job completed ...");
			System.out.println(wc.getJobInfo(jobId));
		} catch (Exception r) {
			System.out.println("Errors " + r.getLocalizedMessage());
			throw r;
		}

	}
}