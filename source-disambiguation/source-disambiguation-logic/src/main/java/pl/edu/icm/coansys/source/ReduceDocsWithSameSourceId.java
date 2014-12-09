/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.ParentModelProtos;

/**
 *
 * @author kura
 */
public class ReduceDocsWithSameSourceId extends Reducer<Writable, BytesWritable, Text, BytesWritable> {

    @Override
    protected void reduce(Writable key, Iterable<BytesWritable> values, Context context) throws IOException, InterruptedException {
        ArrayList<String> docIds = new ArrayList<String>();
        HashSet<String> issns = new HashSet<String>();
        HashSet<String> isbns = new HashSet<String>();
        HashSet<String> titles = new HashSet<String>();
        for (BytesWritable value : values) {

            DocumentProtos.DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
            docIds.add(docWrapper.getRowId());
            String issn = null;
            String isbn = null;
            if (docWrapper.hasDocumentMetadata() && docWrapper.getDocumentMetadata().hasBasicMetadata()) {
                DocumentProtos.BasicMetadataOrBuilder bm = docWrapper.getDocumentMetadata().getBasicMetadataOrBuilder();
                if (bm.hasIssn()) {
                    issn = bm.getIssn();
                }
                if (bm.hasIsbn()) {
                    isbn = bm.getIsbn();
                }
                if (StringUtils.isNotBlank(isbn)) {
                    isbn = isbn.trim().toUpperCase();
                    isbns.add(isbn);
                }
                if (StringUtils.isNotBlank(issn)) {
                    issn = issn.trim().toUpperCase();
                    issns.add(issn);
                }
                if (bm.hasJournal()) {
                    String title=bm.getJournal().trim();
                    if (StringUtils.isNotBlank(title)) {
                        titles.add(title);
                    }
                }
            }
           
        }
        String issn=null;
        String isbn=null;
        ArrayList<String> titleA=new ArrayList<String>(titles);
        Collections.sort(titleA, new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {
                //najdłuższe naprzód
                return o2.length()-o1.length();
            }
            
        });
        
        if (issns.size()>0) {
            issn=issns.iterator().next();
        }
        if (isbns.size()>0) {
            isbn=issns.iterator().next();
        }
        String id="http://comac.ceon.pl/source-";
        if (issn!=null) {
            id+=("issn-"+issn);
            if (isbn!=null) {
                id+="-";
            }
        }
        if (isbn!=null) {
                id+=("isbn-"+isbn);
        }
        for (String docId:docIds){
            ParentModelProtos.ParentDisambiguationOut.Builder parent=ParentModelProtos.ParentDisambiguationOut.newBuilder();
            parent.setDocId(docId);
            parent.setParentId(id);
            parent.addAllParentName(titles);
            context.write(new Text(docId), new BytesWritable(parent.build().toByteArray()));
        }
        
        
    }

}
