/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.util

import java.io._
import libsvm._


/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SvmClassifier(private val model: svm_model) {
  def predictProbabilities(fv: Array[Double]): Map[Int, Double] = {
    val instance: Array[svm_node] =
      fv.zipWithIndex.map{ case (value, index) =>
        val node = new svm_node()
        node.index = index + 1
        node.value = value
        node
      }
    val probEstimates: Array[Double] = new Array[Double](model.label.length)
    svm.svm_predict_probability(model, instance, probEstimates)
    (model.label zip probEstimates).toMap
  }
}

object SvmClassifier {
  def fromFile(file: File) =
    fromReader(new FileReader(file))

  def fromResource(resource: String) =
    fromReader(new InputStreamReader(getClass.getResourceAsStream(resource)))

  def fromReader(reader: Reader) =
    new SvmClassifier(svm.svm_load_model(new BufferedReader(reader)))
}
