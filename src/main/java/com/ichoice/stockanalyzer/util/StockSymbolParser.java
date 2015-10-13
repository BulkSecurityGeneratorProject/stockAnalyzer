package com.ichoice.stockanalyzer.util;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.CharMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * utility to convert yahoo ticker csv file into sql file
 */
public class StockSymbolParser {
    private static final Logger logger = LoggerFactory.getLogger(StockSymbolParser.class);

    Path inputPath, outputPath;

    public StockSymbolParser(Path inputPath, Path outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public static void main(String[] args) throws IOException {
        if (args==null) {
            logger.error("argument list is null");
        }
        else if (args.length!=2) {
            logger.error("argument list should be length of 2");
        }
        else {
            logger.info("input file: " + args[0]);
            logger.info("output file: " + args[1]);

            StockSymbolParser parser = new StockSymbolParser(Paths.get(args[0]), Paths.get(args[1]));
            parser.parse();

        }


        System.exit(0);
    }

    private void parse() throws IOException {


        try (BufferedReader br = Files.newBufferedReader(inputPath);
             BufferedWriter bw = Files.newBufferedWriter(outputPath, StandardOpenOption.CREATE)) {

            // remove first line
            br.readLine();

            List<StockInfoLine> stockInfos = new ArrayList<>();
            Map<String, Integer> categoryMap = new HashMap<>();
            Map<String, Integer> exchangeMap = new HashMap<>();

            readLines(br, stockInfos, categoryMap, exchangeMap);


            writeCategories(categoryMap, bw);

            writeExchanges(exchangeMap, bw);

            writeStockInfo(stockInfos, categoryMap, exchangeMap, bw);
        }
    }

    private void readLines(BufferedReader br, List<StockInfoLine> stockInfos,
                           Map<String, Integer> categoryMap, Map<String, Integer> exchangeMap) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(StockInfoLine.class);
        ObjectReader objectReader = mapper.reader(StockInfoLine.class).with(schema);

        String thisLine;

        int categoryIndex = 1;
        int exchangeIndex = 1;

        logger.info("reading csv file");
        int errorCount = 0;
        while ((thisLine = br.readLine())!=null) {
            if (!CharMatcher.ASCII.matchesAllOf(thisLine)) {
                logger.warn("line contain non-ascii character : {}", thisLine);
            }

            try {
                StockInfoLine stockInfoLine = objectReader.readValue(thisLine);
                stockInfos.add(stockInfoLine);
                if (!categoryMap.containsKey(stockInfoLine.categoryName)) {
                    categoryMap.put(stockInfoLine.categoryName, categoryIndex++);
                }
                if (!exchangeMap.containsKey(stockInfoLine.exchange)) {
                    exchangeMap.put(stockInfoLine.exchange, exchangeIndex++);
                }
            } catch (IOException e) {
                logger.error("unable to read value from this line: \n" + thisLine, e);
                errorCount++;
            }
        }

        logger.info("all lines read, object parsed : {}", stockInfos.size());

        if (errorCount>0) {
            logger.error("error count : {}, exit now!", errorCount);
            System.exit(1);
        }

    }

    private void writeStockInfo(List<StockInfoLine> stockInfos, Map<String, Integer> categoryMap,
                                Map<String, Integer> exchangeMap, BufferedWriter bw) throws IOException {


        bw.write("insert into stock_info(symbol, company_name, category_id, exchange_id) values");

        int index = 0;
        int total = stockInfos.size();
        int valuePerLine = 8;

        logger.info("writing {} stock info records", total);

        for (StockInfoLine sil : stockInfos) {
            if (index++ > 0) bw.write(", ");
            bw.write("('" + sil.getTicker().replace("'", "''") +
                "', '" + sil.getCompanyName().replace("'", "''") +
                "', " + categoryMap.get(sil.getCategoryName()) +
                ", " + exchangeMap.get(sil.getExchange()) +
                ")");
            if (index==total) {
                bw.write(";\n\n\n");
            }
            if (index % valuePerLine == 0) {
                bw.write("\n");
            }
        }
    }

    private void writeExchanges(Map<String, Integer> exchangeMap, BufferedWriter bw) throws IOException {
        bw.write("insert into stock_exchange(id, name) values");

        int index = 0;
        int total = exchangeMap.size();
        int valuePerLine = 10;

        logger.info("writing {} exchange records", total);

        for (Map.Entry<String, Integer> entry : exchangeMap.entrySet()) {
            if (index++ > 0) bw.write(" ,");
            bw.write("(" + entry.getValue() + ", '" + entry.getKey().replace("'", "''") + "')");
            if (index==total) {
                bw.write(";\n\n\n");
            }
            if (index % valuePerLine == 0) {
                bw.write("\n");
            }
        }

    }

    private void writeCategories(Map<String, Integer> categoryMap, BufferedWriter bw) throws IOException {
        bw.write("insert into stock_category(id, name) values");

        int index = 0;
        int total = categoryMap.size();
        int valuePerLine = 10;

        logger.info("writing {} stock category records", total);

        for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
            if (index++ > 0) bw.write(" ,");
            bw.write("(" + entry.getValue() + ", '" + entry.getKey().replace("'", "''") + "')");
            if (index==total) {
                bw.write(";\n\n\n");
            }
            if (index % valuePerLine == 0) {
                bw.write("\n");
            }
        }
    }
}

