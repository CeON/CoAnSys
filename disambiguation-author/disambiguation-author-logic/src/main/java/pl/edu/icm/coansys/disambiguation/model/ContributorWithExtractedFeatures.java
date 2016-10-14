/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.disambiguation.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 *
 * @author kura
 */
public class ContributorWithExtractedFeatures implements Serializable{
    String docKey;
    String contributorId;
    Map<String,Collection<Integer>> metadata;
    int surnameInt;
    String surnameString;
    boolean surnameNotNull;

    public ContributorWithExtractedFeatures(String docKey, String contributorId, Map<String, Collection<Integer>> metadata, int surnameInt, String surnameString, boolean surnameNotNull) {
        this.docKey = docKey;
        this.contributorId = contributorId;
        this.metadata = metadata;
        this.surnameInt = surnameInt;
        this.surnameString = surnameString;
        this.surnameNotNull = surnameNotNull;
    }

    public String getDocKey() {
        return docKey;
    }

    public String getContributorId() {
        return contributorId;
    }

    public Map<String, Collection<Integer>> getMetadata() {
        return metadata;
    }

    public int getSurnameInt() {
        return surnameInt;
    }

    public String getSurnameString() {
        return surnameString;
    }

    public boolean isSurnameNotNull() {
        return surnameNotNull;
    }
    
    public Tuple asTuple(){
        Object[] to = new Object[] { docKey, contributorId, surnameInt,
						hashMapToDataBag(), surnameInt, isSurnameNotNull() };
		return  TupleFactory.getInstance()
						.newTuple(Arrays.asList(to));
    }
    
    private HashMap<Object,DataBag> hashMapToDataBag(){
        TupleFactory tf=TupleFactory.getInstance();
        HashMap<Object,DataBag> ret=new HashMap<>();
        for (Map.Entry<String,Collection<Integer>> entry:metadata.entrySet()){
           DataBag db = new DefaultDataBag();
           for (Integer i:entry.getValue()) {
               db.add(tf.newTuple((Object) i));
           }
           ret.put(entry.getKey(), db);
        }
        return ret;
        
    }
    
    
}
