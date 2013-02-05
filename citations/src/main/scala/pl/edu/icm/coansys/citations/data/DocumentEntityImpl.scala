/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import collection.JavaConversions._

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DocumentEntityImpl(meta: DocumentMetadata) extends DocumentEntity {
  def author = meta.getBasicMetadata.getAuthorList.map(a => if (a.hasName) a.getName else a.getForenames + " " + a.getSurname).mkString(", ")

  def source = meta.getBasicMetadata.getJournal

  def title = meta.getBasicMetadata.getTitleList.map(_.getText).mkString(" ")

  def pages = meta.getBasicMetadata.getPages

  def year = meta.getBasicMetadata.getYear

  def extId = meta.getExtId(0).getValue
}
