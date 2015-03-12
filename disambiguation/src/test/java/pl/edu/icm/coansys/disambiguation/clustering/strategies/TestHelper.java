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

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

public class TestHelper {
	public static float[][] readResourceToFloatArray(String path)
			throws IOException {
		InputStream is = CompleteLinkageHACStrategy_StateOfTheArtTest.class
				.getClassLoader().getResourceAsStream(path);
		String indata = IOUtils.toString(is);

		float maxFloat = Float.MIN_VALUE;
		String[] testLines = indata.split("\n");
		for (String s : testLines) {
			String[] numbers = s.split(",");
			for (String n : numbers) {
				float f = Float.parseFloat(n);
				maxFloat = Math.max(maxFloat, f);
			}
		}

		// System.out.println("============ "+maxFloat);

		String[] lines = indata.split("\n");
		float[][] in = new float[lines.length][];
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			String[] values = line.split(",");

			float[] distLine = new float[i];
			for (int j = 0; j < i; j++) {
				distLine[j] = /* maxFloat- */Float.parseFloat(values[j]);
			}
			in[i] = distLine;
		}
		return in;
	}

	public static void printClusters(int[] out, int length) {
		System.out.println("Pairs <clusterId, [item]>");
		int[][] split = splitIntoClusters(out, length);
		int j = 0;
		for (int i = 0; i < split.length; i++) {
			if (split[i] == null) {
				continue;
			}
			System.out.println(j + ": "
					+ Joiner.on(" ").join(Ints.asList(split[i])));
			j++;
		}
	}

	public static int[][] splitIntoClusters(int[] clusterAssociation, int N) {
		// cluster[ cluster id ] = array with contributors' simIds
		int[][] clusters = new int[N][];
		int[] index = new int[N];
		int[] clusterSize = new int[N];
		assert (clusterAssociation.length == N);

		// preparing clusters' sizes
		for (int i = 0; i < N; i++) {
			clusterSize[clusterAssociation[i]]++;
		}
		// reserving memory
		for (int i = 0; i < N; i++) {
			if (clusterSize[i] > 0) {
				clusters[i] = new int[clusterSize[i]];
			} else {
				clusters[i] = null;
			}

			index[i] = 0;
		}
		// filling clusters
		int id;
		for (int i = 0; i < N; i++) {
			id = clusterAssociation[i];
			clusters[id][index[id]] = i;
			index[id]++;
		}
		return clusters;
	}

	public static int[] clusterDataCLHAC(float[][] in) {
		int[] out = null;
		try {
			out = new CompleteLinkageHACStrategy_StateOfTheArt().clusterize(in);
		} catch (Exception e) {
			System.out.println(StackTraceExtractor.getStackTrace(e));
			Assert.fail();
		}
		return out;
	}

	public static int[] clusterDataSLHAC_LTZ(float[][] in) {
		int[] out = null;
		try {
			out = new SingleLinkageHACStrategy_LessThenZero_Or_Max()
					.clusterize(in);
		} catch (Exception e) {
			System.out.println(StackTraceExtractor.getStackTrace(e));
			Assert.fail();
		}
		return out;
	}

	public static int[] clusterDataSLHAC_OM(float[][] in) {
		int[] out = null;
		try {
			out = new SingleLinkageHACStrategy_OnlyMax().clusterize(in);
		} catch (Exception e) {
			System.out.println(StackTraceExtractor.getStackTrace(e));
			Assert.fail();
		}
		return out;
	}
}
