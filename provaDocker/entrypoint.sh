#! bin/bash

git clone https://github.com/F3kFak/Chat-MerattiLanzi.git
cd Chat-MerattiLanzi/client && 
mvn package && 
java -jar ./target/client-1.0-SNAPSHOT-jar-with-dependencies.jar
