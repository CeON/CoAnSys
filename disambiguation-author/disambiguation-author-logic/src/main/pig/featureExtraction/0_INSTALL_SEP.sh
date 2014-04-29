cd ../../../..
mvn clean install -P sep -DskipTests
cp target/disambiguation-author-*-SNAPSHOT*.jar src/main/pig/disambiguation/lib
