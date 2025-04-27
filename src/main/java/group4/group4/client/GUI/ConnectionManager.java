package group4.group4.client.GUI;

import java.io.*;
import java.net.Socket;

public class ConnectionManager {
    private static ConnectionManager instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private OutputStream outputStream;

    private ConnectionManager() throws IOException {
        socket = new Socket("localhost", 8080);
        outputStream = socket.getOutputStream();
        out = new PrintWriter(outputStream, true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to the server");
    }

    public static ConnectionManager getInstance() throws IOException {
        if (instance == null) instance = new ConnectionManager();
        return instance;
    }

    public Socket getSocket() { return socket; }

    public PrintWriter getOut() { return out; }

    public BufferedReader getIn() { return in; }

    public OutputStream getOutputStream() { return outputStream; }

    public void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
            System.out.println("Connection closed");
        }
        catch (IOException e) { e.printStackTrace(); }
    }
}
