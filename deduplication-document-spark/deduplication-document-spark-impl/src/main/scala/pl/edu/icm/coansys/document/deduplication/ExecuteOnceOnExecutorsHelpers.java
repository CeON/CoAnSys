/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.document.deduplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

/**
 *
 * @author Aleksander Nowinski <aleksander.nowinski@gmail.com>
 */
public class ExecuteOnceOnExecutorsHelpers {

    private static Set<String> executedCode = new HashSet<>();

    public static void executeEverywhere(SparkContext sc, String codeId, boolean runOnDriver, VoidFunction<Iterator> function) throws Exception{
        JavaSparkContext.fromSparkContext(sc).parallelize(Arrays.asList(new Integer[1000])).foreachPartition(new VoidFunction<Iterator<Integer>>() {
            @Override
            public void call(Iterator it) throws Exception {
                if (!executedCode.contains(codeId)) {
                    ExecuteOnceOnExecutorsHelpers.executedCode.add(codeId);
                    function.call(it);
                }
            }
        });
        if (runOnDriver) {
            function.call(null);
        }
    }

}
