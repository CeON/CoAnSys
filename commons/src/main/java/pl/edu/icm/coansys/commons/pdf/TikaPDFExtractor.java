/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.commons.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author akawa
 */
public class TikaPDFExtractor {

    public static String getContent(InputStream inputStream) throws IOException, SAXException, TikaException {



        BodyContentHandler textHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        PDFParser parser = new PDFParser();
     

            parser.parse(inputStream, textHandler, metadata);
            String content = textHandler.toString();

  
return content;



    }
}
