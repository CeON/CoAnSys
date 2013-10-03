cd ../../../..
mvn clean install -DskipTests
mkdir src/main/pig/featureExtraction/lib
cp target/disambiguation-author-*-SNAPSHOT.jar src/main/featureExtraction/lib
