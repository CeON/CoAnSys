package pl.edu.icm.coansys.citationmatching.core.workflow;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.Text;

import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;

/**
 * Util class for reading matched citation sequence file
 * 
 * @author madryk
 *
 */
public class MatchableEntitySequenceFileUtils {
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    private MatchableEntitySequenceFileUtils() {
        throw new IllegalArgumentException("Can't instantiate " + LocalSequenceFileUtils.class.getName() + " class");
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Reads sequence file with matched citations.
     */
    public static List<Pair<MatchableEntity, String>> readMatchedCitations(File matchedCitationsFile) throws IOException {
        
        List<Pair<TextWithBytesWritable, Text>> rawMatchedCitations = LocalSequenceFileUtils.readSequenceFile(matchedCitationsFile,
                TextWithBytesWritable.class, Text.class);
        
        
        List<Pair<MatchableEntity, String>> citations = rawMatchedCitations.stream()
                .map(pair -> new ImmutablePair<MatchableEntity, String>(
                        MatchableEntity.fromBytes(pair.getLeft().bytes().copyBytes()),
                        pair.getRight().toString()))
                .collect(Collectors.toList());
        
        
        return citations;
    }
    
}
