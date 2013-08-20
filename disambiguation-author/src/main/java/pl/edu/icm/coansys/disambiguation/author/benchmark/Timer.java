/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.author.benchmark;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;


public class Timer implements Runnable  {
	
	private long start;
	private long ac;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Timer.class);
    private PrintWriter statistics = null;
    private String logPath;
	private boolean started = false;
    private List<String> monitBuffor = new LinkedList<String> ();
    	
	private void init() {
		
		File f = new File( logPath );
		int suf = 0;
		String newLogPath = logPath;
		
		while ( f.exists() ) {
			newLogPath = logPath + "_" + suf;
			f = new File( newLogPath );
			suf++;
		}
		
		try {
			statistics = new PrintWriter( newLogPath );
		} catch ( FileNotFoundException e ) {
			logger.warn( "Unable to write time statistics in file: " + newLogPath + e.getMessage() );			
		}
		
		started = true;
		
		for ( String monit: monitBuffor ) {
			statistics.println( monit );
		}
		statistics.flush();
	}
	
	public Timer( String logPath ) {
		this.logPath = logPath;
	}
	
	private long currentTime() {
		return System.nanoTime();
	}
	
	@Override
	public void run() {
		start = currentTime(); //nano time		
		ac = 0;
	}
	
	public void play() {
		start = currentTime(); //nano time	
		
		if ( !started ) {
			init();
		}
	}	

	public void stop( Object...monits ) {
		addCheckpoint( monits );
		start = -1;
		ac = 0;
	}		
	
	public void pause( Object...monits ) {
		addCheckpoint( monits );
		ac += currentTime() - start;
		start = -1;
	}
	
	public void pause() {
		ac += currentTime() - start;
		start = -1;
	}
	
	public void addMonit( Object...monits ) {

		StringBuffer monit = new StringBuffer();
		for ( Object o : monits ) {
			monit.append( o.toString() );
			monit.append("\t");
		}
		
		if ( !started ) {
			monitBuffor.add( monit.toString() );
		}
		else {
			statistics.println( monit.toString() );
			statistics.flush();
		}
	}	
	public void addCheckpoint( Object...monits ) {
		long t = currentTime() - start + ac;
		Object[] nm = new Object[ monits.length + 1 ];
		for ( int i = 0; i < monits.length; i++ ) {
			nm[i] = monits[i];
		}
		nm[ monits.length ] = t;
		addMonit( nm );
	}
}
