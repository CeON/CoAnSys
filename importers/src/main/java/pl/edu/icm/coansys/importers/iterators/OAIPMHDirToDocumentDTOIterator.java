/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.iterators;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.parsers.MetadataToProtoMetadataParser;

/**
 *
 * @author Artur Czeczko a.czeczko@icm.edu.pl
 */
public class OAIPMHDirToDocumentDTOIterator implements Iterable<DocumentDTO> {

    private static final Logger logger = LoggerFactory.getLogger(OAIPMHDirToDocumentDTOIterator.class);
    private String collection = "unset";
    private Iterator<File> filesIterator;
    private NodeList nodeList = null;
    private int nodeListIndex;
    private DocumentDTO nextItem;

    public OAIPMHDirToDocumentDTOIterator(String oaipmhDirPath, String collection) {
        this.collection = collection;
        File thisFile = new File(oaipmhDirPath);
        if (thisFile.isDirectory()) {
            filesIterator = FileUtils.listFiles(thisFile, TrueFileFilter.TRUE, TrueFileFilter.TRUE).iterator();
        }
        moveToNextItem();
    }

    @Override
    public Iterator<DocumentDTO> iterator() {
        return new Iterator<DocumentDTO>() {

            @Override
            public boolean hasNext() {
                return nextItem != null;
            }

            @Override
            public DocumentDTO next() {
                if (nextItem == null) {
                    throw new NoSuchElementException();
                }
                DocumentDTO actualItem = nextItem;
                moveToNextItem();
                return actualItem;
            }

            @Override
            public void remove() {
                moveToNextItem();
            }
        };
    }

    private void moveToNextItem() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression recordsExpr;

        try {
            recordsExpr = xpath.compile("//record/metadata/dc");
        } catch (XPathExpressionException ex) {
            logger.error("Error: " + ex);
            throw new RuntimeException(ex);
        }


        nextItem = null;
        while (nextItem == null) {
            while (nodeList == null || nodeListIndex >= nodeList.getLength()) {
                try {
                    if (!filesIterator.hasNext()) {
                        return;
                    }
                    File nextFile = filesIterator.next();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(nextFile);

                    nodeList = (NodeList) recordsExpr.evaluate(doc, XPathConstants.NODESET);
                    if (nodeList.getLength() == 0) {
                        logger.warn("There's no records in " + nextFile.getPath());
                        continue;
                    }
                    nodeListIndex = 0;
                } catch (Exception ex) {
                    logger.error("Error: " + ex);
                }
            }

            Element item = (Element) nodeList.item(nodeListIndex++);

            String str;
            try {
                item.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
                str = nodeToXmlString(item);
                List<DocumentMetadata> docs = MetadataToProtoMetadataParser.parseStream(new ByteArrayInputStream(str.getBytes("UTF-8")),
                        MetadataToProtoMetadataParser.MetadataType.OAI_DC, collection);
                if (docs.size() == 1) {
                    DocumentMetadata dm = docs.get(0);
                    nextItem = new DocumentDTO();
                    nextItem.setKey(dm.getKey());
                    nextItem.setDocumentMetadata(dm);
                } else {
                    logger.error("There was exactly one record in input string; number of output items: " + docs.size()); 
                }
            } catch (Exception ex) {
                logger.error("Error: " + ex);
            }
        }
    }

    private static String nodeToXmlString(Element item) throws TransformerConfigurationException, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource domSource = new DOMSource(item);
        StreamResult streamResult = new StreamResult(buffer);
        transformer.transform(domSource, streamResult);
        return buffer.toString();
    }
}