package group4.group4.server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

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
