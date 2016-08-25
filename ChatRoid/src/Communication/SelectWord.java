package Communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import com.orsoncharts.util.json.JSONObject;

import Study.SwearDiscriminatar;
import tools.Batchhandler;
import tools.Statics;
import tools.Toolbox;
import tools.Worddivide;

public class SelectWord {
	
	//IO
	BufferedReader Machinestream;
	BufferedWriter bw; // 저장되 있는 질문을 가져오기위한 스트림
	BufferedReader QuesStream;
	
	JSONObject comparejson = new JSONObject();
	
	//변수
	ArrayList<String> Machinelist; // 저장 되어있는 질문을 저장하여 비교하기 위한 arraylist;
	ArrayList<String> Queslist; // 문장 하나의 형태소구문을 저장하는 arraylist;
	ArrayList<String> Anslist; // 답변을 따오기 위한 어레이리스
	ArrayList<String> Tmplist; // 임시리스트 저장 공간;
	String word = "start";
	String question;
	char one_word;
	int check;
	int nocheck;
	String a;
	String b;
	WeightCalculation wc = new WeightCalculation();
	Batchhandler batch = new Batchhandler(); // 배치핸들러 객체
	int cnt = 0 ;
	String sendmessage = "";
	
	
	public SelectWord()
	{
		
	}
	
	public void test(String msg)
	{  
		try{
			Machinestream = new BufferedReader(new InputStreamReader(new FileInputStream(Statics.FILE_QUESTION_ANSWER_TEXT),"utf-8"));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Statics.ONE_QUESTION_TXT), "utf-8"));
			QuesStream = new BufferedReader(new InputStreamReader(new FileInputStream(Statics.FILE_WRITE_NAME),"utf-8"));
			question = msg;
			one_Word(question); // 입력 문자받아서 형태소로 만듬 // testing()메소드 시작
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void testing()
	{		
		sendmessage = null;
		boolean check = false;
		System.out.println("테스트 스타트");
		Machinelist = new ArrayList<String>();
		Queslist = new ArrayList<String>();
		Anslist = new ArrayList<String>();
		String abc = null;
		int score = 0;
		int strtmp = 0;
		
		try {			
			while(true)
			{
				String word = QuesStream.readLine();
				if(word.contains("===="))
				{
					break;
				}
				else
				{
					System.out.println("돌린 형태소 : "+word);
					Queslist.add(word);
				}
			}
			while(true)
			{	
				if(check)
				{
					break;
				}
				else
				{
					Machinelist.clear();
				}
				if(word == null)
				{
					break;
				}
				else
				{
					while(true)
					{	
						word = Machinestream.readLine();
						if(word == null)
						{
							break;
						}
						if(word.contains("====="))
						{
							System.out.println("학습시킨 텍스트 : "+word);
							break;
						}
						else
						{
							Machinelist.add(word);
						}	
					}
					comparejson = Toolbox.rankingCompare(Queslist, Machinelist);

					Anslist = (ArrayList<String>)comparejson.get("arr_list");
					System.out.println(Anslist.toString());
					score = (int)comparejson.get("score");
					check = (boolean) comparejson.get("check");	
					if(Anslist!=null)
					{
						// 문장이 딱 들어 맞았을 경우
						if(check)
						{
							for(String str : Anslist)
							{
								if(str.contains("답변 : "))
								{
									abc = str.split("답변 : ")[1];
								}
							}
						}
						//점수 계산
						else
						{
							//System.out.println("전 점수 : "+ strtmp);
							//System.out.println("지금 점수 :" + score);
							if(strtmp < score)
							{
								strtmp = score;
								Tmplist = new ArrayList<>();
								
								for(String str : Anslist)
								{
									Tmplist.add(str);
								}
							}
						}
					}
				}
			}
			
			//System.out.println(Tmplist.toString());
			System.out.println("최종 스코어 : " +strtmp);
			
			if(abc != null)
			{
				sendmessage = abc;
			}
			else
			{
				if(strtmp < 40)
				{
					
					//sendmessage  = "무슨 말인지 잘모르겠어요 ^^";
					wc.weightsetting(Toolbox.getFileList(Statics.FILE_QUESTION_ANSWER_TEXT,false), Toolbox.getFileList(Statics.FILE_WRITE_NAME,false));
					JSONObject message = wc.getWeight();
					System.out.println("들어온 가중치"+message.get("가중치"));
					if((double)message.get("가중치") == 0.0)
					{
						sendmessage  = "무슨 말인지 잘모르겠어요 ^^";
					}
					else
					{
						sendmessage = message.get("답변").toString();
					}
					
				}
				else
				{
					System.out.println("------선택된 문장 ---------");
					for(String str : Tmplist)
					{
						System.out.println(str);
						if(str.contains("답변 : "))
						{
							sendmessage  = str.split("답변 :  ")[1];
						}
					}
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}	
		
		
	}
	
	
	public void one_Word(String word)
	{
		try {
			bw.write(word);
			bw.flush();
			
			
			batch.create_batch(Statics.ONE_QUESTION_TXT);
			batch.start_batch();
			Worddivide.morphemeDivide(Statics.ONE_QUESTION_TXT ,Statics.FILE_WRITE_NAME ,Statics.SENTENCEINCLUE);
			
			//욕설을 판별하기 위한 리스트
			ArrayList<String> list= Toolbox.getFileList(Statics.FILE_WRITE_NAME,true);
			//욕설판별
			if(SwearDiscriminatar.skipSwear(list))
			{
				testing();
			}
			else
			{
				sendmessage = "정상적인 말을 사용해주세요^^";
			}
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public String getMessage(String msg)
	{
		test(msg);
		
		if(sendmessage != null)
		{
			msg = sendmessage;
			sendmessage = null;
		}
		else
		{
			msg = "에러";
		}
		
		try {
			QuesStream.close();
			Machinestream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
		
	}
	
	/*
	public void one_Word(String word)
	{
		try{
		  MorphemeAnalyzer ma = new MorphemeAnalyzer();

	         List<MExpression> ml = ma.analyze("현규야 밥 먹었어?");
	         ml = ma.leaveJustBest(ml);
	         List<Sentence> s = ma.divideToSentences(ml);
	         System.out.println("==================================");
	         for(Sentence target : s)
	         {
	            for(Eojeol e : target)
	            {
	               for(Morpheme m : e)
	               {
	                  System.out.println(m.toString());
	               }
	            }
	         }
	         
	      
			
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SelectWord test = new SelectWord();
	}
}
