package pl.edu.icm.coansys.citations.util.classification.features

/**
 * Feature calculator is able to calculate a single feature's value.
 *
 * @author Dominika Tkaczyk (d.tkaczyk@icm.edu.pl)
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
abstract class FeatureCalculator[A] {
  /**
   * Returns the name of the feature that can be calculated by the calculator.
   * Two different feature calculators of the same parameter types should
   * return different feature names.
   */
  def name: String = {
    var className: String = this.getClass.getName
    val classNameParts: Array[String] = className.split("\\.")
    className = classNameParts(classNameParts.length - 1)
    if (className.contains("Feature")) {
      className.replace("Feature", "")
    }
    else {
      className
    }
  }

  /**
   * Calculates the value of a single feature.
   */
  def calculateValue(obj: A): Double
}

