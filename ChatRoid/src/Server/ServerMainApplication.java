package Server;

import javafx.application.Application;
import javafx.stage.Stage;
import tools.Scenecontroll;
import tools.Statics;

public class ServerMainApplication extends Application{

	
	@Override
	public void start(Stage primaryStage) throws Exception {
	
		Scenecontroll sControll = new Scenecontroll(primaryStage);
		sControll.changeScene(Statics.SERVER_MAIN_FXML);	
	}
}
