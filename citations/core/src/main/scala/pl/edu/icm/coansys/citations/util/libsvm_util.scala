package pl.edu.icm.coansys.citations.util

import pl.edu.icm.cermine.tools.classification.features.FeatureVector

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object libsvm_util {
  /**
   * Converts a feature vector to the text line in LibSVM format.
   */
  def featureVectorToLibSvmLine(fv: FeatureVector, label: Int): String = {
    val features = (Stream.from(1) zip fv.getFeatures).map {
      case (i, v) => i + ":" + v
    }.mkString(" ")
    label + " " + features
  }
}
