package pl.edu.icm.coansys.citations

import java.io.{DataOutput, DataInput}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class MockDocumentWrapper(var id: String, var authors: String) extends DocumentWrapper {
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
