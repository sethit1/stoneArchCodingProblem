package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            final String CSV_LIST_URL = "https://journeyblobstorage.blob.core.windows.net/sabpublic/list";
            List<String> CsvUrls = getCsvUrls(CSV_LIST_URL);
            Map<String, Integer> zipCodeCounts = getZipCodeCounts(CsvUrls);
            printZipCodeCounts(zipCodeCounts);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<String> getCsvUrls(String urlWithCsvList) throws IOException {
        List<String> CsvUrls = new ArrayList<>();
        BufferedReader bufferedReader = getBufferedReader(urlWithCsvList);
        String row;
        while ((row = bufferedReader.readLine()) != null) {
            CsvUrls.add(row);
        }
        return CsvUrls;
    }

    private static Map<String, Integer> getZipCodeCounts(List<String> CsvUrls) throws IOException {
        Map<String, Integer> zipCodeCount = new HashMap<>();
        for (String CsvUrl : CsvUrls) {
            BufferedReader bufferedReader = getBufferedReader(CsvUrl);
            final int ZIP_CODE_COLUMN = 8;
            String row = bufferedReader.readLine();     //moves past the first title row
            while ((row = bufferedReader.readLine()) != null) {
                String zipCode = row.split(",")[ZIP_CODE_COLUMN];
                if (zipCodeCount.containsKey(zipCode)) {
                    zipCodeCount.put(zipCode, zipCodeCount.get(zipCode) + 1);
                } else {
                    zipCodeCount.put(zipCode, 1);
                }
            }
        }
        return zipCodeCount;
    }

    private static BufferedReader getBufferedReader(String urlInput) throws IOException {
        URL url = new URL(urlInput);
        InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
        return new BufferedReader(inputStreamReader);
    }

    private static void printZipCodeCounts(Map<String, Integer> zipCodeCounts){
        for(Map.Entry<String, Integer> zip : zipCodeCounts.entrySet()){
            System.out.println("Zip-Code: " + zip.getKey() + ", Count: " + zip.getValue());
        }
    }
}