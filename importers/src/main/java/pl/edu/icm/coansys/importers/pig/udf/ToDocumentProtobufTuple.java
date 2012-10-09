/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import com.google.protobuf.InvalidProtocolBufferException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;
import org.xml.sax.SAXException;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Media;
import pl.edu.icm.coansys.importers.models.DocumentProtos.MediaContainer;


/**
 *
 * @author akawa
 */
public abstract class ToDocumentProtobufTuple extends EvalFunc<Map> {

    private Map addDocumentMetatdataFields(DocumentMetadata metadata, Map map) {
        map.put("key", metadata.getKey());
        map.put("title", metadata.getTitle());
        map.put("keywords", getConcatenated(metadata.getKeywordList(), "_"));
        map.put("abstract", metadata.getAbstrakt());

        List<String> authorKyes = new ArrayList<String>();
        List<String> authorNames = new ArrayList<String>();
        for (Author author : metadata.getAuthorList()) {
            authorKyes.add(author.getKey());
            authorNames.add(author.getName());
        }
        map.put("contributorKeys", getConcatenated(authorKyes, "_"));
        map.put("contributorNames", getConcatenated(authorNames, "_"));
        return map;

    }

    private Map addDocumentMediaFields(MediaContainer media, Map map) throws IOException, SAXException {
        String content = getFirstPDFContent(media.getMediaList());
        if (content != null) {
            map.put("content", content);
        }
        return map;
    }

    public abstract DocumentMetadata getDocumentMetadata(Tuple input) throws ExecException, InvalidProtocolBufferException;

    public abstract MediaContainer getDocumentMedia(Tuple input) throws ExecException, InvalidProtocolBufferException;

    @Override
    public Map exec(Tuple input) throws IOException  {

        Map<String, Object> map = new HashMap<String, Object>();
        DocumentMetadata metadata = getDocumentMetadata(input);
        map = addDocumentMetatdataFields(metadata, map);
        if (input.size() > 1) {
            MediaContainer media = getDocumentMedia(input);
            try {
                map = addDocumentMediaFields(media, map);
            } catch (SAXException ex) {
                Logger.getLogger(ToDocumentProtobufTuple.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    private String getFirstPDFContent(List<Media> medias) throws IOException, SAXException {
        if (medias != null && medias.size() > 0) {
            for (Media medium : medias) {
                if (ProtoConstants.mediaTypePdf.equals(medium.getMediaType())) {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(medium.getContent().toByteArray());
                }
            }
        }
        return null;
    }

    private String getConcatenated(List<String> list, String separator) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        String concatenated = list.get(0);
        for (int i = 1; i < list.size(); ++i) {
            concatenated += separator + list.get(i);
        }
        return concatenated;
    }
}