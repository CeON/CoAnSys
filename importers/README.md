CoAnSys/importers
=============

## About
CoAnSys/importers is a project containing methods to easily load [BWMeta document](../examples/bwmeta) packages into HBase.

It provides two way to do so - (a) with RichTSV file or (b) via REST.

## Prerequirements
This package depends on (Hadoop)[http://hadoop.apache.org/] from [Cloudera distribution](https://ccp.cloudera.com/display/SUPPORT/CDH+Downloads), version CDH 4.0.1 and appropriate [HBase](hbase.apache.org) version. 
As CDH4 depends on [Protocol Buffers](http://code.google.com/p/protobuf/) 2.4.1 so is CoAnSys/importers.

## Quick Start

### Instalation
```
# repository clone
cd ~  
git clone REPOSITORY_ADDRESS/CoAnSys.git  
cd CoAnSys/importers  
# package instalation  
## if you are not providing hadoop libraries, please modify pom.xml  
## by removing texts "<scope>provided</scope>"
mvn install
mvn assembly:single
```

### Example data acquisition
```
# get
```
[example data](../examples/otworz-ksiazke-tylkobwmeta-20110316-00000.zip)

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

### import
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

#### via RichTSV file (TBD)

