package ttb.gui.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe LauncherIhm.
 * @author Jérémy AUZOU
 * @author Matys ACHART
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class LauncherIhm extends Application
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
            primaryStage.setTitle("Twin Tin Bots");
            primaryStage.setMinHeight(690);
            primaryStage.setMinWidth(900);

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
