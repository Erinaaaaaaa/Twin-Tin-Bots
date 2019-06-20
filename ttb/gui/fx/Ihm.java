package ttb.gui.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ttb.gui.fx.controls.EtatRobot;
import ttb.gui.fx.controls.ImageRobot;
import ttb.gui.fx.controls.MainJoueur;
import ttb.metier.*;

import java.io.File;

import ttb.gui.fx.util.Dialog;

public class Ihm
{
    private int robot = 0;
    private ControleurIhm ctrl;

    @FXML private ToolBar tbButtons;
    @FXML private Accordion accMains;
    @FXML private Button btnInit;
    @FXML private Button btnFinirTour;
    @FXML private ImageView imgPlateau;
    @FXML private Pane panePlateau;
    @FXML private VBox vboxDroite;

    private MainJoueur mainJoueur;

    @FXML
    void initPlateau(ActionEvent event)
    {
        creerPartie();
        afficherPlateau();
    }

    @FXML
    void avancer(ActionEvent event)
    {
        this.ctrl.avancer(ctrl.getJoueurCourant().getRobot(robot), true);
        afficherPlateau();
    }

    @FXML
    void charger(ActionEvent event)
    {
        ctrl.chargerCristal(ctrl.getJoueurCourant().getRobot(robot));
        afficherPlateau();
    }

    @FXML
    void deposer(ActionEvent event)
    {
        ctrl.deposerCristal(ctrl.getJoueurCourant().getRobot(robot));
        afficherPlateau();
    }

    @FXML
    void tournerDroite(ActionEvent event)
    {
        ctrl.getJoueurCourant().getRobot(robot).turnAround(false);
        afficherPlateau();
    }

    @FXML
    void tournerGauche(ActionEvent event)
    {
        ctrl.getJoueurCourant().getRobot(robot).turnAround(true);
        afficherPlateau();
    }

    @FXML
    void changerJoueur(ActionEvent event)
    {
        ctrl.changerJoueur();
        afficherPlateau();
    }

