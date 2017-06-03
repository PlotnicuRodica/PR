package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        Socket socket;
        String line;
        BufferedReader bufferedReader;
        BufferedReader in;
        PrintWriter out;
        boolean isLoggedIn = false;
        socket = new Socket(address, 4998);
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Client address is: " + address);
        System.out.println("Connection established! Write QUIT to end.");
        System.out.println("To see the list of available commands, just type: commands");
        String response;


        line = bufferedReader.readLine();
        while (!line.equalsIgnoreCase("QUIT")){
            if(line.equals("commands")){
                out.println(line);
                for (int i = 0; i < 8; i++) {
                    response = in.readLine();
                    System.out.println("SERVER said: " + response);
                }
                line = bufferedReader.readLine();
            } else if (line.equals("list")){
                if(isLoggedIn){
                    out.println("stat");
                    String number = in.readLine();
                    int n = Character.getNumericValue(number.charAt(0));
                    out.println(line);
                    for (int i = 1; i <= n; i++) {
                        response = in.readLine();
                        System.out.println(i + ". " + response);
                    }
                    line = bufferedReader.readLine();
                } else {
                    System.out.println("You have to login first.");
                    line = bufferedReader.readLine();
                }

            } else if(line.equals("login")){
                out.println(line);
                response = in.readLine();
                System.out.println("SERVER said: " + response);
                if (!response.equals("Incorrect username or password. Please try again!")){
                    isLoggedIn = true;
                }
                line = bufferedReader.readLine();
            } else {
                out.println(line);
                response = in.readLine();
                System.out.println("SERVER said: " + response);
                line = bufferedReader.readLine();
            }
        }


        in.close();
        out.close();
        bufferedReader.close();
        socket.close();
        System.out.println("Connection closed!");
    }

}
