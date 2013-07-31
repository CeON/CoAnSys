/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.clustering.strategies;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.el.util.ReflectionUtil;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public final class ClusteringStrategyFactory {

    private static final String THIS_PACKAGE = new ClusteringStrategyFactory().getClass().getPackage().getName();

    private ClusteringStrategyFactory() {
    }

    public static ClusteringStrategy create(String name) {
        try {
            return (ClusteringStrategy) ReflectionUtil.forName(THIS_PACKAGE + "." + name).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(ClusteringStrategyFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
