package pl.edu.icm.coansys.citations.coansys.input;

import java.io.Serializable;

import com.google.common.base.Preconditions;

import pl.edu.icm.cermine.bibref.CRFBibReferenceParser;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.coansys.citations.converters.RawReferenceToEntityConverter;

/**
 * Factory of {@link RawReferenceToEntityConverter} objects.
 * 
* @author Łukasz Dumiszewski
*/

public class RawReferenceToEntityConverterFactory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates {@link RawReferenceToEntityConverter}. Uses {@link #setModel(String)} internally to create a proper
     * reference parser.
     */
    public RawReferenceToEntityConverter createRawReferenceToEntityConverter(String model) {
        
        Preconditions.checkNotNull(model);
        
        
        CRFBibReferenceParser parser = null;
        
        try {
            
            parser = new CRFBibReferenceParser(this.getClass().getResourceAsStream(model));
        
        } catch (AnalysisException e) {
            throw new RuntimeException(e);
        }
        
       return new RawReferenceToEntityConverter(parser);
    }
    
    
    
}
