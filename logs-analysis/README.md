CoAnSys/logs-analysis
=============

## About

The goal of the project is to perform statistical analysis of logs derived
from portals for scientific community.
The first implemented feature is finding resources that are the most
popular among the users.

## Prerequirements

This package depends on Hadoop from Cloudera distribution, version CDH
4.0.1. CDH4 requires also Protocol Buffers 2.4.1.

## Quick Start

### Installation
```
# repository clone
git clone REPOSITORY_ADDRESS/CoAnSys.git
# build
cd CoAnSys/logs-analysis
mvn install
mvn assembly:single
```

### Generating example logs

If Hadoop is installed in pseudo distributed or distributed mode,
hadoop HDFS daemons (namenode, secondarynamenode, datanode(s)) should be
running. In this case the result file will be created in the HDFS filesystem 
(not in a local fs).
```
# This command generates 5000 example log entries
hadoop jar target/logs-analysis-*-jar-with-dependencies.jar \
    pl.edu.icm.coansys.logsanalysis.logsacquisition.GenerateDummyLogs \
    50000 /tmp/example_logs.log
```

### Analysing logs
```
# This command finds 10 most popular resources from example logs
# Output directory /tmp/output_data must not exist. Results will be
# saved in /tmp/output_data/part_r_00000 file.
hadoop jar target/logs-analysis-*-jar-with-dependencies.jar \
    pl.edu.icm.coansys.logsanalysis.jobs.MostPopular \
    /tmp/example_logs.log /tmp/output_data 10
```

### Reporting results
```
hadoop jar target/logs-analysis-*-jar-with-dependencies.jar \
    pl.edu.icm.coansys.logsanalysis.presentation.ReportMostPopular \
    /tmp/output_data/part-r-00000
```

## Data formats

Project CoAnSys/logs-analysis uses Protocol Buffers as a format of
serialization of input and output data. Hadoop Sequence File is used
to store data on disk.

More info about Protocol Buffers: http://code.google.com/p/protobuf/

Hadoop SequenceFile is described here:
http://hadoop.apache.org/common/docs/current/api/org/apache/hadoop/io/SequenceFile.html

