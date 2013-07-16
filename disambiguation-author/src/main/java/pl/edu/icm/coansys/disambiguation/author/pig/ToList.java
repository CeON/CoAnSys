package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

public class ToList {
	public static List execute(DataBag db){
		Iterator<Tuple> it = db.iterator();
		
		List ret = new LinkedList();
		while(it.hasNext()){
			try {
				ret.add(it.next().get(0));
			} catch (ExecException e) {
				// TODO Auto-generated catch block
				// add logging output of StackTraceExtractor
				e.printStackTrace();
			}
		}
		
		return ret;
		
	}
}
