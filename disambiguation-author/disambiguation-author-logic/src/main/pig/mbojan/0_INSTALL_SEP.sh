cd ../../../..
mvn clean install -P sep -DskipTests
mkdir src/main/pig/mbojan/lib
cp target/disambiguation-author-*.jar src/main/pig/mbojan/lib
