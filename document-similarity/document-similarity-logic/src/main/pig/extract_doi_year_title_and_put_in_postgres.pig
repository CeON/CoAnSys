%default POSTGRESQL_HOST 'localhost'
%default POSTGRESQL_DATABASE 'doc_sim_demo'
%default POSTGRESQL_USER 'doc_sim_demo'
%default POSTGRESQL_PASS 'doc_sim_demo'

REGISTER '/usr/lib/pig/piggybank.jar'
REGISTER 'postgresql-8.4-702.jdbc4.jar'



A = load 'data.in' as (doi, year, title);
--dump A;

/**
STORE A into 'doc_sim_demo' using org.apache.pig.piggybank.storage.DBStorage
	(
		'org.postgresql.Driver',
		'jdbc:postgresql://$POSTGRESQL_HOST/$POSTGRESQL_DATABASE',
		'$POSTGRESQL_USER',
		'$POSTGRESQL_PASS',
		'INSERT INTO documents(doi, year, title) VALUES(?, ?, ?)'
	);
**/