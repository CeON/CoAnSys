/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.constants;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class HBaseConstants {

	//separator in a row id between a type prefix and an id postfix 
	public static final String INTRASEPARATOR = "_";
	//type prefix
	public static final String T_CONTRIBUTIONS_CLUSTER = "authCluster";
	public static final String T_CONTRIBUTOR = "contrib";
	//column family
	public static final String F_RESULT = "r";
	//column qualifier
	public static final String Q_CONTRIBS = "cbs";
	public static final String Q_CLUSTER_ID = "cls";
}
