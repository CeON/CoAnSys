/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.clustering.strategies;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

public class SingleLinkageHACStrategy_LessThenZero_Or_MaxTest {

	private static final Logger logger = LoggerFactory
			.getLogger(SingleLinkageHACStrategy_LessThenZero_Or_MaxTest.class);

	@Test
	public void smallExampleTest() {
		float[][] in = { {}, { 15 }, { -46, -3 }, { -2, -18, -20 },
				{ -100, -100, -3, -200 } };
		int[] out = null;
		try {
			out = new SingleLinkageHACStrategy_OnlyMax().clusterize(in);
		} catch (Exception e) {
			logger.error(StackTraceExtractor.getStackTrace(e));
			Assert.fail();
		}

		StringBuilder sb = new StringBuilder("");
		for (int i : out) {
			sb.append(i).append(" ");
		}
		Assert.assertEquals("Elements are not correctly assigned",
				"1 1 2 3 4 ", sb.toString());
	}

	@Test
	public void mtcarsDistTest() throws Exception {
		float[][] in = TestHelper.readResourceToFloatArray("mtcars.dist.csv");
//		float[][] in = TestHelper.readResourceToFloatArray("eurodist.dist.csv");
		// Instances data = readResourceToInstances("mtcars.base.csv");

		for (float[] line : in) {
			for (float n : line) {
				System.out.print(n + ";");
			}
			System.out.println();
		}

		int[] out = null;

		// System.out.println("======== is now ==========");
		out = TestHelper.clusterDataSLHAC_LTZ(in);
		TestHelper.printClusters(out, in.length);
		// System.out.println("======== weka -- should be ==========");
		// out = wekaTest(data);
		// printClusters(out, in.length);
		// System.out.println("======== hclust@R -- should be ======");
		// out = new int[]{ 1, 1, 1, 2, 3, 2, 3, 1, 1, 1, 1, 2, 2, 2, 4, 4, 4,
		// 5, 5, 5, 1, 2, 2, 3, 3, 5, 1, 1, 3, 6, 7, 1 };
		// printClusters(out, in.length);
	}

}
