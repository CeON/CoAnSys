hadoop fs -rm -r -f docsim-on-oap/docsim-integrated-wf2
cd target
hadoop fs -copyFromLocal oozie-wf docsim-on-oap/docsim-integrated-wf2

