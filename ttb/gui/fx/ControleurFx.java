package ttb.gui.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ttb.metier.Plateau;
import ttb.metier.SetGrille;
import ttb.metier.Tuile;

import java.io.File;

public class ControleurFx
{
    private Plateau plateau;

    @FXML
    private Button btnInit;

    @FXML
    private ImageView imgPlateau;

    @FXML
    private Pane panePlateau;

    @FXML
    void initPlateau(ActionEvent event)
    {
        double imgScale = 0.6;

        Tuile[][] tuiles = this.plateau.getTuiles();

        if (this.plateau.getNbJoueurs() > 4) // Grand plateau
        {
            File file = new File("./ttb/gui/images/plateau5-6.png");
            Image image = new Image(file.toURI().toString());
            imgPlateau.setImage(image);
            for (int i = 0; i < 11; i++)
                for (int j = 0; j < 11; j++)
                {
                    String fic;

                    switch(tuiles[i][j].toString())
                    {
                        case "T":
                            fic = "robots/petit4";
                            break;
                        case "CB":
                            fic = "robots/petit0";
                            break;
                        case "CV":
                            fic = "robots/petit1";
                            break;
                        case "CR":
                            fic = "robots/petit5";
                            break;
                        case "R":
                            fic = "robots/gros0";
                            break;
                        case "B":
                            fic = "bases/base0";
                            break;
                        default:
                            continue;
                    }

                    Image robot = new Image(new File("./ttb/gui/images/"+fic+".png").toURI().toString());

                    ImageView iv = new ImageView(robot);
                    iv.setScaleY(imgScale);iv.setScaleX(imgScale);

                    iv.setLayoutX(((i) % 2 == 0 ? -23 : 0) + (j) * 46 - 5);
                    iv.setLayoutY((i) * 39 - 3);

                    this.panePlateau.getChildren().add(iv);
                }
        }
        else
        {
            File file = new File("./ttb/gui/images/plateau2-4.png");
            Image image = new Image(file.toURI().toString());
            imgPlateau.setImage(image);
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                {
                    String fic;

                    switch(tuiles[i][j].toString())
                    {
                        case "T":
                            fic = "robots/petit4";
                            break;
                        case "CB":
                            fic = "robots/petit0";
                            break;
                        case "CV":
                            fic = "robots/petit1";
                            break;
                        case "CR":
                            fic = "robots/petit5";
                            break;
                        case "R":
                            fic = "robots/gros0";
                            break;
                        case "B":
                            fic = "bases/base0";
                            break;
                        default:
                            continue;
                    }

                    Image robot = new Image(new File("./ttb/gui/images/"+fic+".png").toURI().toString());

                    ImageView iv = new ImageView(robot);
                    iv.setScaleY(imgScale);iv.setScaleX(imgScale);

                    iv.setLayoutX(((i+1) % 2 == 0 ? -23 : 0) + (j+1) * 46 - 5);
                    iv.setLayoutY((i+1) * 39 - 3);

                    this.panePlateau.getChildren().add(iv);
                }
        }

    }

    @FXML
    void initialize()
    {
        this.plateau = SetGrille.initGrille(2);
    }

}
