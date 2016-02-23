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

package pl.edu.icm.coansys.deduplication.document.tool;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.internal.Maps;

import pl.edu.icm.coansys.models.DocumentProtos;

public class DuplicateGenerateMapper extends Mapper<Writable, BytesWritable, Text, BytesWritable> {
    
    private static Logger log = LoggerFactory.getLogger(PartExtractingMapper.class);
    
    static final private Map<String, String> oldNewTitles = Maps.newHashMap();
    
    static {
        oldNewTitles.put("Trwały rozwój regionu transgranicznego Górnego Śląska i Północnych Moraw", "Trwały rozwj regionu transgranicznego Górnego Śląska i Półnoznych Moraw"); // 2 letter gap
        oldNewTitles.put("Rachunek kosztów cyklu życia produktu jako narzędzie strategicznego zarządzania kosztami", "Rachunek kosztów cyklu życia produktu jako narzędzie strategicznego..."); // truncated end
        oldNewTitles.put("The ||Problems of Capital Costs in Controlling System", "The Problems of Capital Costs in Controlling System"); // not letters
        oldNewTitles.put("Genetic Algorithm with Pareto Optimization in Rule Extraction from Neural Network", "The Genetic Algorithm with Pareto Optimization in Rule Extraction from Neural Network"); // stop word at the beginning
    }
    
    
    
    @Override
    protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {

        DocumentProtos.DocumentMetadata docMetadata = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes()).getDocumentMetadata();
        
        context.write(new Text(docMetadata.getKey()), new BytesWritable(value.copyBytes()));
        
        String title0 = docMetadata.getBasicMetadata().getTitle(0).getText();
        log.debug("title = " + title0);
        if (oldNewTitles.containsKey(title0)) {
            DocumentProtos.DocumentMetadata newDocMetadata = MockDocumentMetadataFactory.changeTitle(docMetadata, 0, oldNewTitles.get(title0));
            
            log.debug("changed title = " + newDocMetadata.getBasicMetadata().getTitle(0).getText());
            context.write(new Text(docMetadata.getKey()), new BytesWritable(newDocMetadata.toByteArray()));
        }
        
    }

  
}
