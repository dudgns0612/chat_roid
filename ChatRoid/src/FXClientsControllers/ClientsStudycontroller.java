package FXClientsControllers;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ResourceBundle;
import com.orsoncharts.util.json.JSONObject;

import Files.FileStatics;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import tools.CustomDialog;
import tools.NetworkProtocols;
import tools.Scenecontroll;
import tools.Statics;
import tools.Toolbox;

public class ClientsStudycontroller implements Initializable{
	
	Stage stage;
	Socket socket;
	@FXML TextField answerField;
	@FXML TextField questField;
	@FXML TextField filenameField;
	@FXML ProgressBar progressBar;
	@FXML ProgressBar progressBar2; // 하나의 질문 학습
	@FXML ComboBox<String> fileCombo = new ComboBox<>();
	@FXML Label stateLabel;
	@FXML Label stateLabel2;
	@FXML Button oneStudyBtn;
	@FXML Button fileStudyBtn;
	ObservableList<String> option = FXCollections.observableArrayList("선   택", "기본 텍스트" , "카카오톡 텍스트");
	
	

	Listener listenner;
	
	//Stream
	ObjectOutputStream oo;
	ObjectInputStream oi;
	
	//file Stream
	ObjectOutputStream foo;
	ObjectInputStream foi;
	
	//file
	byte[] bytes;
	Socket fsocket;
	int bytesize;
	int textsize;
	int selectfile;
	File file;

	boolean fileservercheck = true;
	
