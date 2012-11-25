%default sample 0.5
%default parallel 32
%default mapredChildJavaOpts -Xmx8000m

%default inputPath 'full/hbase-dump/mproto-m*'
%default outputPath 'document-similarity-output'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'

REGISTER '$commonJarsPath'

IMPORT 'macros.pig';

doc = load_bwndata_metadata_hdfs('$inputPath', $sample);
doc_raw = foreach doc generate rowkey AS docId, document.title as title, document.keywords AS keywords, document.contributors.name as contributor_names;

store doc_raw into '$outputPath';

