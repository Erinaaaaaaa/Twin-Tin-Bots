package ttb.gui.fx.controls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ttb.gui.fx.ControleurIhm;
import ttb.gui.fx.util.Action;
import ttb.metier.Joueur;

import java.io.IOException;

import static ttb.gui.fx.util.Dialog.demander;

public class EtatRobot extends TitledPane
{
	private Joueur j;
	private ControleurIhm ctrl;

	@FXML	private ImageView imgR1;
	@FXML	private ImageView imgR1A1;
	@FXML	private ImageView imgR1A2;
	@FXML	private ImageView imgR1A3;
	@FXML	private ImageView imgR2;
	@FXML	private ImageView imgR2A1;
	@FXML	private ImageView imgR2A2;
	@FXML   private ImageView imgR2A3;

	public EtatRobot(Joueur j, ControleurIhm ctrl)
	{
		this.ctrl = ctrl;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("EtatRobot.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try
		{
			loader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		this.j = j;

		this.setText("Joueur " + (j.getId() + 1) );

		assert imgR1 != null : "fx:id=\"imgR1\" was not injected: check your FXML file 'EtatRobot.fxml'.";
		assert imgR1A1 != null : "fx:id=\"imgR1A1\" was not injected: check your FXML file 'EtatRobot.fxml'.";
		assert imgR1A2 != null : "fx:id=\"imgR1A2\" was not injected: check your FXML file 'EtatRobot.fxml'.";
		assert imgR1A3 != null : "fx:id=\"imgR1A3\" was not injected: check your FXML file 'EtatRobot.fxml'.";
		assert imgR2 != null : "fx:id=\"imgR2\" was not injected: check your FXML file 'EtatRobot.fxml'.";
		assert imgR2A1 != null : "fx:id=\"imgR2A1\" was not injected: check your FXML file 'EtatRobot.fxml'.";
		assert imgR2A2 != null : "fx:id=\"imgR2A2\" was not injected: check your FXML file 'EtatRobot.fxml'.";
		assert imgR2A3 != null : "fx:id=\"imgR2A3\" was not injected: check your FXML file 'EtatRobot.fxml'.";
	}

	// TODO: Aller lire les ordres dans le Joueur et adapter imgR#A#
	public void updateStatus()
	{
		this.setText("Joueur " + (j.getId() + 1) + " - " + j.getPoints() + " point" + (j.getPoints() > 1 ? "s" : ""));

		this.imgR1.setImage(new Image(
				getClass().getResourceAsStream("../../images/robots/petit" + this.j.getId() + ".png")
		));

		this.imgR1A1.setImage(getImageTuile(this.j.getOrdres(0)[0]));
		this.imgR1A2.setImage(getImageTuile(this.j.getOrdres(0)[1]));
		this.imgR1A3.setImage(getImageTuile(this.j.getOrdres(0)[2]));

		this.imgR2.setImage(new Image(
				getClass().getResourceAsStream("../../images/robots/gros" + this.j.getId() + ".png")
		));

		this.imgR2A1.setImage(getImageTuile(this.j.getOrdres(1)[0]));
		this.imgR2A2.setImage(getImageTuile(this.j.getOrdres(1)[1]));
		this.imgR2A3.setImage(getImageTuile(this.j.getOrdres(1)[2]));
	}

	private Image getImageTuile(char action)
	{
		String uri = MainJoueur.getImageFromTuile(action);

		return new Image(getClass().getResourceAsStream(uri));
	}

	@FXML
	void imgClick(MouseEvent event)
	{
		if (ctrl.getJoueurCourant() != this.j)
			return;

		ImageView iv = (ImageView) event.getSource();

		int robot = -1;
		int indice = -1;


		if (iv == this.imgR1A1) { robot = 0; indice = 0;}
		if (iv == this.imgR1A2) { robot = 0; indice = 1;}
		if (iv == this.imgR1A3) { robot = 0; indice = 2;}

		if (iv == this.imgR2A1) { robot = 1; indice = 0;}
		if (iv == this.imgR2A2) { robot = 1; indice = 1;}
		if (iv == this.imgR2A3) { robot = 1; indice = 2;}

		if (event.getButton().equals(MouseButton.PRIMARY))
		{
			if (ctrl.getSource() != null && (ctrl.getSource().getRobot() == -1 || ctrl.getSource().getRobot() == robot))
			{
				ctrl.ajouterOrdre(robot, indice);
			}
			else
			{
				ctrl.setSource(new Action(indice, robot,
						this.ctrl.getJoueurCourant().getOrdres(robot)[indice]));
			}
		}
		if (event.getButton().equals(MouseButton.SECONDARY))
		{
			if (demander("Vous allez supprimer une tuile"))
			{
				ctrl.supprimerOrdre(robot, indice);
			}
		}
	}

	@FXML
	void robot1Reset(ActionEvent event)
	{
		if (j == this.ctrl.getJoueurCourant())
			this.ctrl.resetRobot(0);
	}

	@FXML
	void robot2Reset(ActionEvent event)
	{
		if (j == this.ctrl.getJoueurCourant())
			this.ctrl.resetRobot(1);
	}
}

