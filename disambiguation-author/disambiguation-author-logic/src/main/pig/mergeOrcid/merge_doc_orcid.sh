
COLLECTION=$1

if [ "$COLLECTION" == "pbn" ] ; then
	NRML_INPUT="projects/201409/and-model/pbn_20131129_T_BW"
        OUTPUT="projects/201409/and-model/pbn_20131129_orcid2013_T_BW"
elif [ "$COLLECTION" == "springer" ] ; then
	NRML_INPUT="projects/201409/and-model/springer-metadata_T_BW"
        OUTPUT="projects/201409/and-model/springer-metadata_orcid2013_T_BW"
elif [ "$COLLECTION" == "yadda" ] ; then
	NRML_INPUT="projects/201409/and-model/yadda_20131129_T_BW"
        OUTPUT="projects/201409/and-model/yadda_20131129_orcid2013_T_BW"
else
	NRML_INPUT="projects/201409/and-model/pm-full_T_BW"
        OUTPUT="projects/201409/and-model/pm-full_orcid2013_T_BW"
fi

echo "COLLECTION=$COLLECTION NRML_INPUT=$NRML_INPUT OUTPUT=$OUTPUT"

hadoop fs -rm -r -f ${OUTPUT} 

pig \
-param orcid_mapredChildJavaOpts="-Xmx20g" \
-param orcid_pool=bigjobs \
-param commonJarPath="lib/*.jar" \
-param orcid_parallel=20 \
-param orcid_input="datasets/ORCID/2013/orcid_2013.tproto.sf" \
-param nrml_input=${NRML_INPUT} \
-param output=${OUTPUT} \
-f merge_doc_orcid.pig 

