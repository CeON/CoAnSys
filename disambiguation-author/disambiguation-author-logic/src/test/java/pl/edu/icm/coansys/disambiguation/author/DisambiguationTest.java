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

package pl.edu.icm.coansys.disambiguation.author;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.DefaultTuple;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.commons.java.DiacriticsRemover;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.CoAuthorsSnameDisambiguatorFullList;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.CosineSimilarity;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.Disambiguator;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.Intersection;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.IntersectionPerMaxval;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.IntersectionPerSum;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.YearDisambiguator;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.DisambiguationExtractorFactory;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractorDocument;
import pl.edu.icm.coansys.disambiguation.author.normalizers.ToEnglishLowerCase;
import pl.edu.icm.coansys.disambiguation.author.normalizers.ToHashCode;
import pl.edu.icm.coansys.disambiguation.author.pig.AND;
import pl.edu.icm.coansys.disambiguation.author.pig.AproximateAND_BFS;
import pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND;
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

// TODO:
/* EXTRACTORS tests. Building metadata (author) example:
// 
		import pl.edu.icm.coansys.models.DocumentProtos.Author;
		Author.Builder ab =  Author.newBuilder();
		ab.setName("Piotr");
		ab.setSurname("Dendek");
		ab.setKey("needed");
		Author author = ab.build();
		String toInitExpected = "P. Dendek";
*/


public class DisambiguationTest {
	 