    @FXML
    void changerRobot(ActionEvent event)
    {
        robot = (robot + 1) % 2;
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
        Robot  r      = this.ctrl.getRobotAPosition(new int[]{i, j});
        Joueur joueur = this.ctrl.getJoueurParBase (new int[]{i, j});
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

    void afficherPlateau()
    {
        this.panePlateau.getChildren().clear();

        // CONTENU PLATEAU
        {
            Tuile[][] tuiles = this.ctrl.getTuiles();

            if (this.ctrl.getNbJoueurs() > 4) // Grand ctrl
            {
                File file = new File("./ttb/gui/images/plateau5-6.png");
                Image image = new Image(file.toURI().toString());
                imgPlateau.setImage(image);

                for (int i = 0; i < 11; i++)
                    for (int j = 0; j < 11; j++)
                    {
                        Robot r = this.ctrl.getRobotAPosition(new int[]{i, j});

                        int x = ((i) % 2 == 0 ? 23 : 0) + (j) * 46 - 5;
                        if (r != null)
                        {
                            ImageRobot ir = new ImageRobot(r);
                            ir.setLayoutX(x);
                            ir.setLayoutY((i) * 39 - 3);
                            ir.setScaleX(0.6);
                            ir.setScaleY(0.6);
                            ir.setRotate(-210 + r.getDir() * 60);
                            this.panePlateau.getChildren().add(ir);
                        }
                        else
                        {
                            ImageView iv = chargerImage(tuiles[i][j], i, j);

                            if (iv == null) continue;

                            iv.setLayoutX(x);
                            iv.setLayoutY((i) * 39 - 3);

                            this.panePlateau.getChildren().add(iv);
                        }
                    }
            } else
            {
                File file = new File("./ttb/gui/images/plateau2-4.png");
                Image image = new Image(file.toURI().toString());
                imgPlateau.setImage(image);

                for (int i = 0; i < 9; i++)
                    for (int j = 0; j < 9; j++)
                    {
                        Robot r = this.ctrl.getRobotAPosition(new int[]{i, j});

                        int x = ((i + 1) % 2 == 0 ? -23 : 0) + (j + 1) * 46 - 5;
                        if (r != null)
                        {
                            ImageRobot ir = new ImageRobot(r);
                            ir.setLayoutX(x);
                            ir.setLayoutY((i + 1) * 39 - 3);
                            ir.setScaleX(0.6);
                            ir.setScaleY(0.6);
                            ir.setRotate(-210 + r.getDir() * 60);
                            this.panePlateau.getChildren().add(ir);
                        }
                        else
                        {
                            ImageView iv = chargerImage(tuiles[i][j], i, j);

                            if (iv == null) continue;

                            iv.setLayoutX(x);
                            iv.setLayoutY((i + 1) * 39 - 3);

                            this.panePlateau.getChildren().add(iv);
                        }


                    }
            }
        }

        // FILE ATTENTE (X0 = 64, Y0 = 450)
        {
            String file = this.ctrl.getFileAttente();

            for (int i = 0; i < file.length(); i++)
            {
                String fic = getFichierFileAttente(file.charAt(i));

                ImageView iv = new ImageView(new Image(new File("./ttb/gui/images/" + fic + ".png").toURI().toString()));

                iv.setLayoutX(64 + 46 * i);
                iv.setLayoutY(448);
                iv.setScaleX(0.6); iv.setScaleY(0.6);

                this.panePlateau.getChildren().add(iv);
            }
        }

        for (TitledPane p : this.accMains.getPanes())
            if (p instanceof EtatRobot)
                ((EtatRobot) p).updateStatus();

        this.vboxDroite.getChildren().remove(this.mainJoueur);
        this.vboxDroite.getChildren().add(this.mainJoueur = new MainJoueur(this.ctrl.getJoueurCourant(), this.ctrl));
    }

    @FXML
    void annulerSelection(ActionEvent event)
    {
        this.ctrl.setSource(null);
    }

    @FXML
    void initialize()
    {
        creerPartie();

        this.afficherPlateau();
    }

    private Button btnPrec = null;
    private Button btnSuiv = null;

    private void creerPartie()
    {
        if (this.btnPrec != null) this.tbButtons.getItems().remove(this.btnPrec);
        if (this.btnSuiv != null) this.tbButtons.getItems().remove(this.btnSuiv);

        int scenario;

        if (Dialog.demander("Voulez-vous charger un scénario?") &&
                (scenario = Dialog.lireNumScenario()) != -1)
        {
            // Scénario mode
            this.ctrl = new ControleurIhm(Dialog.lireNbJoueur(), this, scenario);
            btnPrec = new Button("Précédent");
            btnPrec.setOnAction(event -> {
                ctrl.scenarioPrecedent();
                this.afficherPlateau();
            });
            btnSuiv = new Button("Suivant");
            btnSuiv.setOnAction(event -> {
                ctrl.scenarioSuivant();
                this.afficherPlateau();
            });
            this.tbButtons.getItems().add(this.btnPrec);
            this.tbButtons.getItems().add(this.btnSuiv);
            this.btnFinirTour.setDisable(true);
        }
        else
        {
            // TODO: bloquer les clics en mode scénario (ControleurIhm peut-être?)
            this.ctrl = new ControleurIhm(Dialog.lireNbJoueur(), this);
            this.btnFinirTour.setDisable(false);
        }

        this.accMains.getPanes().clear();

        for (int i = 0; i < this.ctrl.getNbJoueurs(); i++)
        {
            this.accMains.getPanes().add(
                    new EtatRobot(
                            this.ctrl.getJoueur(i),
                            this.ctrl
                    )
            );
        }

        for (TitledPane p : this.accMains.getPanes())
            if (p instanceof EtatRobot)
                ((EtatRobot) p).updateStatus();
    }

}
