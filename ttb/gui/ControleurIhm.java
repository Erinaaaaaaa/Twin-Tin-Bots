package ttb.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import ttb.Plateau;
import ttb.Tuile;

public class ControleurIhm
{
    private Plateau plateau;

    @FXML
    private Button btnInit;

    @FXML
    private Pane panePlateau;


    int x0 = 30;
    int y0 = 30;


    @FXML
    void initPlateau(ActionEvent event)
    {
        Tuile[][] tuilesPlateau = this.plateau.getTuiles();

        for (int i = 0; i < tuilesPlateau.length; i++)
        {
            for (int j = 0; j < tuilesPlateau[i].length; j++)
            {
                Polygon polygon = new Polygon(
                        0,20,
                        -17,10,
                        -17,-10,
                        0,-20,
                        17,-10,
                        17, 10
                );

                switch (tuilesPlateau[j][i].toString())
                {
                    case " ":
                        polygon.setFill(Color.TRANSPARENT);break;
                    case "T":
                        polygon.setFill(Color.DARKBLUE);break;
                    case "B":
                        polygon.setFill(Color.LIGHTBLUE);break;
                    case "V":
                        polygon.setFill(Color.LIGHTGREEN);break;
                    case "R":
                        polygon.setFill(Color.MEDIUMPURPLE);break;
                    default:
                        polygon.setFill(Color.BLACK);break;

                }

                polygon.setRotate(0);

                polygon.setLayoutX(x0 + (40 * i) + (j%2 == 0 ? 20 : 0));
                polygon.setLayoutY(y0 + (35 * j));

                this.panePlateau.getChildren().add(polygon);
            }
        }
    }

    @FXML
    void initialize()
    {
        assert btnInit != null : "fx:id=\"btnInit\" was not injected: check your FXML file 'Untitled'.";
        assert panePlateau != null : "fx:id=\"panePlateau\" was not injected: check your FXML file 'Untitled'.";

        this.plateau = new Plateau(5);
    }

}
