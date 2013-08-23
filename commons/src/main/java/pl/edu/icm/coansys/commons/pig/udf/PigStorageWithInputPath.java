/*
 * Source code from Pig Wiki:
 * https://cwiki.apache.org/PIG/faq.html#FAQ-Q%3AIloaddatafromadirectorywhichcontainsdifferentfile.HowdoIfindoutwherethedatacomesfrom%3F
 */

package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.builtin.PigStorage;
import org.apache.pig.data.Tuple;

public class PigStorageWithInputPath extends PigStorage {
    Path path = null;

    @Override
    public void prepareToRead(@SuppressWarnings("rawtypes") RecordReader reader, PigSplit split) {
        super.prepareToRead(reader, split);
        path = ((FileSplit)split.getWrappedSplit()).getPath();
    }

    @Override
    public Tuple getNext() throws IOException {
        Tuple myTuple = super.getNext();
        if (myTuple != null)
            myTuple.append(path.toString());
        return myTuple;
    }
}