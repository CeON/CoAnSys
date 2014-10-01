hadoop fs -mkdir docsim-demonstrator
hadoop fs -rm -r -f docsim-demonstrator/docsim-uber-wf
hadoop fs -copyFromLocal target/oozie-wf docsim-demonstrator/docsim-uber-wf

