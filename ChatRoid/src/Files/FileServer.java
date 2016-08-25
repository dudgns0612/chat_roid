package Files;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.ArrayList;

import com.orsoncharts.util.json.JSONObject;

import FXClientsControllers.ClientsStudycontroller;
import FXServerControllers.ServerMainUicontroller;
import Study.MachineStudy;
import Study.SwearDiscriminatar;
import javafx.application.Platform;
import tools.Batchhandler;
import tools.CustomDialog;
import tools.NetworkProtocols;
import tools.Statics;
import tools.Toolbox;
import tools.Worddivide;

public class FileServer {
	public ServerSocket ssc;
	Socket socket;
	public Connect fileserver;
	String filename;
	
	public FileServer()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ssc = new ServerSocket(FileStatics.FSERVER_PORT);
					System.out.println("파일서버 오픈");
					while(true)
					{
						socket = ssc.accept();
						if(socket != null)
						{
							System.out.println("클라접속 : "+ socket.toString());
							fileserver = new Connect();
							fileserver.start();
						}
					}
				} catch (SocketException e){
					System.out.println("퍼일서버 다운");
				} catch (IOException e) {
					e.printStackTrace();
				}
			
				
			}
		}).start();
	}
	
	public class Connect extends Thread
	{
		File oneFile = new File(Statics.FILE_ONE_STUDY_FILE);
		BufferedWriter bw;
		byte bytes[];
		public ObjectInputStream oi;
		public ObjectOutputStream oo;
		String fname; // 파일이름 날짜 및 번호
		JSONObject request;
		int bytesize;
		ArrayList<String> wordlist = new ArrayList<>();
		Batchhandler batchhandle = new Batchhandler();
		
		public void closeServer()
		{
			try{
				synchronized (fileserver) {
					ssc.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}	
		public void run()
		{
			System.out.println("파일 커넥 ");
			try {
				oo = new ObjectOutputStream(socket.getOutputStream());
				oi = new ObjectInputStream(socket.getInputStream());
				System.out.println("파일 스트림연결");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				while(true)
				{
					request = (JSONObject) oi.readObject();
					String type = request.get("type").toString();
					
					if(type.equals(NetworkProtocols.FILE_SERVER_RECIEBER_TEXT_REQUEST))
					{	
						bytesize = (int) request.get("bytesize");
						System.out.println("바이트사이즈"+bytesize);	
						
						JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SERVER_RECIEBER_TEXT_RESPOND);
						SendProtocol(json);
					}
					else if(type.equals(NetworkProtocols.FILE_SERVER_BASIC_SEND_REQUEST))
					{
						bytes = new byte[bytesize];
						for(int i=0; i < bytesize ; i++)
						{
							bytes[i] = (byte)oi.read();
							//System.out.println(i);
						}	
						File filedirectory = new File(Statics.USER_BASIC_FILE);
						fname = Statics.USER_BASIC_FILE+"\\"+Toolbox.getTimeFIle()+".txt";
						System.out.println(fname);
						File userbasic = new File(fname);
						if(filedirectory.mkdir())
						{
							System.out.println("파일이 생성 되었습니다.");
							Files.write(userbasic.toPath(), bytes);
						}
						else
						{
							Files.write(userbasic.toPath(), bytes);
						}
						//Worddivide.SwearBatch(fname);
						batchhandle.morphemeanalysis(fname);
						wordlist = Worddivide.morphemeDivide(fname, Statics.USER_BASIC_FILE+"\\test.txt", Statics.CONNECTWORD);
					}
					else if(type.equals(NetworkProtocols.FILE_SERVER_KAKAO_SEND_REQUEST))
					{
						bytes = new byte[bytesize];
						for(int i=0; i < bytesize ; i++)
						{
							bytes[i] = (byte)oi.read();
							//System.out.println(i);
						}
						File filedirectory = new File(Statics.USER_KAKAO_FILE);
						fname = Statics.USER_KAKAO_FILE+"\\"+Toolbox.getTimeFIle()+".txt";
						File userkakao = new File(fname);
						if(filedirectory.mkdir())
						{
							System.out.println("파일이 생성 되었습니다.");
							Files.write(userkakao.toPath(), bytes);
						}
						else
						{
							Files.write(userkakao.toPath(), bytes);
						}
					}
					else if(type.equals(NetworkProtocols.FILE_SEND_SUCCESS_REQUEST))
					{
						if(SwearDiscriminatar.skipSwear(wordlist , oo, Statics.FILE))
						{
							SendProtocol(Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_SUCCESS_RESPOND));
						}
						else
						{
							SendProtocol(Toolbox.JsonRequest(NetworkProtocols.FILE_SWEAR_INCLUDE_REQUEST));
							Toolbox.deleteFile(fname);
						}
					}
					else if(type.equals(NetworkProtocols.WORD_STUDY_ONE_PROTOCOL_REQUEST))
					{
						if(oneFile.mkdir())
						{
							oneFile.createNewFile();
							System.out.println("파일이 생성 되었다는 창 띄우기");
						}
						filename = Statics.FILE_ONE_STUDY_FILE+"\\"+Toolbox.getTimeFIle()+".txt";
						bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
				    	bw.write(request.get("질문").toString());
				    	bw.newLine();
						bw.write(request.get("답변").toString());
						bw.flush();
						bw.close();	
						SendProtocol(Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND));
						batchhandle.morphemeanalysis(filename);
						wordlist = Worddivide.morphemeDivide(filename, Statics.FILE_ONE_STUDY_FILE+"\\test1.txt", Statics.CONNECTWORD);
						Toolbox.deleteFile(Statics.FILE_ONE_STUDY_FILE+"\\test1.txt");
						if(SwearDiscriminatar.skipSwear(wordlist , oo , Statics.WORD))
						{
							new MachineStudy("basic","client",filename , oo);	
						}
						else
						{
							SendProtocol(Toolbox.JsonRequest(NetworkProtocols.WORD_SWEAR_INCLUDE_REQUEST));
						}	
					}
					else if(type.equals(NetworkProtocols.FILE_BASIC_STUDY_REQUEST))
					{
						String filename = request.get("filename").toString();
						new MachineStudy("basic","server",filename , oo);
						
					}
					else if(type.equals(NetworkProtocols.FILE_SEND_STATE_REQUEST))
					{
						JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND);
						json.put("state", "준비");
						SendProtocol(json);	
						
					}
					else if(type.equals(NetworkProtocols.FILE_KAKAO_STUDY_REQUEST))
					{
						String filename = request.get("filename").toString();
						new MachineStudy("kakao","server",filename , oo);
					}
				}
				
			} catch (SocketException e){
			}catch (EOFException e) {
					System.out.println("파일 전송 완료 클라이언트 종료");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		public JSONObject SendProtocol(JSONObject json)
		{
			try {
				oo.writeObject(json);
				oo.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return json;
		}
	}
	

	


}
