package pl.edu.icm.coansys.document.deduplication;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.spark.serializer.KryoRegistrator;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;


/**
 * Simple class which registers custom serializers for the documents protocol 
 * buffer generated classes.
 * @author Aleksander Nowinski <a.nowinski@icm.edu.pl>
 */
public class DocumentWrapperKryoRegistrator implements KryoRegistrator {

    @Override
    public void registerClasses(Kryo kryo) {
        kryo.register(DocumentWrapper.class, new DocumentWrapperSerializer());
        kryo.register(DocumentMetadata.class, new DocumentMetadataSerializer());
    }


    public static class DocumentWrapperSerializer extends Serializer<DocumentWrapper> {

        @Override
        public void write(Kryo kryo, Output output, DocumentWrapper object) {
            byte[] bytes = object.toByteArray();
            
            output.writeInt(bytes.length, true);
            output.writeBytes(bytes);
        }

        @Override
        public DocumentWrapper read(Kryo kryo, Input input, Class<DocumentWrapper> type) {
            int length = input.readInt(true);
            byte[] bytes = input.readBytes(length);
            
            try {
                return DocumentWrapper.parseFrom(bytes);//FIXME: is this exception handling ok?
            } catch (InvalidProtocolBufferException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
    
    public static class DocumentMetadataSerializer extends Serializer<DocumentMetadata> {

        @Override
        public void write(Kryo kryo, Output output, DocumentMetadata object) {
            byte[] bytes = object.toByteArray();
            
            output.writeInt(bytes.length, true);
            output.writeBytes(bytes);
        }

        @Override
        public DocumentMetadata read(Kryo kryo, Input input, Class<DocumentMetadata> type) {
            int length = input.readInt(true);
            byte[] bytes = input.readBytes(length);
            
            try {
                return DocumentMetadata.parseFrom(bytes);//FIXME: is this exception handling ok?
            } catch (InvalidProtocolBufferException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
    
}