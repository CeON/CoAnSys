/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.idgenerators;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.el.util.ReflectionUtil;

/**
 * The factory returning {@link IdGenerator}s from package
 * "pl.edu.icm.coansys.disambiguation.author.idgenerators"
 *
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public final class IdGeneratorFactory {

    private static final String THIS_PACKAGE = new IdGeneratorFactory().getClass().getPackage().getName();

    private IdGeneratorFactory() {
    }

    public static IdGenerator create(String name) {
        try {
            return (IdGenerator) ReflectionUtil.forName(THIS_PACKAGE + "." + name).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(IdGeneratorFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
