package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ToList {

    private static final Logger logger = LoggerFactory.getLogger(ToList.class);

    private ToList() {
    }

    public static List execute(DataBag db) {
        Iterator<Tuple> it = db.iterator();

        List ret = new LinkedList();
        while (it.hasNext()) {
            try {
                ret.add(it.next().get(0));
            } catch (ExecException e) {
                logger.error("Caught exception:", e);
            }
        }

        return ret;

    }
}
