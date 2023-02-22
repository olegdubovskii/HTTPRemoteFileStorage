package com.poit.wsclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class WebClient {

    public static void main(String[] args) throws IOException {
        var scanner = new Scanner(System.in);
        System.out.println("Введите адрес сервера: ");
        var url = scanner.nextLine();

        System.out.println("Введите имя пользователя: ");
        var userName = scanner.nextLine();

        boolean isRunning = true;
        var currentDir = "/Games/ksisthird/WebStorage/src/main/resources/";

        while (isRunning) {
            var closeableHttpClient = HttpClients.createSystem();

            System.out.println("""
                Выберите действие:\s
                1) Выгрузить файл с сервера(GET)
                2) Загрузить/создать файл на сервере(PUT)
                3) Добавить в конец файла(POST)
                4) Удалить файл с сервера(DELETE)
                5) Скопировать файл(COPY)
                6) Переместить файл(MOVE)
                7) Exit
                """);

            int selector = scanner.nextInt();
            scanner.nextLine();

            CloseableHttpResponse response = null;
            switch (selector) {
                case 1 -> {
                    System.out.println("Введите имя файла: ");
                    var fileName = scanner.nextLine();

                    System.out.println("Введите путь назначения: ");
                    var filePath = scanner.nextLine();

                    var httpRequest = RequestBuilder.create("GET")
                            .addHeader("Accept", "*/*")
                            .addHeader("User-agent", "WebStorageClient/1.0")
                            .addHeader("Accept-charset", "utf-8")
                            .addHeader("User-name", userName)
                            .setUri(url + currentDir + fileName)
                            .build();

                    response = closeableHttpClient.execute(httpRequest);
                    if (response.getStatusLine().getReasonPhrase().equals("OK")) {
                        var fileOutputStream = new FileOutputStream(
                                filePath + fileName.replaceAll(".+\\/", "\\\\"));
                        fileOutputStream.write(response.getEntity().getContent().readAllBytes());
                        fileOutputStream.flush();
                    }
                }
                case 2 -> {
                    System.out.println("Введите полный путь к файлу: ");
                    var filePath = scanner.nextLine();

                    System.out.println("Введите имя файла: ");
                    var fileName = scanner.nextLine();

                    if (!fileName.replaceAll(".+(?=\\.\\w+)", "").equals(".txt")) {
                        System.out.println("There should be text file");
                        continue;
                    }

                    var entity = new FileEntity(
                        new File(filePath + fileName),
                        ContentType.DEFAULT_BINARY
                    );

                    var httpRequest = RequestBuilder
                        .create("PUT")
                        .addHeader("Accept", "*/*")
                        .addHeader("User-agent", "WebStorageClient/1.0")
                        .addHeader("Accept-charset", "utf-8")
                        .addHeader("User-name", userName)
                        .setUri(url + currentDir + fileName)
                        .setEntity(entity)
                        .build();

                    response = closeableHttpClient.execute(httpRequest);
                }
                case 3 -> {
                    System.out.println("Введите файл назначения: ");
                    var fileName = scanner.nextLine();

                    System.out.println("Введите полный путь исходного файла: ");
                    var filePath = scanner.nextLine();

                    var entity = new FileEntity(
                        new File(filePath),
                        ContentType.DEFAULT_BINARY
                    );

                    var httpRequest = RequestBuilder
                        .create("POST")
                        .addHeader("Accept", "*/*")
                        .addHeader("User-agent", "WebStorageClient/1.0")
                        .addHeader("Accept-charset", "utf-8")
                        .addHeader("User-name", userName)
                        .setUri(url + currentDir + fileName)
                        .setEntity(entity)
                        .build();

                    response = closeableHttpClient.execute(httpRequest);
                }
                case 4 -> {
                    System.out.println("Введите имя файла: ");
                    var fileName = scanner.nextLine();

                    var httpRequest = RequestBuilder
                        .create("DELETE")
                        .addHeader("Accept", "*/*")
                        .addHeader("User-agent", "WebStorageClient/1.0")
                        .addHeader("Accept-charset", "utf-8")
                        .addHeader("User-name", userName)
                        .setUri(url + currentDir + fileName)
                        .build();

                    response = closeableHttpClient.execute(httpRequest);
                }
                case 5 -> {
                    System.out.println("Введите имя файла: ");
                    var fileName = scanner.nextLine();

                    System.out.println("Введите директорию назначения: ");
                    var destDir = scanner.nextLine();

                    var entity = new StringEntity(currentDir + destDir, ContentType.DEFAULT_TEXT);

                    var httpRequest = RequestBuilder
                        .create("COPY")
                        .addHeader("Accept", "*/*")
                        .addHeader("User-agent", "WebStorageClient/1.0")
                        .addHeader("Accept-charset", "utf-8")
                        .addHeader("User-name", userName)
                        .setUri(url + currentDir + fileName)
                        .setEntity(entity)
                        .build();

                    response = closeableHttpClient.execute(httpRequest);
                }
                case 6 -> {
                    System.out.println("Введите имя файла: ");
                    var fileName = scanner.nextLine();

                    System.out.println("Введите директорию назначения: ");
                    var destDir = scanner.nextLine();

                    var entity = new StringEntity(currentDir + destDir, ContentType.DEFAULT_TEXT);

                    var httpRequest = RequestBuilder
                        .create("MOVE")
                        .addHeader("Accept", "*/*")
                        .addHeader("User-agent", "WebStorageClient/1.0")
                        .addHeader("Accept-charset", "utf-8")
                        .addHeader("User-name", userName)
                        .setUri(url + currentDir + fileName)
                        .setEntity(entity)
                        .build();

                    response = closeableHttpClient.execute(httpRequest);
                }
                case 7 -> {
                    isRunning = false;
                    continue;
                }
                default -> {
                    System.out.println("Неправильный ввод!");
                    continue;
                }
            }
            System.out.println(response.getStatusLine().getReasonPhrase());
            response.close();
            closeableHttpClient.close();
        }
        scanner.close();
        System.out.println("Disconnected");
    }
}
