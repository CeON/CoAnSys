if [ $CHRUM = "" ]
then
	echo "ERROE: No path to chrum location in environment specifed. Set environment variable CHRUM and run again."
	exit 1
fi

${CHRUM}/only_workflow_generation.py ../chrum/cluster.properties.part1  ../chrum/workflow.xml.part1 ../oozie/workflow.xml !
echo "SUCCESS: workflow.xml created."
