package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final static int PORT = 4998;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Socket created!");
        } catch (IOException e) {
            System.out.println("Socket not created. Shutting down!");
            e.printStackTrace();
        }

        System.out.println("Server is listening...");

        while (true){
            try {
                if (serverSocket != null) {
                    socket = serverSocket.accept();
                }
                System.out.println("Connection accepted!");
                new TServer(socket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
