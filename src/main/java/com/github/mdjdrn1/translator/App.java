package com.github.mdjdrn1.translator;

import com.github.mdjdrn1.translator.exceptions.ParsingError;
import com.github.mdjdrn1.translator.exceptions.WritingFileError;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class App {
    public static void main(String[] args) {
        String inputFilePath = "";
        String outputFilePath = "";
        try {
            inputFilePath = args[0];
            if (!isXlsFile(inputFilePath)) {
                throw new IOException();
            }
            if (args.length > 1) {
                if (!args[1].equalsIgnoreCase("-o")) {
                    throw new IOException();
                }
                outputFilePath = args[2];
            } else {
                outputFilePath = generateOutputFilePath(inputFilePath);
            }
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid parameters!");
            System.exit(1);
        }

        System.out.println("STEP 1/3: Parsing input file to list.");
        List<String> foreignWords = parseXlsToList(inputFilePath);
        System.out.println("STEP 2/3: Translating words list.");
        ConcurrentHashMap<String, String> words = getTranslatedWords(new DikiTranslator(), foreignWords);
        System.out.println("STEP 3/3: Saving output file.");
        createNewFile(words, outputFilePath);
    }

    private static boolean isXlsFile(String filePath) {
        return filePath.matches("([^\\s]+\\.(xls|xlsx)$)");
    }

    private static String generateOutputFilePath(String inputFilePath) {
        return inputFilePath.replace(".xls", "_translated.xls");
    }

    private static List<String> parseXlsToList(String xlsFilePath) {
        List<String> firstRowList = new ArrayList<>();

        try {
            Sheet sheet = getXlsSheet(xlsFilePath);
            for (Row row : sheet) {
                Cell firstCell = row.getCell(0);
                String firstCellValue = firstCell.toString();

                firstRowList.add(firstCellValue);
            }
        } catch (IOException e) {
            throw new ParsingError("Cannot parse xls to list of words.");
        }

        return firstRowList;
    }

    private static Sheet getXlsSheet(String xlsFilePath) throws IOException {
        FileInputStream xlsFile = new FileInputStream(new File(xlsFilePath));
        Workbook workbook = new XSSFWorkbook(xlsFile);
        return workbook.getSheetAt(0);
    }

    private static ConcurrentHashMap<String, String> getTranslatedWords(Translator translator, List<String> originalWords) {
        ConcurrentHashMap<String, String> translatedWords = new ConcurrentHashMap<>();

        originalWords.parallelStream().forEach((word) -> {
            String newWord = translator.translate(word);
            if (newWord == null) {
                newWord = "";
            }
            translatedWords.put(word, newWord);
        });

        return translatedWords;
    }

    private static void createNewFile(Map<String, String> words, String xlsOutputFilePath) {
        try {
            FileOutputStream file = new FileOutputStream(xlsOutputFilePath);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();

            addWordsToSheet(words, sheet);

            workbook.write(file);
            file.flush();
            file.close();
        } catch (IOException e) {
            throw new WritingFileError("Writing new file failed.");
        }
    }

    private static void addWordsToSheet(Map<String, String> words, Sheet sheet) {
        int i = 0;
        int wordsSize = words.size();

        for(Map.Entry<String, String> entry : words.entrySet()) {
            Row currentRow = sheet.createRow(i);
            Cell leftCell = currentRow.createCell(0);
            leftCell.setCellValue(entry.getKey());
            Cell rightCell = currentRow.createCell(1);
            rightCell.setCellValue(entry.getValue());
            ++i;
        }
    }
}
