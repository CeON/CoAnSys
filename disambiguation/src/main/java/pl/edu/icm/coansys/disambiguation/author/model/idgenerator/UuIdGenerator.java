/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.idgenerator;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */

public class UuIdGenerator implements IdGenerator {
	
	@Override
	public String genetareId(List<String> args) {
        Collections.sort(args);
        StringBuilder builder = new StringBuilder();
        for (String contributionId : args) {
            builder.append(contributionId).append('\n');
        }
        try {
        	String ret = UUID.nameUUIDFromBytes(builder.toString().getBytes("UTF-8")).toString();
        	if("".equals(ret))
        		throw new IllegalStateException("UUID not generated");
            return  ret;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Cannot happen", e);
        }
    }
}
