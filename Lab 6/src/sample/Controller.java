package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private String line;
    private BufferedReader bufferedReader;
    private BufferedReader in;
    private PrintWriter out;
    boolean isLoggedIn = false;
    private Socket socket;
    String response;
    ObservableList<String> listDevices = FXCollections.observableArrayList();

    @FXML
    private TextField usernameField;

    @FXML
    private Button listMineButton;

    @FXML
    private Button cancelMine;

    @FXML
    private Button registerButton;

    @FXML
    private ListView<String> listView;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button statButton;

    @FXML
    private Button buyButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Button listButton;

    @FXML
    private Button quitButton;

    @FXML
    private Label loggedLabel;

    @FXML
    void onLogin(ActionEvent event) {
        if(isLoggedIn){
            statusLabel.setText("You are already logged in. Please close the connection to login in a new account.");
            statusLabel.setVisible(true);
        } else {
            if(!usernameField.getText().equals("") && !passwordField.getText().equals("")){
                try {
                    out.println("login");
                    response = in.readLine();
                    System.out.println(response);
                    out.println(usernameField.getText());
                    response = in.readLine();
                    System.out.println(response);
                    out.println(passwordField.getText());
                    response = in.readLine();
                    if(!response.equals("Incorrect username or password. Please try again!")){
                        statusLabel.setText(response);
                        statusLabel.setVisible(true);
                        isLoggedIn = true;
                        loggedLabel.setVisible(true);
                    } else {
                        statusLabel.setText(response);
                        statusLabel.setVisible(true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                statusLabel.setText("Please fill up both fields!");
                statusLabel.setVisible(true);
            }

        }


    }

    @FXML
    void onRegister(ActionEvent event) {
        if(usernameField.getText().equals("") && passwordField.getText().equals("")){
            statusLabel.setText("Please fill up both fields!");
            statusLabel.setVisible(true);
        } else if(isLoggedIn) {
            statusLabel.setText("You are already logged in. QUIT to register a new account.");
            statusLabel.setVisible(true);
        } else {
            try {
                String confirmPass = "";
                TextInputDialog userInput = new TextInputDialog();
                userInput.setTitle("Confirmation");
                userInput.setContentText("Please confirm the password in order to register.");
                Optional<String> result = userInput.showAndWait();
                if(result.isPresent()){
                    confirmPass = result.get();
                }
                if(passwordField.getText().equals(confirmPass)){
                    out.println("register");
                    response = in.readLine();
                    System.out.println(response);

                    out.println(usernameField.getText());
                    response = in.readLine();
                    System.out.println(response);

                    out.println(passwordField.getText());
                    response = in.readLine();
                    System.out.println(response);

                    out.println(confirmPass);
                    response = in.readLine();
                    statusLabel.setText(response);
                    statusLabel.setVisible(true);
                } else {
                    statusLabel.setText("Passwords must be tha same");
                    statusLabel.setVisible(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @FXML
    void onStat(ActionEvent event) {
        if(isLoggedIn){
            try {
                out.println("stat");
                response = in.readLine();
                statusLabel.setText(response);
                statusLabel.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("You have to login first.");
            statusLabel.setText("You have to login first");
            statusLabel.setVisible(true);
        }
    }

    @FXML
    void onList(ActionEvent event) {
        if(isLoggedIn){
            listView.getItems().clear();
            try {
                out.println("stat");
                String number = in.readLine();
                int n = Character.getNumericValue(number.charAt(0));
                out.println("list");
                for (int i = 1; i <= n; i++) {
                    response = in.readLine();
                    System.out.println(i + ". " + response);
                    listDevices.add(response);
                }

                listView.setItems(listDevices);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("You have to login first.");
            statusLabel.setText("You have to login first");
            statusLabel.setVisible(true);
        }
    }

    @FXML
    void onBuy(ActionEvent event) {
        if(isLoggedIn){
            try {
                out.println("buy");
                response = in.readLine();
                System.out.println(response);
                statusLabel.setText(response);
                statusLabel.setVisible(true);
                Integer item = listView.getSelectionModel().getSelectedIndex();
                System.out.println("Index is: " + item);
                if(item >= 0){
                    out.println(item+1);
                    response = in.readLine();
                    statusLabel.setText(response);
                    statusLabel.setVisible(true);
                } else {
                    statusLabel.setText("No device selected!");
                    statusLabel.setVisible(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("You have to login first.");
            statusLabel.setText("You have to login first");
            statusLabel.setVisible(true);
        }
    }

    @FXML
    void onListMine(ActionEvent event) {
       if(isLoggedIn){
           try {
               listView.getItems().clear();
               out.println("list-mine");
               response = in.readLine();
               ObservableList<String> mine = FXCollections.observableArrayList();
               if(!response.equals("No devices bought!")){
                   mine.add(response);
                   listView.setItems(mine);
               } else {
                   statusLabel.setText("No devices bought!");
                   statusLabel.setVisible(true);
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       } else {
           System.out.println("You have to login first.");
           statusLabel.setText("You have to login first");
           statusLabel.setVisible(true);
       }


    }

    @FXML
    void onCancelMine(ActionEvent event) {
        if(isLoggedIn){
            try {
                listView.getItems().clear();
                out.println("cancel-mine");
                response = in.readLine();
                statusLabel.setText(response);
                statusLabel.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("You have to login first.");
            statusLabel.setText("You have to login first");
            statusLabel.setVisible(true);
        }

    }

    @FXML
    void onQuit(ActionEvent event) {
        try {
            in.close();
            out.close();
            bufferedReader.close();
            socket.close();
            System.out.println("Connection closed!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        listView.getItems().clear();
        loggedLabel.setVisible(false);
        isLoggedIn = false;
        initialize(null, null);
        statusLabel.setText("You have logged out successfully.");
        usernameField.setText("");
        passwordField.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            socket = new Socket(address, 4998);
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Client address is: " + address);
            System.out.println("Connection established! Write QUIT to end.");
            System.out.println("Available commands:");
            out.println("commands");
            for (int i = 0; i < 8; i++) {
                response = in.readLine();
                System.out.println(response);
            }

            statusLabel.setText("Connection established!");
            statusLabel.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
