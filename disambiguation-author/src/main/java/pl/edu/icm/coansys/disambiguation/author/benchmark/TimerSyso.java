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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;


public class TimerSyso implements Runnable  {

	private long start;
	private long ac;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Timer.class);
	private boolean started = false;
	private String logPath;
    private List<String> monitBuffor = new LinkedList<String> ();

	private void init() {
		started = true;
		logger.info( "Writing statistics into standard output." );
			
		for ( String monit: monitBuffor ) {
			System.out.println( monit );
		}
	}

	public TimerSyso( String logName ) {
		this.logPath = logName;
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
			System.out.println( monit.toString() );
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
