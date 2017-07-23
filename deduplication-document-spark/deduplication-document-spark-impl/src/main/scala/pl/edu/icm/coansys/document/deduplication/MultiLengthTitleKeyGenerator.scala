/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2017 ICM-UW
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
package pl.edu.icm.coansys.document.deduplication
import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils
import pl.edu.icm.coansys.commons.java.StringTools
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata

/**
 * Generator for the keys used in early stage of the document deduplication.
 */
class MultiLengthTitleKeyGenerator(val keySizes: Seq[Int]) {
  def cleanUpString(title: String): String = {
    val normalized = StringTools.normalize(title);
    //seems that normalize removes stopwords, which is wrong, and quite expensive
    //val normalized = StringTools.removeStopWords(StringTools.normalize(title));
    val res = normalized.replaceAll("\\s+", "")
    res
  }

  def generateKeys(title: String): Seq[String] = {
    val ctitle = cleanUpString(title)
    val mlen = keySizes.max
    val longestKey = ctitle.zipWithIndex.filter(_._2 % 2 == 0).map(_._1).take(mlen).mkString
    keySizes.map(keyLength => longestKey.substring(0, Math.min(keyLength, longestKey.size))).distinct
  }

  def generateKeys(document: DocumentMetadata): Seq[String] = {
    val title: String = DocumentWrapperUtils.getMainTitle(document)
    generateKeys(title)
  }
}


object MultiLengthTitleKeyGenerator {
  def generateKeys(document: DocumentMetadata)(keySizes: Seq[Int]): Seq[String] = {
    val generator = new MultiLengthTitleKeyGenerator(keySizes)
    generator.generateKeys(document)
  }
}
