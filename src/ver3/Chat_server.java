package ver3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HP on 18/10/2017.
 */
public class Chat_server extends Application implements Runnable{
    TextField tfInputMsgBoth;
    TextField tfInputMsgC1;
    TextField tfInputMsgC2;
    Button btnSaveC1;
    Button btnSaveC2;
    TextArea taPanelC1;
    TextArea taPanelC2;
    Label lblSendBoth;
    Label lblTypeHereC1;
    Label lblTypeHereC2;

    Label lblUserType;
    Label lblUsername;
    Label lblEmail;



    NetworkConnection connection = new Server(7777, data ->{
        Platform.runLater(() -> {
            taPanelC1.appendText(data.toString());
        });
    });
    NetworkConnection connectionC2 = new Server(7778, data ->{
        Platform.runLater(() -> {
            taPanelC2.appendText(data.toString());
        });
    });

    Stage mainStage;

    @Override
    public void run() {
        try {
            connection.startConnection();
            connectionC2.startConnection();
            mainStage = new Stage();
            start(mainStage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setTitle(String title){
        mainStage.setTitle(title);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //component initialization
        tfInputMsgBoth = new TextField();
        tfInputMsgC1 = new TextField();
        tfInputMsgC2 = new TextField();
        btnSaveC1 = new Button("Save Client1 Message");
        btnSaveC2 = new Button("Save Client2 Message");
        taPanelC1 = new TextArea();
        taPanelC2 = new TextArea();
        lblSendBoth = new Label("Send to both Client");
        lblTypeHereC1 = new Label("Type here for Client 1:");
        lblTypeHereC2 = new Label("Type here for Client 2:");
        lblUserType = new Label("Agent");
        lblUsername = new Label("dummyAgent");
        lblEmail = new Label("dummyvalue@gmail.com");



        //set textArea size
        taPanelC1.setPrefSize(550, 550);
        taPanelC2.setPrefSize(550, 550);
        //disable textArea as an input
        taPanelC1.setEditable(false);
        taPanelC2.setEditable(false);

        //root pane
        HBox root = new HBox();
        //leftmost Pane for agent infomation
        GridPane gridPaneInfo = new GridPane();
        gridPaneInfo.setHgap(10);
        gridPaneInfo.setVgap(10);
        gridPaneInfo.add(lblUserType, 0, 0);
        gridPaneInfo.add(new Label("Username: "), 0, 1);
        gridPaneInfo.add(new Label("Email: "), 0, 2);
        gridPaneInfo.add(lblUsername, 1, 1);
        gridPaneInfo.add(lblEmail, 1, 2);
        gridPaneInfo.setColumnSpan(lblUserType, gridPaneInfo.REMAINING);
        gridPaneInfo.setHalignment(lblUserType, HPos.CENTER);

        //Pane required for setting up the rightmost panel which contain the componenet for client1 and client 2
        VBox vBoxBothClient = new VBox();//Will contain the hBoxBothClient and a textField to send to both client
        HBox hBoxBothClient = new HBox();//Will contain the both gridPane for Client1 and Client2
        hBoxBothClient.setPadding(new Insets(10, 10, 10, 10));
        hBoxBothClient.setSpacing(10);

        //gridPane containing client 1 component
        GridPane gridPaneC1 = new GridPane();
        gridPaneC1.setVgap(5);
        gridPaneC1.setHgap(5);
        gridPaneC1.add(taPanelC1, 0, 0);
        gridPaneC1.add(lblTypeHereC1, 0, 1);
        gridPaneC1.add(btnSaveC1, 1, 1);
        gridPaneC1.add(tfInputMsgC1, 0, 2);
        gridPaneC1.setColumnSpan(taPanelC1, gridPaneC1.REMAINING);
        gridPaneC1.setHalignment(btnSaveC1, HPos.RIGHT);
        gridPaneC1.setColumnSpan(tfInputMsgC1, gridPaneC1.REMAINING);
        //gridPane containing client 2 component
        GridPane gridPaneC2 = new GridPane();
        gridPaneC2.setVgap(5);
        gridPaneC2.setHgap(5);
        gridPaneC2.add(taPanelC2, 0, 0);
        gridPaneC2.add(lblTypeHereC2, 0, 1);
        gridPaneC2.add(btnSaveC2, 1, 1);
        gridPaneC2.add(tfInputMsgC2, 0, 2);
        gridPaneC2.setColumnSpan(taPanelC2, gridPaneC2.REMAINING);
        gridPaneC2.setHalignment(btnSaveC2, HPos.RIGHT);
        gridPaneC2.setColumnSpan(tfInputMsgC2, gridPaneC2.REMAINING);
        //gridPane containing both client gridPane and an area to send input to both client
        GridPane gridPaneBoth = new GridPane();
        gridPaneBoth.setVgap(20);
        gridPaneBoth.setHgap(30);
        gridPaneBoth.setPadding(new Insets(10, 10, 10, 10));
        gridPaneBoth.add(gridPaneC1, 0, 0);
        gridPaneBoth.add(gridPaneC2, 1, 0);
        gridPaneBoth.add(lblSendBoth, 0, 1);
        gridPaneBoth.add(tfInputMsgBoth, 0, 2);
        gridPaneBoth.setHalignment(lblSendBoth, HPos.CENTER);
        gridPaneBoth.setColumnSpan(lblSendBoth, gridPaneBoth.REMAINING);
        gridPaneBoth.setColumnSpan(tfInputMsgBoth, gridPaneBoth.REMAINING);

        //setting pane into one another
        root.getChildren().addAll(gridPaneInfo, vBoxBothClient);
        vBoxBothClient.getChildren().addAll(gridPaneBoth);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Agent Server");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

        //ActionEvent for tfInputMsgC1, tfInputMsgC2, tfInputBoth
        tfInputMsgC1.setOnAction(event -> {
            String message = tfInputMsgC1.getText();
            if(!message.equals("")){
                System.out.println("To Client 1: " + message);
                tfInputMsgC1.clear();

                sendMessage(connection, message, taPanelC1);
            }
        });
        tfInputMsgC2.setOnAction(event -> {
            String message = tfInputMsgC2.getText();
            if(!message.equals("")){
                System.out.println("To Client 2: " + message);
                tfInputMsgC2.clear();

                sendMessage(connectionC2, message, taPanelC2);
            }
        });
        tfInputMsgBoth.setOnAction(event -> {
            String message = tfInputMsgBoth.getText();
            if(!message.equals("")){
                System.out.println("To Both: " + message);
                tfInputMsgBoth.clear();

                sendMessage(connection, message, taPanelC1);
                sendMessage(connectionC2, message, taPanelC2);
            }
        });

        //Action even for save button
        btnSaveC1.setOnAction(event -> {
            Date date = new Date() ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
            String filename = dateFormat.format(date) + "client1";
            File file = new File(filename + ".txt") ;
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                out.write(taPanelC1.getText());
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });


        btnSaveC2.setOnAction(event -> {
            Date date = new Date() ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
            String filename = dateFormat.format(date) + "client2";
            File file = new File(filename + ".txt") ;
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                out.write(taPanelC2.getText());
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });


        primaryStage.setOnCloseRequest(event -> {
            try {
                stop();
            }catch (Exception e){
                System.out.println("Agent/server exited");
            }
        });
    }

    @Override
    public void stop() throws Exception {
        connection.endConnection();
        connectionC2.endConnection();
    }

    public void sendMessage(NetworkConnection connection, String message, TextArea textArea){
        try {
            String fullMessage = "Agent: " + message + "\n";
            connection.send(fullMessage);
            textArea.appendText(fullMessage);
        }catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error, cannot send message");
            alert.setTitle("Failed to send");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) throws Exception{
        Application.launch(args);
    }
}
