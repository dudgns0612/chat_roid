package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.orsoncharts.util.json.JSONObject;

import FXClientsControllers.CustomInputDialogController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.JobSettings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tools.CustomDialog;
import tools.NetworkProtocols;
import tools.Scenecontroll;
import tools.Statics;
import tools.Toolbox;

public class ClientsMainApplication extends Application{
	Stage stage;
	Scenecontroll sceneC;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		new ClientsMainApplication();
		stage = primaryStage;
		sceneC = new Scenecontroll(stage);
		Platform.runLater(new Runnable() {		
			@Override
			public void run() {
				CustomDialog.customInputDialog(Statics.CUSTOM_INPUT_DIALOG_FXML, "입력창", "네트워크 정보 입력", "서버IP주소를 입력 하여 주세요.EX)127.0.0.0");
				Scenecontroll.start();		
			}
		});
	}
}
