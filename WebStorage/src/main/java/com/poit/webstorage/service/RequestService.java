package com.poit.webstorage.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class RequestService {
    public RequestService() {
    }

    public byte[] processGet(String filePath) throws IOException {
        var fileInputStream = new FileInputStream(filePath);
        return fileInputStream.readAllBytes();
    }

    public void processPut(String filePath, byte[] fileContent) throws IOException {
        var fileOutputStream = new FileOutputStream(filePath, false);
        fileOutputStream.write(fileContent);
        fileOutputStream.flush();
    }

    public void processPost(String filePath, byte[] fileContent) throws IOException {
        if (!Files.exists(Path.of(filePath))) {
            throw new FileNotFoundException();
        } else {
            var fileOutputStream = new FileOutputStream(filePath, true);
            fileOutputStream.write(fileContent);
            fileOutputStream.flush();
        }
    }

    public void processDelete(String filePath) {
        var file = new File(filePath);
        file.delete();
    }

    public void processCopy(String filePath, String destPath) throws IOException {

        if (Files.exists(Path.of(destPath)) || Files.exists(Path.of(filePath))) {
            Files.copy(Path.of(filePath), Path.of(destPath), REPLACE_EXISTING);
        } else {
            throw new FileNotFoundException();
        }
    }

    public void processMove(String filePath, String destPath) throws IOException {

        if (Files.exists(Path.of(destPath)) || Files.exists(Path.of(filePath))) {
            Files.move(Path.of(filePath), Path.of(destPath), REPLACE_EXISTING);
        } else {
            throw new FileNotFoundException();
        }
    }
}
