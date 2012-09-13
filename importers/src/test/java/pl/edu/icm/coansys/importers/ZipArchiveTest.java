/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.testng.annotations.*;
import static org.junit.Assert.*;

/**
 *
 * @author Artur Czeczko
 */
public class ZipArchiveTest {

    @Test(groups = {"fast"})
    public void myFastTest() throws IOException {
    }

    @Test(groups = {"medium"})
    public void myMediumTest() throws IOException {
    }

    @Test(groups = {"slow"})
    public void myTest() throws IOException {
        URL zipPath = this.getClass().getClassLoader().getResource("test_archive.zip");
        ZipArchive archive = new ZipArchive(zipPath.getPath());

        assertEquals(3, archive.listFiles().size());
        assertTrue(archive.listFiles().contains("directory/file1.txt"));
        assertEquals(2, archive.filter(".*txt").size());

        BufferedReader br = new BufferedReader(new InputStreamReader(archive.getFileAsInputStream("directory/file2.dat")));
        assertEquals("test file 2", br.readLine());
    }
}
