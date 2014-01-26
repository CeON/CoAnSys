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

-------------------------------------------------------
-- load BWMeta documents form sequence files stored in hdfs
-------------------------------------------------------
DEFINE load_from_hdfs(inputPath, sampling) RETURNS C {
	A = LOAD '$inputPath' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 
		 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);	
	B = SAMPLE A $sampling;	
	$C = FOREACH B GENERATE $0 as docId, pl.edu.icm.coansys.similarity.pig.udf.DocumentProtobufToTupleMap($1) as document ;
};
-------------------------------------------------------
-- clean and drop nulls
-------------------------------------------------------
DEFINE stem_words(docterms, id, type) RETURNS dt {
	doc_keyword_stemmed = FOREACH $docterms GENERATE $id, FLATTEN(StemmedPairs((chararray)$type)) AS term;
	doc_keyword_filtered = FILTER doc_keyword_stemmed BY term IS NOT NULL AND (chararray)term != '';
	$dt = FOREACH doc_keyword_filtered GENERATE $id, (chararray) term;
};

-------------------------------------------------------
-- filer out nulls
-------------------------------------------------------
DEFINE drop_nulls(A, column) RETURNS B {
	$B = FILTER $A BY $A.$column IS NOT NULL;
};

DEFINE drop_nulls2(A, column1, column2) RETURNS B {
	$B =  FILTER $A BY $A.$column1 IS NOT NULL AND $A.column2 IS NOT NULL;
};

DEFINE drop_nulls3(A, column1, column2, column3) RETURNS B {
	$B =  FILTER $A BY $A.$column1 IS NOT NULL AND $A.column2 IS NOT NULL AND $A.column3 IS NOT NULL;
};

DEFINE drop_nulls4(A, column1, column2, column3, column4) RETURNS B {
	$B =  FILTER $A BY $A.$column1 IS NOT NULL AND $A.column2 IS NOT NULL AND $A.column3 IS NOT NULL AND $A.column4 IS NOT NULL;
};

-------------------------------------------------------
-- distinct 
-------------------------------------------------------
DEFINE get_distinct(A, column1, column2) RETURNS unique {
	B = FOREACH $A GENERATE $A.$column1, $A.$column2;
	$unique = DISTINCT B;
};

-------------------------------------------------------
-- copy 
-------------------------------------------------------
DEFINE get_copy(A) RETURNS B {
	$B = FOREACH $A GENERATE *;
};

-------------------------------------------------------
-- find stopwords
-------------------------------------------------------
DEFINE find_stopwords(doc_word, doc_field, term_field, percentage) RETURNS stopwords {
	doc_word_grouped = GROUP $doc_word BY $term_field;
	doc_word_counted = FOREACH doc_word_grouped GENERATE group AS $term_field, COUNT_STAR($doc_word) AS count;
	doc_unique = DISTINCT (FOREACH $doc_word GENERATE $doc_field);
	ndocs = FOREACH (GROUP doc_unique ALL) GENERATE COUNT_STAR(doc_unique) AS total_count;
	$stopwords = FOREACH (FILTER doc_word_counted BY count >= ($percentage * (double)ndocs.total_count)) GENERATE term;
};

-------------------------------------------------------
-- remove stowords
-------------------------------------------------------
DEFINE remove_stopwords(doc_word, stopwords, doc_field, term_field, CC) RETURNS non_stopwords {	
	doc_word_joined = JOIN $doc_word BY $term_field LEFT OUTER, $stopwords BY $term_field USING 'replicated';
	doc_word_non_stop = FILTER doc_word_joined BY $stopwords$CC$term_field IS NULL;
	$non_stopwords = FOREACH doc_word_non_stop GENERATE $doc_word$CC$doc_field AS docId, $doc_word$CC$term_field AS term;
};

