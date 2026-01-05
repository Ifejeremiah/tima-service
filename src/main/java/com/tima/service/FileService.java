package com.tima.service;

import com.tima.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
public class FileService {
    @Value("${FILE.DIRECTORY}")
    private String location;

    public String saveFile(MultipartFile file) {
        validateFile(file);
        Path tempFile = createFile();
        try (BufferedOutputStream stream = new BufferedOutputStream(Files.newOutputStream(tempFile, CREATE_NEW))) {
            stream.write(file.getBytes());
        } catch (IOException error) {
            throw new BadRequestException("Error saving file", error);
        }
        return location + "/" + tempFile.toFile().getName();
    }

    private Path createFile() {
        try {
            Path dir = Files.createDirectories(Paths.get(location));
            return dir.resolve(String.format("file_%s.txt", new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime())));
        } catch (Exception error) {
            throw new BadRequestException("Error creating file", error);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file should not be empty");
        }
    }
}
