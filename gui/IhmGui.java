package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IhmGui extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Ihm.fxml"));
            VBox root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
