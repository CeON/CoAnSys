package pl.edu.icm.coansys.disambiguation.author.pig.normalizers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.models.DocumentProtos.Author;


public class AuthorToInitials implements PigNormalizer {

	private static final Logger logger = LoggerFactory.getLogger(AuthorToInitials.class);

	public Object normalize( Object obj ) {
		
		if (obj instanceof Author) {
			Author author = (Author) obj;
			String name = author.getName();
			String sname = author.getSurname();

			if (name == null) {
				logger.info("No name for author with key: " + author.getKey());
				return sname;
			}

			if (sname == null) {
				logger.info("No surname for author with key: "
						+ author.getKey());
			}

			return name.substring(0, 1) + ". " + sname;
			
		} else {
			logger.info("Given object is not instance of Author class");			
			return obj;
		}
	}
}
