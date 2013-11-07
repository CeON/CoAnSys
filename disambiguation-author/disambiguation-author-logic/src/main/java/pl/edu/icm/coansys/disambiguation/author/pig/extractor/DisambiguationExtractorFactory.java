package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

public class DisambiguationExtractorFactory {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(DisambiguationExtractorFactory.class);

	private Map<String, String> nameToId;
	private Map<String, String> idToName;
	private final String THIS_PACKAGE = new DisambiguationExtractor()
			.getClass().getPackage().getName();

	public DisambiguationExtractorFactory() throws Exception {

		nameToId = new HashMap<String, String>();
		idToName = new IdentityHashMap<String, String>();

		Reflections reflections = new Reflections(THIS_PACKAGE);

		Set<Class<? extends DisambiguationExtractor>> classes = reflections
				.getSubTypesOf(DisambiguationExtractor.class);

		@SuppressWarnings("unchecked")
		Class<? extends DisambiguationExtractor>[] ar = 
				classes.toArray(new Class[classes.size()]);

		String name, eid;
		for (Class<? extends DisambiguationExtractor> c : ar) {
			name = c.getSimpleName();

			// if this is not extractor
			if (!name.startsWith("EX_")) {
				continue;
			}

			DisambiguationExtractor e = c.newInstance();
			eid = e.getId();

			if (eid == null) {
				String m = "Creating extractor: " + name
						+ " with no id value given (null).";
				logger.error(m);
				throw new Exception(m);
			}

			nameToId.put(name, eid);

			// checking, if every extractors has unique id
			if (idToName.containsKey(eid)) {
				String m = "Some extractors have the same id: " + eid + ": "
						+ name + ", " + idToName.get(eid) + ".";
				logger.error(m);
				throw new Exception(m);
			}
			idToName.put(eid, name);
		}
	}

	/*
	 * Converting extractor name/id to opposite one.
	 */
	public String convertExtractorNameOrIdToOpposite(String feature) {
		if (feature.length() == 1) {
			return convertExIdToName(feature);
		} else {
			return convertExNameToId(feature);
		}
	}

	public String convertExNameToId(String extractorName) {
		return nameToId.get(extractorName);
	}

	public String convertExIdToName(String extractorId) {
		return idToName.get(extractorId);
	}

	public String toExName(String extractorNameOrId) {
		String name = idToName.get(extractorNameOrId);
		if (name != null) {
			return name;
		}
		if (nameToId.containsKey(extractorNameOrId)) {
			return extractorNameOrId;
		}
		return null;
	}

	public String toExId(String extractorNameOrId) {
		String id = nameToId.get(extractorNameOrId);
		if (id != null) {
			return id;
		}
		if (idToName.containsKey(extractorNameOrId)) {
			return extractorNameOrId;
		}
		return null;
	}

	public DisambiguationExtractor create(FeatureInfo fi)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		String extractorName = toExName(fi.getFeatureExtractorName());
		String currentClassName = THIS_PACKAGE + "." + extractorName;

		// creating extractor with given name
		Class<?> c = null;
		try {
			c = Class.forName(currentClassName);
		} catch (ClassNotFoundException e) {
			String m = "Cannot find class for create: " + currentClassName;
			logger.error(m + StackTraceExtractor.getStackTrace(e));
			throw new ClassNotFoundException(m, e);
		}

		return (DisambiguationExtractor) c.newInstance();
	}
}
