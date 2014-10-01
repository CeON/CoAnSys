hadoop fs -rm -r -f projects/201408/and-model/features_pm-full_orcid2013_T_BW

pig \
-param dc_m_hdfs_inputDocsData=projects/201408/and-model/merged_pm-full_orcid2013_T_BW \
-param        dc_m_hdfs_output=projects/201408/and-model/features_pm-full_orcid2013_T_BW \
-param commonJarPath="lib/*.jar" \
-param mapredChildJavaOpts="-Xmx10g" \
-param pool=bigjobs \
-param paralleli_param=20 \
-f feature_extraction.pig 

