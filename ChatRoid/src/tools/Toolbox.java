package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import com.orsoncharts.util.json.JSONObject;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;



public class Toolbox  {
	static BufferedReader br;
	static BufferedWriter wr;
	static int serverstate;
	
	@SuppressWarnings("unchecked")
	public static JSONObject JsonRequest(String type)
	{
		JSONObject json = new JSONObject();
		
		json.put("type", type);
		
		return json;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject JsonRequest(String type ,String[] keys, String[] values)
	{
		JSONObject json = new JSONObject();
		
		json.put("type", type);
		
		for(int i = 0 ; i  < keys.length ; i++)
		{
			json.put(keys[i], values[i]);
		}
		
		return json; 
	}
	
	
	public static BufferedReader kakaoDelete(BufferedReader br)
	{
		//System.out.println("딜리트 실행");
		HashSet<Integer> a = new HashSet<Integer>();
		

		a.toArray(new Integer[0]);
		String word;
		try{
			word = br.readLine().split("\t")[0];
				while(true)
				{
					if(word.contains("]/SW"))
					{
						break;
					}
					else if(word.contains("------"))
					{
						break;
					}
					else
					{
						word= br.readLine();
						System.out.println("\t걸러내는문장들 : "+word);
					}
				}
			
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		return br;
	}
	
	
	public static String getMessage(String word)
	{
		String str ="";
		int cnt_check = 0;
		int change;
		for(int i=0;i<word.length();i++)
		{
			if(word.charAt(i)==']')
			{
				cnt_check++;
				if(cnt_check==2)
				{
					change = i;
					str = word.substring(change+2 , word.length());
				}
			}
		}
		
		return str;
	}
	
	
	public static String getName(String word)
	{
		int cnt = 0;
		int lastname =0;
		String name ="";
		for(int i=0;i<word.length();i++)
		{
			if(cnt == 0)
			{
				if(word.charAt(i)==']')
				{
					lastname = i;
					cnt++;
				}
			}
		}
		for(int i= 1 ; i < lastname; i++)
		{
			name = word.substring(1, lastname);
		}
		return name;
	}
	
	public static JSONObject rankingCompare(ArrayList<String> Queslist, ArrayList<String> MachineList)
	{
		JSONObject json = new JSONObject();
 		boolean check = true;
		String Q = "";
		String A = "";
		String Q1 = "";
		String A1 = "";
		int score = 0;
				
		
		for(String word : Queslist)
		{
			if(word.contains("문장 : "))
			{
				Q = word;
			}
		}
		for(String word : MachineList)
		{
			if(word.contains("문장 : "))
			{
				A = word;
			}
		}
		
		if(Q.equals(A))
		{
			score = 100;
			json.put("score", 100);
			json.put("check", check);
			json.put("arr_list", MachineList);
		}
		else
		{
			for(String qus : Queslist)
			{
				if(qus.contains("/NNG"))
				{
					Q1= qus;
					for(String ans : MachineList)
					{
						if(ans.contains("/NNG"))
						{
							A1 = ans;
							if(Q1.equals(A1))
							{
								System.out.println("명사 맞음 : "+ans);
								score += 60;
							}
						}
					}
				}
			}
			
			for(String qus : Queslist)
			{
				if(qus.contains("/VV"))
				{
					Q1= qus;
					for(String ans : MachineList)
					{
						if(ans.contains("/VV"))
						{
							A1 = ans;
							if(Q1.equals(A1))
							{
								score += 30;
							}
						}
					}
				}
			}
			
			for(String qus : Queslist)
			{
				if(qus.contains("/UNK"))
				{
					Q1= qus;
					//System.out.println("질문 : "+Q1);
					for(String ans : MachineList)
					{
						if(ans.contains("/UNK"))
						{
							A1 = ans;
							if(Q1.equals(A1))
							{
								score += 50;
							}
						}
					}
				}
			}
			
			
			for(String qus : Queslist)
			{
				if(qus.contains("문장 : "))
				{
					Q1= qus.split("문장 : ")[1];
					for(String ans : MachineList)
					{
						if(ans.contains("문장 :  "))
						{
							A1 = ans.split("문장 :  ")[1];
							for(int i = 0 ; i < Q1.length(); i++)
							{
								for(int j = 0 ; j< A1.length(); j++)
								{
									if(Q1.charAt(i)==A1.charAt(j))
									{
										if(!(Q1.charAt(i)+"").equals(" "))
										{
											score += 15;
										}
									}
									else
									{
										if(!(Q1.charAt(i)+"").equals(" "))
										{
											score -= 1;
										}	
									}
								}
							}
						}
					}
				}
			}
			check = false;
			json.put("check", check);
			json.put("score", score);
			json.put("arr_list", MachineList);
		}
		
		return json;

 }


	public static boolean wordDivide(String filename,String C , ObjectOutputStream oo ,String type)
	{
		
		if(C.equals("2"))
		{
			System.out.println("카카오톡 텍스트 학습 시작");
		}
		
		
		String name;
		String word_plus;
		boolean check = true;
		try {
			if(type.equals("client"))
			{
				oo.writeObject(Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND));
				oo.flush();
			}
			else
			{
				JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND);
				json.put("state", "질문 답변 나누는중...");
				oo.writeObject(json);
				oo.flush();
			}
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
			wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Statics.FILE_CONNECT_TEXT),"utf-8"));
			
			
			String Q = "";
			String A = "";
			
			String word = br.readLine();
			name = Toolbox.getName(word);
			word_plus = Toolbox.getMessage(word);
			
			int cnt = 0;
			
			while(true)
			{	
				String str = br.readLine();
		
				if(str == null)
				{
					Q = word_plus;
					wr.append(A+ "::" +Q);
					wr.newLine();
					wr.flush();
					break;
				}
				
				if(!name.equals(Toolbox.getName(str)))
				{
					cnt++;
					if(cnt%2 == 0)
					{
						A = word_plus;
						wr.append(Q+ "::" +A);
						wr.newLine();
						wr.flush();
					}
					else
					{
						Q = word_plus;
						if(A.length()!=0)
						{
							wr.append(A+ "::" +Q);
							wr.newLine();
							wr.flush();
						}
					}
					word_plus = Toolbox.getMessage(str);
					name = Toolbox.getName(str);
				}
				else
				{
					word_plus += Toolbox.getMessage(str);
				}
	
			}
			if(type.equals("client"))
			{
				oo.writeObject(Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND));
				oo.flush();
			}
			else
			{
				JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND);
				json.put("state", "질문 답변 나누는중...");
				oo.writeObject(json);
				oo.flush();
			}
			wr.close();
			br.close();
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	
	 	return check;
	}
	
	
	public static boolean wordDivide(String filename , ObjectOutputStream oo ,String type)
	{
		boolean check = true;
		try {
			if(type.equals("client"))
			{
				oo.writeObject(Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND));
				oo.flush();
			}
			else
			{
				JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND);
				json.put("state", "질문 답변 나누는중...");
				oo.writeObject(json);
				oo.flush();
			}
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
			wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Statics.FILE_CONNECT_TEXT),"utf-8"));
			
			
			String Q = "";
			String A = "";
			
			int cnt = 0;
			
			while(true)
			{	
				cnt++;
				String word = br.readLine();

				if(word == null)
				{
					break;
				}
				else
				{
					if(cnt%2 == 0)
					{
						A = word;
						wr.append(Q+ "::" +A);
						wr.newLine();
						wr.flush();
					}
					else
					{
						Q = word;
						if(A.length()!=0)
						{
							wr.append(A+ "::" +Q);
							wr.newLine();
							wr.flush();
						}
					}
				}
	
			}
			wr.close();
			br.close();
			if(type.equals("client"))
			{
				oo.writeObject(Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND));
				oo.flush();
			}
			else
			{
				JSONObject json = Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND);
				json.put("state", "질문 답변 나누는중...");
				oo.writeObject(json);
				oo.flush();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	
	 	return check;
	}


	public static String getTime()
	{
		Calendar cal = Calendar.getInstance();
		
		SimpleDateFormat time =new SimpleDateFormat("a hh:mm");
		
		String datetime = time.format(cal.getTime());
			
		return datetime;
	}
	
	
	public static String getTimeFIle()
	{
		Calendar cal = Calendar.getInstance();
		
		SimpleDateFormat time =new SimpleDateFormat("yyyyMMddhhmmss");
		
		String datetime = time.format(cal.getTime());
			
		return datetime;
	}
	
	
	public static ObservableList<VBox> chatAreaWrite(String check, String msg, ObservableList<VBox> observableList)
	{
		String firstMsg;
		String twoMsg;
		VBox box = new VBox();
		if(check.equals("User"))
		{
			if(msg.length()<25)
			{
				
				Label label = new Label("User");
				label.setFont(new Font("Arial" , 20));
				label.setPrefHeight(Control.USE_COMPUTED_SIZE);
				label.setPrefWidth(Control.USE_COMPUTED_SIZE);
				label.setAlignment(Pos.CENTER_RIGHT);
				Label label1 = new Label(msg);
				label1.setPrefHeight(Control.USE_COMPUTED_SIZE);
				label1.setPrefWidth(Control.USE_COMPUTED_SIZE);
				label1.setAlignment(Pos.CENTER_RIGHT);
				label1.setStyle("-fx-background-color :linear-gradient(to bottom, rgba(254,252,234,1) 0%,rgba(254,252,234,1) 0%,rgba(254,252,234,1) 0%,rgba(241,218,54,1) 100%);"
						+ "  -fx-padding : 7 ; -fx-background-radius:7 ;");
				Label time = new Label(getTime());
				time.setPrefHeight(Control.USE_COMPUTED_SIZE);
				time.setPrefWidth(Control.USE_COMPUTED_SIZE);
				time.setAlignment(Pos.CENTER_RIGHT);
				time.setFont(new Font("Arial", 10));
				time.setOpacity(0.5);
				box.setMaxHeight(100);
				box.setMaxWidth(365);
				box.setAlignment(Pos.CENTER_RIGHT);
				box.getChildren().addAll(label,label1,time);
				observableList.add(box);
			}
			else
			{
				firstMsg = msg.substring(0,26);
				twoMsg = msg.substring(26, msg.length()-1);
				Label label = new Label("User");
				label.setFont(new Font("Arial" , 20));
				label.setPrefHeight(Control.USE_COMPUTED_SIZE);
				label.setPrefWidth(Control.USE_COMPUTED_SIZE);
				label.setAlignment(Pos.CENTER_RIGHT);
				Label label1 = new Label(firstMsg);
				label1.setMaxHeight(100);
				label1.setMaxWidth(365);
				label1.setStyle("-fx-background-color : linear-gradient(to bottom, rgba(254,252,234,1) 0%,rgba(254,252,234,1) 0%,rgba(254,252,234,1) 0%,rgba(241,218,54,1) 100%);"
						+ "   -fx-padding : 9 ; -fx-background-radius:7 ; -fx-background-insets : 2 ");
				Label label2 = new Label(twoMsg);
				label2.setPrefHeight(Control.USE_COMPUTED_SIZE);
				label2.setPrefWidth(Control.USE_COMPUTED_SIZE);
				label2.setAlignment(Pos.CENTER_RIGHT);
				label2.setStyle("-fx-background-color : linear-gradient(to bottom, rgba(254,252,234,1) 0%,rgba(254,252,234,1) 0%,rgba(254,252,234,1) 0%,rgba(241,218,54,1) 100%);"
						+ "   -fx-padding : 9 ; -fx-background-radius:7 ; -fx-background-insets : 2 ");
				Label time = new Label(getTime());
				time.setPrefHeight(Control.USE_COMPUTED_SIZE);
				time.setPrefWidth(Control.USE_COMPUTED_SIZE);
				time.setAlignment(Pos.CENTER_RIGHT);
				time.setFont(new Font("Arial", 10));
				time.setOpacity(0.5);
				box.setMaxHeight(100);
				box.setMaxWidth(390);
				box.setAlignment(Pos.CENTER_RIGHT);
				box.getChildren().addAll(label,label1,label2,time);
				observableList.add(box);
			}
		}
		else
		{
			if(msg.length()<25)
			{
				Label label = new Label("ChatRoid");
				label.setFont(new Font("Arial" , 20));
				label.setMaxHeight(100);
				label.setMaxWidth(300);
				label.setAlignment(Pos.CENTER_LEFT);
				Label label1 = new Label(msg);
				label1.setPrefHeight(Control.USE_COMPUTED_SIZE);
				label1.setPrefWidth(Control.USE_COMPUTED_SIZE);
				label1.setStyle("-fx-background-color : linear-gradient(to bottom, rgba(233,246,253,1) 0%,rgba(211,238,251,1) 100%); ; -fx-padding : 7 ; -fx-background-radius:7 ;"
						+ "-fx-background-insets : 2");
				label1.setAlignment(Pos.CENTER_LEFT);
				Label time = new Label(getTime());
				time.setPrefHeight(Control.USE_COMPUTED_SIZE);
				time.setPrefWidth(Control.USE_COMPUTED_SIZE);
				time.setAlignment(Pos.CENTER_LEFT);
				time.setFont(new Font("Arial", 10));
				time.setOpacity(0.5);
				box.setMaxHeight(100);
				box.setMaxWidth(390);
				box.setAlignment(Pos.CENTER_LEFT);
				box.getChildren().addAll(label,label1,time);
				observableList.add(box);
			}
			else
			{
				firstMsg = msg.substring(0,26);
				twoMsg = msg.substring(26, msg.length()-1);
				
				Label label = new Label("ChatRoid");
				label.setFont(new Font("Arial" , 20));
				label.setMaxHeight(100);
				label.setMaxWidth(300);
				label.setAlignment(Pos.CENTER_LEFT);
				Label label1 = new Label(firstMsg);
				label1.setPrefHeight(Control.USE_COMPUTED_SIZE);
				label1.setPrefWidth(Control.USE_COMPUTED_SIZE);
				label1.setStyle("-fx-background-color : linear-gradient(to bottom, rgba(233,246,253,1) 0%,rgba(211,238,251,1) 100%); ; -fx-padding : 9 ; -fx-background-radius:7 ; -fx-background-insets : 2 ");
				label1.setAlignment(Pos.CENTER_LEFT);
				Label label2 = new Label(twoMsg);
				label2.setPrefHeight(Control.USE_COMPUTED_SIZE);
				label2.setPrefWidth(Control.USE_COMPUTED_SIZE);
				label2.setStyle("-fx-background-color : linear-gradient(to bottom, rgba(233,246,253,1) 0%,rgba(211,238,251,1) 100%); ; -fx-padding : 9 ; -fx-background-radius:7 ; -fx-background-insets : 2");
				label2.setAlignment(Pos.CENTER_LEFT);
				Label time = new Label(getTime());
				time.setMaxHeight(100);
				time.setMaxWidth(300);
				time.setAlignment(Pos.CENTER_LEFT);
				time.setFont(new Font("Arial", 10));
				time.setOpacity(0.5);
				box.setMaxHeight(100);
				box.setMaxWidth(390);
				box.setAlignment(Pos.CENTER_LEFT);
				box.getChildren().addAll(label,label1,label2,time);
				observableList.add(box);
			}
		}
		

		
		return observableList;
	}

	
	public static int fileSize(String filename)
	{
		int size = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			while(true)
			{
				if(br.readLine() != null)
				{
					size++;
				}
				else
				{
					break;
				}
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return size;
	}
	
	public static void deleteMorpheme(String filename)
	{
		File mafile = new File(filename+".ma");
		File debugfile = new File(filename+".debug");
		
		
		mafile.delete();
		debugfile.delete();
	}
	
	public static void deleteFile(String filename)
	{
		File txtfile = new File(filename);
		
		txtfile.delete();
	}
	
	public static boolean fileEncodingConfirm(File file)
	{
		boolean check = false;
		try
		{
			byte[] BOM = new byte[4];
			FileInputStream fis = new FileInputStream(file);
			fis.read(BOM, 0, 4);

			if( (BOM[0] & 0xFF) == 0xEF && (BOM[1] & 0xFF) == 0xBB && (BOM[2] & 0xFF) == 0xBF )
			    check = true;
			else
			    check = false;
		}catch(Exception e){
			e.printStackTrace();
		}
		return check;
	}
	
	public static ArrayList<String> getFileList(String filepath , boolean deletenumber)
	{  
		ArrayList<String> list = new ArrayList<>();
		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath),"utf-8"));
			while(true)
			{
				String word = br.readLine();
			
				
				if(word == null)
				{
					break;
				}
				else
				{
					if(deletenumber)
					{
						System.out.println(word);
						if(!word.contains("문장 :  "))
						{
							word = word.split("\t")[0];
							list.add(word);
						}
					}
					else
					{
						list.add(word);
					}
				}
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	
}