-------------------------------------------------------
-- calculate tf-idf
-------------------------------------------------------
DEFINE calculate_tfidf(in_relation, id_field, token_field, in_tfidfMinValue) RETURNS tfidf_values { 
          -- Calculate the term count per document
        doc_word_group = group $in_relation by ($id_field, $token_field);
          doc_word_totals = foreach doc_word_group generate 
                    FLATTEN(group) as ($id_field, token), 
                COUNT($in_relation) as doc_total;

          -- Calculate the document size
        pre_term_group = group doc_word_totals by $id_field;
          pre_term_counts = foreach pre_term_group generate
                    group AS $id_field,
                    FLATTEN(doc_word_totals.(token, doc_total)) as (token, doc_total), 
                    SUM(doc_word_totals.doc_total) as doc_size;
 
          -- Calculate the TF
          term_freqs = foreach pre_term_counts generate $id_field as $id_field,
                    token as token,
                    ((double)doc_total / (double)doc_size) AS term_freq;
 
          -- Get count of documents using each token, for idf
        token_usages_group = group term_freqs by token;
          token_usages = foreach token_usages_group generate
                    FLATTEN(term_freqs) as ($id_field, token, term_freq),
                    COUNT(term_freqs) as num_docs_with_token;

          -- Get document count
          just_ids = foreach $in_relation generate $id_field;
        just_unique_ids = distinct just_ids;
        ndocs_group = group just_unique_ids all;
          ndocs = foreach ndocs_group generate 
                COUNT(just_unique_ids) as total_docs;
 

          -- Note the use of Pig Scalars to calculate idf
          tfidf_all = foreach token_usages {
                    idf    = LOG((double)ndocs.total_docs/(double)num_docs_with_token);
                    tf_idf = (double)term_freq * idf;
                    generate $id_field as $id_field,
                              token as $token_field,
                              tf_idf as tfidf,
                        idf,
                        ndocs.total_docs,
                        num_docs_with_token;
          };
        -- get only important terms
        $tfidf_values = FILTER tfidf_all BY tfidf >= $in_tfidfMinValue;
};

DEFINE calculate_tfidf_nofiltering(in_relation, id_field, token_field) RETURNS tfidf_all { 
          -- Calculate the term count per document
        doc_word_group = group $in_relation by ($id_field, $token_field);
          doc_word_totals = foreach doc_word_group generate 
                    FLATTEN(group) as ($id_field, token), 
                COUNT($in_relation) as doc_total;

          -- Calculate the document size
        pre_term_group = group doc_word_totals by $id_field;
          pre_term_counts = foreach pre_term_group generate
                    group AS $id_field,
                    FLATTEN(doc_word_totals.(token, doc_total)) as (token, doc_total), 
                    SUM(doc_word_totals.doc_total) as doc_size;
 
          -- Calculate the TF
          term_freqs = foreach pre_term_counts generate $id_field as $id_field,
                    token as token,
                    ((double)doc_total / (double)doc_size) AS term_freq;
 
          -- Get count of documents using each token, for idf
        token_usages_group = group term_freqs by token;
          token_usages = foreach token_usages_group generate
                    FLATTEN(term_freqs) as ($id_field, token, term_freq),
                    COUNT(term_freqs) as num_docs_with_token;

          -- Get document count
          just_ids = foreach $in_relation generate $id_field;
        just_unique_ids = distinct just_ids;
        ndocs_group = group just_unique_ids all;
          ndocs = foreach ndocs_group generate 
                COUNT(just_unique_ids) as total_docs;
 

          -- Note the use of Pig Scalars to calculate idf
          $tfidf_all = foreach token_usages {
                    idf    = LOG((double)ndocs.total_docs/(double)num_docs_with_token);
                    tf_idf = (double)term_freq * idf;
                    generate $id_field as $id_field,
                              token as $token_field,
                              tf_idf as tfidf,
                        idf,
                        ndocs.total_docs,
                        num_docs_with_token;
          };
};
-------------------------------------------------------
-- find N most importants group_field
-------------------------------------------------------
DEFINE get_topn_per_group(in_relation, group_field, order_field, order_direction, topn) RETURNS out_relation { 
	grouped = GROUP $in_relation BY $group_field;
	$out_relation = FOREACH grouped {
           sorted = ORDER $in_relation BY $order_field $order_direction;
           top = LIMIT sorted $topn;
           GENERATE flatten(top);-- as (docId1:chararray,docId2:chararray,similarity:double);
	};
};


-------------------------------------------------------
-- calculate similarity using pairwise similarity method
-- that compares only those document that have at least
-- one common words
-------------------------------------------------------
-------------------------------------------------------
-- calculate similarity using pairwise similarity method
-- that compares only those document that have at least
-- one common words
-------------------------------------------------------
DEFINE calculate_pairwise_similarity_filter(in_relation, in_relation2, doc_field, term_field, tfidf_field, CC, joinParallel,in_filter) RETURNS out_relation {

		-- join on terms
        joined = JOIN $in_relation BY $term_field, $in_relation2 BY $term_field USING 'merge' PARALLEL $joinParallel;
        projected = FOREACH joined GENERATE 
                $in_relation$CC$term_field AS term,
                $in_relation$CC$doc_field AS docId1, $in_relation2$CC$doc_field As docId2,
                $in_relation$CC$tfidf_field AS tfidf1, $in_relation2$CC$tfidf_field As tfidf2;

		-- represent each <docIdA,docIdB,sim> by one record
        filtered = FILTER projected BY $in_filter;

		-- calculate similarity for <docIdA,docIdB,sim>
        term_doc_similarity = FOREACH filtered GENERATE term, docId1, docId2, tfidf1, tfidf2,
                KeywordSimilarity(term, docId1, tfidf1, docId2, tfidf2) AS similarity;

        docs_terms_group = GROUP term_doc_similarity BY (docId1, docId2);
        docs_terms_similarity = FOREACH docs_terms_group GENERATE FLATTEN(group) AS (docId1, docId2),
		DocsCombinedSimilarity(term_doc_similarity.docId1, term_doc_similarity.docId2, term_doc_similarity.similarity) AS similarity;

        $out_relation = FOREACH docs_terms_similarity GENERATE docId1, docId2, similarity;
};




