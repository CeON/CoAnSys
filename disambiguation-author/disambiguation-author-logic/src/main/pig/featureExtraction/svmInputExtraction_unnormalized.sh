hadoop fs -mkdir -p projects/201507/and-model-from-orcid/unnormalized_data
hadoop fs -rm -r -f projects/201507/and-model-from-orcid/unnormalized_data

echo "COLLECTION=orcid INPUT=/srv/orcid/20150724 OUTPUT=projects/201507/and-model-from-orcid/unnormalized_data"

pig \
-c \
-param dc_m_hdfs_inputDocsData=/srv/orcid/20150724 \
-param dc_m_hdfs_output=projects/201507/and-model-from-orcid/unnormalized_data \
-param dc_m_str_feature_info="Intersection#EX_CLASSIFICATION_CODES#1#1,Intersection#EX_KEYWORDS_SPLIT#1#1,Intersection#EX_KEYWORDS#1#1,Intersection#EX_TITLE_SPLIT#1#1,Intersection#EX_YEAR#1#1,Intersection#EX_TITLE#1#1,Intersection#EX_DOC_AUTHS_SNAMES#1#1,Intersection#EX_DOC_AUTHS_FNAME_FST_LETTER#1#1,Intersection#EX_AUTH_FNAMES_FST_LETTER#1#1,Intersection#EX_AUTH_FNAME_FST_LETTER#1#1,Intersection#EX_EMAIL#1#1,Intersection#EX_PERSON_ID#1#1" \
-param commonJarPath="lib/*.jar" \
-param mapredChildJavaOpts="-Xmx8g" \
-param pool=default \
-param parallel_param=8 \
-f svmInputExtraction_unnormalized.pig

