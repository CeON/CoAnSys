/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.disambiguation.author.features.extractors;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractor;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

public class DisambiguationExtractorFactory {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(DisambiguationExtractorFactory.class);

	private Map<String, String> nameToId;
	private Map<String, String> idToName;
	// taking package with extractors using any of them
	private final String THIS_PACKAGE = this.getClass().getPackage().getName();

	public DisambiguationExtractorFactory() throws InstantiationException, IllegalAccessException {
		
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
			
			// all extractors must have id
			if (eid == null || eid.isEmpty() ) {
				String m = "Creating extractor: " + name
						+ " with no id value given (null).";
				logger.error(m);
				throw new IllegalStateException(m);
			}

			nameToId.put(name, eid);

			// checking, if every extractors has unique id
			if (idToName.containsKey(eid)) {
				String m = "Some extractors have the same id: " + eid + ": "
						+ name + ", " + idToName.get(eid) + ".";
				logger.error(m);
				throw new IllegalStateException(m);
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
		if(extractorName == null){
			extractorName = fi.getFeatureExtractorName();
		}

		if(extractorName == null){
			System.out.println("Cannot find class(FeatureExtractionName) with name: " + 
				fi.getFeatureExtractorName());
			System.out.println("Cannot read mapping of class(FeatureExtractionName) with name: " + 
				toExName(fi.getFeatureExtractorName()));
		}
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
