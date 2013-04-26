/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.extractors;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.zookeeper.KeeperException.UnimplementedException;

import pl.edu.icm.coansys.classification.documents.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.auxil.Pair;
import pl.edu.icm.coansys.importers.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.TextWithLanguage;

/**
 *
 * @author pdendek
 */
@SuppressWarnings("rawtypes")
public class EXTRACT_MAP_WHEN_CATEG_LIM extends EvalFunc<Map> {

	enum Action{
		REMOVE,
		TRANSLATE
	}
	
	private String language = null;
	private Action action = null;
	
	public EXTRACT_MAP_WHEN_CATEG_LIM(String language, String action) throws Exception{
		this.language = language;
		
		if(action.equalsIgnoreCase("remove")){
			this.action = Action.REMOVE;
		}else if (action.equalsIgnoreCase("translate")){
			this.action = Action.TRANSLATE;
		}else{
			throw new Exception("You have to choose one of two actions \"remove\" or \"translate\". " +
					"Your proposition is \""+action+"\". " +
					"Please modify your proposition.");
		}
	}

	public EXTRACT_MAP_WHEN_CATEG_LIM(String language){
		this.language = language;
		System.out.println("Default action taken against non-alphanumeric signs in title or abstract is a symbol removal.");
		this.action = Action.REMOVE;
	}
	
	public EXTRACT_MAP_WHEN_CATEG_LIM(){
		this.action = Action.REMOVE;
	}
	
    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.MAP);
        } catch (FrontendException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Map exec(Tuple input) throws IOException {
        try {
            DataByteArray protoMetadata = (DataByteArray) input.get(0);
            int lim = (Integer) input.get(1);
            DocumentMetadata metadata = DocumentMetadata.parseFrom(protoMetadata.get());

            if(language!=null){
            	return generateConcreteLanguageMap(metadata,lim);
            }else{
            	return generateAllLanguageMap(metadata,lim);
            }
        } catch (Exception e) {
            // Throwing an exception will cause the task to fail.
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }

    protected Map generateConcreteLanguageMap(DocumentMetadata dm, int lim){
    	String docTitle;
        String docAbstract;
        
        if((docTitle = extractLangTitle(dm))==null) return null;
        docAbstract = extractLangAbstract(dm);
        Pair<String, DataBag> kwCc = extractLangKeywords(dm);
        
        if(action==Action.TRANSLATE){
        	docTitle = translateNonAlphaNumeric(docTitle);
        	docAbstract = translateNonAlphaNumeric(docAbstract);
        }else{
        	docTitle = removeAllNonAlphaNumberic(docTitle);
        	docAbstract = removeAllNonAlphaNumberic(docAbstract);
        }
        
        if (kwCc.getY().size() > lim) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key", dm.getKey());
            map.put("title", docTitle);
            map.put("keywords", kwCc.getX());
            map.put("abstract", docAbstract);
            map.put("categories", kwCc.getY());
            return map;
        }
        return null;
    }
    
    private String removeAllNonAlphaNumberic(String str){
    	str = str.replaceAll(",", "");
    	str = str.replaceAll("#", "");
    	return str;
    }
    
    private String translateNonAlphaNumeric(String str){
    	str = str.replaceAll(",", " COMMA ");
    	str = str.replaceAll("#", " HASH ");
    	return str;
    }
    
	private Pair<String, DataBag> extractLangKeywords(DocumentMetadata dm) {
		List<String> kws = new ArrayList<String>();
		Set<String> ctgs = new HashSet<String>();
		for(TextWithLanguage twl : dm.getKeywordList()){
			if(language.equalsIgnoreCase(twl.getLanguage())){
				String str = twl.getText();
				
				if(action==Action.TRANSLATE) str = translateNonAlphaNumeric(str);
		        else str = removeAllNonAlphaNumberic(str);
		        
				if(!isClassifCode(str)) kws.add(str);
				else ctgs.add(str);
			}
		}
		
		for(ClassifCode cc : dm.getBasicMetadata().getClassifCodeList()){
			for(String s : cc.getValueList())
				ctgs.add(s);
		}
		
		DataBag db = new DefaultDataBag();
		for(String s : ctgs){
			db.add(TupleFactory.getInstance().newTuple(s));
		}
		
		return new Pair<String,DataBag>(Joiner.on(" ").join(kws),db);
	}

	private boolean isClassifCode(String str) {
		if(isMSc(str))
			return true;
		else return false;
	}

	private boolean isMSc(String str) {
		return str.toUpperCase().matches("[0-9][0-9][A-Z][0-9][0-9]");
	}

	private String extractLangAbstract(DocumentMetadata dm) {
		String docAbstract;
		List<String> abstractsList = new ArrayList<String>();
		//getDocumentAbstractList()
        for (TextWithLanguage documentAbstract : dm.getDocumentAbstractList()) {
        	if(language.equalsIgnoreCase(documentAbstract.getLanguage()));
            	abstractsList.add(documentAbstract.getText());
        }
        docAbstract = Joiner.on(" ").join(abstractsList);
		return docAbstract;
	}

	private String extractLangTitle(DocumentMetadata dm) {
		List<String> titleList = new ArrayList<String>();
		//getTitleList()
        for (TextWithLanguage title : dm.getBasicMetadata().getTitleList()) {
        	if(language.equalsIgnoreCase(title.getLanguage())); 
            	titleList.add(title.getText());
        }
        
        String docTitle;
        switch(titleList.size()){
        case 0:
        	System.out.println("No title IN GIVEN LANG ("+language+") out of "+dm.getBasicMetadata().getTitleCount()
        			+" titles. Ignoring record!");
        	return null;
        case 1:
        	docTitle = titleList.get(0);
        	break;
        default:
        	System.out.println("Number of titles IN GIVEN LANGUAGE ("+language+") is more then one. " +
        			"Titles will be concatenated");
        	docTitle = Joiner.on(" ").join(titleList);
        	break;
        }
        if(docTitle.trim().isEmpty()) return null;
		return docTitle;
	}
    
    
    
    protected Map generateAllLanguageMap(DocumentMetadata dm, int lim) throws UnimplementedException{
    	throw new UnimplementedException();
    }    
    
    private DataBag getCategories(List<ClassifCode> classifCodeList) {
        DataBag db = new DefaultDataBag();
        for (ClassifCode code : classifCodeList) {
            for (String co_str : code.getValueList()) {
//       		System.out.print(" "+co_str);
                db.add(TupleFactory.getInstance().newTuple(co_str));
            }
        }
        return db;
    }

    private String getConcatenated(List<TextWithLanguage> list) {
        if (list == null || list.isEmpty()) {return null;}
        return Joiner.on(" ").join(list);
    }
}
