package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.Vector;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;


public class Udf2  extends EvalFunc<Integer>{

	private Vector <Integer> colToSum = null;
	private AuxilI auxil;
	
	private void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		auxil = (Auxil) Class.forName( "pl.edu.icm.coansys.disambiguation.author.pig.Auxil" ).newInstance();
	}
	
	public Udf2() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		init();
	}
	
	public Udf2( String param ) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		colToSum = new Vector <Integer>();
		
		String[] strTab = param.split(",");
		
		for ( String col : strTab )
			colToSum.add( Integer.parseInt( col )  ); 
	
		init();
	}
	
	public Integer exec( Tuple input ) throws ExecException {
		
		int sum = 0;
		
		if ( colToSum == null )
			for ( int i = 0; i < input.size(); i++ )
				sum = auxil.execute( sum, (Integer) input.get(i) );
		else
			for ( int i = 0; i < colToSum.size(); i++ )
				sum = auxil.execute( sum, (Integer) input.get( colToSum.get(i) ) );
				
		return sum;
	}
}
