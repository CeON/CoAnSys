rm lib/disambiguation*
cd ../../..
rm target/disambiguation-author-*
mvn clean install
cp target/disambiguation-author-*-SNAPSHOT.jar src/main/pig/lib
cd src/main/pig
