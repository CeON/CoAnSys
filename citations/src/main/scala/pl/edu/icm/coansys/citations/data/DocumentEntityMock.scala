/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DocumentEntityMockOld(_author: String = "",
                            _source: String = "",
                            _title: String = "",
                            _pages: String = "",
                            _year: String = "",
                            _docId: String = "",
                            _extId: String = "") extends DocumentEntityOld {
  def author = _author

  def source = _source

  def title = _title

  def pages = _pages

  def year = _year

  def toBytes = throw new UnsupportedOperationException

  def docId = _docId

  def extId = _extId
}
