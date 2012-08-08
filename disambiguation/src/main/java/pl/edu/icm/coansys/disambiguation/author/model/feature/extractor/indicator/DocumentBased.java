/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.feature.extractor.indicator;

import pl.edu.icm.coansys.disambiguation.author.model.feature.Extractor;

/**
 * The interface for indicating that an {@link Extractor} results are not author specific and may be calculated once per document 
 * (and e.g. add to all authors feature value map) 
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public interface DocumentBased {
}