    // Follow name scheme of tests: package_class[_method]
	// TODO: split tests into correct packages, rename
	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void pig_normalizers_ALL() {
		String text = "é{(Zaaaażółć 'gęślą', \"jaź(ń)\"}]# æ 1234567890 !@#$%^&*() _+=?/>.<,-";
		String diacRmExpected = "e{(Zaaaazolc 'gesla', \"jaz(n)\"}]# ae 1234567890 !@#$%^&*() _+=?/>.<,-";
		String toELCExpected = "ezaaaazolc gesla jazn ae 1234567890";
		Integer toHashExpected = -1486600746;
		Integer DisExtrExpected = diacRmExpected.toLowerCase().hashCode();
		Object a, b, c, d, e, f;
		String tmp;
		
		// testing critical changes in DiacriticsRemover
		tmp = DiacriticsRemover.removeDiacritics( text );
		assert( tmp.equals(diacRmExpected) );
		
		// testing normalizers
		a = (new ToEnglishLowerCase()).normalize( text );
		assert( a.equals( toELCExpected ) );
		b = (new ToEnglishLowerCase()).normalize( a );
		assert( a.equals( b ) );
		a = (new ToHashCode()).normalize( text );
		assert( a.equals( toHashExpected ) );
		a = (new ToHashCode()).normalize( (Object) text );
		assert( a.equals( toHashExpected ) );
		f = (new pl.edu.icm.coansys.disambiguation.author.normalizers.DiacriticsRemover()).normalize((Object) text);
		assert( f.equals( diacRmExpected ) );
		
		// checking null argument / multi spaces:
		String doublespace = "  ";
		c = (new ToEnglishLowerCase()).normalize( doublespace );
		assert( c == null );
		
		String manyspaces = "  a  b  ";
		d = (new ToEnglishLowerCase()).normalize( manyspaces );
		assert( d.equals("a b") );			
		
		e = (new ToEnglishLowerCase()).normalize( null );
		assert( e == null );		
		e = (new ToHashCode()).normalize( null );
		assert( e == null );		

		
		// DisambiguationExtractor - normalizeExtracted tests:
		// testing normalize tool, which is using after data extraction
		DisambiguationExtractorDocument DED = 
				new DisambiguationExtractorDocument();
		a = DED.normalizeExtracted( text );
		assert( a.equals( DisExtrExpected ) );
   	}
   	
   	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void pig_extractor_DisambiguationExtractorFactory_ALL() 
   			throws Exception {
   		
   		DisambiguationExtractorFactory factory = 
   				new DisambiguationExtractorFactory();
   		
   		String[] extractors = {
   			   	"EX_DOC_AUTHS_SNAMES",
   			   	"EX_KEYWORDS_SPLIT",
   			   	"EX_EMAIL",
   			   	"EX_YEAR",
   			   	"EX_TITLE",
   			   	"EX_TITLE_SPLIT",
   			   	"EX_PERSON_ID",
   			   	"EX_KEYWORDS",
   			   	"EX_COAUTH_SNAME",
   			   	"EX_CLASSIFICATION_CODES",
   			   	"EX_AUTH_FNAMES_FST_LETTER",
   			   	"EX_EMAIL_PREFIX" ,
   			   	"EX_DOC_AUTHS_FNAME_FST_LETTER",
   			   	"EX_AUTH_FNAME_FST_LETTER",
   			   	"EX_PERSON_IDS",
   			   	"EX_PERSON_PBN_ID"
   		};
   		String[] ids = {
   				"0",
   				"6",
   				"4",
   				"B",
   				"A",
   				"9",
   				"8",
   				"7",
   				"2",
   				"1",
   				"5",
   				"3",
   				"C",
   				"D",
   				"E",
   				"8.2"
   		};
   		
   		assert ( ids.length == extractors.length );
   		
   		for ( int i = 0; i < extractors.length; i++ ) {
   			assert( ids[i].equals(factory.convertExNameToId( extractors[i] )) );
   			assert( extractors[i].equals( factory.convertExIdToName( ids[i] ) ));
   			assert( ids[i].equals( factory.toExId( ids[i] ) ) );
   			assert( ids[i].equals( factory.toExId( extractors[i] ) ) );
   			assert( extractors[i].equals( factory.toExName( ids[i] ) ) );
   			assert( extractors[i].equals( factory.toExName( extractors[i] ) ) );
   			
   			// testing disambiguator class creating for both type of 
   			// extractor names: explicit class name and extractor id
   			FeatureInfo f = new FeatureInfo(null,extractors[i],0,0);
   			DisambiguationExtractor c = factory.create( f );
   			assert( c != null );
   			f.setFeatureExtractorName( ids[i] );
   			c = factory.create( f );
   			assert( c != null );
   		}
   	}
   	
   	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void features_disambiguator_factory() {
   		String featureDescription = "Intersection#whatever#0.0#0";
		List<FeatureInfo> filist = FeatureInfo.parseFeatureInfoString(featureDescription);
		FeatureInfo fi = filist.get(0);
		
   		DisambiguatorFactory ff = new DisambiguatorFactory();
   		Disambiguator d = ff.create(fi);
   		assert(d != null);
   	}
   	
   	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void features_disambiguator_calculateAffinity() {
   		Disambiguator COAUTH = new CoAuthorsSnameDisambiguatorFullList(1,1);
   		Disambiguator IPS = new IntersectionPerSum(1,1);
   		Disambiguator IPM = new IntersectionPerMaxval(1,1);
   		Disambiguator IPM2 = new IntersectionPerMaxval(3.0,10.0);
   		Disambiguator I = new Intersection();
   		
   		Object atab[] = {-1,"one", "two", "three", "four", 5, 6, 7, 8, 9.0, 10.0};
   		Object btab[] = {-2,-1,"one", "two", 5, 9.0, "eleven", 12, 13.0};		
   		List<Object> a = Arrays.asList(atab);
   		List<Object> b = Arrays.asList(btab);
   		
  		assert( IPS.calculateAffinity(a, b) == 5.0 / 15.0 );
  		assert( IPM.calculateAffinity(a, b) == 5.0 );
  		assert( IPM2.calculateAffinity(a, b) == 5.0 / 10.0 * 3.0  );
  		assert( I.calculateAffinity(a, b) == 5.0 );
  		assert( COAUTH.calculateAffinity(a, b) == 4.0 / 15.0 );
   	}
   	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void features_disambiguator_cosineSimilarity() {
   		Disambiguator C = new CosineSimilarity(1000, 1);
   		Object atab[] = {2, 3};
   		Object btab[] = {3};
   		List<Object> a = Arrays.asList(atab);
   		List<Object> b = Arrays.asList(btab);
   		assert( C.calculateAffinity(a, b) == 1/Math.sqrt(2) * 1000 );
   	}
   	
   	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void features_disambiguator_Year() {
   		Disambiguator Y = new YearDisambiguator(2, 30);
   		Object atab[] = {1930};
   		Object btab[] = {"1930"};
   		List<Object> a = Arrays.asList(atab);
   		List<Object> b = Arrays.asList(btab);
   		assert( Y.calculateAffinity(a, b) == 2 );
   	}
   	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void features_disambiguator_calculateAffinity_indeterminate_forms() {
   		Disambiguator COAUTH = new CoAuthorsSnameDisambiguatorFullList(Double.POSITIVE_INFINITY,1);
   		Disambiguator IPS = new IntersectionPerSum(Double.POSITIVE_INFINITY,1);
   		Disambiguator IPM = new IntersectionPerMaxval(Double.POSITIVE_INFINITY,1);
   		Disambiguator I = new Intersection(Double.POSITIVE_INFINITY, 1);
   		Disambiguator C = new CosineSimilarity(Double.POSITIVE_INFINITY, 1);
   		Disambiguator Y = new YearDisambiguator(Double.POSITIVE_INFINITY, 1);
   		
   		Object atab[] = {1};
   		Object btab[] = {2};		
   		List<Object> a = Arrays.asList(atab);
   		List<Object> b = Arrays.asList(btab);
   		
		assert (IPS.calculateAffinity(a, b) == 0);
		assert (IPM.calculateAffinity(a, b) == 0);
		assert (I.calculateAffinity(a, b) == 0);
		assert (COAUTH.calculateAffinity(a, b) == 0);
		assert (C.calculateAffinity(a, b) == 0);
		assert (Y.calculateAffinity(a, b) == 0);

   	}
   	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void pig_extractor_EXTRACT_CONTRIBDATE_parsing_arguments() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
   		
