package com.tima.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tima.enums.JobStatus;
import com.tima.model.ExcelRow;
import com.tima.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
class FileReaderTest {

    @Test
    void streamLargeFiles() throws Exception {
        ClassPathResource resource = new ClassPathResource("Book.xlsx");
        ExcelParser reader = new ExcelParser();
        List<ExcelRow> rows = reader.read(resource.getInputStream());
        for (ExcelRow row : rows) {
            Question question = new Question();
            if (StringUtils.isEmpty(row.getCol1()) || StringUtils.isEmpty(row.getCol2()) || StringUtils.isEmpty(row.getCol3())) {
                question.setJobStatus(JobStatus.FAILED);
                question.setStatusMessage("Failed to parse record at line " + row.getRowNumber() + ": One or more data not provided. Please correct.");
            }
            question.setQuestion(row.getCol1());
            question.setOptions(row.getCol2());
            question.setAnswer(row.getCol3());
            String result = new ObjectMapper().writeValueAsString(question);
            log.info(result);
        }
    }

    @Test
    void readXlsxFile() {
        ClassPathResource resource = new ClassPathResource("Book.xlsx");
        try (InputStream is = resource.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row dataRow = sheet.getRow(0);
            log.info(dataRow.getCell(0).getStringCellValue());
            LinkedHashSet<String> options = convertStringArrayToSet(dataRow.getCell(1).getStringCellValue());
            for (String option : options) {
                log.info("option: {}", option);
            }
            log.info("{}", dataRow.getCell(2).getNumericCellValue());
        } catch (Exception error) {
            log.error("Error reading xlsx file", error);
        }
    }

    private LinkedHashSet<String> convertStringArrayToSet(String fields) {
        String[] optionList = fields.trim().split("\\|");
        return new LinkedHashSet<>(Arrays.asList(optionList));
    }

    private String convertSetToString(LinkedHashSet<String> fields) {
        return String.join("|", fields);
    }

    @Test
    public void stringManipulation() {
        String fields = "hi|hello";
        LinkedHashSet<String> set = convertStringArrayToSet(fields);
        log.info("{}",set);

        String string = convertSetToString(set);
        log.info(string);
    }
}