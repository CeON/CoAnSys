%default inputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/tfidf-new-lzo3/weighted'
%default outputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/tfidf-top'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'

%default parallel 32
%default termLimit 20
%default tmpCompressionCodec gz

IMPORT 'macros.pig';

REGISTER '$commonJarsPath';

set default_parallel $parallel
set pig.tmpfilecompression true
set pig.tmpfilecompression.codec $tmpCompressionCodec

t = LOAD '$inputPath' AS (docId: bytearray, term: chararray, tfidf: double);

top = top_n_per_group(t, docId, tfidf, desc, $termLimit, $parallel);
top_named = FOREACH top GENERATE $0 AS docId, $1 AS term, $2 AS tfidf;
top_sorted = ORDER top_named BY (term, docId);

STORE top_sorted INTO '$outputPath';
