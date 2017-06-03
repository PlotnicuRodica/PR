import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class client {
    public static void main(String args[]) {
        DatagramSocket socket = null;
        int port = 666;
        String string;

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        try {
            socket = new DatagramSocket();
            InetAddress host = InetAddress.getByName("localhost");

            while (true) {
                //take input and send the packet
                System.out.println("Enter message: ");
                string = bufferRead.readLine();
                byte[] bytes = string.getBytes();

                DatagramPacket dp = new DatagramPacket(bytes, bytes.length, host, port);
                socket.send(dp);

                //now receive reply
                //buffer to receive incoming data
                byte[] buffer = new byte[6666];
                DatagramPacket message = new DatagramPacket(buffer, buffer.length);
                socket.receive(message);

                byte[] data = message.getData();
                string = new String(data, 0, message.getLength());

                System.out.println(message.getAddress().getHostAddress() + " : " + message.getPort() + " - " + string);
            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
        }
    }
}