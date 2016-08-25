package FXClientsControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import tools.Scenecontroll;
import tools.Statics;

public class ClientExplantioncontroller implements Initializable{
	Stage stage;
	
	public void INIT_CONTROLLER(Stage stage)
	{
		this.stage = stage;
	}
	
	
	@FXML
	private void onCancle()
	{
		Scenecontroll.changeScene(Statics.MAIN_FXML);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
