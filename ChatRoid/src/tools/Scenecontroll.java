package tools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import com.sun.org.apache.regexp.internal.recompile;

import FXClientsControllers.ChattingWindowcontroller;
import FXClientsControllers.ClientExplantioncontroller;
import FXClientsControllers.ClientsMainUicontroller;
import FXClientsControllers.ClientsStudycontroller;
import FXClientsControllers.CustomDialogController;
import FXClientsControllers.CustomInputDialogController;
import FXClientsControllers.ServerErrorController;
import FXServerControllers.ServerMainUicontroller;
import FXServerControllers.ServerStudyUicontroller;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Scenecontroll {
	static Stage stage;
	String title;
	public static Socket socket;
	static FXMLLoader loader;
	
	//클라이언트 스트림
	public static ObjectOutputStream oo;
	public static ObjectInputStream oi;
	
	public static String currentScene;
	public static int cnt = 0;
	
	public Scenecontroll(Stage stage)
	{
		this.stage = stage;
		this.title = title;
	}
	
	
	public static void start()
	{
		try {
			socket = new Socket(CustomInputDialogController.inputword, NetworkProtocols.SERVER_PORT_INFO);
			if(socket != null)
			{
				changeScene(Statics.MAIN_FXML);
				oo = new ObjectOutputStream(socket.getOutputStream());
				oi = new ObjectInputStream(socket.getInputStream());
			}
			
		}catch(ConnectException e){
				if(cnt < 10)
				{		
						System.out.println("서버 접속 오류 :"+e);
						if(socket == null)
						{
							cnt++;
							//if(cnt == 1)
							//changeScene(Statics.SERVER_ERROR_FXML);
							start();	
						}
						else
						{
							start();
						}
				}
				else
				{
					
					Platform.runLater(new Runnable() {				
						@Override
						public void run() {
							int ok = CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "서버접속오류", "서버에 접속할 수 없습니다. 다시 접속 해주세요.");
							if(ok == Statics.OK_SELECTION)
							{
								System.exit(0);
							}
						}
					});
				}
		}catch (SocketException e){
			//System.out.println("소켓 종료 ");
			e.printStackTrace();
		}catch (UnknownHostException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "서버호스트오류", "서버의 호스트주소가 올바르지 않습니다.");
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public static void changeScene(String scene)
	{
		currentScene = scene;
		System.out.println("FXML : "+currentScene);
		Platform.runLater(new Runnable() {
			public void run() {	
				loader = new FXMLLoader(getClass().getResource(currentScene));					
				try {									
					if(scene.equals(Statics.MAIN_FXML))
					{
						System.out.println("메인 fxml");
						final Parent root = loader.load();
						
						System.out.println(root.toString());
						
						
						stage.setScene(new Scene(root));
						stage.setResizable(true);
						stage.setTitle("ChatRoid Main Window");
						stage.setX(600);
						stage.setY(200);
						stage.setHeight(550);
						stage.setWidth(750);
						stage.setMaxHeight(550);
						stage.setMaxWidth(750);
						stage.show();
						
						ClientsMainUicontroller controller = loader.getController();
						controller.INIT_CONTROLLER(stage);
					}
					else if(scene.equals(Statics.CHATTING_PLAY_FXML))
					{
						final Parent root = loader.load();

						stage.setScene(new Scene(root));
						stage.setResizable(true);
						stage.setTitle("ChatWindow");
						stage.setX(500);
						stage.setY(100);
						stage.setHeight(880);
						stage.setWidth(960);
						stage.setMaxHeight(880);
						stage.setMaxWidth(960);
						stage.show();
						
						ChattingWindowcontroller controller = loader.getController();
						controller.INIT_CONTROLLER(stage);		
					}
					else if(scene.equals(Statics.CLIENTS_STUDY_FXML))
					{
						final Parent root = loader.load();
						
						stage.setScene(new Scene(root));
						stage.setResizable(true);
						stage.setTitle("ChatWindow");
						stage.setX(500);
						stage.setY(150);
						stage.setHeight(720);
						stage.setWidth(620);
						stage.setMaxHeight(720);
						stage.setMaxWidth(610);
						stage.show();
						
						ClientsStudycontroller controller = loader.getController();
						controller.INIT_CONTROLLER(stage , socket);		
					}
					else if(scene.equals(Statics.SERVER_MAIN_FXML))
					{
						final Parent root = loader.load();
						stage.setScene(new Scene(root));
						stage.setResizable(true);
						stage.setTitle("ChatWindow");
						stage.setX(500);
						stage.setY(150);
						stage.setHeight(720);
						stage.setWidth(850);
						stage.setMaxHeight(720);
						stage.setMaxWidth(850);
						stage.show();
						ServerMainUicontroller controller = loader.getController();
						System.out.println(controller.toString());
						controller.INIT_CONTROLLER(stage);	
					}
					else if(scene.equals(Statics.CLIENTS_EXPLAN_FXML))
					{
						final Parent root = loader.load();
						stage.setScene(new Scene(root));
						stage.setResizable(true);
						stage.setTitle("EXPLANTION");
						stage.setX(650);
						stage.setY(190);
						stage.setHeight(460);
						stage.setWidth(680);
						stage.setMaxHeight(460);
						stage.setMaxWidth(680);
						stage.show();
						ClientExplantioncontroller controller = loader.getController();
						System.out.println(controller.toString());
						controller.INIT_CONTROLLER(stage);
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
	public static void WindiwModalPane(String scene , String windowname)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(scene));
					Parent root = (Parent)loader.load();
					Stage stage = new Stage();
					stage.initModality(Modality.WINDOW_MODAL);
					stage.setTitle(windowname);
					stage.setScene(new Scene(root));
					stage.show();
					
					
					ServerStudyUicontroller controller = loader.getController();
					controller.INIT_CONTROLLER(stage);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	

	
}
