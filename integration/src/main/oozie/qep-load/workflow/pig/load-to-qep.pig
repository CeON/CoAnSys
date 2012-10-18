REGISTER /usr/lib/zookeeper/zookeeper-3.4.3-cdh4.0.1.jar;
REGISTER /usr/lib/hbase/hbase.jar;
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar;

%default disambigPath 'full/disambiguation-author/contribperson'
%default coauthorsPath 'full/coauthor-pairs/all'
%default docsimPath 'full/similarity/docsim'
%default separator '_'
%default qepTable 'qep'

disambig = LOAD '$disambigPath' AS (contributorId: chararray, personId: chararray);
coauthors = LOAD '$coauthorsPath' AS (personId1: chararray, personId2: chararray, count: long);
docsim = LOAD '$docsimPath' AS (docId1: chararray, docId2: chararray, similarity: double);

disambig_filtered = FILTER disambig BY contributorId IS NOT NULL AND personId IS NOT NULL;
coauthors_filtered = FILTER coauthors BY personId1 IS NOT NULL AND personId2 IS NOT NULL AND count IS NOT NULL;
docsim_filtered = FILTER docsim BY docId1 IS NOT NULL AND docId2 IS NOT NULL AND similarity IS NOT NULL;

disambig_typed = FOREACH disambig_filtered GENERATE CONCAT('da$separator', contributorId), personId;
coauthors_typed = FOREACH coauthors_filtered GENERATE CONCAT('cp$separator', CONCAT(personId1, CONCAT('$separator', personId2))), count;
docsim_typed = FOREACH docsim_filtered GENERATE CONCAT('ds$separator', CONCAT(docId1, CONCAT('$separator', docId2))), similarity;

STORE disambig_typed INTO 'hbase://$qepTable' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('v:personId');
STORE coauthors_typed INTO 'hbase://$qepTable' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('v:cnt');
STORE docsim_typed INTO 'hbase://$qepTable' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('v:sim');
