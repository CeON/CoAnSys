cd ../../../..
mvn clean install -P sep -DskipTests
mkdir src/main/pig/disambiguation/lib
cp target/disambiguation-author-*-SNAPSHOT.jar src/main/disambiguation/lib
