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

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
public class FileService {
    @Value("${FILE.DIRECTORY}")
    private String location;

    public void saveFile(MultipartFile file, Long id) {
        Path tempFile = createFile(id);
        try (BufferedOutputStream stream = new BufferedOutputStream(Files.newOutputStream(tempFile, CREATE_NEW))) {
            stream.write(file.getBytes());
        } catch (IOException error) {
            throw new BadRequestException("Error saving file", error);
        }
    }

    private Path createFile(Long id) {
        try {
            Path jobInputDir = Files.createDirectories(Paths.get(location, id.toString()));
            return jobInputDir.resolve("file_upload.txt");
        } catch (Exception error) {
            throw new BadRequestException("Error creating file", error);
        }
    }
}