   		// testing required
   		boolean catched = false;
   		try {
   			new EXTRACT_CONTRIBDATA_GIVENDATA("");
   		} catch (com.beust.jcommander.ParameterException e) {
   			catched = true;
   		}
   		assert( catched );
   		
   		// only required arguments
   		String featureinfo = "IntersectionPerMaxval#EX_DOC_AUTHS_FNAME_FST_LETTER#1.0#1";
		HashMap<String,Object> minimum = new HashMap<String,Object>();		
		EXTRACT_CONTRIBDATA_GIVENDATA ex1 = new EXTRACT_CONTRIBDATA_GIVENDATA("-featureinfo " + featureinfo );
		minimum.put( "-featureinfo", featureinfo );
		assert( minimum.equals( ex1.debugComponents() ) );
   		
   		// all parameters
		HashMap<String,Object> full = new HashMap<String,Object>();
		full.put( "-lang", "pl" );
		// Note, that boolean parameters do not take arguments
		full.put( "-skipEmptyFeatures", true );
		full.put( "-useIdsForExtractors", true );
		full.put( "-returnNull", true );
		full.put( "-featureinfo", featureinfo );
		
		String arg = full.toString();
		arg = arg.substring(1, arg.length() - 1).replace('=', ' ').replace(",", "");
		
