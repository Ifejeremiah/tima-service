package com.tima.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tima.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}