package pl.edu.icm.coansys.disambiguation.author.benchmark;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.slf4j.LoggerFactory;


public class Timer implements Runnable  {

	//TODO: rysowanie wykresow, java
	
	private long start;
	private long ac;
	private boolean off = false;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Timer.class);
    private PrintWriter statistics = null;
    
	public void turnOn() {
		off = false;
	}

	public void turnOff() {
		off = true;
	}
	
	public Timer( String logPath ) {
		
		File f = new File( logPath );
		int suf = 0;
		String newLogPath = logPath;
		
		while ( f.exists() ) {
			//Date date = new Date();
			//logPath = logPath + " " + date.toString();
			newLogPath = logPath + "_" + suf;
			f = new File( newLogPath );
			suf++;
		}
		
		try {
			statistics = new PrintWriter( newLogPath );
		} catch ( FileNotFoundException e ) {
			logger.warn( "Unable to write time statistics in file: " + newLogPath + e.getMessage() );			
		}
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
		if ( off ) return;
		
		StringBuffer monit = new StringBuffer();
		for ( Object o : monits ) {
			monit.append( o.toString() );
			monit.append("\t");
		}
		
		statistics.println( monit.toString() );
		statistics.flush();
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
