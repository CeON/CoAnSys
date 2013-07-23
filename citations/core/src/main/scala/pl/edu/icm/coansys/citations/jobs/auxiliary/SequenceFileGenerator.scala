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
