hadoop fs -rm -r -f projects/201408/and-model/merged_pm-full_orcid2013_T_BW 

pig \
-param orcid_mapredChildJavaOpts="-Xmx10g" \
-param orcid_pool=bigjobs \
-param commonJarPath="lib/*.jar" \
-param orcid_parallel=20 \
-param orcid_input="datasets/ORCID/2013/orcid_2013.tproto.sf" \
-param nrml_input="datasets/pm-full/pm-full_T_BW" \
-param output="projects/201408/and-model/merged_pm-full_orcid2013_T_BW" \
-f merge_doc_orcid.pig 
