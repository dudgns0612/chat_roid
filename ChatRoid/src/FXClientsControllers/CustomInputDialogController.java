package FXClientsControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import tools.CustomDialog;
import tools.Scenecontroll;
import tools.Statics;

public class CustomInputDialogController implements Initializable{
	Stage stage;
	@FXML Label titleLabel;
	@FXML Label mainLabel;
	@FXML TextField inputField;
	public static String inputword;
	
	public void INIT_CONTROLLER(Stage stage , String sLabel , String bLabel)
	{
		this.stage = stage;
		titleLabel.setText(sLabel);
		mainLabel.setText(bLabel);
	}
	
	
	@FXML
	private void onOkBtn()
	{
		inputword = inputField.getText().trim();
		System.out.println(inputword);
		if(inputword.equals(""))
		{
			CustomDialog.customOkDialog(Statics.CUSTOM_OK_DIALOG_FXML, "알림창", "에    러", "질문에 올바른 답을 입력하여 주세요.");
		}
		else
		{
			stage.close();
		}	
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		inputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode() == KeyCode.ENTER)
				{
					inputword = inputField.getText().trim();
					System.out.println(inputword);
					if(inputword.equals(""))
					{
						CustomDialog.customOkDialog(Statics.CUSTOM_OK_DIALOG_FXML, "알림창", "에    러", "질문에 올바른 답을 입력하여 주세요.");
					}
					else
					{
						stage.close();
					}
				}
			}
		});
		
	}
	
	
	

}
