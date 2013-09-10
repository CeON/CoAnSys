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

import org.slf4j.LoggerFactory;


public class TimerSyso implements Runnable  {

	private long start;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Timer.class);

    public TimerSyso() {
    	logger.info( "Writing statistics into standard output." );
    }
    
	private long currentTime() {
		return System.nanoTime();
	}

	@Override
	public void run() {
	}

	public void play() {
		start = currentTime(); //nano time
	}

	public void stop( Object...monits ) {
		addCheckpoint( monits );
		start = -1;
	}

	public void addMonit( Object...monits ) {

		StringBuffer monit = new StringBuffer();
		for ( Object o : monits ) {
			if ( o != null ) {
				monit.append( o.toString() );
				monit.append("\t");
			}
		}

		System.out.println( monit.toString() );
	}
	
	public void addCheckpoint( Object...monits ) {
		long T = currentTime() - start;
		double t = T / 1000000000.0;
		
		Object[] nm = new Object[ monits.length + 1 ];
		boolean isTimeAdded = false;
		for ( int i = 0; i < monits.length; i++ ) {
			if (  monits[i].equals("#time") ) {
				nm[i] = t;
				isTimeAdded = true;
			} else {
				nm[i] = monits[i];
			}
		}
		if( !isTimeAdded ) {
			nm[ monits.length ] = t; //in sec
		}
		addMonit( nm );
	}
}
