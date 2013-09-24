cd ../../..
mvn clean install -DskipTests
mkdir src/main/pig/lib
cp target/disambiguation-author-*-SNAPSHOT.jar src/main/pig/lib
