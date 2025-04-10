package group4.group4.server;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.DaoMobilePhone;
import group4.group4.server.dao.DaoMobilePhoneImpl;
import group4.group4.server.dto.MobilePhone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    static final int PORT_NUMBER = 8080;
    public static void main(String[] args) throws IOException {
        ServerMain server = new ServerMain();
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
             ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());

                fixedThreadPool.submit(new ClientHandler(clientSocket));
            }
        }
    }



}
