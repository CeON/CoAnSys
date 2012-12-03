package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.auxiliar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WFList {
	public String name = null;
	public List<String> vals = null;
	
	public WFList(String[] line){
		name = line[0];
		
		int size = line.length-1;
		String[] part = new String[size];
		System.arraycopy(line, 1, part, 0, size);
		vals = Arrays.asList(part);
	}
	
	public static class WFElement{
		public String name = null;
		public String val = null;
		
		public WFElement(String name, String val){
			this.name=name;
			this.val=val;
		}
	}
	
	public static ArrayList<ArrayList<WFElement>> createCrossProduct(List<WFList> wflists){
		
		ArrayList<ArrayList<WFElement>> retVal = new ArrayList<ArrayList<WFElement>>();
		
		return createCrossProduct(wflists, 0, retVal);
	}
	
	private static ArrayList<ArrayList<WFElement>> createCrossProduct(List<WFList> wflists, 
			int depth, ArrayList<ArrayList<WFElement>> inVal){ 
		
		ArrayList<ArrayList<WFElement>> retVal = new ArrayList<ArrayList<WFElement>>();
		if(wflists.size()==0) return retVal;
		
		if(depth==0){
			String this_name = wflists.get(depth).name;
			for(String this_val : wflists.get(depth).vals){
				
				ArrayList<WFElement> wfelements = new ArrayList<WFElement>();
				wfelements.add(new WFElement(this_name,this_val));
				retVal.add(wfelements);
			}
		}else{
			for(ArrayList<WFElement> wfelements : inVal){
				String this_name = wflists.get(depth).name;
				for(String this_val : wflists.get(depth).vals){
					
					@SuppressWarnings("unchecked")
					ArrayList<WFElement> wfclones = (ArrayList<WFElement>) wfelements.clone();
					wfclones.add(new WFElement(this_name,this_val));
					retVal.add(wfclones);
				}
			}
		}
		
		if(depth+1>=wflists.size()) return retVal;
		return createCrossProduct(wflists, depth+1, retVal);
	}
}
