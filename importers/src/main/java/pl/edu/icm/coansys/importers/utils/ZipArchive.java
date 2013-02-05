/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author Artur Czeczko a.czeczko@icm.edu.pl
 *
 * Class which indexes a ZIP archive;
 * provides file filtering by name (using regular expression);
 * gives access to file as InputStream
 *
 */
public class ZipArchive {

    private ZipFile zipFile;
    private Enumeration<? extends ZipEntry> entries;
    private Map<String, ZipEntry> filesMap = new HashMap<String, ZipEntry>();

    public ZipArchive(String zipFilePath) throws IOException {
        zipFile = new ZipFile(zipFilePath);
        entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                filesMap.put(entry.getName(), entry);
            }
        }
    }

    public List<String> filter(String regExp) {
        List<String> resultList = new ArrayList<String>();
        for (String path : filesMap.keySet()) {
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(path);
            if (matcher.matches()) {
                resultList.add(path);
            }
        }
        return resultList;
    }

    public List<String> listFiles() {
        return new ArrayList(filesMap.keySet());
    }

    public InputStream getFileAsInputStream(String path) throws IOException {
        ZipEntry entry = filesMap.get(path);
        if (entry != null) {
            return zipFile.getInputStream(entry);
        }
        return null;
    }
    
    public String getZipFilePath() {
        return (zipFile != null ? zipFile.getName() : null);
    }
}
