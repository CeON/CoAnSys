--
-- This file is part of CoAnSys project.
-- Copyright (c) 2012-2013 ICM-UW
--
-- CoAnSys is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- CoAnSys is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
-- GNU Affero General Public License for more details.
--
-- You should have received a copy of the GNU Affero General Public License
-- along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------


%DEFAULT and_inputDocsData /srv/bwndata/seqfile/bazekon-20130314.sf
%DEFAULT and_cid_dockey 'cid_dockey'
%DEFAULT and_time 20130709_1009
%DEFAULT and_feature_info 'CoAuthorsSnameDisambiguatorFullList#EX_AUTH_INITIALS#-0.0000166#8,ClassifCodeDisambiguator#EX_CLASSIFICATION_CODES#0.99#12,KeyphraseDisambiguator#EX_KEYWORDS_SPLIT#0.99#22,KeywordDisambiguator#EX_KEYWORDS#0.0000369#40'
%DEFAULT and_lang 'all'
%DEFAULT and_skip_empty_features 'true'
%DEFAULT and_use_extractor_id_instead_name 'true'

DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA('$and_feature_info','$and_lang','$and_skip_empty_features','$and_use_extractor_id_instead_name');

%DEFAULT and_threshold '-0.8'
%DEFAULT and_statistics 'true'
DEFINE featuresCheck pl.edu.icm.coansys.disambiguation.author.pig.FeaturesCheck('$and_threshold','$and_feature_info','$and_use_extractor_id_instead_name','$and_statistics');


%DEFAULT and_sample 1.0
%DEFAULT and_exhaustive_limit 6627
%DEFAULT and_aproximate_sim_limit 1000000
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-*-cdh4.*-security.jar
REGISTER /usr/lib/hbase/lib/guava-*.jar


-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT and_parallel_param 85
%DEFAULT pig_tmpfilecompression_param true
%DEFAULT pig_tmpfilecompression_codec_param gz
%DEFAULT job_priority normal
%DEFAULT pig_cachedbag_mem_usage 0.1
%DEFAULT pig_skewedjoin_reduce_memusage 0.3
%DEFAULT mapredChildJavaOpts -Xmx8000m

set default_parallel $and_parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
set mapred.child.java.opts $mapredChildJavaOpts
-- ulimit must be more than two times the heap size value !
-- set mapred.child.ulimit unlimited
set dfs.client.socket-timeout 60000
%default and_scheduler benchmark80
set mapred.fairscheduler.pool $and_scheduler 
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

A1 = LOAD '$and_inputDocsData' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
A2 = sample A1 $and_sample;

B1 = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (dockey:chararray, cId:chararray, sname:int, metadata:map[{(int)}]);

B = FILTER B1 BY (dockey is not null) AND featuresCheck(cId, sname, metadata);

-- removing docId column
C1 = foreach B generate cId as cId, sname as sname, metadata as metadata;

C = group C1 by sname;
-- D: {sname: chararray, datagroup: {(cId: chararray,cPos: int,sname: chararray,data: map[{(val_0: chararray)}])}, count: long}
-- TODO: remove sname from datagroup. Then in UDFs as well..
D = foreach C generate group as sname, C1 as datagroup, COUNT(C1) as count;

split D into
        D1 if count == 1,
        D100 if (count > 1 and count <= $and_exhaustive_limit),
        DX if (count > $and_exhaustive_limit and count <= $and_aproximate_sim_limit),
        D1000 if count > $and_aproximate_sim_limit;

%DEFAULT semi 'tmp'
%DEFAULT final 'identities'
%DEFAULT and_splitter_output 'splitted'
%DEFAULT one 'one'
%DEFAULT exh 'exh'
%DEFAULT appSim 'app-sim'
%DEFAULT appNoSim 'app-no-sim'
%DEFAULT cid_dockey 'cid_dockey'

store D1 into '$and_splitter_output/$one';
store D100 into '$and_splitter_output/$exh';
store D1000 into '$and_splitter_output/$appSim';
store DX into '$and_splitter_output/$appNoSim';

Q = foreach B generate cId, dockey;
store Q into '$and_cid_dockey';
-- TODO: wygenerowac tabele (dockey, cId) i zapisac


