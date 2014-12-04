FEATURE_INFO="#EX_CLASSIFICATION_CODES#1#1,#EX_KEYWORDS_SPLIT#1#1,#EX_KEYWORDS#1#1,#EX_TITLE_SPLIT#1#1,YearDisambiguator#EX_YEAR#1#1,#EX_TITLE#1#1,CoAuthorsSnameDisambiguatorFullList#EX_DOC_AUTHS_SNAMES#1#1,#EX_DOC_AUTHS_FNAME_FST_LETTER#1#1,#EX_AUTH_FNAMES_FST_LETTER#1#1,#EX_AUTH_FNAME_FST_LETTER#1#1,#EX_PERSON_ID#1#1,#EX_EMAIL#1#1"
THRESHOLD="-0.8" 

COLLECTION=$1
INFIX="HADOOP"

if [ "$COLLECTION" == "pbn" ] ; then
        INPUT="projects/201409/and-model/pbn_20131129_orcid2013_T_BW"
	OUTPUT="projects/201409/and-model/pbn_orcid2013_features_${INFIX}_T_BW"
elif [ "$COLLECTION" == "springer" ] ; then
        INPUT="projects/201409/and-model/springer-metadata_orcid2013_T_BW"
	OUTPUT="projects/201409/and-model/springer_orcid2013_features_${INFIX}_T_BW"
elif [ "$COLLECTION" == "pm" ] ; then
        INPUT="projects/201409/and-model/pm-full_orcid2013_T_BW"
	OUTPUT="projects/201409/and-model/pm-full_orcid2013_features_${INFIX}_T_BW"
else
	INPUT="projects/201409/and-model/yadda_20131129_orcid2013_T_BW"
	OUTPUT="projects/201409/and-model/yadda_orcid2013_features_${INFIX}_T_BW"
fi

echo "COLLECTION=$COLLECTION INPUT=$INPUT OUTPUT=$OUTPUT"

hadoop fs -rm -r -f ${OUTPUT}

pig \
-param dc_m_hdfs_inputDocsData=${INPUT} \
-param        dc_m_hdfs_output=${OUTPUT} \
-param commonJarPath="lib/*.jar" \
-param mapredChildJavaOpts="-Xmx20g" \
-param pool=bigjobs \
-param paralleli_param=20 \
-f feature_extraction.pig 

