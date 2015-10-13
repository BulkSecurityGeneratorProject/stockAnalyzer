package com.ichoice.stockanalyzer.dataRetriever.historical;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class YahooHistoricRetriever {
    // http://ichart.finance.yahoo.com/table.csv?g=d&ignore=.csv&s=A&d=11&e=31&f=1969&a=9&b=12&c=2015
    // http://ichart.finance.yahoo.com/table.csv?d=9&e=12&f=2015&g=d&a=11&b=31&c=1969&ignore=.csv&s=A
    // http://ichart.finance.yahoo.com/table.csv?d=6&e=1&f=2009&g=d&a=7&b=19&c=2004&ignore=.csv&s=A

    private static final String URL_PREFIX = "http://ichart.finance.yahoo.com/x?y=0&z=30000";

    private static final String PARAM_START_DATE_MONTH = "a";
    private static final String PARAM_START_DATE_DAY   = "b";
    private static final String PARAM_START_DATE_YEAR  = "c";
    private static final String PARAM_END_DATE_MONTH   = "d";
    private static final String PARAM_END_DATE_DAY     = "e";
    private static final String PARAM_END_DATE_YEAR    = "f";
    private static final String PARAM_DATA_TYPE    = "g";
    private static final String PARAM_DATA_TYPE_VALUE_DAILY_PRICE    = "d";
    private static final String PARAM_DATA_TYPE_VALUE_DIVIDEND_AND_SPLIT    = "v";
    private static final String PARAM_IGNORE_WITH_VALUE    = "ignore=.csv";
    private static final String PARAM_SYMBOL    = "s";


    private static final int BUFFER_SIZE = 4096;

    private File     outputDirectory;
    private Calendar startDate;
    private Calendar endDate;

    private List<String> symbols;

    public YahooHistoricRetriever(File outputDirectory, File symbolListFile, Date startDate, Date endDate)
        throws Exception {
        this.outputDirectory = outputDirectory;

        verifyOutputDirectory();
        verifyAndSetDates(startDate, endDate);

        loadSymbolsFromFile(symbolListFile);
    }

    public void retrieveData() throws IOException {
        String url;
        for (String symbol : symbols) {
            url = buildUrl(symbol, PARAM_DATA_TYPE_VALUE_DAILY_PRICE);
            saveDataFromUrl(url, symbol + ".dailyPrice");

            url = buildUrl(symbol, PARAM_DATA_TYPE_VALUE_DIVIDEND_AND_SPLIT);
            saveDataFromUrl(url, symbol + ".dividendAndSplits");
        }
    }

    private void loadSymbolsFromFile(File symbolListFile) throws IOException {
        symbols = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(symbolListFile))) {
            String line;
            while ((line = br.readLine())!=null) {
                line = line.trim();
                if (line.length()>0)
                    symbols.add(line);
            }
        }
    }

    private void verifyAndSetDates(Date startDate, Date endDate) throws Exception {
        if (startDate==null) {
            startDate = new Date(0);
        }

        if (endDate == null) {
            endDate = new Date();
        }

        if (startDate.after(endDate)) {
            throw new Exception("start date " + startDate + " is after end date " + endDate);
        }

        this.startDate = calendarFromDate(startDate);
        this.endDate = calendarFromDate(endDate);
    }

    private Calendar calendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private void verifyOutputDirectory() throws Exception {
        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdirs()) {
                throw new Exception("output directory '" + outputDirectory + "' doesn't exist and can not be created");
            }
        }
        else if (!outputDirectory.isDirectory()) {
            throw new Exception("output directory '" + outputDirectory + "' invalid : not a directory");
        }
        else if (!outputDirectory.canWrite()) {
            throw new Exception("output directory '" + outputDirectory + "' invalid : can not write to");
        }
    }

    private void saveDataFromUrl(String url, String symbol) throws IOException {
        System.out.println("saveDateFromUrl(" + url + ") for symbol : " + symbol);
        URL urlObject = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) urlObject.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = symbol + ".csv";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = outputDirectory.getAbsolutePath() + File.separator + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }

    private String buildUrl(String symbol, String dataTypeValue) {
        // http://ichart.finance.yahoo.com/table.csv?d=9&e=12&f=2015&g=d&a=11&b=31&c=1969&ignore=.csv&s=A

        return URL_PREFIX +
            PARAM_END_DATE_MONTH + "=" + endDate.get(Calendar.MONTH) +
            "&" + PARAM_END_DATE_DAY + "=" + endDate.get(Calendar.DAY_OF_MONTH) +
            "&" + PARAM_END_DATE_YEAR + "=" + endDate.get(Calendar.YEAR) +

            "&" + PARAM_DATA_TYPE + "=" + dataTypeValue +

            "&" + PARAM_START_DATE_MONTH + "=" + startDate.get(Calendar.MONTH) +
            "&" + PARAM_START_DATE_DAY + "=" + startDate.get(Calendar.DAY_OF_MONTH) +
            "&" + PARAM_START_DATE_YEAR + "=" + startDate.get(Calendar.YEAR) +

            "&" + PARAM_IGNORE_WITH_VALUE +

            "&" + PARAM_SYMBOL + "=" + symbol;
    }

    public static void main(String[] args) throws Exception {
        YahooHistoricRetriever retriever = new YahooHistoricRetriever(new File(args[0]), new File(args[1]),
            new Date(0), new Date());
        retriever.retrieveData();
    }
}
