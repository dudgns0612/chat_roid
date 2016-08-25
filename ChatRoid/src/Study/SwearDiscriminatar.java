package Study;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.snu.ids.ha.ma.Tokenizer;

import FXServerControllers.ServerMainUicontroller;
import javafx.application.Platform;
import tools.CustomDialog;
import tools.NetworkProtocols;
import tools.Statics;
import tools.Toolbox;

public class SwearDiscriminatar {
	//나이브 베이지안 공식 활용
	BufferedWriter nomalbw; // 정상 학습 텍스트 
	BufferedWriter swearbw; // 욕설 학습 텍스트
	BufferedReader learningbr; // 학습시킬 텍스트
	
	

	private static int undulicate = 0; //중복이 없는 문장 갯수
	
	
	private int nomality = 0; 
	private int unnomality =0;
	
	StringTokenizer st;
	
    static ArrayList<String> alllist = new ArrayList<>(); // 전체 문장 저장
	static ArrayList<String> nomallist = new ArrayList<>(); // 정상 문장 저장
	static ArrayList<String> unnoamllist = new ArrayList<>(); //비정상 문장 저장
	static ArrayList<String> undulicatelist = new ArrayList<>(); //전체 문장에서 중복 문장 뺀 리스트
	
	public SwearDiscriminatar()
	{
		/*
		new File(Statics.BAD_NOMAL_STUDY_TXT);
		new File(Statics.BAD_STUDY_WORDS_TXT);
		String word;
		try {
			nomalbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Statics.BAD_NOMAL_STUDY_TXT),"utf-8"));
			swearbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Statics.BAD_SWEAR_STUDY_TXT),"utf-8"));
			learningbr = new BufferedReader(new InputStreamReader(new FileInputStream(Statics.BAD_STUDY_WORDS_TXT)));
			while(true)
			{
				word = learningbr.readLine();
				//System.out.println( word);
				if(word == null)
				{
					break;
				}
				word = word.replaceAll(Pattern.quote("+"), " ");
				if(word.startsWith("-1"))
				{
					int pass = word.split("\t")[0].length();
					word = word.substring(pass, word.length());
					word = word.replaceAll(Pattern.quote("\t")," ");
					st = new StringTokenizer(word," ");
					
					while(st.hasMoreTokens())
					{
						word = st.nextToken();
						if(word.contains("/"))
						{
							alllist.add(word);
							nomallist.add(word);
						}
					}
				}
				
				if(word.startsWith("1"))
				{
					int pass = word.split("\t")[0].length();
					word = word.substring(pass, word.length());
					word = word.replaceAll(Pattern.quote("\t")," ");
					st = new StringTokenizer(word," ");
					
					while(st.hasMoreTokens())
					{
						word = st.nextToken();
						if(word.contains("/"))
						{
							unnoamllist.add(word);
							alllist.add(word);
						}
					}					
				}	
			}
			
			for(String str : unnoamllist)
			{
				swearbw.write(str);
				swearbw.newLine();
				swearbw.flush();
			}	
			for(String str : nomallist)
			{
				nomalbw.write(str);
				nomalbw.newLine();
				nomalbw.flush();
			}
				
			undulicatelist = overlap(alllist); //전체 문장의 중복 문장 빼는 메소드
			
			nomalbw.close();
			swearbw.close();
			learningbr.close();
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		//badworddiscriminatar(alllist);
		*/
		 
	}
	
	
	//나이브 베이지안 이용 확률 구함.
	public static double badwordDiscriminatar(ArrayList<String> list)
	{
		int cnt = 1; // 문장 갯수;  1로 시작 이유 : Smoothing이용 0 방지
		double tmp;
		
		double nomalvalue = 0; // 총 정상 비율
		double unnomalvalue = 0; // 총 비정상 비율
		
		double nomalweight; // 전체 정상단어 비율
		double swearweight; // 전체 비정상단어 비율
		
		int txtall = alllist.size();
		int nomalcnt = nomallist.size(); //노말 문장 갯수
		int swearcnt = unnoamllist.size(); // 비정상 문장 갯수
		int unduplicate = undulicatelist.size(); // 중복없는 문장 갯수
		double probability = 0;
		
		nomalweight = ((double)nomalcnt / txtall);
		swearweight = ((double)swearcnt / txtall);
		
		System.out.println("전체 단어  갯수 : " + txtall);
		System.out.println("정상 단어 갯수  : " + nomalcnt);
		System.out.println("비정상 단어  갯수 : " + swearcnt);
		System.out.println("중복 없는 전체 단어 갯수 : " + unduplicate);
		System.out.println("정상단어 비율 : " + nomalweight);
		System.out.println("비정상단어 비율 : " + swearweight);
		
		System.out.println("=========== 정상 ================");
		//정상일 확률
		for(String str : list)
		{
			System.out.println(str);
			if(str == null)
			{
				break;
			}
			for(String liststr : nomallist)
			{
				if(str.equals(liststr))
				{
					cnt++;
				}
			}
			System.out.println("한뭉장 갯수 : "+cnt);
			// 단어당 비율 누적
			tmp = ((double)cnt /(double)(nomalcnt+unduplicate));
			tmp = Math.log(tmp);
			cnt = 1;
			nomalvalue = nomalvalue+tmp;
			
			
			System.out.println("한문장의 비율 : "+ tmp);
			System.out.println("총 문장 비율 : "+ nomalvalue );
			
		}
		nomalweight = Math.log(nomalweight);
		
		nomalvalue = nomalvalue + nomalweight;
		System.out.println("정상일 확률 : " + nomalvalue);
		
		
		System.out.println("=========== 비정상 ================");
		//비정상일 확률
		for(String str : list)
		{
			System.out.println(str);
			if(str == null)
			{
				break;
			}
			for(String liststr : unnoamllist)
			{
				if(str.equals(liststr))
				{
					cnt++;
				}
			}
			System.out.println("한뭉장 갯수 : "+cnt);
			// 단어당 비율 누적
			tmp = ((double)cnt /(double)(swearcnt+unduplicate));
			//System.out.println(tmp);
			tmp = Math.log(tmp);
			cnt = 1;
			
			unnomalvalue = unnomalvalue+tmp;
			
			System.out.println("한문장의 비율 : "+ tmp);
			System.out.println("총 문장 비율 : "+ unnomalvalue );
		}
		swearweight = Math.log(swearweight);
		
		unnomalvalue = unnomalvalue + swearweight;
		System.out.println("비정상일 확률 : " + unnomalvalue);
		
		
		return probability;
	}
	

	
	public static boolean skipSwear(ArrayList<String> list)
	{
		boolean check = true;
		ArrayList<String> swearlist = ServerMainUicontroller.swearlist;
			for(String a: list)
			{
				for(String b : swearlist)
				{
					if(a.equals(b))
					{
						check = false;
					}
				}
				if(check == false)
				{
					break;
				}
			}
		
		return check;
	}
	
