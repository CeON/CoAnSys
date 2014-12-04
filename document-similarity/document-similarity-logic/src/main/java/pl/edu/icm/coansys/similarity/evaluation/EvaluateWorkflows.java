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
	static final String bwndataMetadataInputPath = "" + nameNode
			+ "/user/pdendek/oap-500k";
	static final String similarityOutputPath = workflowPath
			+ "/../results_oap_eval";
	static final String scheduler = "bigjobs";
	static final String tmpCompressionCodec = "gz";

	public static void main(String[] args) throws Exception {

		String[] removal_least_used_arr = new String[] { "0" };

String[] removal_rate_arr = new String[] { 
"0.955",
"0.96",
"0.965",
"0.97",
"0.975",
"0.98",
"0.985",
"0.99",
"0.995",
"0.996",
"0.997",
"0.998",
"0.999"
};
		
String[] tfidfTopnTermPerDocument_arr = new String[] {
				"80"};
		String[] sample_arr = new String[] { "1.0" };
		String[] mapredChildJavaOpts_arr = new String[] { "12"};
		String[] parallel_arr = new String[] { "40" };

//		for (String removal_least_used : removal_least_used_arr) {
//			executeWorkflow(removal_least_used,
//					removal_rate_arr[0], tfidfTopnTermPerDocument_arr[0],
//					sample_arr[0], mapredChildJavaOpts_arr[0],
//					parallel_arr[0]);
//		}
//
		for (String removal_rate : removal_rate_arr) {
			executeWorkflow(removal_least_used_arr[0],
					removal_rate, tfidfTopnTermPerDocument_arr[0],
					sample_arr[0], mapredChildJavaOpts_arr[0],
					parallel_arr[0]);
		}
/*		for (String tfidfTopnTermPerDocument : tfidfTopnTermPerDocument_arr) {
			executeWorkflow(removal_least_used_arr[0],
					removal_rate_arr[0], tfidfTopnTermPerDocument,
					sample_arr[0], mapredChildJavaOpts_arr[0],
					parallel_arr[0]);
		}*/
//		for (String sample : sample_arr) {
//			executeWorkflow(removal_least_used_arr[0],
//					removal_rate_arr[0], tfidfTopnTermPerDocument_arr[0],
//					sample, mapredChildJavaOpts_arr[0],
//					parallel_arr[0]);
//		}
//		for (String mapredChildJavaOpts : mapredChildJavaOpts_arr) {
//			executeWorkflow(removal_least_used_arr[0],
//					removal_rate_arr[0], tfidfTopnTermPerDocument_arr[0],
//					sample_arr[0], mapredChildJavaOpts,
//					parallel_arr[0]);
//		}
//		for (String parallel : parallel_arr) {
//			executeWorkflow(removal_least_used_arr[0],
//					removal_rate_arr[0], tfidfTopnTermPerDocument_arr[0],
//					sample_arr[0], mapredChildJavaOpts_arr[0],
//					parallel);
//		}

	}

	private static void executeWorkflow(String removal_least_used,
			String removal_rate, String tfidfTopnTermPerDocument,
			String sample, String mapredChildJavaOpts, String parallel) throws Exception {

		String params = StringUtils.join(new String[] { removal_least_used,
				removal_rate, tfidfTopnTermPerDocument, sample,
				mapredChildJavaOpts, parallel },"-");
		params = params.replaceAll("\\.", "_");

		OozieClient wc = new OozieClient(
				"http://hadoop-master.vls.icm.edu.pl:11000/oozie");
		Properties conf = wc.createConfiguration();
conf.setProperty("oozie.use.system.libpath","true");
conf.setProperty("oozie.action.sharelib.for.pig","/user/oozie/share/lib/pig");

		conf.setProperty("masterNode", masterNode);
		conf.setProperty("sample", sample); 
		conf.setProperty("removal_least_used", removal_least_used);
		conf.setProperty("removal_rate", removal_rate);
		conf.setProperty("similarityTopnDocumentPerDocument", "20");
		conf.setProperty("mapredChildJavaOpts", "-Xmx"+mapredChildJavaOpts+"g"); 
		conf.setProperty("parallel", parallel);
		conf.setProperty("tfidfTopnTermPerDocument", tfidfTopnTermPerDocument);
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
		conf.setProperty("bwndataMetadataInputPath",
				bwndataMetadataInputPath);
		conf.setProperty("similarityOutputPath", similarityOutputPath+"/"+params);
		conf.setProperty("scheduler", scheduler);
		conf.setProperty("tmpCompressionCodec", tmpCompressionCodec);

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
