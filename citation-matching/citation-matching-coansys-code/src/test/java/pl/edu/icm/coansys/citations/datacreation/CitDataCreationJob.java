package pl.edu.icm.coansys.citations.datacreation;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.python.google.common.collect.Lists;

import pl.edu.icm.cermine.bibref.CRFBibReferenceParser;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.coansys.citations.CoreCitationMatchingSimpleFactory;
import pl.edu.icm.coansys.citations.DefaultInputReader;
import pl.edu.icm.coansys.citations.InputCitationReader;
import pl.edu.icm.coansys.citations.converters.RawReferenceToEntityConverter;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.entity_id.CitEntityId;
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;
import scala.Tuple2;

/**
 * 
 * Citation matching job.
 * 
 * @author Łukasz Dumiszewski
 */

public class CitDataCreationJob {
    
    private static CoreCitationMatchingSimpleFactory coreCitationMatchingFactory = new CoreCitationMatchingSimpleFactory();
    
    
    //------------------------ LOGIC --------------------------
    
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        
        SparkConf conf = new SparkConf();
        //conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        //conf.set("spark.kryo.registrator", "pl.edu.icm.coansys.citations.MatchableEntityKryoRegistrator");
        
        
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            
            InputCitationReader<String, MatchableEntity> inputCitationReader = new DefaultInputReader();
            inputCitationReader.setSparkContext(sc);
                
            String citationPath = "/C:/workspace/coansys/citation-matching/citation-matching-core-code/src/test/resources/heuristic/citations";
            
            JavaPairRDD<String, MatchableEntity> citations = inputCitationReader.readCitations(citationPath, 1);
        
            JavaPairRDD<String, Tuple2<Integer, MatchableEntity>> docIdCitations = citations.mapToPair(cit->new Tuple2<String, Tuple2<Integer, MatchableEntity>>(citIdToDocId(cit._1()), new Tuple2<Integer, MatchableEntity>(citIdToPos(cit._1()), cit._2())));
            
            JavaPairRDD<String, Iterable<Tuple2<Integer, MatchableEntity>>> docIdCitIterable = docIdCitations.groupByKey();
            
            docIdCitIterable.collect().forEach(x->System.out.println(x._1() + "(" + x._1() + "): " + x._2()));
            
            JavaPairRDD<DocumentWrapper, Iterable<Tuple2<Integer,MatchableEntity>>> docWrappersSrcME = 
                    docIdCitIterable.mapToPair(docIdCit->new Tuple2<DocumentWrapper, Iterable<Tuple2<Integer,MatchableEntity>>>(convertToDocumentWrapper(docIdCit._1(), docIdCit._2()), docIdCit._2()));
            
            
                     
            
            docWrappersSrcME.collect().forEach(x-> {
            
                System.out.println(x._1());
                System.out.println("zrodlowe cytowania");
                x._2().forEach(m->System.out.println(m._1() + ": " + toMString(m._2())));
            }
            );
            
            docWrappersSrcME.mapToPair(k->new Tuple2<Text, BytesWritable>(new Text(k._1().getRowId()), new BytesWritable(k._1().toByteArray()))).saveAsNewAPIHadoopFile("/c:/temp/rawcitations", Text.class, BytesWritable.class, SequenceFileOutputFormat.class);
            
            JavaPairRDD<DocumentWrapper, List<MatchableEntity>> docWrappersResME = 
                    docWrappersSrcME.mapToPair(d->new Tuple2<DocumentWrapper, List<MatchableEntity>> (d._1(), convertToMatchableEntities(d._1())));
            
        
            docWrappersResME.collect().forEach(x-> {
                
                System.out.println(x._1());
                System.out.println("wynikowe cytowania");
                x._2().forEach(m->System.out.println(toMString(m)));
            }
            );
        }
    }
    
    private static String citIdToDocId(String citId) {
        String docId = citId.substring(4);
        int indexOfLastSep = docId.lastIndexOf("_");
        docId = docId.substring(0, indexOfLastSep);
        return docId.replaceAll("_", "=");
    }
    
    private static Integer citIdToPos(String citId) {
        int indexOfLastSep = citId.lastIndexOf("_");
        return Integer.parseInt(citId.substring(indexOfLastSep+1));
        
    }
    
    
    private static DocumentWrapper convertToDocumentWrapper(String docId, Iterable<Tuple2<Integer, MatchableEntity>> citations) {
        
        DocumentMetadata.Builder docMetadataB = DocumentMetadata.newBuilder().setKey(docId);
        
        for (Tuple2<Integer, MatchableEntity> cit : citations) {
            ReferenceMetadata.Builder refMetadataB = ReferenceMetadata.newBuilder();

            BasicMetadata.Builder bmB = BasicMetadata.newBuilder();
            refMetadataB.setBasicMetadata(bmB.build());
            
            refMetadataB.setPosition(cit._1());
            refMetadataB.setRawCitationText(toRawText(cit._2()));
            docMetadataB.addReference(refMetadataB.build());
            docMetadataB.setBasicMetadata(bmB.build());
            
        }
        
        DocumentWrapper.Builder docWrapperB = DocumentWrapper.newBuilder();
        docWrapperB.setDocumentMetadata(docMetadataB.build());
        docWrapperB.setRowId(docId);
        return docWrapperB.build();
    }


    private static String toRawText(MatchableEntity entity) {
        /*
        String citText = entity.author();
        citText += " (" + entity.year() + "). ";
        if (StringUtils.isNotBlank(entity.title())) {
            citText += "\"" + entity.title() + ".\" ";
        }
        if (StringUtils.isNotBlank(entity.source())) {
            citText += entity.source();
            if (StringUtils.isNotBlank(entity.volume())) {
                citText += ", " + entity.volume();
            }
            if (StringUtils.isNotBlank(entity.pages())) {
                citText += ": " + entity.pages();
            }
        }
        */
        //Humphrey, William, Andrew Dalke, and Klaus Schulten. "VMD: visual molecular dynamics." Journal of molecular graphics 14.1 (1996): 33-38.
        // HUMPHREY, William; DALKE, Andrew; SCHULTEN, Klaus. VMD: visual molecular dynamics. Journal of molecular graphics, 1996, 14.1: 33-38.
        //Humphrey, W., Dalke, A., & Schulten, K. (1996). VMD: visual molecular dynamics. Journal of molecular graphics, 14(1), 33-38.
        
        //System.out.println(citText);
        
        return entity.toReferenceString();
    }

    private static List<MatchableEntity> convertToMatchableEntities(DocumentWrapper docWrapper) {
        CRFBibReferenceParser parser = null;
        try {
            parser = new CRFBibReferenceParser(CitDataCreationJob.class.getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf.ser.gz"));
        } catch (AnalysisException e) {
            e.printStackTrace();
        }
        RawReferenceToEntityConverter rawReferenceConverter = new RawReferenceToEntityConverter(parser);
        
        List<MatchableEntity> entities = Lists.newArrayList();
        for (ReferenceMetadata ref : docWrapper.getDocumentMetadata().getReferenceList()) {
            MatchableEntity entity = rawReferenceConverter.convert(new CitEntityId(docWrapper.getDocumentMetadata().getKey(), ref.getPosition()), ref.getRawCitationText());
            entities.add(entity);
        }
        
        return entities;
        
    }
    
    private static String toMString(MatchableEntity entity) {
        return entity.id() + ":" + entity.author() + ":" + entity.title() + ":" + entity.year() + ":" + entity.source() + ":" + entity.pages();
    }
    
    
}
