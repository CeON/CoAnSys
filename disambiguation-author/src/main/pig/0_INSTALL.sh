cd ../../..
mvn clean install -DskipTests
cp target/disambiguation-author-*-SNAPSHOT.jar src/main/pig/lib
cd src/main/pig
