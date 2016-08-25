package FXClientsControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import FXServerControllers.ServerMainUicontroller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tools.Scenecontroll;
import tools.Statics;
import tools.Toolbox;

public class ClientsMainUicontroller implements Initializable{
	Stage stage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void INIT_CONTROLLER(Stage stage)
	{
		this.stage = stage;
	}

	
	@FXML
	private void onStudy()
	{
		Scenecontroll.changeScene(Statics.CLIENTS_STUDY_FXML);
	}
	
	@FXML
	private void onChatStart()
	{
		Scenecontroll.changeScene(Statics.CHATTING_PLAY_FXML);	
	}
	
	@FXML
	private void onCancle()
	{
		System.out.println("\t\t클라이언트 종료");
		System.exit(0);
	}
	
	@FXML
	private void onExplan()
	{
		Scenecontroll.changeScene(Statics.CLIENTS_EXPLAN_FXML);
	}
	
	

}
