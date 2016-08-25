package tools;

import java.io.IOException;

import FXClientsControllers.CustomDialogController;
import FXClientsControllers.ServerErrorController;
import FXClientsControllers.CustomInputDialogController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomDialog extends Stage{
	Object controller = null;
	
	public CustomDialog(String scene , String windowname)
	{
		try {
			System.out.println(scene);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(scene));
			Parent root;
			root = (Parent) fxmlLoader.load();
			controller = fxmlLoader.getController();
			this.initModality(Modality.APPLICATION_MODAL);
			this.setTitle(windowname);
			this.setScene(new Scene(root)); 
		} catch (IOException e) {
			e.printStackTrace();
		} 	

	}
	
	public Object getController()
	{
		return controller;
	}
	

	public static void customInputDialog(String scene, String windowname ,String strapline ,String substance)
	{
		CustomDialog custom = new CustomDialog(scene , windowname);
		CustomInputDialogController controller = (CustomInputDialogController)custom.getController();
		controller.INIT_CONTROLLER(custom , strapline , substance);
		custom.showAndWait();
	}
	
	public static int customWarringDialog(String scene, String windowname ,String strapline ,String substance)
	{
		CustomDialog custom = new CustomDialog(scene , windowname );
		CustomDialogController controller = (CustomDialogController) custom.getController();
		controller.INIT_CONTROLLER(custom , strapline , substance);	
		custom.showAndWait();
		
		return (int)custom.getUserData();
	}
	
	public static void customOkDialog(String scene, String windowname ,String strapline ,String substance)
	{	
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				CustomDialog custom = new CustomDialog(scene , windowname );
				CustomDialogController controller = (CustomDialogController) custom.getController();
				controller.INIT_CONTROLLER(custom , strapline , substance);	
				custom.show();
			}
		});
	}
	
}
