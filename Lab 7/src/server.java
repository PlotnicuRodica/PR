import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class server {
    public static void main(String args[]) {
        try {
            DatagramSocket socket = new DatagramSocket(666);

            //buffer to receive incoming data
            byte[] buffer = new byte[6666];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            // Wait for an incoming data
            System.out.println("Server created and working fine.");

            //communication loop
            while (true) {
                socket.receive(incoming);
                byte[] data = incoming.getData();
                String string = new String(data, 0, incoming.getLength());

                System.out.println(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + string);

                string = "OK : " + string;
                DatagramPacket dp = new DatagramPacket(string.getBytes(), string.getBytes().length, incoming.getAddress(), incoming.getPort());
                socket.send(dp);
            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
        }
    }
}