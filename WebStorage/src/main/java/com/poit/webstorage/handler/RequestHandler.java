package com.poit.webstorage.handler;

import com.poit.webstorage.service.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.HttpStatus;

import java.io.FileNotFoundException;
import java.io.IOException;

public class RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int status = HttpStatus.SC_OK;
        byte[] data = new byte[0];

        System.out.println(exchange.getRequestMethod()
            + " " + exchange.getRequestURI()
            + " " + exchange.getProtocol()
            + " from " + exchange.getRequestHeaders().get("User-name"));

        var path = "C:" + exchange.getRequestURI().getPath().replaceAll("\\/", "\\\\");
        var requestService = new RequestService();
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> {
                    data = requestService.processGet(path);
                }
                case "PUT" -> {
                    data = exchange.getRequestBody().readAllBytes();
                    requestService.processPut(path, data);
                    status = HttpStatus.SC_CREATED;
                }
                case "POST" -> {
                    data = exchange.getRequestBody().readAllBytes();
                    requestService.processPost(path, data);
                }
                case "DELETE" -> {
                    requestService.processDelete(path);
                }
                case "COPY" -> {
                    var destPath = "C:" + new String(exchange.getRequestBody().readAllBytes());
                    requestService.processCopy(path, destPath.replaceAll("\\/", "\\\\"));
                }
                case "MOVE" -> {
                    var destPath = "C:" + new String(exchange.getRequestBody().readAllBytes());
                    requestService.processMove(path, destPath.replaceAll("\\/", "\\\\"));
                }
                default -> {
                    status = HttpStatus.SC_METHOD_NOT_ALLOWED;
                }
            }
        } catch (FileNotFoundException e) {
            status = HttpStatus.SC_NOT_ACCEPTABLE;
        }
        exchange.sendResponseHeaders(status, data.length);

        if (data.length != 0) {
            exchange.getResponseBody().write(data);
        }
    }
}
