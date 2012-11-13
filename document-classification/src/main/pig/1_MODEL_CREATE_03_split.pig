--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hdfs_src /tmp/dataForDocClassif
%DEFAULT dc_m_int_concreteInvestigatedFold 0
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar

REGISTER '$commonJarsPath'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- declare section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DECLARE TR _Tr_
%declare TE _Te_
-- -----------------------------------------------------
-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
-- -----------------------------------------------------
IMPORT 'AUXIL_docsim.macros.def.pig';
IMPORT 'AUXIL_macros.def.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
set default_parallel 16
set pig.tmpfilecompression true
set pig.tmpfilecompression.codec gz
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------


D = LOAD '$dc_m_hdfs_docClassifMapping';
split D into
	Te if $2 == $dc_m_int_concreteInvestigatedFold,
	Tr if $2 != $dc_m_int_concreteInvestigatedFold;
store Tr into '$dc_m_hdfs_src$TR$dc_m_int_concreteInvestigatedFold';
store Te into '$dc_m_hdfs_src$TE$dc_m_int_concreteInvestigatedFold';