	public void INIT_CONTROLLER(Stage stage , Socket socket)
	{
		this.socket = socket;
		this.stage = stage;
		oo = Scenecontroll.oo;
		oi = Scenecontroll.oi;
		fileCombo.setItems(option);
		new Listener().start();
		SendProtocol(Toolbox.JsonRequest(NetworkProtocols.SERVER_CONNECT_CHECK_REQUEST));
	}
	

	
	//심심이서버 리스너
	class Listener extends Thread
	{
		public void run()
		{
			while(true)
			{
				try {			
					JSONObject request = (JSONObject)oi.readObject();
						
					String type = request.get("type").toString();
					
					if(type.equals(NetworkProtocols.LISTENER_CLOSE_RESPOND))
					{
						
						System.out.println("학습창 꺼짐");
						return;
					}
					else if(type.equals(NetworkProtocols.SERVER_CONNECT_CHECK_RESPOND))
					{
						System.out.println("fileserver");
						fileserverStart();
					}
				} catch (SocketException e){
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							int ok = CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "서버에러", "서버와 접속이 끊어졌습니다.");
							if(ok == Statics.OK_SELECTION)
							{
								System.exit(0);
							}
						}
					});
					break;
				} catch(EOFException e) {
					Platform.runLater(new Runnable() {		
						@Override
						public void run() {
							int ok = CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "서버에러", "서버와 접속이 끊어졌습니다.");
							if(ok == Statics.OK_SELECTION)
							{
								System.exit(0);
							}	
						}
					});
					break;
				}  catch (IOException e){
					e.printStackTrace();	
				} catch (ClassNotFoundException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void fileserverStart()
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					fsocket = new Socket(CustomInputDialogController.inputword, FileStatics.FSERVER_PORT);	
					if(fsocket != null)
					{
						new FileListener().start();
						System.out.println("파일 서버 접속 완료");	
						fileservercheck = true;
					}
					else
					{
						System.out.println("파일 소켓 접속오류 다이얼로그");
					}
				} catch(SocketException e){	
					System.out.println(socket.isConnected());
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							int ok = CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알    림", "파일서버 접속오류", "파일서버 점검중입니다. 잠시후에 시도하여 주세요.");
							if(ok == Statics.OK_SELECTION)
							SendProtocol(Toolbox.JsonRequest(NetworkProtocols.LISTENER_CLOSE_REQUEST));
						}
					});
					Scenecontroll.changeScene(Statics.MAIN_FXML);
				}catch (UnknownHostException e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							int OK = CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "서버 호스트 에러", "서버의 주소가 올바르지 않습니다. 다시 접속하여 주세요.");
							if(OK == Statics.OK_SELECTION)
							{
								 System.exit(0);
							}
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}								
			}
		});				
	}
	
	
	private void SendProtocol(JSONObject json)
	{
		try {
			oo.writeObject(json);
			oo.flush();
		} catch (IOException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					int ok = CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "서버에러", "서버와 접속이 끊어졌습니다.");
					if(ok == Statics.OK_SELECTION)
					{
						System.exit(0);
					}
				}
			});
		}
	}
	

	
	
	//질문 과 답변 하나 씩을 저장하는 버튼
	@FXML
	private void onOneBtn()
	{
		String answer = answerField.getText().trim();
		String question = questField.getText().trim();
		stateLabel2.setText("욕설 판별중....^^");
		System.out.println(answer+":"+question);
		//둘다 널이 아니면
		if(!(answer.equals("")) && !(question.equals("")))
		{
			String[] keys = {"질문","답변"};
			String[] values = {question,answer};
			
			JSONObject json = Toolbox.JsonRequest(NetworkProtocols.WORD_STUDY_ONE_PROTOCOL_REQUEST, keys, values);
			System.out.println("출력 : "+json.toString());
			SendFileProtocol(json);
		}
		else
		{
			CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "경    고", "질문과 답변 모두 입력하여 주세요.");
		}
	}
	
	//취소 버튼 
	@FXML
	private void onCancle()
	{
		Platform.runLater(new Runnable() {	
			@Override
			public void run() {
				if(fileservercheck)
				{
					if(!(stateLabel.getText().trim().contains("업로드")) && !(stateLabel2.getText().trim().contains("학습중")))
					{
						try {
							foi.close();
							foo.close();
							fsocket.close();
						} catch(SocketException e){
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						fileservercheck = false;
					}
				}
				SendProtocol(Toolbox.JsonRequest(NetworkProtocols.LISTENER_CLOSE_REQUEST));
				Scenecontroll.changeScene(Statics.MAIN_FXML);	
			}
		});
	}
	
	//텍스트 파일로 학습하는 버튼
	@FXML
	private void onFileBtn()
	{
		if(file == null)
		{
			CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "경    고", "파일을 선택하지 않았습니다.");
			return;
		}
		else
		{
			fileStudyBtn.setDisable(true);
			if(selectfile == 1)
			{
				JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SERVER_RECIEBER_TEXT_REQUEST);
				json.put("bytesize", bytesize);				
				SendFileProtocol(json);
				stateLabel.setText("업로드중...^^");
			}
			else if(selectfile == 2)
			{
				JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SERVER_RECIEBER_TEXT_REQUEST);
				json.put("bytesize", bytesize);				
				SendFileProtocol(json);
				stateLabel.setText("업로드중....^^");
			}
		}
	}
	
	//텍스트 파일 선택버튼
	@FXML
	private void onFileSelectBtn()
	{	
		Platform.runLater(new Runnable() {	
			@Override
			public void run() {	
				FileChooser filecho = new FileChooser();
				filecho.setTitle("파일 선택");	
				filecho.getExtensionFilters().add(new ExtensionFilter("TXT", "*.txt"));
				if(selectfile == 0)
				{
					CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "경    고" , "파일의 종류를 선택 해 주세요,");
				}
				else if(selectfile == 1)
				{
					file = filecho.showOpenDialog(stage);
					if(file == null)
					{
						return;
					}
					else
					{
						boolean encoding = Toolbox.fileEncodingConfirm(file); //파일 인코딩 확인
						if(encoding)
						{
							String filename = file.getPath();
						    filenameField.setText(filename);
							try {
								bytes = Files.readAllBytes(file.toPath());
								bytesize = bytes.length;	
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "파일 인코딩 오류", "파일 인코딩을 UTF-8로 해주세요.");
							fileCombo.getSelectionModel().select(0);
						}
					}
					
				}
				else if(selectfile == 2)
				{
					file = filecho.showOpenDialog(stage);
					if(file == null)
					{
						return;
					}
					else
					{
						boolean encoding = Toolbox.fileEncodingConfirm(file); //파일 인코딩 확인
						if(encoding)
						{
							String filename = file.getPath();
						    filenameField.setText(filename);
							try {
								bytes = Files.readAllBytes(file.toPath());
								bytesize = bytes.length;	
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창", "파일 인코딩 오류", "파일 인코딩을 UTF-8로 해주세요.");
							fileCombo.getSelectionModel().select(0);
							
						}
						
					}
				}
			}
		});
	}
	
	
	//파일 서버 리스너
	class FileListener extends Thread
	{
		int idx = 0;
		JSONObject frequest;
		
		public void run()
		{
			try {
				foo = new ObjectOutputStream(fsocket.getOutputStream());
				foi = new ObjectInputStream(fsocket.getInputStream());
				System.out.println("파일 스트림 연결 클라");
				while(true)
				{
					frequest = (JSONObject)foi.readObject();
					String type = frequest.get("type").toString();
					if(type.equals(NetworkProtocols.FILE_SERVER_RECIEBER_TEXT_RESPOND))
					{
						if(selectfile == 1)
						{
							JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SERVER_BASIC_SEND_REQUEST);
							SendFileProtocol(json);
							for(int i=0; i < bytesize ; i++)
							{
								foo.write(bytes[i]);
								foo.flush();
								//System.out.println(i);
								progressBar.setProgress((double)i / (double)bytesize);
							}
							JSONObject jsono = Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_SUCCESS_REQUEST);
							SendFileProtocol(jsono);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									stateLabel.setText("파일 욕설 판별중..");	
								}
							});
							
						}
						else
						{
							JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SERVER_KAKAO_SEND_REQUEST);
							SendFileProtocol(json);
							for(int i=0; i < bytesize ; i++)
							{
								foo.write(bytes[i]);
								foo.flush();
								//System.out.println(i);
								progressBar.setProgress((double)i / (double)bytesize);
							}
							JSONObject jsono = Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_SUCCESS_REQUEST);
							SendFileProtocol(jsono);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									stateLabel.setText("파일 욕설 판별중..");	
								}
							});
						
						}
					}
					else if(type.equals(NetworkProtocols.FILE_SEND_SUCCESS_RESPOND))
					{	
						CustomDialog.customOkDialog(Statics.CUSTOM_OK_DIALOG_FXML, "업로드 확인 창", "알    림!", "서버에 업로드가 완료 되었습니다. 심의 후 적용 됩니다.");
						Platform.runLater(new Runnable() {		
							@Override
							public void run() {
								fileStudyBtn.setDisable(false);
								filenameField.setText("");
								progressBar.setProgress(0);
								stateLabel.setText("-- -- --");		
								fileCombo.getSelectionModel().select(0);
								if(!fileservercheck)
								{
									try {
										foi.close();
										foo.close();
										fsocket.close();
									} catch(SocketException e){
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									fileservercheck = true;
								}
							}
						});
					}
					else if(type.equals(NetworkProtocols.FILE_SEND_STATE_RESPOND))
					{
						new Thread(new Runnable() {
							@Override
							public void run() {
								
								for(int i = 0+idx ; i < idx+100 ; i++)
								{
									progressBar2.setProgress((double)i / (double)1000);
									if(idx == 300)
									{
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												stateLabel2.setText("학습중....^^");
												
											}
										});
									}
									if(i == 1099)
									{
										Platform.runLater(new Runnable() {	
											@Override
											public void run() {
												oneStudyBtn.setDisable(false);
												progressBar2.setProgress(0);
												answerField.setText("");
												questField.setText("");
												stateLabel2.setText("-- -- --");
												CustomDialog.customOkDialog(Statics.CUSTOM_OK_DIALOG_FXML, "학습 확인 창", "알    림!" , "입력하신 질문과 답변이 학습 되었습니다.");
												idx = 0;
											}
										});	
									}
								}								
								idx += 100;
							}
						}).start();
					}
					else if(type.equals(NetworkProtocols.WORD_SWEAR_INCLUDE_REQUEST))
					{
						Platform.runLater(new Runnable() {	
							@Override
							public void run() {
								CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창","비적합 단어 포함", "적합하지 않은 "
										+ "단어가 포함되어 있습니다.");
								oneStudyBtn.setDisable(false);
								progressBar2.setProgress(0);
								answerField.setText("");
								questField.setText("");
								stateLabel2.setText("-- -- --");
								idx = 0;
							}
						});
					}
					else if(type.equals(NetworkProtocols.FILE_SWEAR_INCLUDE_REQUEST))
					{
						Platform.runLater(new Runnable() {	
							@Override
							public void run() {
								CustomDialog.customWarringDialog(Statics.CUSTOM_WARNING_DIALOG_FXML, "알림창","비적합 단어 포함 학습불가", "적합하지 않은 "
										+ "단어가 포함되어 있습니다. ");
								fileStudyBtn.setDisable(false);
								filenameField.setText("");
								progressBar.setProgress(0);
								stateLabel.setText("-- -- --");		
								fileCombo.getSelectionModel().select(0);
							
							}
						});
					}
			    }
				
					
				
			} catch(SocketException e){	
				System.out.println("소켓 정상종료");
				try {
					foo.close();
					foi.close();
					fsocket.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}	
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e){
				e.printStackTrace();
				
			}
			
			
		}
	}
	

	private void SendFileProtocol(JSONObject json)
	{
		try {
			foo.writeObject(json);
			foo.flush();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onFileSelect()
	{
		selectfile = fileCombo.getSelectionModel().getSelectedIndex();
		//System.out.println(selectfile);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
}
