cd ../../..
mvn clean install -P sep
cp target/disambiguation-author-*-SNAPSHOT*.jar src/main/pig/lib
cd src/main/pig
