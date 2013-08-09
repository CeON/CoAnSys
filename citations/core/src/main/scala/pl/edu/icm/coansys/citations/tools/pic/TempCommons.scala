package pl.edu.icm.coansys.citations.tools.pic

import pl.edu.icm.coansys.citations.util.{misc, BytesConverter}
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.util.sequencefile.ConvertingSequenceFileIterator
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.commons.scala.automatic_resource_management._
import scala.io.Source
import java.io.File
import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.KeyValue

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object TempCommons {

  def readPicGroundTruth(file: File) = using(Source.fromFile(file)) {
      source =>
        source.getLines().toList.map(_.split(','))
          .map(x => ("cit_" + x(0) + "_" + (x(1).toInt), if (x.length == 3) x(2) else ""))
    }

  private implicit val docConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom)

  def readRawDocumentsDatabase(dbUrl: String) = {
    implicit val strConverter = new BytesConverter[String](misc.uuidEncode, misc.uuidDecode)
    ConvertingSequenceFileIterator.fromLocal[String, DocumentWrapper](dbUrl).toMap
  }

  def readDocumentsDatabase(dbUrl: String) = {
    readRawDocumentsDatabase(dbUrl).mapValues(x => MatchableEntity.fromDocumentMetadata(x.getDocumentMetadata))
  }

  def readParsedCitations(parsedUrl: String, unparsedUrl: String) = {
    val parsedWithoutRaw = ConvertingSequenceFileIterator.fromLocal[String, MatchableEntity](parsedUrl).toMap
    val unparsed = ConvertingSequenceFileIterator.fromLocal[String, String](unparsedUrl).toMap
    parsedWithoutRaw.mapValues {
      ent =>
        val builder = ent.data.toBuilder
        builder.addAuxiliary(KeyValue.newBuilder().setKey("rawText").setValue(unparsed(ent.id)))
        new MatchableEntity(builder.build())
    }
  }

  def readHeuristic(heurUrl: String) = {
    ConvertingSequenceFileIterator.fromLocal[MatchableEntity, String](heurUrl)
      .toList.map {
      case (k, v) => (k.id, v)
    }.groupBy(_._1).mapValues(_.unzip._2.toSet)
  }

  def libSvmFileIterator(source: Source) =
    source.getLines().map { line =>
      val Array(label, features) = line.split(raw"\s+", 2)
      val featureMap = features.split(raw"\s+").map { entry =>
        val Array(index, value) = entry.split(":", 2)
        (index.toInt, value.toDouble)
      }.toMap
      (label, featureMap)
    }


  val stopWords =
    """
      |a
      |about
      |above
      |after
      |again
      |against
      |already
      |also
      |although
      |always
      |among
      |an
      |and
      |another
      |any
      |anyone
      |are
      |as
      |at
      |avec
      |be
      |been
      |before
      |being
      |but
      |by
      |can
      |cannot
      |could
      |d
      |das
      |de
      |dem
      |der
      |des
      |did
      |die
      |do
      |does
      |doing
      |done
      |du
      |due
      |durch
      |ein
      |eine
      |einer
      |eines
      |either
      |else
      |esp
      |etc
      |ever
      |for
      |from
      |further
      |gave
      |get
      |gets
      |give
      |given
      |gives
      |giving
      |got
      |had
      |has
      |have
      |having
      |hence
      |here
      |how
      |however
      |if
      |in
      |instead
      |into
      |is
      |it
      |its
      |itself
      |just
      |la
      |le
      |les
      |made
      |make
      |may
      |might
      |mit
      |more
      |most
      |mostly
      |much
      |must
      |nach
      |neither
      |none
      |nor
      |not
      |now
      |of
      |often
      |on
      |only
      |or
      |other
      |ought
      |our
      |out
      |par
      |same
      |see
      |seen
      |so
      |some
      |sometime
      |somewhat
      |such
      |sur
      |take
      |takes
      |taking
      |than
      |that
      |the
      |their
      |theirs
      |them
      |then
      |there
      |therefore
      |these
      |they
      |this
      |those
      |though
      |through
      |throughout
      |to
      |too
      |took
      |ueber
      |um
      |un
      |une
      |until
      |up
      |upon
      |use
      |used
      |using
      |vol
      |vom
      |von
      |vor
      |was
      |were
      |what
      |when
      |where
      |whether
      |which
      |while
      |who
      |whose
      |why
      |will
      |with
      |within
      |without
      |would
      |yet
      |zu
      |zum
      |zur
    """.stripMargin.split(raw"\s+").toSet
}
