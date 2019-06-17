package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ControleurIhm
{
    @FXML
    private Button btnInit;

    @FXML
    private Pane panePlateau;

    @FXML
    void initPlateau(ActionEvent event)
    {

    }

    @FXML
    void initialize()
    {
        assert btnInit != null : "fx:id=\"btnInit\" was not injected: check your FXML file 'Untitled'.";
        assert panePlateau != null : "fx:id=\"panePlateau\" was not injected: check your FXML file 'Untitled'.";



    }

}
