package pl.edu.icm.coansys.disambiguation.work.tool;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

import com.beust.jcommander.internal.Maps;

public class DuplicateGenerateMapper extends Mapper<Writable, BytesWritable, Text, BytesWritable> {
    
    private static Logger log = LoggerFactory.getLogger(PartExtractingMapper.class);
    
    static private Map<String, String> oldNewTitles = Maps.newHashMap();
    
    static {
        oldNewTitles.put("Trwały rozwój regionu transgranicznego Górnego Śląska i Północnych Moraw", "Trwały rozwj regionu transgranicznego Górnego Śląska i Półnoznych Moraw"); // 2 letter gap
        oldNewTitles.put("Rachunek kosztów cyklu życia produktu jako narzędzie strategicznego zarządzania kosztami", "Rachunek kosztów cyklu życia produktu jako narzędzie strategicznego..."); // truncated end
        oldNewTitles.put("The ||Problems of Capital Costs in Controlling System", "The Problems of Capital Costs in Controlling System"); // not letters
        oldNewTitles.put("Genetic Algorithm with Pareto Optimization in Rule Extraction from Neural Network", "The Genetic Algorithm with Pareto Optimization in Rule Extraction from Neural Network"); // stop word at the beginning
    }
    
    
    
    @Override
    protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {

        DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
        
        context.write(new Text(docWrapper.getRowId()), new BytesWritable(value.copyBytes()));
        
        String title0 = docWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText();
        log.debug("title = " + title0);
        if (oldNewTitles.containsKey(title0)) {
            DocumentWrapper newDocWrapper = MockDocumentWrapperFactory.changeTitle(docWrapper, 0, oldNewTitles.get(title0));
            
            log.debug("changed title = " + newDocWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText());
            context.write(new Text(docWrapper.getRowId()), new BytesWritable(newDocWrapper.toByteArray()));
        }
        
    }

  
}
