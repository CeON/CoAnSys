package pl.edu.icm.coansys.citations.coansys.input;

import java.io.Serializable;

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
    
    private String model;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates {@link RawReferenceToEntityConverter}. Uses {@link #setModel(String)} internally to create a proper
     * reference parser.
     */
    public RawReferenceToEntityConverter createRawReferenceToEntityConverter() {
        
        CRFBibReferenceParser parser = null;
        
        try {

            if (this.model != null) {
                parser = new CRFBibReferenceParser(this.getClass().getResourceAsStream(this.model));
            }
            else {
                parser = new CRFBibReferenceParser(this.getClass().getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf.ser.gz"));
            }
        
        
        } catch (AnalysisException e) {
            throw new RuntimeException(e);
        }
        
       return new RawReferenceToEntityConverter(parser);
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setModel(String model) {
        this.model = model;
    }
    
    
    
    
    
}
