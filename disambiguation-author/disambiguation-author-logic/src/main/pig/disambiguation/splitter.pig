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
%DEFAULT and_splitted_output_one 'splitted/one'
%DEFAULT and_splitted_output_exh 'splitted/exh'
%DEFAULT and_splitted_output_apr_sim 'splitted/apr-sim'
%DEFAULT and_splitted_output_apr_no_sim 'splitted/apr-no-sim'

%DEFAULT and_time 20130709_1009
%DEFAULT and_sample 1.0
%DEFAULT and_exhaustive_limit 6627
%DEFAULT and_aproximate_sim_limit 1000000

%DEFAULT and_feature_info 'IntersectionPerMaxval#EX_DOC_AUTHS_FNAME_FST_LETTER#1.0#1'
%DEFAULT and_lang 'all'
%DEFAULT and_skip_empty_features 'true'
%DEFAULT and_use_extractor_id_instead_name 'true'
DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA('-featureinfo $and_feature_info -lang $and_lang -skipEmptyFeatures $and_skip_empty_features -useIdsForExtractors $and_use_extractor_id_instead_name');

%DEFAULT and_threshold '-0.8'
%DEFAULT and_statistics 'false'
DEFINE featuresCheck pl.edu.icm.coansys.disambiguation.author.pig.FeaturesCheck('$and_threshold','$and_feature_info','$and_use_extractor_id_instead_name','$and_statistics');

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
%DEFAULT mapredChildJavaOpts -Xmx4096m

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

-- -----------------------------------------------------
-- READING SQ, FIRST FILTERING
-- -----------------------------------------------------

A1 = LOAD '$and_inputDocsData' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
A2 = sample A1 $and_sample;

B1 = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (dockey:chararray, cId:chararray, sname:int, metadata:map[{(int)}]);

B = FILTER B1 BY (dockey is not null);

-- removing docId column 
-- add bool - true when contributor is similar to himself
FC = foreach B generate cId as cId, sname as sname, metadata as metadata, featuresCheck(cId, sname, metadata) as gooddata;

split FC into
		BAD if gooddata == false,
		GOOD if gooddata == true;

-- -----------------------------------------------------
-- PROCESSING CONTRIBUDORS DISIMILAR TO THEMSELVES
-- -----------------------------------------------------

-- simulating grouping ( 'by sname' ) and counting ( = 1 )
-- in fact we will get different groups with the same sname - and that is what we need in that case
-- becouse each contributor with bad data need to be in separate cluster size 1
D1A = foreach BAD generate sname as sname, {(cId,sname,metadata)} as datagroup, 1 as count;


-- -----------------------------------------------------
-- PROCESSING CONTRIBUDORS SIMILAR TO THEMSELVES
-- -----------------------------------------------------

C = group GOOD by sname;
-- D: {sname: chararray, datagroup: {(cId: chararray,sname: int,metadata: map[{(val_0: int)}])}, count: long}
-- TODO: remove sname from datagroup. Then in UDFs as well..
D = foreach C generate group as sname, GOOD as datagroup, COUNT(GOOD) as count;

split D into
        D1B if count == 1,
        D100 if (count > 1 and count <= $and_exhaustive_limit),
        DX if (count > $and_exhaustive_limit and count <= $and_aproximate_sim_limit),
        D1000 if count > $and_aproximate_sim_limit;
        

-- -----------------------------------------------------
-- STORING DATA READY TO DISAMBIGUATION
-- -----------------------------------------------------

-- add contributors with bad data to table D (single contributors)
D1 = union D1A, D1B;

store D1 into '$and_splitted_output_one';
store D100 into '$and_splitted_output_exh';
store D1000 into '$and_splitted_output_apr_sim';
store DX into '$and_splitted_output_apr_no_sim';

-- storing relation contributor id - document id, which we need in future during serialization
Q = foreach B generate cId, dockey;
store Q into '$and_cid_dockey';
