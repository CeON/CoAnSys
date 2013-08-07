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
