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

public class App {
    public static void main(String[] args) {
        // todo: take xlsInputFilePath from args
        String xlsInputFilePath = "in.xlsx";

        List<String> originalWords = parseXlsToList(xlsInputFilePath);
        List<String> translatedWords = getTranslatedWords(new DikiTranslator(), originalWords);

        String xlsOutputFilePath = "out.xlsx";
        createNewFile(originalWords, translatedWords, xlsOutputFilePath);
    }

    public static List<String> parseXlsToList(String xlsFilePath) {
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

    private static List<String> getTranslatedWords(Translator translator, List<String> originalWords) {
        List<String> translatedWords = new ArrayList<>();

        for (String word : originalWords) {
            String newWord = translator.translate(word);
            if (newWord != null) {
                translatedWords.add(newWord);
            } else
                translatedWords.add("");
        }

        return translatedWords;
    }

    private static void createNewFile(List<String> originalWords, List<String> translatedWords, String xlsOutputFilePath) {
        try {
            FileOutputStream file = new FileOutputStream(xlsOutputFilePath);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();

            addWordsToSheet(originalWords, translatedWords, sheet);

            workbook.write(file);
            file.flush();
            file.close();
        } catch (IOException e) {
            throw new WritingFileError("Writing new file failed.");
        }
    }

    private static void addWordsToSheet(List<String> originalWords, List<String> translatedWords, Sheet sheet) {
        for (int i = 0; i < originalWords.size(); ++i) {
            Row currentRow = sheet.createRow(i);
            Cell leftCell = currentRow.createCell(0);
            leftCell.setCellValue(originalWords.get(i));
            Cell rightCell = currentRow.createCell(1);
            rightCell.setCellValue(translatedWords.get(i));
        }
    }
}
