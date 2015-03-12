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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

@SuppressWarnings("unused")
public class CompleteLinkageHACStrategy_StateOfTheArtTest {

	private static final Logger logger = LoggerFactory
			.getLogger(CompleteLinkageHACStrategy_StateOfTheArtTest.class);

	@Test
	public void emptyTest() {
		float[][] in = null;
		try {
			new CompleteLinkageHACStrategy_StateOfTheArt().clusterize(in);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void oneElementTest() {
		float[][] in = { {} };
		int[] out = TestHelper.clusterDataCLHAC(in);

		String outS = Joiner.on(" ").join(Ints.asList(out));
		Assert.assertEquals("Elements are not correctly assigned", "0", outS);
		System.out.println("One element in one group");
		System.out.println(outS);
		System.out.println("====================");
	}

	@Test
	public void twoSameElementsTest() {
		float[][] in = { {}, { 1 } };
		int[] out = TestHelper.clusterDataCLHAC(in);

		String outS = Joiner.on(" ").join(Ints.asList(out));
		Assert.assertEquals("Elements are not correctly assigned", "0 0", outS);
		System.out.println("Two elements in the same group");
		System.out.println(outS);
		System.out.println("====================");
	}

	@Test
	public void twoNotSameElementsTest() {
		float[][] in = { {}, { -1 } };
		int[] out = TestHelper.clusterDataCLHAC(in);

		String outS = Joiner.on(" ").join(Ints.asList(out));
		Assert.assertEquals("Elements are not correctly assigned", "0 1", outS);
		System.out.println("Two elements in the saparate groups");
		System.out.println(outS);
		System.out.println("====================");
	}

	@Test
	public void diffTest() {
		float[][] in = { {}, { -1 }, { -1, -1 }, { -1, -1, -1 },
				{ -1, -1, -1, -1 } };
		int[] out = TestHelper.clusterDataCLHAC(in);

		String outS = Joiner.on(" ").join(Ints.asList(out));
		Assert.assertEquals("Elements are not correctly assigned", "0 1 2 3 4",
				outS);
		System.out.println("Five elements in different groups");
		System.out.println(outS);
		System.out.println("====================");
	}

	@Test
	public void allTheSameTest() {
		float[][] in = { {}, { 1 }, { 1, 1 }, { 1, 1, 1 }, { 1, 1, 1, 1 } };
		int[] out = TestHelper.clusterDataCLHAC(in);

		String outS = Joiner.on(" ").join(Ints.asList(out));
		Assert.assertEquals("Elements are not correctly assigned", "4 4 4 4 4",
				outS);
		System.out.println("Five objects in the same group");
		System.out.println(outS);
		System.out.println("====================");
	}

	@Test
	public void smallExampleTest() {
		float[][] in = { {}, { 15 }, { -46, -3 }, { -2, -18, -20 },
				{ -100, -100, -3, -200 } };
		int[] out = TestHelper.clusterDataCLHAC(in);

		String outS = Joiner.on(" ").join(Ints.asList(out));
		Assert.assertEquals("Elements are not correctly assigned", "0 0 2 3 4",
				outS);
		System.out.println("Four separate groups");
		System.out.println(outS);
		System.out.println("====================");
	}

	@Test
	public void oneCluster() {
		float[][] in = { {}, { 15 }, { 15, 15 } };
		int[] out = TestHelper.clusterDataCLHAC(in);
		String outS = Joiner.on(" ").join(Ints.asList(out));
		System.out.println("Three elements in the same group");
		System.out.println(outS);
		System.out.println("====================");
	}

	@Test
	public void oneCluster2() {
		float[][] in = { {}, { -1 }, { -1, -1 } };
		int[] out = TestHelper.clusterDataCLHAC(in);
		String outS = Joiner.on(" ").join(Ints.asList(out));
		System.out.println("Three elements in different groups");
		System.out.println(outS);
		System.out.println("====================");
	}

	@Test
	public void mtcarsDistTest() throws Exception {
		float[][] in = TestHelper.readResourceToFloatArray("mtcars.dist.csv");
		// float[][] in = readResourceToFloatArray("eurodist.dist.csv");
		// Instances data = readResourceToInstances("mtcars.base.csv");

		// for(float[] line : in){
		// for(float n : line){
		// System.out.print(n+";");
		// }
		// System.out.println();
		// }

		int[] out = null;

		// System.out.println("======== is now ==========");
		out = TestHelper.clusterDataCLHAC(in);
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
