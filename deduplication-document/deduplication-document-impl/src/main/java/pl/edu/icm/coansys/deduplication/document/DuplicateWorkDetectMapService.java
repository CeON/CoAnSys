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
package pl.edu.icm.coansys.deduplication.document;

import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.commons.spring.DiMapService;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 *
 * @author Łukasz Dumiszewski
 *
 */
@Service("duplicateWorkDetectMapService")
public class DuplicateWorkDetectMapService implements DiMapService<Writable, BytesWritable, Text, BytesWritable> {

    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectMapService.class);

    @Override
    public void map(Writable key, BytesWritable value, Mapper<Writable, BytesWritable, Text, BytesWritable>.Context context)
            throws IOException, InterruptedException {

        DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());

        String docKey = WorkKeyGenerator.generateKey(docWrapper.getDocumentMetadata(), 0);

        if (!docKey.isEmpty()) {
            DocumentWrapper thinDocWrapper = DocumentWrapperUtils.cloneDocumentMetadata(docWrapper);
            context.write(new Text(docKey), new BytesWritable(thinDocWrapper.toByteArray()));
        }
    }
    //******************** PRIVATE ********************
}
