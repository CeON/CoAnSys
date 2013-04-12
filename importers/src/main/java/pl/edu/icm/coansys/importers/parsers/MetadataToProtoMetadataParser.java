/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.utils.ZipArchive;
import pl.edu.icm.model.bwmeta.YElement;
import pl.edu.icm.model.bwmeta.YExportable;
import pl.edu.icm.model.transformers.TransformationException;

/**
 *
 * @author acz
 */
public interface MetadataToProtoMetadataParser {
    
    public enum MetadataType {

        BWMETA, OAI_DC, DMF
    }
    
    /**
     * Converts stream containing metadata (BWmeta, OAI DC, DMF) to a list of YExportable items
     * 
     * @param stream
     * @param type
     * @return
     * @throws TransformationException
     * @throws IOException 
     */
    public List<YExportable> streamToYExportable(InputStream stream, MetadataType type) throws TransformationException, IOException;
    
    /**
     * Converts YElement item to DocumentMetadata
     * 
     * @param yElement
     * @param zipArchive
     * @param sourcePath
     * @param collection
     * @return 
     */
    public DocumentMetadata yelementToDocumentMetadata(YElement yElement, ZipArchive zipArchive, String sourcePath, String collection);
    
    /**
     * Converts stream containing metadata (BWmeta, OAI DC, DMF) to a list of DocumentMetadata items
     * 
     * @param stream
     * @param type
     * @param collection
     * @return 
     */
    public List<DocumentMetadata> parseStream(InputStream stream, MetadataType type, String collection);
}
