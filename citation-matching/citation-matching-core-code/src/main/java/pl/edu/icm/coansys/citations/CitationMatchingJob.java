package pl.edu.icm.coansys.citations;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.collect.Lists;

import pl.edu.icm.coansys.citations.hashers.HashGenerator;

/**
 * 
 * Citation matching job.
 * 
 * @author Łukasz Dumiszewski
 */

public class CitationMatchingJob {
    
    
    //------------------------ LOGIC --------------------------
    
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        CitationMatchingJobParameters params = new CitationMatchingJobParameters();
        JCommander jcommander = new JCommander(params);
        jcommander.parse(args);
        
        
        SparkConf conf = new SparkConf();
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        conf.set("spark.kryo.registrator", "pl.edu.icm.coansys.citations.MatchableEntityKryoRegistrator");
        
        
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            
            @SuppressWarnings("rawtypes")
            ConfigurableCitationMatchingService citationMatchingService = createConfigurableCitationMatchingService(sc, params);
            
            citationMatchingService.matchCitations(params.citationPath, params.documentPath, params.outputDirPath);
            
           
        }
        
    }



    //------------------------ PRIVATE --------------------------
    
    @SuppressWarnings("rawtypes")
    private static ConfigurableCitationMatchingService createConfigurableCitationMatchingService(JavaSparkContext sc, CitationMatchingJobParameters params) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        ConfigurableCitationMatchingService configurableCitationMatchingService = new ConfigurableCitationMatchingService();
        
        CoreCitationMatchingService coreCitationMatchingService = createCoreCitationMatchingService(sc, params);
        
        configurableCitationMatchingService.setCoreCitationMatchingService(coreCitationMatchingService);
        configurableCitationMatchingService.setNumberOfPartitions(params.numberOfPartitions);
        
        createAndSetInputCitationReaderAndConverter(configurableCitationMatchingService, sc, params);
        createAndSetInputDocumentReaderAndConverter(configurableCitationMatchingService, sc, params);
        createAndSetOutputWriterAndConverter(configurableCitationMatchingService, sc, params);
        
        return configurableCitationMatchingService;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void createAndSetInputCitationReaderAndConverter(ConfigurableCitationMatchingService citationMatchingService, JavaSparkContext sc, CitationMatchingJobParameters params) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        InputCitationReader inputCitationReader = Class.forName(params.inputCitationReaderClass).asSubclass(InputCitationReader.class).newInstance();
        inputCitationReader.setSparkContext(sc);
        citationMatchingService.setInputCitationReader(inputCitationReader);
        
        InputCitationConverter inputCitationConverter = Class.forName(params.inputCitationConverterClass).asSubclass(InputCitationConverter.class).newInstance();
        citationMatchingService.setInputCitationConverter(inputCitationConverter);
        
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void createAndSetInputDocumentReaderAndConverter(ConfigurableCitationMatchingService service, JavaSparkContext sc, CitationMatchingJobParameters params) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        InputDocumentReader inputDocumentReader = Class.forName(params.inputDocumentReaderClass).asSubclass(InputDocumentReader.class).newInstance();
        inputDocumentReader.setSparkContext(sc);
        service.setInputDocumentReader(inputDocumentReader);
        
        InputDocumentConverter inputDocumentConverter = Class.forName(params.inputDocumentConverterClass).asSubclass(InputDocumentConverter.class).newInstance();
        service.setInputDocumentConverter(inputDocumentConverter);
        
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void createAndSetOutputWriterAndConverter(ConfigurableCitationMatchingService citationMatchingService, JavaSparkContext sc, CitationMatchingJobParameters params) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        OutputWriter outputWriter = Class.forName(params.outputWriterClass).asSubclass(OutputWriter.class).newInstance();
        citationMatchingService.setOutputWriter(outputWriter);
        
        OutputConverter outputConverter = Class.forName(params.outputConverterClass).asSubclass(OutputConverter.class).newInstance();
        citationMatchingService.setOutputConverter(outputConverter);
        
    }
    
    
    private static CoreCitationMatchingService createCoreCitationMatchingService(JavaSparkContext sc, CitationMatchingJobParameters params) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        CoreCitationMatchingService coreCitationMatchingService = new CoreCitationMatchingService();
        coreCitationMatchingService.setSparkContext(sc);
        coreCitationMatchingService.setMaxHashBucketSize(params.maxHashBucketSize);
        coreCitationMatchingService.setMatchableEntityHashers(createMatchableEntityHashers(params.hashGeneratorClasses));
        
        return coreCitationMatchingService;
    }
    
    private static List<Pair<MatchableEntityHasher, MatchableEntityHasher>> createMatchableEntityHashers(List<String> hashGeneratorClassNames) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        List<Pair<MatchableEntityHasher, MatchableEntityHasher>> matchableEntityHashers = Lists.newArrayList();
        
        for (String citAndDocHashGeneratorClassNames : hashGeneratorClassNames) {
            String citationHashGeneratorClassName = citAndDocHashGeneratorClassNames.split(":")[0];
            String documentHashGeneratorClassName = citAndDocHashGeneratorClassNames.split(":")[1];
            
            MatchableEntityHasher citationHasher = createMatchableEntityHasher(citationHashGeneratorClassName);
            MatchableEntityHasher documentHasher = createMatchableEntityHasher(documentHashGeneratorClassName);
            
            matchableEntityHashers.add(new ImmutablePair<MatchableEntityHasher, MatchableEntityHasher>(citationHasher, documentHasher));
        }
        
        return matchableEntityHashers;
    }
    
    private static MatchableEntityHasher createMatchableEntityHasher(String hashGeneratorClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        HashGenerator hashGenerator = (HashGenerator) Class.forName(hashGeneratorClass).newInstance();
        MatchableEntityHasher matchableEntityHasher = new MatchableEntityHasher();
        matchableEntityHasher.setHashGenerator(hashGenerator);
        return matchableEntityHasher;
    }
    
    
    @Parameters(separators = "=")
    private static class CitationMatchingJobParameters {
        
        @Parameter(names = "-citationPath", required = true, description = "path to directory/file with citations")
        private String citationPath;
        
        @Parameter(names = "-documentPath", required = true, description = "path to directory/file with documents")
        private String documentPath;
        
        
        @Parameter(names = "-hashGeneratorClasses", required = true, 
                description = "Names of classes used for generating hashes for citations and documents. The names must be seperated by colon sign (:). "
                        + "The first class will be used for citations and the second for documents."
                        + "The classes must implement pl.edu.icm.coansys.citations.hashers.HashGenerator interface."
                        + "This parameter may be specified multiple times. Separately for each given heurisitic.")
        private List<String> hashGeneratorClasses;
        
        @Parameter(names = "-outputDirPath", required = true, description = "path to directory with results")
        private String outputDirPath;
        
        @Parameter(names="-maxHashBucketSize", required = false, description = "max number of the citation-documents pairs for a given hash")
        private long maxHashBucketSize = 10000;
        
        @Parameter(names="-numberOfPartitions", required = false, description = "number of partitions used for rdds with citations and documents read from input files, if not set it will depend on the input format")
        private Integer numberOfPartitions;
        
        
        @Parameter(names = "-inputDocumentReaderClass", required = false)
        private String inputDocumentReaderClass = DefaultInputReader.class.getName();
        
        @Parameter(names = "-inputDocumentConverterClass", required = false)
        private String inputDocumentConverterClass = DummyInputConverter.class.getName();
        
        
        @Parameter(names = "-inputCitationReaderClass", required = false)
        private String inputCitationReaderClass = DefaultInputReader.class.getName();
        
        @Parameter(names = "-inputCitationConverterClass", required = false)
        private String inputCitationConverterClass = DummyInputConverter.class.getName();
        
        
        @Parameter(names = "-outputWriterClass", required = false)
        private String outputWriterClass = DefaultOutputWriter.class.getName();
        
        @Parameter(names = "-outputConverterClass", required = false)
        private String outputConverterClass = DummyOutputConverter.class.getName();
    }
    
    
}
