package pl.edu.icm.coansys.disambiguation.author.pig.normalizers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.models.DocumentProtos.Author;


public class AuthorToInitials implements PigNormalizer {

	private static final Logger logger = LoggerFactory.getLogger(AuthorToInitials.class);

	public Object normalize( Object obj ) {
		
		if (obj instanceof Author) {
			Author author = (Author) obj;
			String fname = author.getForenames(); 
			if (fname == null || fname.isEmpty()) {
				logger.info("No forenames for author with key: " + author.getKey());
				return "";
			}

			return fname.substring(0, 1);
			
		} else {
			logger.info("Given object is not instance of Author class");			
			return obj;
		}
	}
}
