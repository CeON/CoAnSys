package pl.edu.icm.coansys.importers.pig.udf;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.MediaContainer;

/**
 *
 * @author akawa
 */
public class DocumentProtobufBytesToTuple extends DocumentProtobufToTupleBase {

    @Override
    public DocumentMetadata getDocumentMetadata(Tuple input) throws ExecException, InvalidProtocolBufferException {
        DataByteArray protoMetadata = (DataByteArray) input.get(0);
        DocumentMetadata metadata = DocumentMetadata.parseFrom(protoMetadata.get());
        return metadata;
    }

    @Override
    public MediaContainer getDocumentMedia(Tuple input) throws ExecException, InvalidProtocolBufferException {
        MediaContainer media = null;
        if (input.size() >= 1) {
            DataByteArray protoMedia = (DataByteArray) input.get(1);
            media = MediaContainer.parseFrom(protoMedia.get());
        }
        return media;
    }
}