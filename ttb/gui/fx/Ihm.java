package ttb.gui.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ttb.gui.fx.controls.EtatRobot;
import ttb.gui.fx.controls.ImageRobot;
import ttb.gui.fx.controls.MainJoueur;

import java.io.File;

import ttb.gui.fx.util.Dialog;
import ttb.metier.Joueur;
import ttb.metier.Robot;
import ttb.metier.Tuile;

/**
 * Classe Ihm
 * Ihm de la version GUI
 * @author Jérémy Auzou
 * @author Matys Achart
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class Ihm
{

    private int robot = 0;
    private ControleurIhm ctrl;

    @FXML private HBox      hboxGame;
    @FXML private ToolBar   tbButtons;
    @FXML private Accordion accMains;
    @FXML private Button    btnFinirTour;
    @FXML private ImageView imgPlateau;
    @FXML private Pane      panePlateau;
    @FXML private VBox      vboxDroite;

    private MainJoueur mainJoueur;


    @FXML
    void initPlateau(ActionEvent event)
    {
        creerPartie();
        afficherPlateau();
    }

    @FXML
    void changerJoueur(ActionEvent event)
    {
        ctrl.changerJoueur();
        afficherPlateau();
        gestionFinPartie();
    }

    /**
     * Obtient le chemin de l'image représentant la tuile indiquée
     * @param nomTuile nom de la tuile
     * @return chemin de l'image
     */
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

    /**
     * Obtient le chemin de l'image représentant la tuile indiquée
     * @param nomCristal nom du cristal
     * @return chemin de l'image
     */
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

    /**
     * Crée le composant ImageView nécessaire pour afficher le contenu de
     * la tuile aux coordonnées ( i ; j )
     *
     * @param tuile Tuile aux coordonées i;j
     * @param i Coordonnée i
     * @param j Coordonée j
     * @return ImageView avec l'image correspondante, redimensionnée et pivotée au besoin
     */
    private ImageView chargerImage(Tuile tuile, int i, int j)
    {
        Robot  r      = this.ctrl.getRobotAPosition(new int[]{i, j});
        Joueur joueur = this.ctrl.getJoueurParBase (new int[]{i, j});
        double angle = 0.0;
        String fic;

        // Il y a un robot
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
        // Il y a une base
        else if (joueur != null)
        {
            fic = "bases/base" + joueur.getId();
        }
        // Il y a autre chose
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

    /**
     * Met à jour le plateau sur l'IHM GUI.
     */
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

                        // Placement de l'image
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
            }
            else
            {
                File file = new File("./ttb/gui/images/plateau2-4.png");
                Image image = new Image(file.toURI().toString());
                imgPlateau.setImage(image);

                for (int i = 0; i < 9; i++)
                    for (int j = 0; j < 9; j++)
                    {
                        Robot r = this.ctrl.getRobotAPosition(new int[]{i, j});

                        // Placement de l'image
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

        // FILE ATTENTE (X0 = 64, Y = 450)
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

        // Mettre à jour l'état des robots des joueurs
        for (TitledPane p : this.accMains.getPanes())
            if (p instanceof EtatRobot)
                ((EtatRobot) p).updateStatus();

        // Forcer l'ouverture d'un panneau de l'accordéon en fonction du joueur courant
        this.accMains.setExpandedPane(this.accMains.getPanes().get(this.ctrl.getJoueurCourant().getId()));

        // Mettre à jour la main du joueur
        this.vboxDroite.getChildren().remove(this.mainJoueur);
        this.vboxDroite.getChildren().add(this.mainJoueur = new MainJoueur(this.ctrl.getJoueurCourant(), this.ctrl));

        if (ctrl.derniersTours())
            this.lblComm.setText(ctrl.toursRestants() + " tours");

        this.gestionFinPartie();
    }

    @FXML
    void annulerSelection(ActionEvent event)
    {
        this.ctrl.setSource(null);
    }

    @FXML
    void initialize()
    {
        // Créer une partie
        creerPartie();

        this.afficherPlateau();
    }

    private Button btnPrec = null;
    private Button btnSuiv = null;
    private Label  lblComm = null;

    /**
     * Prépare une partie
     */
    private void creerPartie()
    {
        if (this.btnPrec != null) this.tbButtons.getItems().remove(this.btnPrec);
        if (this.btnSuiv != null) this.tbButtons.getItems().remove(this.btnSuiv);
        if (this.lblComm != null) this.tbButtons.getItems().remove(this.lblComm);

        int scenario;

        if (Dialog.demander("Voulez-vous charger un scénario?") &&
                (scenario = Dialog.lireNumScenario()) != -1)
        {
            // Mode scénario
            this.ctrl = new ControleurIhm(Dialog.lireNbJoueur(), this, scenario);

            btnPrec = new Button("Précédent");
            btnPrec.setOnAction(event -> {
                ctrl.scenarioPrecedent();

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

                this.updateCommentaire();
                this.afficherPlateau();
            });

            btnSuiv = new Button("Suivant");
            btnSuiv.setOnAction(event -> {
                ctrl.scenarioSuivant();
                this.updateCommentaire();
                this.afficherPlateau();
            });

            lblComm = new Label("// Commentaires mode debug");

            this.tbButtons.getItems().add(this.btnPrec);
            this.tbButtons.getItems().add(this.btnSuiv);
            this.tbButtons.getItems().add(this.lblComm);
            this.btnFinirTour.setDisable(true);
        }
        else
        {
            this.ctrl = new ControleurIhm(Dialog.lireNbJoueur(), this);
            this.lblComm = new Label("");
            this.tbButtons.getItems().add(this.lblComm);
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

    private void updateCommentaire()
    {
        if (this.lblComm != null)
            this.lblComm.setText(ctrl.getDernierCommentaire());
    }

    private void gestionFinPartie()
    {
        if (ctrl.partieTerminee())
        {
            this.hboxGame.setDisable    (true);
            this.btnFinirTour.setDisable(true);
            if (!ctrl.getVictoireAck())
            {
                Dialog.afficherNoms(ctrl.getGagnants());
                ctrl.acknowlegeVictoire();
            }
        }
        else
        {
            this.hboxGame.setDisable    (false);
            this.btnFinirTour.setDisable(false);
        }
    }
}