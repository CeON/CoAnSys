In order to run the application you have to have Cloudera's Hadoop installed. The steps of installation procedure are described below.

Hadoop: installation and configuration
======================================
Warning: Because of a bug in the Oozie version provided with Cloudera's Hadoop (which is removed in the version available in the source code repository), **you have to have (Oracle) Java JDK 1.6 installed**. This version of Oozie does not work with JDK 1.7.

---

Install Cloudera Hadoop CDH4 with MRv1 in accordance with the instructions given in [Cloudera CDH4 intallation guide](https://ccp.cloudera.com/display/CDH4DOC/CDH4+Installation+Guide) , to be more specific:

Hadoop can be run in one of three modes:

- **standalone mode** - runs all of the Hadoop processes in a single JVM which makes it easy to debug the application. 
- **pseudo-distributed mode** - runs a full-fledged Hadoop on your local computer
- **distributed mode** - runs on a cluster consisting of many nodes/hosts

Below we will show how to install Hadoop initially in pseudo-distributed mode but we will be able to switch between standalone and pseudo-distributed modes.

Install Hadoop in pseudo-distributed mode, see  [Cloudera CDH4 pseudo distributed mode installation guide](https://ccp.cloudera.com/display/CDH4DOC/Installing+CDH4+on+a+Single+Linux+Node+in+Pseudo-distributed+Mode)

In case of Ubuntu 12.04:

- create new file `/etc/apt/sources.list.d/cloudera.list` with contents:

		deb http://archive.cloudera.com/cdh4/ubuntu/precise/amd64/cdh precise-cdh4 contrib
		deb-src http://archive.cloudera.com/cdh4/ubuntu/precise/amd64/cdh precise-cdh4 contrib

- add a repository key:
		
		curl -s http://archive.cloudera.com/cdh4/ubuntu/precise/amd64/cdh/archive.key | sudo apt-key add -
		
- update
			
		sudo apt-get update
	
- install packages 
			
		sudo apt-get install hadoop-0.20-conf-pseudo
			
- next, follow the steps described in the Cloudera's guide for installing Hadoop in pseudo-distributed mode starting from the step "Step 1: Format the NameNode." This is available at [Cloudera CDH4 pseudo distributed mode installation guide - "Step 1: Format the Namenode"](https://ccp.cloudera.com/display/CDH4DOC/Installing+CDH4+on+a+Single+Linux+Node+in+Pseudo-distributed+Mode#InstallingCDH4onaSingleLinuxNodeinPseudo-distributedMode-Step1%3AFormattheNameNode.).
		
---

You can **switch between standalone and pseudo-distributed configurations** (or others) of Hadoop using the `update-alternatives` command, e.g.:

- `update-alternatives --display hadoop-conf` for list of available configurations and information which is the active one 
- `sudo update-alternatives --set hadoop-conf /etc/hadoop/conf.empty` to set the active configuration to `/etc/hadoop/conf.empty` which corresponds to Hadoop standalone mode.

You can view the web interfaces to the following services using the addresses:

- **NameNode** - provides a web console for viewing HDFS, number of Data Nodes, and logs - [http://localhost:50070/](http://localhost:50070/)
	- In the pseudo-distributed configuration, you should see one live DataNode named "localhost".
- **JobTracker** - allows viewing and running completed and failed jobs with logs - [http://localhost:50030/](http://localhost:50030/)

Oozie: installation and configuration
-------------------------------------
Based on [Cloudera CDH4 Oozie installation guide](https://ccp.cloudera.com/display/CDH4DOC/Oozie+Installation#OozieInstallation-ConfiguringOozieinstall)

- Install Oozie with

		sudo apt-get install oozie oozie-client

- Create Oozie database schema

		sudo -u oozie /usr/lib/oozie/bin/ooziedb.sh create -run
	

	- this should result in a similar output:

			Validate DB Connection
			DONE
			Check DB schema does not exist
			DONE
			Check OOZIE_SYS table does not exist
			DONE
			Create SQL schema
			DONE
			Create OOZIE_SYS table
			DONE
		
			Oozie DB has been created for Oozie version '3.1.3-cdh4.0.1'

			The SQL commands have been written to: /tmp/ooziedb-8221670220279408806.sql

- Install ExtJS version 2.2 library:
	- download the zipped library from [http://extjs.com/deploy/ext-2.2.zip](http://extjs.com/deploy/ext-2.2.zip)
	- copy the zip file to `/var/lib/oozie` end extract it there
- Install Oozie ShareLib:

		mkdir /tmp/ooziesharelib
		cd /tmp/ooziesharelib
		tar -zxf /usr/lib/oozie/oozie-sharelib.tar.gz
		sudo -u hdfs hadoop fs -mkdir /user/oozie
		sudo -u hdfs hadoop fs -chown oozie /user/oozie
		sudo -u oozie hadoop fs -put share /user/oozie/share
	
- Start the Oozie server:

		sudo service oozie start

- Check Status of the server:
	- from command-line:

			oozie admin -oozie http://localhost:11000/oozie -status
	
	This should be printed out:

			System mode: NORMAL
	
	- Through a webpage - use a web browser to open: [http://localhost:11000/oozie/](http://localhost:11000/oozie/)

If you want to check if Oozie correctly executes workflows, you can ran some example workflows as described in [Cloudera Oozie example workflows](http://archive.cloudera.com/cdh4/cdh/4/oozie/DG_Examples.html). Note that contrary to what is written there, the Oozie server is not available at `http://localhost:8080/oozie` but at `http://localhost:11000/oozie` address