DEFINE calculate_pairwise_similarity(in_relation, in_relation2, doc_field, term_field, tfidf_field, CC, joinParallel) RETURNS out_relation {

		-- join on terms
        joined = JOIN $in_relation BY $term_field, $in_relation2 BY $term_field USING 'merge' PARALLEL $joinParallel;
        projected = FOREACH joined GENERATE 
                $in_relation$CC$term_field AS term,
                $in_relation$CC$doc_field AS docId1, $in_relation2$CC$doc_field As docId2,
                $in_relation$CC$tfidf_field AS tfidf1, $in_relation2$CC$tfidf_field As tfidf2;

		-- represent each <docIdA,docIdB,sim> by one record
        filtered = FILTER projected BY docId1 < docId2;

		-- calculate similarity for <docIdA,docIdB,sim>
        term_doc_similarity = FOREACH filtered GENERATE term, docId1, docId2, tfidf1, tfidf2,
                KeywordSimilarity(term, docId1, tfidf1, docId2, tfidf2) AS similarity;

        docs_terms_group = GROUP term_doc_similarity BY (docId1, docId2);
        docs_terms_similarity = FOREACH docs_terms_group GENERATE FLATTEN(group) AS (docId1, docId2),
		DocsCombinedSimilarity(term_doc_similarity.docId1, term_doc_similarity.docId2, term_doc_similarity.similarity) AS similarity;

        $out_relation = FOREACH docs_terms_similarity GENERATE docId1, docId2, similarity;
};

DEFINE calculate_pairwise_similarity_cosine_nominator_no_filtering(in_relation, in_relation2, doc_field, term_field, tfidf_field, CC, joinParallel) RETURNS out_relation {

	-- join on terms
	joined = JOIN $in_relation BY $term_field, $in_relation2 BY $term_field USING 'merge' PARALLEL $joinParallel;
	projected = FOREACH joined GENERATE 
		$in_relation$CC$term_field AS term,
       	$in_relation$CC$doc_field AS docId1, $in_relation2$CC$doc_field As docId2,
       	$in_relation$CC$tfidf_field AS tfidf1, $in_relation2$CC$tfidf_field As tfidf2;

	-- calculate similarity for <docIdA,docIdB,sim>
	term_doc_similarity = FOREACH filtered GENERATE docId1, docId2, tfidf1*tfidf2 as partial;
	docs_terms_group = GROUP term_doc_similarity BY (docId1, docId2);
	$out_relation = FOREACH docs_terms_group GENERATE FLATTEN(group) AS (docId1, docId2), SUM(term_doc_similarity.partial) as similarity;
};








DEFINE calculate_pairwise_similarity_cosine_nominator(in_relation, in_relation2, doc_field, term_field, tfidf_field, CC, joinParallel) RETURNS out_relation {

	-- join on terms
	joined = JOIN $in_relation BY $term_field, $in_relation2 BY $term_field USING 'merge' PARALLEL $joinParallel;
	projected = FOREACH joined GENERATE 
		$in_relation$CC$term_field AS term,
       	$in_relation$CC$doc_field AS docId1, $in_relation2$CC$doc_field As docId2,
       	$in_relation$CC$tfidf_field AS tfidf1, $in_relation2$CC$tfidf_field As tfidf2;

	-- represent each <docIdA,docIdB,sim> by one record
	filtered = FILTER projected BY docId1 < docId2;

	-- calculate similarity for <docIdA,docIdB,sim>
	term_doc_similarity = FOREACH filtered GENERATE docId1, docId2, tfidf1*tfidf2 as partial;
	docs_terms_group = GROUP term_doc_similarity BY (docId1, docId2);
	$out_relation = FOREACH docs_terms_group GENERATE FLATTEN(group) AS (docId1, docId2), SUM(term_doc_similarity.partial) as similarity;
};

DEFINE calculate_pairwise_similarity_cosine_denominator(in_relation, doc_field, term_field, tfidf_field) RETURNS out_relation {
	A = foreach $in_relation generate $doc_field as docId, $term_field as term, $tfidf_field as tfidf;
	B = group A by docId;
	C = foreach B generate group as docId, BagPow(A.$tfidf_field) as pows:bag{(pow:float)};
	$out_relation = foreach C generate docId, SQRT(SUM(pows)) as denominator;
};
