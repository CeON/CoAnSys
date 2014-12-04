hadoop fs -mkdir projects/201408/docsim-demonstrator
hadoop fs -rm -r -f projects/201408/docsim-demonstrator/docsim-demo-wf
hadoop fs -copyFromLocal target/oozie-wf projects/201408/docsim-demonstrator/docsim-demo-wf
