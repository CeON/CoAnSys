hadoop fs -rm -r -f docsim-on-oap-C/docsim-ranked-wf
cd target
hadoop fs -mkdir docsim-on-oap-C
hadoop fs -copyFromLocal oozie-wf docsim-on-oap-C/docsim-ranked-wf

