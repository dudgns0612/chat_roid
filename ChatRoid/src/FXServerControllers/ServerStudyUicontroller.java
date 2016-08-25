package FXServerControllers;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import com.orsoncharts.util.json.JSONObject;
import com.sun.net.httpserver.Authenticator.Success;

import FXClientsControllers.ClientsStudycontroller;
import Files.FileStatics;
import Study.MachineStudy;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import jdk.nashorn.internal.ir.CatchNode;
import sun.rmi.runtime.NewThreadAction;
import tools.CustomDialog;
import tools.NetworkProtocols;
import tools.Scenecontroll;
import tools.Statics;
import tools.Toolbox;

public class ServerStudyUicontroller implements Initializable{
	//fx변수
	Stage stage;
	@FXML ComboBox<String> fileselectCombo;
	@FXML TextField filename;
	@FXML Label currnetLabel;
	@FXML ProgressIndicator progress;
	@FXML Button startBtn;
	
	ObservableList<String> option = FXCollections.observableArrayList("선    택","기본텍스트","카카오텍스트");
	
	//controller변수
	String textpath;
	int selectfile;
	
	//파일스트림
	ObjectInputStream oi;
	ObjectOutputStream oo;
	
	//그 외 변수
	public static int idx = -100;
	public static String currentstr;
	Socket filesocket;
	File file;

	
	public void INIT_CONTROLLER(Stage stage)
	{
		this.stage = stage;
		fileselectCombo.setItems(option);
	}
	

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	@FXML
	private void onfileSelectBtn()
	{	
	
		if(selectfile == 0 || selectfile == -1)
		{
			CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알    림", "파일 선택 오류", "파일의 종류를 선택하여 주세요.");
		}
		else
		{
			FileChooser filechooser = new FileChooser();
			filechooser.setInitialDirectory(new File("C:\\ChatroidFIle"));
			filechooser.getExtensionFilters().add(new ExtensionFilter("TEXT", "*.txt"));
			filechooser.setTitle("텍스트파일 선택");
			file = filechooser.showOpenDialog(stage);
			
			if(file == null)
			{
				return;
			}
			else
			{
				try {
					filesocket = new Socket(FileStatics.FSERVER_IP, FileStatics.FSERVER_PORT);
					if(filesocket != null)
					{
						System.out.println("파일 소켓 연결");
						new Listener().start();
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Platform.runLater(new Runnable() {		
					@Override
					public void run() {
						textpath = file.getPath();
						filename.setText(textpath);
						sendProtocol(Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_REQUEST));	
					}
				});
			

			}
		}
	}
	
	
	@FXML
	private void onfileSelectCancleBtn()
	{
		if(filesocket != null)
		{
			try {
				fileselectCombo.getSelectionModel().select(-1);
				oi.close();
				oo.close();
				filesocket.close();
				filename.setText("");
				progress.setProgress(0);
				currnetLabel.setText("-- -- -- --");
				idx = - 100;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알    림", "파일 선택 에러", "선택하신 파일이 없습니다.");
		}
		
	}

	@FXML
	private void onStart()
	{
		startBtn.setDisable(true);
		
		if(file != null)
		{
			if(selectfile == 1)
			{
				textpath = file.getPath();
				JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_BASIC_STUDY_REQUEST);
				json.put("filename", textpath);
				sendProtocol(json);
	
			}
			else
			{
				textpath = file.getPath();
				System.out.println(textpath);
				JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_KAKAO_STUDY_REQUEST);
				json.put("filename", textpath);
				sendProtocol(json);
			}
		}
		else
		{ 
			startBtn.setDisable(false);
			CustomDialog.customOkDialog(Statics.CUSTOM_OK_DIALOG_FXML, "알림창", "경    고", "선택하신 파일이 없습니다.");
		}
		
	
	}
	
	class Listener extends Thread 
	{
		public void run()
		{
			try{
				oo = new ObjectOutputStream(filesocket.getOutputStream());
				oi = new ObjectInputStream(filesocket.getInputStream());	
				while(true)
				{
					JSONObject request= (JSONObject) oi.readObject();
					String type = request.get("type").toString();
					if(type.equals(NetworkProtocols.FILE_SEND_STATE_RESPOND))
					{
						new Thread(new Runnable() {	
							@Override
							public void run() {
								Platform.runLater(new Runnable() {							
									@Override
									public void run() {
										String state = request.get("state").toString();
										System.out.println(state);
										currnetLabel.setText(state);
										for(int i = 0+idx ; i < idx+100 ; i++)
										{
											progress.setProgress((double)i / (double)899);					
											if(i == 889)
											{
												Platform.runLater(new Runnable() {	
													@Override
													public void run() {
														CustomDialog.customOkDialog(Statics.CUSTOM_OK_DIALOG_FXML, "학습 확인 창", "알    림!" , "입력하신 질문과 답변이 학습 되었습니다.");
														filename.setText("");
														progress.setProgress(0);
														currnetLabel.setText("-- -- -- --");
														sendProtocol(Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_SUCCESS_REQUEST));
														idx = -100;
													}
												});	
											}
										}								
										idx += 100;	
									}
								});		
								
							}
						}).start();
						
					}
					else if(type.equals(NetworkProtocols.FILE_SEND_SUCCESS_RESPOND))
					{
						Platform.runLater(new Runnable() {					
							@Override
							public void run() {
								fileselectCombo.getSelectionModel().select(-1);
								startBtn.setDisable(false);
							}
						});
						filesocket.close();
					}
				}
			}catch (SocketException e){
				try {
					oo.close();
					oi.close();
					System.out.println("학습 후 정상 종료");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
			
		}
		
	}
	public void sendProtocol(JSONObject json)
	{
		try {
			oo.writeObject(json);
			oo.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void onCancle()
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(!currnetLabel.getText().trim().contains("-- --"))
				{
					CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "경    고", "파일을 학습중입니다. 잠시만 기다려주세요.");
				}
				else
				{
					if(filesocket != null)
					{
						try {
							oi.close();
							oo.close();
							filesocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					stage.close();
				}
			}
		});
	}
	
	@FXML
	private void onSelectCombo()
	{
		selectfile = fileselectCombo.getSelectionModel().getSelectedIndex();
		System.out.println(selectfile);
	}
	
	
}
