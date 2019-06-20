package ttb.gui.fx.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

import java.util.Optional;

public class Dialog
{
	public static int lireNbJoueur()
	{
		TextInputDialog dialog = new TextInputDialog("2");
		dialog.setTitle("Choix de joueur");
		dialog.setHeaderText("Choix du nombre de joueurs");
		dialog.setContentText("Entrez le nombre de joueurs voulu:");
		dialog.initStyle(StageStyle.UTILITY);

		Optional<String> result = dialog.showAndWait();
		return result.map(Integer::parseInt).orElse(-1);
	}

	public static boolean demander(String message)
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Attention");
		alert.setHeaderText(message);
		alert.setContentText("Êtes-vous sûr?");
		alert.initStyle(StageStyle.UTILITY);

		Optional<ButtonType> result = alert.showAndWait();

		return result.isPresent() && result.get() == ButtonType.OK;
	}
}
