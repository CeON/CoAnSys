/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.clustering.strategy;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.el.util.ReflectionUtil;


/**
 *
 * @author pdendek
 */
public class ClusteringStrategyFactory {

    private static final String THIS_PACKAGE = new ClusteringStrategyFactory().getClass().getPackage().getName();

    public static ClusteringStrategy create(String name) {
        try {
            return (ClusteringStrategy) ReflectionUtil.forName(THIS_PACKAGE + "." + name).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(ClusteringStrategyFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
