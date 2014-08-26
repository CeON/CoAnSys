cd ../../../..
mvn clean install  -DskipTests
mkdir -p src/main/pig/generalized_feature_extraction/lib
cp target/disambiguation-author-*-SNAPSHOT*.jar src/main/pig/generalized_feature_extraction/lib

