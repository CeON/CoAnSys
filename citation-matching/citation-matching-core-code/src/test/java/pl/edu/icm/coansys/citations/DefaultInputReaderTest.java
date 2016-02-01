package pl.edu.icm.coansys.citations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.citations.data.MatchableEntity;
import scala.Tuple2;

/**
 * @author madryk
 */
public class DefaultInputReaderTest {

    private DefaultInputReader inputReader = new DefaultInputReader();
    
    
    @Mock
    private JavaPairRDD<Text, BytesWritable> documentsWritable;
    @Mock
    private JavaPairRDD<String, MatchableEntity> documents;
    
    @Mock
    private JavaPairRDD<Text, BytesWritable> citationsWritable;
    @Mock
    private JavaPairRDD<String, MatchableEntity> citations;
    
    @Mock
    JavaSparkContext sparkContext;
    
    @Captor
    private ArgumentCaptor<PairFunction<Tuple2<Text, BytesWritable>, String, MatchableEntity>> mapToEntityFunction;
    
    
    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        inputReader.setSparkContext(sparkContext);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void readDocuments() throws Exception {
        
        // given
        
        doReturn(documentsWritable).when(sparkContext).sequenceFile("/path/to/documents/", Text.class, BytesWritable.class, 10);
        doReturn(documents).when(documentsWritable).mapToPair(any());
        
        
        // execute
        
        JavaPairRDD<String, MatchableEntity> retDocuments = inputReader.readDocuments("/path/to/documents/", 10);
        
        
        // assert
        
        assertTrue(retDocuments == documents);
        
        verify(sparkContext).sequenceFile("/path/to/documents/", Text.class, BytesWritable.class, 10);
        verify(documentsWritable).mapToPair(mapToEntityFunction.capture());
        assertMapToEntityFunction(mapToEntityFunction.getValue());
        
    }
    
    
    @Test
    public void readDocuments_NULL_PARTITIONS() throws Exception {
        
        // given
        
        doReturn(documentsWritable).when(sparkContext).sequenceFile("/path/to/documents/", Text.class, BytesWritable.class);
        doReturn(documents).when(documentsWritable).mapToPair(any());
        
        
        // execute
        
        JavaPairRDD<String, MatchableEntity> retDocuments = inputReader.readDocuments("/path/to/documents/", null);
        
        
        // assert
        
        assertTrue(retDocuments == documents);
        
        verify(sparkContext).sequenceFile("/path/to/documents/", Text.class, BytesWritable.class);
        verify(documentsWritable).mapToPair(mapToEntityFunction.capture());
        assertMapToEntityFunction(mapToEntityFunction.getValue());
        
    }
    
    
    @Test
    public void readCitations() throws Exception {
        
        // given
        
        doReturn(citationsWritable).when(sparkContext).sequenceFile("/path/to/citations/", Text.class, BytesWritable.class, 10);
        doReturn(citations).when(citationsWritable).mapToPair(any());
        
        
        // execute
        
        JavaPairRDD<String, MatchableEntity> retCitations = inputReader.readCitations("/path/to/citations/", 10);
        
        
        // assert
        
        assertTrue(retCitations == citations);
        
        verify(sparkContext).sequenceFile("/path/to/citations/", Text.class, BytesWritable.class, 10);
        verify(citationsWritable).mapToPair(mapToEntityFunction.capture());
        assertMapToEntityFunction(mapToEntityFunction.getValue());
        
    }
    
    
    @Test
    public void readCitations_NULL_PARTITIONS() throws Exception {
        
        // given
        
        doReturn(citationsWritable).when(sparkContext).sequenceFile("/path/to/citations/", Text.class, BytesWritable.class);
        doReturn(citations).when(citationsWritable).mapToPair(any());
        
        
        // execute
        
        JavaPairRDD<String, MatchableEntity> retCitations = inputReader.readCitations("/path/to/citations/", null);
        
        
        // assert
        
        assertTrue(retCitations == citations);
        
        verify(sparkContext).sequenceFile("/path/to/citations/", Text.class, BytesWritable.class);
        verify(citationsWritable).mapToPair(mapToEntityFunction.capture());
        assertMapToEntityFunction(mapToEntityFunction.getValue());
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertMapToEntityFunction(PairFunction<Tuple2<Text, BytesWritable>, String, MatchableEntity> function) throws Exception {
        
        Text idWritable = new Text("some_id");
        MatchableEntity entity = MatchableEntity.fromParameters("some_id", "John Doe", null, "Some Title", null, null, null);
        BytesWritable entityWritable = new BytesWritable(entity.data().toByteArray());
        Tuple2<Text, BytesWritable> entityWritableTuple = new Tuple2<Text, BytesWritable>(idWritable, entityWritable);
        
        Tuple2<String, MatchableEntity> entityTuple = function.call(entityWritableTuple);
        
        assertEquals(entityTuple._1, "some_id");
        assertEquals(entityTuple._2.id(), "some_id");
        assertEquals(entityTuple._2.author(), "John Doe");
        assertEquals(entityTuple._2.title(), "Some Title");
    }
    
}
