package pl.edu.icm.coansys.citations.coansys;

import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import pl.edu.icm.coansys.citations.ConfigurableCitationMatchingService;
import pl.edu.icm.coansys.citations.CoreCitationMatchingService;
import pl.edu.icm.coansys.citations.CoreCitationMatchingSimpleFactory;
import pl.edu.icm.coansys.citations.coansys.input.CoansysInputCitationConverter;
import pl.edu.icm.coansys.citations.coansys.input.CoansysInputCitationReader;
import pl.edu.icm.coansys.citations.coansys.input.CoansysInputDocumentConverter;
import pl.edu.icm.coansys.citations.coansys.input.CoansysInputDocumentReader;
import pl.edu.icm.coansys.citations.coansys.output.CoansysOutputConverter;
import pl.edu.icm.coansys.citations.coansys.output.CoansysOutputWriter;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;
import pl.edu.icm.coansys.models.PICProtos.PicOut;


/**
 * Coansys citation matching job
 * 
 * @author Łukasz Dumiszewski
 */
public class CoansysCitationMatchingJob {

    
    private static CoreCitationMatchingSimpleFactory coreCitationMatchingFactory = new CoreCitationMatchingSimpleFactory();
    
    
    
    //------------------------ LOGIC --------------------------
    
    public static void main(String[] args) throws IOException {
        
        CoansysCitationMatchingJobParameters params = new CoansysCitationMatchingJobParameters();
        JCommander jcommander = new JCommander(params);
        jcommander.parse(args);
        
        
        SparkConf conf = new SparkConf();
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        conf.set("spark.kryo.registrator", "pl.edu.icm.coansys.citations.MatchableEntityKryoRegistrator");
        
        
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            
            ConfigurableCitationMatchingService<String, ReferenceMetadata, String, DocumentWrapper, String, PicOut> citationMatchingService = createConfigurableCitationMatchingService(sc, params);
            
            citationMatchingService.matchCitations(sc, params.inputCitationPath, params.inputDocumentPath, params.outputDirPath);
            
           
        }
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static ConfigurableCitationMatchingService<String, ReferenceMetadata, String, DocumentWrapper, String, PicOut> createConfigurableCitationMatchingService(JavaSparkContext sc, CoansysCitationMatchingJobParameters params) {
        
        ConfigurableCitationMatchingService<String, ReferenceMetadata, String, DocumentWrapper, String, PicOut> configurableCitationMatchingService = new ConfigurableCitationMatchingService<>();
        
        
        CoreCitationMatchingService coreCitationMatchingService = coreCitationMatchingFactory.createCoreCitationMatchingService(sc, params.maxHashBucketSize);
        
        configurableCitationMatchingService.setCoreCitationMatchingService(coreCitationMatchingService);
        configurableCitationMatchingService.setNumberOfPartitions(params.numberOfPartitions);
        
        
        CoansysInputCitationReader inputCitationReader = new CoansysInputCitationReader();
        configurableCitationMatchingService.setInputCitationReader(inputCitationReader);
        
        CoansysInputCitationConverter inputCitationConverter = new CoansysInputCitationConverter();
        inputCitationConverter.setModel(params.cermineCitationMetadataExtractModel);
        configurableCitationMatchingService.setInputCitationConverter(inputCitationConverter);
        
        
        CoansysInputDocumentReader inputDocumentReader = new CoansysInputDocumentReader();
        configurableCitationMatchingService.setInputDocumentReader(inputDocumentReader);
        
        CoansysInputDocumentConverter inputDocumentConverter = new CoansysInputDocumentConverter();
        configurableCitationMatchingService.setInputDocumentConverter(inputDocumentConverter);
        
        
        
        CoansysOutputConverter outputConverter = new CoansysOutputConverter();
        configurableCitationMatchingService.setOutputConverter(outputConverter);
        
        CoansysOutputWriter outputWriter = new CoansysOutputWriter();
        configurableCitationMatchingService.setOutputWriter(outputWriter);
        
        
        return configurableCitationMatchingService;
    }
    
    
    @Parameters(separators = "=")
    private static class CoansysCitationMatchingJobParameters {
        
        @Parameter(names = "-inputDocumentPath", required = true, description = "path to directory/file with documents")
        private String inputDocumentPath;

        @Parameter(names = "-inputCitationPath", required = true, description = "path to directory/file with citations")
        private String inputCitationPath;
        
        @Parameter(names = "-outputDirPath", required = true, description = "path to directory with results")
        private String outputDirPath;
        
        @Parameter(names="-maxHashBucketSize", required = false, description = "max number of the citation-documents pairs for a given hash")
        private long maxHashBucketSize = 10000;
        
        @Parameter(names="-numberOfPartitions", required = false, description = "number of partitions used for rdds with citations and documents read from input files, if not set it will depend on the input format")
        private Integer numberOfPartitions;
        
        @Parameter(names="-cermineCitationMetadataExtractModel", required = false, description = "classpath to model used by cermine to extract metadata from raw citation")
        private String cermineCitationMetadataExtractModel = "/pl/edu/icm/cermine/bibref/acrf.ser.gz";
        
    }
}
