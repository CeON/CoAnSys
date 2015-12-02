cd ../../../..
mvn clean install -P sep -DskipTests
rm -rf src/main/pig/featureExtraction/lib
mkdir  src/main/pig/featureExtraction/lib
cp target/*SNAPSHOT*.jar src/main/pig/featureExtraction/lib
