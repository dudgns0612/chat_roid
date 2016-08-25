package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Batchhandler {
	File createbat = new File(Statics.USER_FILE+"\\"+"BAT.bat");
	BufferedWriter bw;
	
	//bat 파일로 추출 된 ma파일의 마지막줄에 ==========추가
	BufferedWriter bwbat;
	File mafilename;
	
	String filename;
	
	public void create_batch(String filename)
	{
		try {
			//String file = null ;
			this.filename = filename;
			//System.out.println(file);
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(createbat),"utf-8"));
			bw.write("cd C:\\Users\\LG\\Desktop\\cnuma\\cnuma\\py_ver");
			bw.newLine();
			bw.write("ma.bat "+filename);
			bw.newLine();
			bw.write("pause");
			bw.flush();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
	
	public void start_batch()
	{
		System.out.println("\t\t 형태소 분석 시작");
		String filename = createbat.getPath();
		System.out.println("\t 셍성된 배치파일 이름 : "+filename);
		
		try{
		    Process p = Runtime.getRuntime().exec(filename);
		    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    
		    String line = null;
		    while((line = br.readLine()) != null){

		        System.out.println(line);
		    }
		    
		    System.out.println("\t\t\t 형태소 분석 완료");
		    modify();
		}catch(Exception e){
		    System.out.println(e);
		}
		
	}
	public void modify()
	{
		//마파일을 불러와서 마지막줄에  ==============추가하기
		System.out.println("modify");
		String file = filename.split(Pattern.quote("\\"))[2];
		String word;
		System.out.println(file);
		mafilename = new File(filename+".ma");
		try {
			bwbat = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mafilename,true),"utf-8"));		
		
			bwbat.append("============");
			bwbat.newLine();
			bwbat.flush();
			bwbat.close();
			
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
	
	public void morphemeanalysis(String filename)
	{
		create_batch(filename);
		start_batch();
	}

}
