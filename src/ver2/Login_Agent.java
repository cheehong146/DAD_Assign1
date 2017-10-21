package ver2;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.TreeMap;

/**
 * Created by User on 20/10/2017.
 */
public class Login_Agent extends Application {
    Label lblUsername;
    Label lblPassword;
    TextField tfUsername;
    TextField tfPassword;
    Button btnLogin;

    @Override
    public void start(Stage primaryStage) throws Exception {


        lblUsername = new Label("Username: ");
        lblPassword = new Label("Password: ");
        tfUsername = new TextField();
        tfPassword = new PasswordField();
        btnLogin = new Button("LOGIN");

        GridPane root = new GridPane();
        root.setVgap(10);
        root.setHgap(10);
        root.add(lblUsername, 0, 0);
        root.add(lblPassword, 0, 1);
        root.add(tfUsername, 1, 0);
        root.add(tfPassword, 1, 1);
        root.add(btnLogin, 1, 2);
        root.setHalignment(btnLogin, HPos.RIGHT);

        Scene scene = new Scene(root);
        primaryStage.setTitle("AGENT LOGIN");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.sizeToScene();

        btnLogin.setOnAction(event -> {
            String username = tfUsername.getText();
            String password = tfPassword.getText();

            try{
                Socket socket = new Socket("127.0.0.1", 7777);
                boolean isRealAgent = isRealAgent(username, password, socket);

                if(isRealAgent){
                    System.out.println("agent authenticated");
                    //receive port here
                    int portC1 = getPort(socket);
                    int portC2 = getPort(socket);
                    System.out.println("Port received from mainServer: " + portC1 + " " + portC2);

                    Chat_server server = new Chat_server();


                    NetworkConnection connectionC1 = server.connection;
                    NetworkConnection connectionC2 = server.connectionC2;
                    connectionC1.setPort(portC1);
                    connectionC2.setPort(portC2);

                    try {
                        server.run();
                        server.setTitle(username);
                        primaryStage.close();
                    } catch (Exception e) {
                        System.out.println("ConnectionHandler in Login_Agent failed.");
                    }

                }else{
                    System.out.println("not real agent");
                }

            }catch (IOException e){
                System.out.println("IOException occurred when receiving port from mainServer");
                e.printStackTrace();
            }catch (Exception e){
                System.out.println("Exception occured when receiving port from mainServer");
                e.printStackTrace();
            }
        });
    }

    public boolean isRealAgent(String username, String password, Socket socket) throws Exception{
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF("agent");
        out.writeUTF(username);
        out.writeUTF(password);

        boolean isRealAgent;
        isRealAgent = in.readBoolean();

        return isRealAgent;
    }

    public int getPort(Socket socket) throws Exception{
        DataInputStream in = new DataInputStream(socket.getInputStream());

        int port = in.readInt();
        return port;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
