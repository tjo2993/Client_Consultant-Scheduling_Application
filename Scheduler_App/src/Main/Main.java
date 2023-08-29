package Main;

import DAO.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        stage.setTitle("Login");
        stage.setScene(new Scene(root, 550, 625));
        stage.show();
        stage.centerOnScreen();
    }


    public static void main(String[] args)  {
        DBConnection.openConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
