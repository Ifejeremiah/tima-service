package com.tima.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tima.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;

@Slf4j
class FileReaderTest {
    Path filePath = Paths.get("src", "test", "resources", "sample.csv");

    @Test
    void readFile() {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            FileReader reader = new FileReader(inputStream);
            for (Question question = new Question(); reader.hasNext(question); ) {
                String result = new ObjectMapper().writeValueAsString(question);
                log.info(result);
            }
        } catch (Exception error) {
            log.error("Error reading file", error);
        }
    }

    @Test
    public void readXlsxFile() {
        ClassPathResource resource = new ClassPathResource("Book.xlsx");
        try (InputStream is = resource.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row dataRow = sheet.getRow(1);
            log.info(dataRow.getCell(0).getStringCellValue());
            LinkedHashSet<String> options = convertStringArrayToSet(dataRow.getCell(1).getStringCellValue());
            for (String option : options) {
                log.info("option: {}", option);
            }
            log.info(dataRow.getCell(2).getStringCellValue());
        } catch (Exception error) {
            log.error("Error reading xlsx file", error);
        }
    }

    private LinkedHashSet<String> convertStringArrayToSet(String fields) {
        String[] optionList = fields.trim().split("\\|");
        return new LinkedHashSet<>(Arrays.asList(optionList));
    }
}