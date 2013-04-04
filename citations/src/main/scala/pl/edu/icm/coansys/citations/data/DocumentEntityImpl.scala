/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import collection.JavaConversions._

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata
import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover.removeDiacritics

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DocumentEntityImplOld(meta: DocumentMetadata) extends DocumentEntityOld {
  def author = {
    val authors = meta.getBasicMetadata.getAuthorList
    val authorsNames = authors.map(a => if (a.hasName) a.getName else a.getForenames + " " + a.getSurname)

    removeDiacritics(authorsNames.mkString(", "))
  }

  def source = removeDiacritics(meta.getBasicMetadata.getJournal)

  def title = removeDiacritics(meta.getBasicMetadata.getTitleList.map(_.getText).mkString(" "))

  def pages = meta.getBasicMetadata.getPages

  def year = meta.getBasicMetadata.getYear

  def extId = meta.getExtId(0).getValue

  def docId = meta.getKey

  def toBytes = meta.toByteArray
}
