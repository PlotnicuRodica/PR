package sample;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TServer extends Thread {
    private Socket socket = null;
    String username = null;
    String password = null;

    public TServer(Socket socket){
        this.socket = socket;
    }


    public void run() {
        InputStream inp;
        BufferedReader brinp;
        PrintWriter out;
        FileWriter fileWriter;
        boolean isLoggedIn = false;
        boolean isCounted = false;
        boolean isBought = false;
        int counter;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            return;
        }
        String line;
        outer:
        while (true) {
            try {
                line = brinp.readLine();
                if (line.equalsIgnoreCase("quit")) {
                    socket.close();
                    System.out.println("Connection closed for " + this.getName());
                    return;
                } else {
                    switch (line){
                        case "register":
                            if(isLoggedIn){
                                out.println("You are already logged in! Please close the connection to create an other account!");
                                continue outer;
                            } else {
                                out.println("Input username:");
                                username = brinp.readLine();
                                out.println("Input password: ");
                                password = brinp.readLine();
                                out.println("Input password again:");
                                String confirmPassword = brinp.readLine();
                                if(password.equals(confirmPassword)){
                                    try {
                                        fileWriter = new FileWriter("users.txt", true);
                                        fileWriter.append(username).append(" ").append(password).append("\n");
                                        out.println("User with username " + username + " was successfully created!");
                                        fileWriter.close();
                                    } catch (IOException e) {
                                        out.println("Couldn't register. Please try again later.");
                                        e.printStackTrace();
                                    }
                                    continue outer;
                                } else {
                                    out.println("Passwords don't match! Please try again.");
                                    continue outer;
                                }
                            }
                        case "login":
                            out.println("Input username");
                            username = brinp.readLine();
                            out.println("Input password");
                            password = brinp.readLine();
                            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
                            String userLine;
                            boolean match = false;
                            while ((userLine = br.readLine()) != null){
                                if (userLine.equals(username + " " + password)){
                                    match = true;
                                }
                            }
                            br.close();
                            if (match){
                                out.println("Welcome " + username + "!");
                                isLoggedIn = true;
                                isCounted = false;
                                isBought = false;
                                continue outer;
                            }else{
                                out.println("Incorrect username or password. Please try again!");
                                continue outer;
                            }

                        case "commands":
                            out.println("This server supports the following commands: ");
                            out.println("register - used for registration");
                            out.println("login  - used to login");
                            out.println("list - list all the available devices");
                            out.println("buy - buy the device");
                            out.println("stat - check number of available devices");
                            out.println("list-mine - list your bought devices");
                            out.println("cancel-mine - return back the device that you bought");
                            continue outer;
                        case "list":
                            if(isLoggedIn && isCounted){
                                br = new BufferedReader(new FileReader("smartphones.txt"));
                                String deviceLine;
                                while ((deviceLine = br.readLine()) != null){
                                    out.println(deviceLine);
                                }
                                br.close();
                                continue outer;
                            } else {
                                out.println("In order to see the list of devices, please login or register first.");
                                continue outer;
                            }
                        case "stat":
                            if(isLoggedIn){
                                counter = numberDevices();
                                out.println( counter + " devices available right now.");
                                isCounted = true;
                                continue outer;
                            } else {
                                out.println("In order to see the number of devices, please login or register first.");
                                continue outer;
                            }
                        case "buy":
                            if(isLoggedIn && isCounted && !isBought){
                                out.println("Input the index of the device to buy.");
                                String response = brinp.readLine();
                                if(response.matches("^[+-]?\\d+$")){
                                    System.out.println("String " + response);
                                    int number = Integer.parseInt(response);
                                    System.out.println("Number " + number);
                                    String device = Files.readAllLines(Paths.get("smartphones.txt")).get(number-1);
                                    fileWriter = new FileWriter(username + "_bought.txt", true);
                                    fileWriter.append(device).append("\n");
                                    fileWriter.close();
                                    removeLineFromFile("smartphones.txt", device);
                                    out.println("You have bought the device number " + response);
                                    isBought = true;
                                    continue outer;
                                } else {
                                    out.println("Bad index, try again!");
                                    continue  outer;
                                }
                            } else {
                                if(!isCounted){
                                    out.println("Don't buy devices before you see them! List them first!");
                                } else if(isBought) {
                                    out.println("You have already bought enough devices for today!");
                                } else  {
                                    out.println("In order to see your devices, please login or register first.");
                                }
                                continue outer;
                            }
                        case "list-mine":
                            if(isLoggedIn){
                                File f = new File("D:\\Univer\\PR\\exemple\\PR\\laborator_6\\pr_6");
                                File[] found = f.listFiles((dir, name) -> name.startsWith(username) && name.endsWith("_bought.txt"));
                                if((found != null ? found.length : 0) > 0){
                                    br = new BufferedReader(new FileReader(found[0]));
                                    String bLine = br.readLine();
                                    out.println(bLine);
                                    isBought = true;
                                    br.close();
                                } else {
                                    out.println("No devices bought!");
                                }
                                continue outer;
                            } else {
                                out.println("In order to see your devices, please login or register first.");
                                continue outer;
                            }
                        case "cancel-mine":
                            if(isLoggedIn){
                                removeBought(username + "_bought.txt");
                                if(!new File(username+"_bought.txt").isFile()){
                                    out.println("You have successfully canceled your order!");
                                    isBought = false;
                                    continue outer;
                                } else {
                                    out.println("Couldn't cancel your order. Try again later!");
                                    continue outer;
                                }
                            } else {
                                out.print("In order to cancel your devices, you need to login first!");
                                continue outer;
                            }

                        default:
                            out.println("Unrecognized command. Please try again.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private static int numberDevices(){
        BufferedReader br = null;
        int counter = 0;
        try {
            br = new BufferedReader(new FileReader("smartphones.txt"));
            String deviceLine;
            while ((deviceLine = br.readLine()) != null){
                counter++;
            }
             br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return counter;
    }

    private static void removeLineFromFile(String file, String lineToRemove){

        try {
            File inFile = new File(file);
            if(!inFile.isFile()){
                System.out.println("Its not a file!");
                return;
            }
            File temp = new File(inFile.getAbsolutePath() + "tmp");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(temp), true);

            String line;
            while ((line = br.readLine()) != null){
                if(!line.equals(lineToRemove)){
                    pw.println(line);
                }
            }
            pw.close();
            br.close();

            if(!inFile.delete()){
                System.out.println("Couldn't delete the file!");
                return;
            }
            if(!temp.renameTo(inFile)){
                System.out.println("Couldn't rename the file");
                return;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeBought(String boughtFile){
        try {
            BufferedReader br = new BufferedReader(new FileReader(boughtFile));
            String line = br.readLine();
            PrintWriter pr = new PrintWriter(new FileWriter("smartphones.txt", true));
            pr.append(line).append("\n");
            pr.flush();
            File f = new File(boughtFile);
            if(!f.isFile()){
                System.out.println("Its not a file!");
                return;
            }
            br.close();
            pr.close();
            if(f.delete()){
                System.out.println("Successfully deleted!");
            } else {
                System.out.println("Couldn't delete the file!");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
