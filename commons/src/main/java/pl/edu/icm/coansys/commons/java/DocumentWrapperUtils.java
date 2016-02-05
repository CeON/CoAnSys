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

package pl.edu.icm.coansys.commons.java;

import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;

import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/** 
 * Contains various utility methods related to the {@link DocumentWrapper} class
 * @author Łukasz Dumiszewski
 * 
 * */
public abstract class DocumentWrapperUtils {

    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(DocumentWrapperUtils.class);
    
    
    private DocumentWrapperUtils() {
        throw new IllegalStateException("a helper class, not to instantiate");
    }
    
    
    /** 
     * documentWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText() 
     * 
     * */
    public static String getMainTitle(DocumentProtos.DocumentMetadata documentMetadata) {
        BasicMetadata basicMetadata = documentMetadata.getBasicMetadata();
        if (basicMetadata.getTitleCount() > 0) {
            return basicMetadata.getTitle(0).getText();
        } else {
            return "";
        }
    }
    
    
    
    /**
     * Returns the author of the publication that is on the given authorPosition. 
     * Returns null if there is no author on the authorPosition.  
     */
    public static Author getAuthor(DocumentWrapper documentWrapper, int authorPosition) {
        List<Author> authors = getAuthors(documentWrapper);
        for (Author author : authors) {
            if (author.getPositionNumber()==authorPosition) {
                return author;
            }
        }
        return null;
    }
    
    /**
     * wrapper for documentWrapper.getDocumentMetadata().getBasicMetadata().getAuthorList(); Never returns null
     */
    public static List<Author> getAuthors(DocumentWrapper documentWrapper) {
        List<Author> authors = documentWrapper.getDocumentMetadata().getBasicMetadata().getAuthorList();
        if (authors==null) {
            authors = Lists.newArrayList();
        }
        return authors;
    }
    
    
    
    /** 
     * documentWrapper.getDocumentMetadata().getBasicMetadata().getYear()
     */
    public static String getPublicationYear(DocumentWrapper documentWrapper) {
        return documentWrapper.getDocumentMetadata().getBasicMetadata().getYear();
    }
    
    
    
    /** 
     * Returns list of {@link DocumentWrapper}s parsed from values
     */
    public static List<DocumentWrapper> extractDocumentWrappers(Text key, Iterable<BytesWritable> values) throws InvalidProtocolBufferException {
        List<DocumentWrapper> documents = Lists.newArrayList();
        
        for (BytesWritable value : values) {
            DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
            documents.add(docWrapper);
        }
        
        return documents;
    }
    
    /**
     * 
     */
    public static List<DocumentProtos.DocumentMetadata> extractDocumentMetadata(Text key, Iterable<BytesWritable> values) throws InvalidProtocolBufferException {
        List<DocumentProtos.DocumentMetadata> documents = Lists.newArrayList();
        
        for (BytesWritable value : values) {
            DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
            documents.add(docWrapper.getDocumentMetadata());
        }
        
        return documents;
    }

    /**
     * Returns the clone of the passed DocumentWrapper with filled {@link DocumentWrapper#getDocumentMetadata()} and {@link DocumentWrapper#getRowId()} only 
     */
    public static DocumentWrapper cloneDocumentMetadata(DocumentWrapper docWrapper) {
        return DocumentWrapper.newBuilder().setDocumentMetadata(docWrapper.getDocumentMetadata()).setRowId(docWrapper.getRowId()).build();
    }


        }
