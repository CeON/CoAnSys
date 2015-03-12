/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.source;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author kura
 */
public class MapDocProtosToSourceBasIds extends Mapper<Writable, BytesWritable, Text, BytesWritable> {

       
    @Override
    protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {
       DocumentProtos.DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
       String issn=null;
       String isbn=null;
       if (docWrapper.hasDocumentMetadata() && docWrapper.getDocumentMetadata().hasBasicMetadata()) {
           DocumentProtos.BasicMetadataOrBuilder bm=docWrapper.getDocumentMetadata().getBasicMetadataOrBuilder();
           if (bm.hasIssn()) {
               issn=bm.getIssn();
           }
           if (bm.hasIsbn()) {
               isbn=bm.getIsbn();
           }
           if (StringUtils.isBlank(isbn)) {
               isbn=null;
           }
           if (StringUtils.isBlank(issn)) {
               issn=null;
           }
       }
       
       if (issn!=null) {
           String id=issn.replaceAll("\\W", "").toUpperCase();
           // issn ni a ma znaków spoza 7 bitow
           UUID uuid=UUID.nameUUIDFromBytes(("issn"+id).getBytes(Charset.forName("US-ASCII")));
        
           context.write(new Text(uuid.toString()), value);
       }
       if (isbn!=null) {
           String id=isbn.replaceAll("\\W", "").toUpperCase();
           // issn ni a ma znaków spoza 7 bitow
           UUID uuid=UUID.nameUUIDFromBytes(("isbn"+id).getBytes(Charset.forName("US-ASCII")));
           context.write(new Text(uuid.toString()), value);
       }
    
    }
    
    
}
