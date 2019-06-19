package ttb.gui.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ttb.gui.fx.controls.EtatRobot;
import ttb.metier.*;

import java.io.File;

public class ControleurFx
{
    private Plateau plateau;

    @FXML private Accordion accMains;
    @FXML private Button btnInit;
    @FXML private ImageView imgPlateau;
    @FXML private Pane panePlateau;

    @FXML
    void initPlateau(ActionEvent event)
    {
        afficherPlateau();

        for (TitledPane p : this.accMains.getPanes())
            if (p instanceof EtatRobot)
                ((EtatRobot) p).updateStatus();
    }

    @FXML
    void avancer(ActionEvent event)
    {
        this.plateau.avancer(plateau.getJoueur(0).getRobot(0), true);
        afficherPlateau();
    }

    @FXML
    void charger(ActionEvent event)
    {
        plateau.chargerCristal(plateau.getJoueur(0).getRobot(0));
        afficherPlateau();
    }

    @FXML
    void deposer(ActionEvent event)
    {
        plateau.deposerCristal(plateau.getJoueur(0).getRobot(0));
        afficherPlateau();
    }

    @FXML
    void tournerDroite(ActionEvent event)
    {
        plateau.getJoueur(0).getRobot(0).turnAround(false);
        afficherPlateau();
    }

    @FXML
    void tournerGauche(ActionEvent event)
    {
        plateau.getJoueur(0).getRobot(0).turnAround(true);
        afficherPlateau();
    }

    private String getFichierPlateau(String nomTuile)
    {
        switch(nomTuile)
        {
            // case "T":
            //     return "inconnu";
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

    private String getFichierFileAttente(char nomCristal)
    {
        switch (nomCristal)
        {
            case 'B':
                return "cristaux/gem2";
            case 'V':
                return "cristaux/gem3";
            case 'R':
                return "cristaux/gem4";
            default:
                return "inconnu";

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
            angle = (-210 + r.getDir() * 60);

            if (r.hasCristal())
                fic = "inconnu";
            else
            {
                fic = "robots/";

                if (r.getId() == 0)
                    fic += "petit";
                else
                    fic += "gros";



                fic += r.getJoueur().getId();
            }

        }
        else if (joueur != null) // c'est une base
        {
            fic = "bases/base" + joueur.getId();
        }
        else
        {
            fic = getFichierPlateau(tuile.toString());
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

        // CONTENU PLATEAU
        {
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


                        iv.setLayoutX((i % 2 == 0 ? 23 : 0) + (j) * 46 - 5);
                        iv.setLayoutY((i) * 39 - 3);

                        this.panePlateau.getChildren().add(iv);
                    }
            } else
            {
                File file = new File("./ttb/gui/images/plateau2-4.png");
                Image image = new Image(file.toURI().toString());
                imgPlateau.setImage(image);

                for (int i = 0; i < 9; i++)
                    for (int j = 0; j < 9; j++)
                    {
                        ImageView iv = chargerImage(tuiles[i][j], i, j);

                        if (iv == null) continue;

                        iv.setLayoutX(((i + 1) % 2 == 0 ? -23 : 0) + (j + 1) * 46 - 5);
                        iv.setLayoutY((i + 1) * 39 - 3);

                        this.panePlateau.getChildren().add(iv);
                    }
            }
        }

        // FILE ATTENTE (X0 = 64, Y0 = 450)
        {
            String file = this.plateau.getFileAttente();
            System.out.println(file);

            for (int i = 0; i < file.length(); i++)
            {
                String fic = getFichierFileAttente(file.charAt(i));

                System.out.println(fic);

                ImageView iv = new ImageView(new Image(new File("./ttb/gui/images/" + fic + ".png").toURI().toString()));

                iv.setLayoutX(64 + 46 * i);
                iv.setLayoutY(448);
                iv.setScaleX(0.6); iv.setScaleY(0.6);

                this.panePlateau.getChildren().add(iv);
            }
        }

        System.out.println(plateau);
    }

    @FXML
    void initialize()
    {
        this.plateau = SetGrille.initGrille(3);

        for (int i = 0; i < this.plateau.getNbJoueurs(); i++)
        {
            this.accMains.getPanes().add(
                    new EtatRobot(
                            this.plateau.getJoueur(i)
                    )
            );
        }

        for (TitledPane p : this.accMains.getPanes())
            if (p instanceof EtatRobot)
                ((EtatRobot) p).updateStatus();

        this.afficherPlateau();
    }

}
