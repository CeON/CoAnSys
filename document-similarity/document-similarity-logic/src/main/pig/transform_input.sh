hadoop fs -rm -r -f docsim-demonstrator/working/
hadoop fs -mkdir docsim-demonstrator/working/

pig \
-f transform_input.pig \
-param commonJarsPath="lib/*.jar" \
-param dsdemo_parallel=20 \
-param dsdemo_pool=default \
-param dsdemo_input=/user/pdendek/docsim-demonstrator/pm-full_T_BW/ \
-param dsdemo_output=/user/pdendek/docsim-demonstrator/working/ \
-param dsdemo_authors=/user/pdendek/docsim-demonstrator/working/AUTH/ \
-param dsdemo_doc_basic=/user/pdende/docsim-demonstrator/working/DOC_BASIC/ \
-param dsdemo_doc_complete=/user/pdendek/docsim-demonstrator/working/DOC_COMPLETE/ \


