/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.misc._
import pl.edu.icm.coansys.citations.util.AugmentedDList._
import pl.edu.icm.cermine.bibref.{CRFBibReferenceParser, HMMBibReferenceParser}
import pl.edu.icm.coansys.citations.util.NoOpClose
import java.lang.String
import pl.edu.icm.cermine.tools.classification.hmm.{HMMServiceImpl, HMMService}
import java.io.{FileInputStream, InputStream}
import com.thoughtworks.xstream.XStream
import pl.edu.icm.cermine.bibref.parsing.features._
import pl.edu.icm.cermine.tools.classification.hmm.model.HMMProbabilityInfo
import pl.edu.icm.cermine.bibref.parsing.model.{Citation, CitationToken, CitationTokenLabel}
import pl.edu.icm.cermine.tools.classification.features.{FeatureCalculator, FeatureVectorBuilder}
import java.util

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Parser extends ScoobiApp {

  class MyCrfParser {

  }

  class HMMParser {
    protected final val hmmProbabilitiesFile: String = "/pl/edu/icm/cermine/bibref/hmmCitationProbabilities.xml"
    private var hmmService: HMMService = new HMMServiceImpl
    private var bibReferenceParser: HMMBibReferenceParser = null

    private val is: InputStream = this.getClass.getResourceAsStream(hmmProbabilitiesFile)
    private val xstream: XStream = new XStream
    private var hmmProbabilities: HMMProbabilityInfo[CitationTokenLabel] = null
    try {
      hmmProbabilities = xstream.fromXML(is).asInstanceOf[HMMProbabilityInfo[CitationTokenLabel]]
    }
    finally {
      is.close()
    }
    val vectorBuilder: FeatureVectorBuilder[CitationToken, Citation] = new FeatureVectorBuilder[CitationToken, Citation]
    vectorBuilder.setFeatureCalculators(util.Arrays.asList[FeatureCalculator[CitationToken, Citation]](new DigitRelativeCountFeature, new IsAllDigitsFeature, new IsAllLettersFeature, new IsAllLettersOrDigitsFeature, new IsAllLowercaseFeature, new IsAllRomanDigitsFeature, new IsAllUppercaseFeature, new IsAndFeature, new IsCityFeature, new IsClosingParenthesisFeature, new IsClosingSquareBracketFeature, new IsCommaFeature, new IsCommonPublisherWordFeature, new IsCommonSeriesWordFeature, new IsCommonSourceWordFeature, new IsDashBetweenWordsFeature, new IsDashFeature, new IsDigitFeature, new IsDotFeature, new IsLaquoFeature, new IsLowercaseLetterFeature, new IsOpeningParenthesisFeature, new IsOpeningSquareBracketFeature, new IsQuoteFeature, new IsRaquoFeature, new IsSingleQuoteBetweenWordsFeature, new IsSlashFeature, new IsUppercaseLetterFeature, new IsUppercaseWordFeature, new IsWordAndFeature, new IsWordDeFeature, new IsWordHttpFeature, new IsWordJrFeature, new IsWordLeFeature, new IsNumberTextFeature, new IsPagesTextFeature, new IsWordTheFeature, new IsWordTheoryFeature, new IsCommonSurnamePartFeature, new IsVolumeTextFeature, new IsYearFeature, new LengthFeature, new LetterRelativeCountFeature, new LowercaseRelativeCountFeature, new StartsWithUppercaseFeature, new StartsWithWordMcFeature, new UppercaseRelativeCountFeature))
    bibReferenceParser = new HMMBibReferenceParser(hmmService, hmmProbabilities, vectorBuilder)

    def parseBibReference = bibReferenceParser.parseBibReference _
  }

  override lazy val upload = false

  def run() {
    configuration.set("mapred.max.split.size", 500000)
    configuration.setMinReducers(4)

    //val modelFile = "/pl/edu/icm/cermine/bibref/acrf-small.ser.gz"
    val modelFile = args(0)

    val citations = readCitationsFromDocumentsFromSeqFiles(List(args(1)), null)
    val parsedCitations = citations.
      //mapWithResource(new CRFBibReferenceParser(this.getClass.getResourceAsStream(modelFile)) with NoOpClose) {
      //mapWithResource(new HMMParser with NoOpClose) {
      mapWithResource(new CRFBibReferenceParser(new FileInputStream(modelFile)) with NoOpClose) {
      case (parser, citation) =>
        ""
      //        val parsed = parser.parseBibReference(citation.rawText)
      //        val nlm = new BibEntryToNLMElementConverter().convert(parsed)
      //        new XMLOutputter().outputString(nlm)
    }
    persist(toTextFile(parsedCitations, args(2), overwrite = true))
  }
}
