package com.poit.webstorage.server;

import com.sun.net.httpserver.HttpServer;
import com.poit.webstorage.handler.RequestHandler;

import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {
        try {
            System.out.println("Введите номер порта: ");
            var scanner = new Scanner(System.in);
            HttpServer server = HttpServer.create(new InetSocketAddress(scanner.nextInt()), 5);
            server.createContext("/", new RequestHandler());
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            System.out.println("Сервер запущен");
        } catch (InputMismatchException e) {
            System.out.println("Неправильный ввод");
        } catch (BindException e) {
            System.out.println("Порт уже используется");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}