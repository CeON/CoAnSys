/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;
import pl.edu.icm.coansys.commons.pdf.TikaPDFExtractor;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Media;
import pl.edu.icm.coansys.importers.models.DocumentProtos.MediaContainer;

/**
 *
 * @author akawa
 */
public class DocumentProtobufBytesToTuple extends EvalFunc<Map> {

    private <T> DataBag getBag(List<T> items) throws ExecException {
        DataBag bag = null;
        if (items != null) {
            bag = BagFactory.getInstance().newDefaultBag();
            for (T item : items) {
                Tuple tuple = TupleFactory.getInstance().newTuple(2);
                tuple.set(0, item);
                bag.add(tuple);
            }
        }
        return bag;
    }

    @Override
    public Map exec(Tuple input) throws IOException {
        try {
            DataByteArray protoMetadata = (DataByteArray) input.get(0);
            DocumentMetadata metadata = DocumentMetadata.parseFrom(protoMetadata.get());

            Map<String, Object> map = new HashMap<String, Object>();
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

            if (input.size() > 1) {
                DataByteArray protoMedia = (DataByteArray) input.get(1);
                if (protoMedia != null) {
                    MediaContainer media = MediaContainer.parseFrom(protoMedia.get());
                    String content = getFirstPDFContent(media.getMediaList());
                    if (content != null) {
                        map.put("content", content);
                    }
                }
            }

            return map;
        } catch (Exception e) {
            // Throwing an exception will cause the task to fail.
            throw new RuntimeException("Error while parsing DocumentMetadata", e);
        }
    }

    private String getFirstPDFContent(List<Media> medias) throws IOException, SAXException, TikaException {
        if (medias != null && medias.size() > 0) {
            for (Media medium : medias) {
                if (ProtoConstants.mediaTypePdf.equals(medium.getMediaType())) {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(medium.getContent().toByteArray());
                    try {
                        String content = TikaPDFExtractor.getContent(inputStream);
                        return content;
                    } finally {
                        inputStream.close();
                    }
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