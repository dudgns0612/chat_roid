package FXClientsControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import tools.Scenecontroll;
import tools.Statics;

public class ServerErrorController implements Initializable{
	Stage stage;
	@FXML Label errornameLabel;
	@FXML Label contentLabel1;
	@FXML Label contentLabel2;
	@FXML TextArea currentList;
	String substance;
	
	
	public void INIT_CONTROLLER(Stage stage)
	{   
		this.stage = stage;
	}
	
	
	public void init()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int cnt;
				cnt = Scenecontroll.cnt;
				
				if(cnt != Scenecontroll.cnt)
				{
					contentLabel2.setText(substance+Scenecontroll.cnt);
				}
			}
		}).start();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	
	@FXML
	private void onOkBtn()
	{
		stage.close();
	}

}
