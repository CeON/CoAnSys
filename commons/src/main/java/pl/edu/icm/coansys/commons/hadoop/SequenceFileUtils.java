/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.commons.hadoop;

import static java.lang.System.out;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

import com.google.common.collect.Lists;
import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;

/**
 * Utility methods related to bw2proto sequence files
 * @author Łukasz Dumiszewski
 *
 */

public final class SequenceFileUtils {

    private SequenceFileUtils() {}
    
    public static List<DocumentWrapper> readDocWrappers(String inputFileUri) {
        
        List<DocumentWrapper> docWrappers = Lists.newArrayList();
        
        SequenceFile.Reader reader = null;
        try {
            Configuration conf = new Configuration();
            reader = getSequenceFileReader(inputFileUri, conf);
            Writable key = (Writable)ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Writable value = (Writable)ReflectionUtils.newInstance(reader.getValueClass(), conf);
            while (reader.next(key, value)) {
                DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(((BytesWritable)value).copyBytes());
                docWrappers.add(docWrapper);
            }
            
        }
        catch (IOException e) {}
        finally {
            IOUtils.closeStream(reader);
        }
        return docWrappers;
    }

    public static List<String> readTexts(String inputFileUri) {
        List<String> texts = Lists.newArrayList();

        SequenceFile.Reader reader = null;
        try {
            Configuration conf = new Configuration();
            reader = getSequenceFileReader(inputFileUri, conf);
            Writable key = (Writable)ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Writable value = (Writable)ReflectionUtils.newInstance(reader.getValueClass(), conf);
            while (reader.next(key, value)) {
                texts.add(((Text)value).toString());
            }
        } catch (IOException e) {
        } finally {
            IOUtils.closeStream(reader);
        }

        return texts;
    }

    
    public static void formatAndPrintToConsole(String inputFileUri) throws IOException {
        SequenceFile.Reader reader = null;
        try {
            Configuration conf = new Configuration();
            reader = getSequenceFileReader(inputFileUri, conf);
            SequenceFile.Reader.bufferSize(250000);
            Writable key = (Writable)ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Writable value = (Writable)ReflectionUtils.newInstance(reader.getValueClass(), conf);
            while (reader.next(key, value)) {
                DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(((BytesWritable)value).copyBytes());
                out.println(format(key, docWrapper));
                
            }
            
        }
        finally {
            IOUtils.closeStream(reader);
        }
    }
    
    
    
    //******************** PRIVATE ********************
    
    private static String format(Writable key, DocumentWrapper documentWrapper) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------------------------\n");
        sb.append("key    : ").append(key).append("\n");
        sb.append("rowid  : ").append(documentWrapper.getRowId()).append("\n");
        sb.append("title0 : ").append(DocumentWrapperUtils.getMainTitle(documentWrapper.getDocumentMetadata())).append("\n");
        sb.append("year   : ").append(DocumentWrapperUtils.getPublicationYear(documentWrapper)).append("\n");
        for (Author author : documentWrapper.getDocumentMetadata().getBasicMetadata().getAuthorList()) {
            sb.append(author.getPositionNumber()).append(". ").append(author.getName()).append(" ").append(author.getSurname()).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
    
   
    @SuppressWarnings("deprecation")
    private static SequenceFile.Reader getSequenceFileReader(String inputFileUri, Configuration conf) throws IOException {
        SequenceFile.Reader reader;
        URI uri = URI.create(inputFileUri);
        FileSystem fs = FileSystem.get(uri, conf);
        Path path = new Path(uri);
      
        reader = new SequenceFile.Reader(fs, path, conf);
        return reader;
    }
        
}
