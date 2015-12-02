cd ../../../..
mvn clean install -DskipTests
mkdir  src/main/pig/featureExtraction/lib
cp target/*SNAPSHOT.jar src/main/pig/featureExtraction/lib
