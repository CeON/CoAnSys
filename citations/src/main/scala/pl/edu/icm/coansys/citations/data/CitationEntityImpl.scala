/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import collection.JavaConversions._
import pl.edu.icm.coansys.importers.models.DocumentProtos.ReferenceMetadata

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class CitationEntityImpl(meta: ReferenceMetadata) extends CitationEntity {
  def author = meta.getBasicMetadata.getAuthorList.map(a => if (a.hasName) a.getName else a.getForenames + " " + a.getSurname).mkString(", ")

  def source = meta.getBasicMetadata.getJournal

  def title = meta.getBasicMetadata.getTitleList.map(_.getText).mkString(" ")

  def pages = meta.getBasicMetadata.getPages

  def year = meta.getBasicMetadata.getYear

  def rawText = meta.getRawCitationText

  def sourceDocKey = meta.getSourceDocKey

  def position = meta.getPosition

  def toBytes = meta.toByteArray
}
