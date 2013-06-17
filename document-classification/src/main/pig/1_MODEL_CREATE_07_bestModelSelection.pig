--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT dc_m_hdfs_ending */part*
%DEFAULT dc_m_hdfs_modelEvaluation /tmp/dataTestEval
%DEFAULT dc_m_chararray_modelCriteria acc
%DEFAULT dc_m_hdfs_bestModelPath /tmp/model
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
A = load '$dc_m_hdfs_modelEvaluation$dc_m_hdfs_ending' as (source:chararray,acc:double, p:double,r:double,f1:double, hlDivCategCount:double, zol:double);
B = order A by $dc_m_chararray_modelCriteria desc;
C = limit B 1;
D = foreach C generate source;
store D into '$dc_m_hdfs_bestModelPath';
