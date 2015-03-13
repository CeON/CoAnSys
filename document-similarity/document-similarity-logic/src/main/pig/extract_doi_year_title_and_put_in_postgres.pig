/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

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