		EXTRACT_CONTRIBDATA_GIVENDATA ex2 = new EXTRACT_CONTRIBDATA_GIVENDATA( arg );
		assert( full.equals( ex2.debugComponents() ) );
   	}
   	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void pig_extractor_EXTRACT_CONTRIBDATA_GIVENDATA_getting_indicators() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
   		// test common contruction
   		new EXTRACT_CONTRIBDATA_GIVENDATA("-featureinfo Intersection#EX_PERSON_ID#1.0#1");	
   		// test "climbing up" through inherited tree of extractor, note that EX_PERSON_COANSYS_ID extends EX_PERSON_ID
   		new EXTRACT_CONTRIBDATA_GIVENDATA("-featureinfo Intersection#EX_PERSON_COANSYS_ID#1.0#1");
   	}
   	
    // Tools:
   	private Tuple contribCreator(Object id, Object sname, 
   			Map<String,DataBag>features){
   		Tuple res = new DefaultTuple();
   		res.append(id);
   		res.append(sname);
   		res.append(features);
   		return res;
   	}
   	
   	private DataBag createFeatureDescriptionBag( Object... descriptions ) {
   		DataBag descriptionBag = new DefaultDataBag();
		TupleFactory tf = TupleFactory.getInstance();
		
   		for ( Object d : descriptions ) {
   			Tuple t = tf.newTuple();
   			t.append(d);
   			descriptionBag.add(t);
   		}
 
   		return descriptionBag;
   	}
   	
   	private DataBag tupleListToDataBag( List<Tuple> list ) {
   		DataBag res = new DefaultDataBag();
   		for ( Tuple c : list ) {
   			res.add(c);
   		}
   		return res;
   	}
   	
   	private void addFeatureToMap( Map<String,DataBag>map, String featureName, 
   			Object... descriptions) {
   		map.put( featureName, createFeatureDescriptionBag(descriptions) );
   	}
   	// end of tools
   	
   	
   	@org.testng.annotations.Test(groups = {"fast"})
   	public void pig_ANDs_exec_0() throws Exception {
   		// Test whether extractors has changed
   		DisambiguationExtractorFactory factory = new DisambiguationExtractorFactory();
  		String COAUTH = factory.convertExNameToId("EX_DOC_AUTHS_SNAMES");
  		String CC = factory.convertExNameToId("EX_CLASSIFICATION_CODES");
  		String KP = factory.convertExNameToId("EX_KEYWORDS_SPLIT");
  		String KW = factory.convertExNameToId("EX_KEYWORDS");
  		
  		assert( COAUTH != null && CC != null && KP != null && KW != null );
  		
  		// Creating example input
   		List<Tuple> contribs = new ArrayList<Tuple>();
   		
   		// contrib#0
   		Map<String,DataBag>map0 = new HashMap<String,DataBag>();
   		addFeatureToMap(map0, COAUTH, 1, 2, 3);
   		addFeatureToMap(map0, CC, 1, 2, 3); //classif codes
   		addFeatureToMap(map0, KP, 1, 2, 3); //key phrase
   		addFeatureToMap(map0, KW, 1, 2, 3); //key words
   		contribs.add( contribCreator("0", 0, map0) );
   		
   		// contrib#1
   		contribs.add( contribCreator("1", 1, map0) );

   		// contrib#2
   		Map<String,DataBag>map2 = new HashMap<String,DataBag>();
   		addFeatureToMap(map2, COAUTH, 1, 2, 3);
   		addFeatureToMap(map2, CC, 4, 5, 6); //classif codes
   		addFeatureToMap(map2, KP, 1, 2, 3); //key phrase
   		addFeatureToMap(map2, KW, 1, 2, 3); //key words
   		contribs.add( contribCreator("2", 2, map2) );
   		
   		// contrib#3
   		Map<String,DataBag>map3 = new HashMap<String,DataBag>();
   		addFeatureToMap(map3, COAUTH, 7, 8, 9);
   		addFeatureToMap(map3, CC, 7, 8, 9); //classif codes
   		addFeatureToMap(map3, KP, 7, 8, 9); //key phrase
   		addFeatureToMap(map3, KW, 7, 8, 9); //key words  		
   		contribs.add( contribCreator("3", 3, map3) );
   		
   		// contrib#4
   		contribs.add( contribCreator("4", 4, map3) );
   		
   		// contrib#5
   		Map<String,DataBag>map5 = new HashMap<String,DataBag>();
   		addFeatureToMap(map5, COAUTH, 1, 2, 3);
   		addFeatureToMap(map5, CC, 4, 5, 6); //classif codes
   		addFeatureToMap(map5, KP, 7, 8, 9); //key phrase
   		addFeatureToMap(map5, KW, 7, 8, 9); //key words  		
   		contribs.add( contribCreator("5", 5, map5) );
   		
   		/*
   		// contrib#6
   		Map<String,DataBag>map6 = new HashMap<String,DataBag>();
   		addFeatureToMap(map5, COAUTH, 10,11,12);
   		addFeatureToMap(map5, CC, 4, 5, 6); //classif codes
   		addFeatureToMap(map5, KP, 7, 8, 9); //key phrase
   		addFeatureToMap(map5, KW, 7, 8, 9); //key words  		
   		contribs.add( contribCreator(5, 5, map5) );*/
   		
   		Tuple input = new DefaultTuple();
   		DataBag contribsBag = tupleListToDataBag(contribs);
		input.append( contribsBag );

		// APROXIMATE
   		AND<DataBag> aproximate = new AproximateAND_BFS(
   				"-2.0", 
  				"CoAuthorsSnameDisambiguatorFullList#EX_DOC_AUTHS_SNAMES#1#2,IntersectionPerMaxval#EX_CLASSIFICATION_CODES#1#3,IntersectionPerMaxval#EX_KEYWORDS_SPLIT#1#3,IntersectionPerMaxval#EX_KEYWORDS#1#3",
   				"true",
   				"true",
   				"false");
   		
		String apr_out = "{({(0,0,[1#{(1),(2),(3)},0#{(1),(2),(3)},7#{(1),(2),(3)},6#{(1),(2),(3)}]),(1,1,[1#{(1),(2),(3)},0#{(1),(2),(3)},7#{(1),(2),(3)},6#{(1),(2),(3)}]),(2,2,[1#{(4),(5),(6)},0#{(1),(2),(3)},7#{(1),(2),(3)},6#{(1),(2),(3)}])},{(1,0,1.6666666),(2,0,0.6666667)}),({(3,3,[1#{(7),(8),(9)},0#{(7),(8),(9)},7#{(7),(8),(9)},6#{(7),(8),(9)}]),(4,4,[1#{(7),(8),(9)},0#{(7),(8),(9)},7#{(7),(8),(9)},6#{(7),(8),(9)}]),(5,5,[1#{(4),(5),(6)},0#{(1),(2),(3)},7#{(7),(8),(9)},6#{(7),(8),(9)}])},{(1,0,1.6666666),(2,0,0.0)})}";
		assert( aproximate.exec(input).toString().equals( apr_out ) );
   	
   		// EXHAUSTIVE
   		AND<DataBag> exhaustive = new ExhaustiveAND(
   				"-2.0", 
  				"CoAuthorsSnameDisambiguatorFullList#EX_DOC_AUTHS_SNAMES#1#2,IntersectionPerMaxval#EX_CLASSIFICATION_CODES#1#3,IntersectionPerMaxval#EX_KEYWORDS_SPLIT#1#3,IntersectionPerMaxval#EX_KEYWORDS#1#3",
   				"true",
   				"false"
   			);
   		// TODO check whether this out is correct
   		String exh_out = "{(996dc30f-a92a-388d-9392-9ed16588ec72,{(0),(1),(2)}),(04b6c550-264c-39e8-b533-d7f7b977415e,{(3),(4),(5)})}";
   		exhaustive.exec(input).toString().equals(exh_out);
   	}

	@org.testng.annotations.Test(groups = {"fast"})
   	public void pig_aproximateAND_exec_1() throws Exception {
   		AND<DataBag> aproximate = new AproximateAND_BFS(
   				"-0.98025095811271", 
  				"Intersection#EX_PERSON_IDS#inf#1",
   				"false",
   				"false",
   				"false"
   			);
		
  		// Creating example input
   		List<Tuple> contribs = new ArrayList<Tuple>();
   		
   		// contrib#0
   		Map<String,DataBag>map0 = new HashMap<String,DataBag>();
   		addFeatureToMap(map0, "EX_PERSON_IDS", 1, 2);
   		contribs.add( contribCreator("cid-0", "sname-0", map0) );

   		// contrib#1
   		Map<String,DataBag>map1 = new HashMap<String,DataBag>();
   		addFeatureToMap(map1, "EX_PERSON_IDS", 1);
   		contribs.add( contribCreator("cid-1", "sname-0", map1) );

   		// contrib#2
   		Map<String,DataBag>map2 = new HashMap<String,DataBag>();
   		addFeatureToMap(map2, "EX_PERSON_IDS", 3);
   		contribs.add( contribCreator("cid-2", "sname-0", map2) );
   		
   		// contrib#3
   		Map<String,DataBag>map3 = new HashMap<String,DataBag>();
   		addFeatureToMap(map3, "EX_PERSON_IDS", 2, 3);
   		contribs.add( contribCreator("cid-3", "sname-0", map3) );
   		
   		// contrib#4
   		Map<String,DataBag>map4 = new HashMap<String,DataBag>();
   		addFeatureToMap(map4, "EX_PERSON_IDS", 4);
   		contribs.add( contribCreator("cid-4", "sname-0", map4) );
   		
   		Tuple input = new DefaultTuple();
   		DataBag contribsBag = tupleListToDataBag(contribs);
		input.append( contribsBag );
		
		String apr_out = "{({(cid-0,sname-0,[EX_PERSON_IDS#{(1),(2)}]),(cid-1,sname-0,[EX_PERSON_IDS#{(1)}]),(cid-3,sname-0,[EX_PERSON_IDS#{(2),(3)}]),(cid-2,sname-0,[EX_PERSON_IDS#{(3)}])},{}),({(cid-4,sname-0,[EX_PERSON_IDS#{(4)}])},{})}";
   		
		assert( aproximate.exec(input).toString().equals( apr_out ) );
	}
   	
	
	@org.testng.annotations.Test(groups = {"fast"})
   	public void pig_exhaustiveAND_exec_0() throws Exception {
		AND<DataBag> exhaustive = new ExhaustiveAND(
   				"-0.98025095811271", 
  				"Intersection#EX_PERSON_IDS#inf#1",
   				"false",
   				"false"
   			);
		
  		// Creating example input
   		List<Tuple> contribs = new ArrayList<Tuple>();
   		
   		// contrib#0
   		Map<String,DataBag>map0 = new HashMap<String,DataBag>();
   		addFeatureToMap(map0, "EX_PERSON_IDS", 1, 2);
   		contribs.add( contribCreator("cid-0", "sname-0", map0) );

   		// contrib#1
   		Map<String,DataBag>map1 = new HashMap<String,DataBag>();
   		addFeatureToMap(map1, "EX_PERSON_IDS", 1);
   		contribs.add( contribCreator("cid-1", "sname-0", map1) );

   		// contrib#2
   		Map<String,DataBag>map2 = new HashMap<String,DataBag>();
   		addFeatureToMap(map2, "EX_PERSON_IDS", 3);
   		contribs.add( contribCreator("cid-2", "sname-0", map2) );
   		
   		Tuple input = new DefaultTuple();
   		DataBag contribsBag = tupleListToDataBag(contribs);
		input.append( contribsBag );
		
		String exh_out = "{(5db152fb-46d2-3e34-a8e6-bdcc17b4d1bd,{(cid-0),(cid-1)}),(c0e1fcc8-9047-3749-a5fb-de397a278870,{(cid-2)})}";
		System.out.println( exhaustive.exec(input).toString().equals(exh_out) );
	}
}

