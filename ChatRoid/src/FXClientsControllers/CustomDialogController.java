package FXClientsControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tools.CustomDialog;
import tools.Statics;

public class CustomDialogController implements Initializable{
	
	//FXº¯¼ö
	@FXML Label titleLabel;
	@FXML Label contentLabel;
	

	CustomDialog stage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void INIT_CONTROLLER(CustomDialog stage , String sLabel , String bLabel)
	{
		this.stage = stage;
		titleLabel.setText(sLabel);
		contentLabel.setText(bLabel);
	}
	
	
	@FXML
	public void onOkBtn()
	{
		stage.setUserData(Statics.OK_SELECTION);
		stage.close();
	}
	
	

}
