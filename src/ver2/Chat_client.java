package ver2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by HP on 18/10/2017.
 */
public class Chat_client extends Application implements Runnable{
    TextField tfInputMsg;
    TextArea taPanel;
    Label lblTypeHere;

    Label lblUserType;
    Label lblUsername;
    Label lblEmail;

    NetworkConnection connection = new Client("127.0.0.1", 7778, data -> {
        Platform.runLater(() -> {
            taPanel.appendText(data.toString());
        });
    });
    Stage mainStage;

    @Override
    public void run() {
        try {
            connection.startConnection();
            mainStage = new Stage();
            start(mainStage);
        }catch (Exception e){
            System.out.println("Error occurred calling run() in Chat_client");
            e.printStackTrace();
        }
    }

    public void setTitle(String title){
        mainStage.setTitle(title);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //component initialization
        tfInputMsg = new TextField();
        taPanel = new TextArea();
        lblTypeHere = new Label("Type Here: ");
        lblUserType = new Label("Customer");
        lblUsername = new Label("dummyClient");
        lblEmail = new Label("dummy@gmail.com");

        //set TextArea size
        taPanel.setPrefSize(550, 550);
        taPanel.setEditable(false);

        //root Pane
        HBox root = new HBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setSpacing(10);

        //gridPane for leftmost side containing user info
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

        //gridPane for rightmost containing textArea and textField for sending and receiving.
        GridPane gridPaneClient = new GridPane();
        gridPaneClient.setVgap(5);
        gridPaneClient.setHgap(5);
        gridPaneClient.add(taPanel, 0, 0);
        gridPaneClient.add(lblTypeHere, 0, 1);
        gridPaneClient.add(tfInputMsg, 0, 2);

        root.getChildren().addAll(gridPaneInfo, gridPaneClient);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.sizeToScene();

        tfInputMsg.setOnAction(event -> {
            String message = tfInputMsg.getText();
            if(!tfInputMsg.getText().equals("")){
                System.out.println("To Sever: " + message);
                tfInputMsg.clear();

                sendMessage(message);
            }
        });

        primaryStage.setOnCloseRequest(event -> {
            try {
                stop();
            }catch (Exception e){
                System.out.println("Program exited.");
            }
        });
    }

    @Override
    public void stop() throws Exception {
        connection.endConnection();
    }

    public void sendMessage(String message) {
        try{
            String fullMessage = "User: " + message + "\n";
            taPanel.appendText(fullMessage);
            connection.send(fullMessage);
        }catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error, failed to send message");
            alert.setTitle("Failed to sen");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) throws IOException{
        Application.launch();
    }
}
