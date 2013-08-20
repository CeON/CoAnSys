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

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object SequenceFileGenerator extends ScoobiApp {
  override lazy val upload = false

  def run() {
    val outUri = args(0)
    val pairs = DList(("10", """<?xml version="1.0" encoding="UTF-8"?>
                               |<ref id="bib31">
                               |    <citation citation-type="journal">
                               |        <person-group person-group-type="author">
                               |            <name>
                               |                <surname>aa</surname>
                               |                <given-names>M</given-names>
                               |            </name>
                               |            <name>
                               |                <surname>Virmani</surname>
                               |                <given-names>AK</given-names>
                               |            </name>
                               |            <name>
                               |                <surname>Ashfaq</surname>
                               |                <given-names>R</given-names>
                               |            </name>
                               |            <name>
                               |                <surname>Rogers</surname>
                               |                <given-names>TE</given-names>
                               |            </name>
                               |            <name>
                               |                <surname>Rathi</surname>
                               |                <given-names>A</given-names>
                               |            </name>
                               |            <name>
                               |                <surname>Liu</surname>
                               |                <given-names>Y</given-names>
                               |            </name>
                               |            <name>
                               |                <surname>Kodagoda</surname>
                               |                <given-names>D</given-names>
                               |            </name>
                               |            <name>
                               |                <surname>Cunningham</surname>
                               |                <given-names>HT</given-names>
                               |            </name>
                               |            <name>
                               |                <surname>Gazdar</surname>
                               |                <given-names>AF</given-names>
                               |            </name>
                               |        </person-group>
                               |        <article-title>Development of a sensitive, specific reverse transcriptase polymerase chain reaction-based assay for epithelial tumour cells in effusions</article-title>
                               |        <source>Br J Cancer</source>
                               |        <year>1999</year>
                               |        <volume>79</volume>
                               |        <fpage>416</fpage>
                               |        <lpage>422</lpage>
                               |        <!--PubMed citation query: 'Br J Cancer||79|416||bib31|'-->
                               |        <pub-id pub-id-type="pmid">10027307</pub-id>
                               |    </citation>
                               |</ref>
                               | """.stripMargin),
                               ("20", """<?xml version="1.0" encoding="UTF-8"?>
                               |<ref id="bib31">
                               |    <citation citation-type="journal">
                               |        <person-group person-group-type="author">
                               |            <name>
                               |                <surname>aa</surname>
                               |                <given-names>M</given-names>
                               |            </name>
                               |        </person-group>
                               |        <article-title>Development of a sensitive, specific reverse transcriptase polymerase chain reaction-based assay for epithelial tumour cells in effusions</article-title>
                               |        <source>Br J Cancer</source>
                               |        <year>1999</year>
                               |        <volume>79</volume>
                               |        <fpage>416</fpage>
                               |        <lpage>422</lpage>
                               |        <!--PubMed citation query: 'Br J Cancer||79|416||bib31|'-->
                               |        <pub-id pub-id-type="pmid">10027307</pub-id>
                               |    </citation>
                               |</ref>
                               | """.stripMargin))
    persist(toSequenceFile(pairs, outUri, overwrite = true))
  }
}
