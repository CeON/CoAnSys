package pl.edu.icm.coansys.citations.util.classification.features

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class FeatureVectorBuilder[A](calculators: List[FeatureCalculator[A]]) {
  def calculateFeatureVectorValues(obj: A): Array[Double] =
    calculators.map(_.calculateValue(obj)).toArray
}
