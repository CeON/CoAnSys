/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.util.{scoobi, NoOpClose}
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.cermine.bibref.{BibReferenceParser, CRFBibReferenceParser}
import pl.edu.icm.cermine.bibref.model.BibEntry

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object ReferencesToEntitiesConverter extends ScoobiApp {
  scoobi.addDistCacheJarsToConfiguration(configuration)

  def run() {
    var parser: (()=> BibReferenceParser[BibEntry] with NoOpClose) = null
    var inUri: String = null
    var outUri: String = null
    if(args(0) == "-model") {
      parser = () => new CRFBibReferenceParser(args(1)) with NoOpClose
      inUri = args(2)
      outUri = args(3)
    }
    else {
      parser = () => new CRFBibReferenceParser(
        this.getClass.getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf-small.ser.gz")) with NoOpClose
      inUri = args(0)
      outUri = args(1)
    }

    val entities = convertFromSequenceFile[String, String](inUri)
      .flatMapWithResource(parser()) {
      case (the_parser, (id, text)) if !text.isEmpty =>
        Some(id, MatchableEntity.fromUnparsedReference(the_parser, id, text))
      case _ =>
        None
    }

    persist(convertToSequenceFile(entities, outUri))
  }
}
