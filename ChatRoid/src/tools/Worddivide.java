package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Worddivide {
	static BufferedReader br;
	static BufferedWriter bw;
	
	
	
	public static ArrayList<String> morphemeDivide(String readfile ,String  writefile, String type)
	{
		String divide[] = null;
		String word;
		ArrayList<String> arraylist = null;
		ArrayList<String> sendlist = new ArrayList<>();
		//System.out.println(readfile+"파일 분석시작");
		// 형태소 문장만 각각 따로 분리
		if(type.equals(Statics.EVERYWORD))
		{
			try {
				
				br = new BufferedReader(new InputStreamReader(new FileInputStream(readfile+".ma"),"utf-8"));
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writefile),"utf-8"));
				arraylist = new ArrayList<String>();
				while(true){
					word = br.readLine();
					if(word == null)
					{
						break;
					}
					else if(word.contains("INPUT"))
					{
					}
					else if(word.contains("TOKEN"))
					{
					}
					else if(word.contains("NUM_OUTPUT"))
					{
						int index = Integer.parseInt(word.split(" ")[1]);
						String name[] = new String[index];
						String num[] = new String[index];
						if(index == 1)
						{
							word = br.readLine();
							
							word = word.split("\t")[0];
							if(word.contains("+"))
							{
								int cnt = 0;
								for(int i =0; i < word.length(); i++)
								{
									if(word.charAt(i) == '+')
									{
										cnt++;
									}
								}
								for(int j = 0; j < cnt+1 ; j++)
								{
									divide = word.split(Pattern.quote("+"));
									arraylist.add(divide[j]);
								}								
							}
							else
							{
								if(!word.equals("/SW"))
								{
									arraylist.add(word);
								}
							}		
						}
						else{
							int change = 0;
							for(int i=0; index > i; i++)
							{
								word = br.readLine();
								num[i]= word.split("\t")[1];
								name[i] = word.split("\t")[0];
								
								int change1 = Integer.parseInt(num[i]);
								if(change < change1)
								{
									change = change1;
								}	
							}
							for(int j =0; index > j ; j++)
							{
								if(num[j]==null)
								{
									break;
								}
								if(Integer.parseInt(num[j]) == change)
								{
									word = name[j];
									
									word = word.split("\t")[0];
									if(word.contains("+"))
									{
										int cnt = 0;
										for(int i =0; i < word.length(); i++)
										{
											if(word.charAt(i) == '+')
											{
												cnt++;
											}
										}
										for(int k = 0; k < cnt+1 ; k++)
										{
											divide = word.split(Pattern.quote("+"));
											arraylist.add(divide[k]);
										}	
									}
									else
									{
										arraylist.add(word);
									}
									break;
								}
							}
						}
					}
					else if(word.contains("====="))
					{
						for(int i =0; i<arraylist.size() ; i++)
						{
							sendlist.add(arraylist.get(i));
							bw.append(arraylist.get(i).trim());
							bw.newLine();
							bw.flush();
						}
						arraylist = new ArrayList<String>();
					}
					//System.out.println("리스트 확인 : "+arraylist.toString());
				}
				System.out.println(readfile+"파일 분석 끝");
				br.close();
				bw.close();
				//Toolbox.deleteMorpheme(readfile); // 사용한 형태소 파일 지움 
				//Toolbox.deleteMorpheme(writefile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// 문장  + 형태소
		else if(type.equals(Statics.SENTENCEINCLUE))
		{	
			//System.out.println("스터디 인포");
			try {
				
				br = new BufferedReader(new InputStreamReader(new FileInputStream(readfile+".ma"),"utf-8"));
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writefile),"utf-8"));
				
	
				
				arraylist = new ArrayList<String>();
				while(true){
					word = br.readLine();
					if(word == null)
					{
						break;
					}
					else if(word.contains("INPUT"))
					{
						word = word.split("T: ")[1];
						bw.append("문장 :  "+word);
						bw.newLine();
						bw.flush();
					}
					else if(word.contains("TOKEN"))
					{
						
					}
					else if(word.contains("NUM_OUTPUT"))
					{
						int index = Integer.parseInt(word.split(" ")[1]);
						String name[] = new String[index];
						String num[] = new String[index];
						if(index == 1)
						{
							word = br.readLine();
							arraylist.add(word);
						}
						else{
							int change = 0;
							for(int i=0; index > i; i++)
							{
								word = br.readLine();
								num[i]= word.split("\t")[1];
								name[i] = word.split("\t")[0];
								
								int change1 = Integer.parseInt(num[i]);
								if(change < change1)
								{
									change = change1;
								}	
							}
							for(int j =0; index > j ; j++)
							{
								if(num[j]==null)
								{
									break;
								}
								if(Integer.parseInt(num[j]) == change)
								{
									arraylist.add(name[j]+"\t"+num[j]);
									break;
								}
							}
						}
					}
					else if(word.contains("====="))
					{
						for(int i =0; i<arraylist.size() ; i++)
						{
							bw.append(arraylist.get(i).trim());
							bw.newLine();
							bw.flush();
							int size = arraylist.size();
							if(i+1 == size)
							{
								bw.append("===============");
								bw.newLine();
								bw.flush();
							}
						}
						arraylist = new ArrayList<String>();
					}
				}
				System.out.println(readfile+"파일 분석 끝");
				br.close();
				bw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(type.equals(Statics.CONNECTWORD))
		{
			try {	
				br = new BufferedReader(new InputStreamReader(new FileInputStream(readfile+".ma"),"utf-8"));
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writefile),"utf-8"));
				arraylist = new ArrayList<String>();
				while(true){
					word = br.readLine();
					
					if(word == null)
					{
						break;
					}
					else if(word.contains("INPUT"))
					{
					}
					else if(word.contains("TOKEN"))
					{
					}
					else if(word.contains("NUM_OUTPUT"))
					{
						int index = Integer.parseInt(word.split(" ")[1]);
						String name[] = new String[index];
						String num[] = new String[index];
						if(index == 1)
						{
							word = br.readLine();
							
							word = word.split("\t")[0];
							//System.out.println(word);
							if(!word.equals("/SW"))
							{
								arraylist.add(word);
							}
						}
						else{
							int change = 0;
							for(int i=0; index > i; i++)
							{
								word = br.readLine();
								num[i]= word.split("\t")[1];
								name[i] = word.split("\t")[0];
								
								int change1 = Integer.parseInt(num[i]);
								if(change < change1)
								{
									change = change1;
								}	
							}
							for(int j =0; index > j ; j++)
							{
								if(num[j]==null)
								{
									break;
								}
								if(Integer.parseInt(num[j]) == change)
								{
									word = name[j];
									word = word.split("\t")[0];
									if(!word.equals("/SW"))
									{
										arraylist.add(word);
									}
									break;
								}
							}
						}
					}
					else if(word.contains("====="))
					{
						for(int i =0; i<arraylist.size() ; i++)
						{
							sendlist.add(arraylist.get(i));
							bw.append(arraylist.get(i).trim());
							bw.newLine();
							bw.flush();
						}
						arraylist = new ArrayList<String>();
					}
					//System.out.println("리스트 확인 : "+arraylist.toString());
				}
				System.out.println(readfile+"파일 분석 끝");
				br.close();
				bw.close();
				Toolbox.deleteMorpheme(readfile); // 사용한 형태소 파일 지움 
				Toolbox.deleteFile(writefile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(type.equals(Statics.SWEARDIVIDE))
		{
			try {	
				br = new BufferedReader(new InputStreamReader(new FileInputStream(readfile+".ma"),"utf-8"));
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writefile),"utf-8"));
				arraylist = new ArrayList<String>();
				while(true){
					word = br.readLine();
					
					if(word == null)
					{
						break;
					}
					else if(word.contains("INPUT"))
					{
					}
					else if(word.contains("TOKEN"))
					{
					}
					else if(word.contains("NUM_OUTPUT"))
					{
						int index = Integer.parseInt(word.split(" ")[1]);
						String name[] = new String[index];
						String num[] = new String[index];
						if(index == 1)
						{
							word = br.readLine();
							
							word = word.split("\t")[0];
							//System.out.println(word);
							if(!word.contains("/SW"))
							{
								String check[];
								check = word.split("/");
								if(!(check[0].length() == 1))
								{
									arraylist.add(word);
								}	
							}
							
						}
						else{
							int change = 0;
							for(int i=0; index > i; i++)
							{
								word = br.readLine();
								num[i]= word.split("\t")[1];
								name[i] = word.split("\t")[0];
								
								int change1 = Integer.parseInt(num[i]);
								if(change < change1)
								{
									change = change1;
								}	
							}
							for(int j =0; index > j ; j++)
							{
								if(num[j]==null)
								{
									break;
								}
								if(Integer.parseInt(num[j]) == change)
								{
									word = name[j];
									word = word.split("\t")[0];
									if(!word.contains("/SW"))
									{
										String check[];
										check = word.split("/");
										if(!(check[0].length() == 1))
										{
											arraylist.add(word);
										}	
									}
									
									
									break;
								}
							}
						}
					}
					else if(word.contains("====="))
					{
						for(int i =0; i<arraylist.size() ; i++)
						{
							sendlist.add(arraylist.get(i));
							bw.append(arraylist.get(i).trim());
							bw.newLine();
							bw.flush();
						}
						arraylist = new ArrayList<String>();
					}
					//System.out.println("리스트 확인 : "+arraylist.toString());
				}
				System.out.println(readfile+"파일 분석 끝");
				br.close();
				bw.close();
				Toolbox.deleteMorpheme(readfile); // 사용한 형태소 파일 지움 
				//Toolbox.deleteMorpheme(writefile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sendlist;
	}
	
	
	
	// 형태소 번호 지우고 / 형태소 단어별 나누기 메소드
	public static ArrayList<String> getSplitList(ArrayList<String> list ,boolean deletenumber)
	{
		ArrayList<String> returnlist = new ArrayList<>();
		String divide[];

		for(String str : list)
		{
			if(deletenumber)
			{
				str = str.split("\t")[0];	
			}
			if(str.contains("+") && str.contains("/"))
			{
				int cnt = 0;
				for(int i =0; i < str.length(); i++)
				{
					if(str.charAt(i) == '+')
					{
						cnt++;
					}
				}
				for(int j = 0; j < cnt+1 ; j++)
				{
					divide = str.split(Pattern.quote("+"));
					returnlist.add(divide[j]);
				}			
			}
			else
			{
				returnlist.add(str);
			}
		}
		
		
		
		
		return returnlist;
	}

	
	
	
}
