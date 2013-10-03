cd ../../../..
mvn clean install -DskipTests
mkdir src/main/pig/legacy/lib
cp target/disambiguation-author-*-SNAPSHOT.jar src/main/pig/legacy/lib