	public static boolean skipSwear(ArrayList<String> list, ObjectOutputStream oo, String type)
	{
		boolean check = true;
		ArrayList<String> swearlist = ServerMainUicontroller.swearlist;
		try {
			for(String a: list)
			{
				for(String b : swearlist)
				{
					if(a.equals(b))
					{
						check = false;
					}
				}
				if(check == false)
				{
					break;
				}
			}
			if(type.equals(Statics.WORD))
			{
				oo.writeObject(Toolbox.JsonRequest(NetworkProtocols.FILE_SEND_STATE_RESPOND));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return check;
	}
	
	
	
	//중복 된 문장 갯수 뺌
	public static ArrayList<String> overlap(ArrayList<String> list)
	{
		ArrayList<String> duplicate = new ArrayList<>();
		boolean check = false;
		for(int i = 0 ; i < list.size() ; i++)
		{
			for(int j = i+1 ; j < list.size() ; j++)
			{
				if(list.get(i).equals(list.get(j)))
				{
					check = true;
					break;
				}
				else
				{
					check = false;
					continue;
				}
			}
			if(!check)
			{
				duplicate.add(list.get(i));
				check = true;
			}
		}
		return duplicate;
	}
	
	public static void main(String[] args) {
		//new SwearDiscriminatar();
	}
}
