package ttb.gui.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ttb.metier.*;

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
        afficherPlateau();
    }

    private String getFichier(String nomTuile)
    {
        switch(nomTuile)
        {
            case "2":
                return "cristaux/gem2";
            case "3":
                return "cristaux/gem3";
            case "4":
                return "cristaux/gem4";
            case "R":
                return "robots/gros0";
            case "B":
                return "bases/base0";
            default:
                return null;
        }
    }

    private ImageView chargerImage(Tuile tuile, int i, int j)
    {
        Robot  r      = this.plateau.getRobotAPosition(new int[]{i, j});
        Joueur joueur = this.plateau.getJoueurParBase (new int[]{i, j});
        double angle = 0.0;
        String fic;

        if (r != null)
        {
            fic = "robots/";

            if (r.getId() == 0)
                fic += "petit";
            else
                fic += "gros";

            angle = (-210 + r.getDir() * 60);

            fic += r.getJoueur().getId();

        }
        else if (joueur != null) // c'est une base
        {
            fic = "bases/base" + joueur.getId();
        }
        else
        {
            fic = getFichier(tuile.toString());
        }

        if (fic == null) return null;

        Image robot = new Image(new File("./ttb/gui/images/"+fic+".png").toURI().toString());

        ImageView iv = new ImageView(robot);
        iv.setScaleY(0.6);iv.setScaleX(0.6);
        iv.setRotate(angle);

        return iv;
    }

    private void afficherPlateau()
    {
        this.panePlateau.getChildren().clear();

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
                    ImageView iv = chargerImage(tuiles[i][j], i, j);
                    if (iv == null) continue;

                    iv.setScaleY(imgScale);iv.setScaleX(imgScale);


                    iv.setLayoutX((i % 2 == 0 ? 23 : 0) + (j) * 46 - 5);
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
                    ImageView iv = chargerImage(tuiles[i][j], i, j);

                    if (iv == null) continue;

                    iv.setLayoutX(((i+1) % 2 == 0 ? -23 : 0) + (j+1) * 46 - 5);
                    iv.setLayoutY((i+1) * 39 - 3);

                    this.panePlateau.getChildren().add(iv);
                }
        }

        System.out.println(plateau);
    }

    @FXML
    void initialize()
    {
        this.plateau = SetGrille.initGrille(6);
    }

}
