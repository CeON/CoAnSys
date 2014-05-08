cd ../../../..
mvn clean install -DskipTests
mkdir src/main/pig/mbojan/lib
cp target/disambiguation-author-*-SNAPSHOT.jar src/main/pig/mbojan/lib
