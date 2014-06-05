hadoop fs -rm -r -f docsim-on-oap/docsim-integrated-wf
cd target
hadoop fs -copyFromLocal oozie-wf docsim-on-oap/docsim-integrated-wf

