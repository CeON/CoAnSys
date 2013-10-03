cd ../../../..
mvn clean install -P sep -DskipTests
mkdir src/main/pig/legacy/lib
cp target/disambiguation-author-*-SNAPSHOT*.jar src/main/pig/legacy/lib
