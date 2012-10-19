package pl.edu.icm.coansys.citations

import java.io.{DataOutput, DataInput}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class MockDocumentWrapper extends DocumentWrapper {
  var id: String = ""
  var authors: String = ""

  def this(id: String, authors: String) {
    this()
    this.id = id
    this.authors = authors
  }

  def normalisedAuthorTokens = authors.toLowerCase.split("""\s+""")

  def write(out: DataOutput) {
    out.writeUTF(id)
    out.writeUTF(authors)
  }

  def readFields(in: DataInput) {
    id = in.readUTF()
    authors = in.readUTF()
  }
}
