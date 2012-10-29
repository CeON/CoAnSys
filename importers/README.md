CoAnSys/importers
=============

## About
CoAnSys/importers is a project containing methods to easily load [BWMeta document](../examples/bwmeta) packages into HBase.

It provides two way to do so:
* via REST
* via put or bulkload method

## Prerequirements
This package depends on [Hadoop](http://hadoop.apache.org/) from [Cloudera distribution](https://ccp.cloudera.com/display/SUPPORT/CDH+Downloads), version CDH 4.0.1 and appropriate [HBase](hbase.apache.org) version. 
As CDH4 depends on [Protocol Buffers](http://code.google.com/p/protobuf/) 2.4.1 so is CoAnSys/importers.

## Quick Start

### Instalation
```
# repository clone
cd ~  
git clone REPOSITORY_ADDRESS/CoAnSys.git  
cd CoAnSys/importers  
# package instalation  
## if you are not providing hadoop libraries, please modify pom.xml by removing texts "<scope>provided</scope>"
mvn install
mvn assembly:single
```

### Example data acquisition
```
# get sample data from the link bellow
```
[Example Data](https://svn.ceon.pl/research/CoAnSys-examples/GROTOAP-10.zip)

```
cd ~
mkdir HBASE_IMPORT
cd HBASE_IMPORT
wget 'LOCALIZATION_FROM_THE_LINK'
```

### HBase output table creation
```
echo "create 'testProto','m','c'" | hbase shell
```

### Import
#### via REST
```
cd ~/CoAnSys/importers/target
# the following command assign "TESTCOLLECTION" collection name 
# to all articles from  BWMeta zip files from the folder
# ~/HBASE_IMPORT/ and send them
# to HBase table "testProto" localized on localhost.
# Port 8080 is the default port for such a communication.
# Please specify the port adequate for your hbase configuration
java -cp importers-1.0-SNAPSHOT-jar-with-dependencies.jar pl.edu.icm.coansys.importers.io.writers.hbaserest.HBaseRestWriter_Bwmeta ~/HBASE_IMPORT/ TESTCOLLECTION localhost 8080 testProto
```

#### via put or bulkload method

Before running this data load make sure that
* target table is already created in HBase
* importers and commons project are successfully build (jars commons/target/commons-1.0-SNAPSHOT-jar-with-dependencies.jar and importers/target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar are available)

Two steps are required:
* Pack the content of BWMeta archives (both metadata records and pdf/plain-text files) into sequence files in HDFS
```
$ cd importers-sf
$ # ./generate-sequence-file.sh <importers.jar> <bwndata_collection_directory> <collection_name> <sequence_file_in_hdfs>
$ ./generate-sequence-file.sh workflow/lib/importers*.jar /mnt/tmp/bwndata/collection-date collection bwndata/sequence-file/collection-date.sf
```

* Import the sequence files into HBase using "put" or "bulkloading" method (depending on a parameter) using Oozie workflow
```
cd ..
vim importers-sf/available.collections.cluster.properties # specify at least "outputTableName" and "collectionDocumentWrapperSequenceFile" properties
./submit-to-oozie.sh importers-sf 1 akawa hadoop-master importers-sf/available.collections.cluster.properties
```
