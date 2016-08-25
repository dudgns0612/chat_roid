package Communication;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.orsoncharts.util.json.JSONArray;
import com.orsoncharts.util.json.JSONObject;


public class WeightCalculation {
	ArrayList<String> questionlist = new ArrayList<>(); //질문 문장 list             
	ArrayList<String> studylist = new ArrayList<>(); //전체 학습 문장 list
	
	ArrayList<String> sentencelist = new ArrayList<>(); // 문장 list
	ArrayList<String> ketwordlist = new ArrayList<>(); //키워드 list
	ArrayList<String> wordlist = new ArrayList<>();  //총 단어 list
	
	
	JSONObject sentenceinfo = new JSONObject(); // 문장  /단어들/가중치 저장 하는 json;
	JSONObject weightinfo = new JSONObject();
	JSONArray  weightarray = new JSONArray(); //모든 가중치json ;
	
	
	int allsentence = 0;
	double compare = 0;
	public void weightsetting(ArrayList<String> study, ArrayList<String> question)
	{
		System.out.println("tf-idf 시작");
		String divide[];
		
		System.out.println(study.toString());
		try
		{
			for(String str : study)
			{
				if(str == null)
				{
					break;
				}
				else
				{
					studylist.add(str); 
					if(str.contains("문장"))
					{
						allsentence++; //문서 전체수
					}
				}
			}
			
			for(String str : question)
			{
				if(str.contains("==========="))
				{
					break;
				}
				else
				{
					if(str.contains("문장 :  "))
					{
						
					}
					else
					{
						if(str.contains("+"))
						{
							int cnt = 0;
							for(int i =0; i < str.length(); i++)
							{
								if(str.charAt(i) == '+')
								{
									cnt++;
								}
							}
							for(int k = 0; k < cnt+1 ; k++)
							{
								divide = str.split(Pattern.quote("+"));
								questionlist.add(divide[k]);
							}	
						}
						else
						{
							questionlist.add(str);
						}
						
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		setsentenceinfo(studylist);
	}
	
	
	public void setsentenceinfo(ArrayList<String> studylist)
	{
		System.out.println("sentendceinfo 시작");
		try
		{
			for(String str : studylist)
			{
				//System.out.println(str);
				if(str == null)
				{
					break;
				}
				else
				{
					if(str.contains("문장 :  "))
					{
						sentenceinfo.put("문장", str);
					}
					else if(str.contains("답변 :  "))
					{
						str= str.split("답변 :  ")[1];
						sentenceinfo.put("답변", str);
					}
					else if(str.contains("======="))
					{
						wordlist.add("===========");
						sentenceinfo.put("단어리스트", wordlist);
						wordWeight(sentenceinfo);
						wordlist.clear();
						sentenceinfo.clear();
						
					}
					else
					{
						String divide[];
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
								wordlist.add(divide[j]);
							}								
						}
						else
						{
							wordlist.add(str);
						}
					}
					
				}
			}	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		//System.out.println(questionlist.toString());
	}
	
	//한단어가 포함된 문서의 갯수 구하는 메소드
	
	
	public void wordWeight(JSONObject sentenceinfo)
	{
		System.out.println("wordWeight 시작");
		double tf = 0;
		double idf = 0;
		double tf_idf = 0;
		double weight = 0;
		for(String str : questionlist)
		{
			tf = getTf(sentenceinfo, str);
			idf = getIdf(str);
			//System.out.println("tf : "+ tf);
			//System.out.println("idf : "+idf);
			tf_idf = tf * idf;
			if(tf_idf == 0)
			{
				continue;
			}
			else
			{
				weight = weight + tf_idf;
			}
		}
		//System.out.println(sentenceinfo.get("문장"));
		//System.out.println("한 문장의 가중치 : "+weight);
		
		//비교 
		if(weightinfo.get("가중치") != null)
		{
			compare= (double)weightinfo.get("가중치");
			if(compare > weight)
			{
				weightinfo.put("문장", sentenceinfo.get("문장"));
				weightinfo.put("답변", sentenceinfo.get("답변"));
				weightinfo.put("가중치", weight);
			}
		}
		else
		{
			weightinfo.put("문장", sentenceinfo.get("문장"));
			weightinfo.put("답변", sentenceinfo.get("답변"));
			weightinfo.put("가중치", weight);
		}
		
		//System.out.println("현재 문장 : " + weightinfo.get("문장"));
		//System.out.println("현재 답변 : " + weightinfo.get("답변"));
		//System.out.println("가중치 : " + weightinfo.get("가중치"));
	}
	
	public double getIdf(String word)
	{
		System.out.println("getIdf 시작");
		double idf = 0;
		int cnt = 0;
		//System.out.println("분류한 wordlist : "+ wordlist.toString());
		
		for(int i = 0 ; i < wordlist.size(); i++)
		{
			//System.out.println("학습된 : "+wordlist.get(i));
			//System.out.println("Word : "+word);
			if(wordlist.get(i) == null)
			{
				break;
			}
			else if(wordlist.get(i).contains("문장 :  "))
			{
				
			}
			else
			{
				if(wordlist.get(i).equals(word))
				{
					cnt++;
					while(true)
					{
						if(wordlist.get(i).contains("======"))
						{
							break;
						}
						else
						{
							i++;
						}
					}
				}
			}
		}
		
		//System.out.println("단어를 포함한 문장갯수 : "+cnt);
		if(cnt == 0)
		{
			idf = 0;
		}
		else
		{
			idf = Math.log((double)cnt / allsentence);
		}
		return idf;
	}
	
	//한 단어에 대한 tf;
	public double getTf(JSONObject info , String word)
	{
		System.out.println("getTf");
		double weight=0;
		ArrayList<String> wordlist = (ArrayList<String>)info.get("단어리스트");
		int wordsize = wordlist.size();//문장내에 모든 단어 갯수
		int includeword = 0; // 문장내 키워드 갯수
		
		
		// 한단어마다 그문장에 tf 가중치를 구해서 넘겨줌
		// wordweight에서 그 질문의 가중치를 구해  비교한 문장의 가중치를 json에 각각 저장함.
		for(String str : wordlist)
		{
			if(str.equals(word))
			{
				includeword++;
			}
		}
		
		weight = ((double)includeword / wordsize);
		
		
		return weight;
	}
	public JSONObject getWeight()
	{
		return weightinfo; 
	}
	public static void main(String[] args) {
		/*
		BufferedReader br;
		BufferedReader br1;
		try {
			br1 = new BufferedReader(new InputStreamReader(new FileInputStream(Statics.FILE_WRITE_NAME), "utf-8"));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(Statics.FILE_QUESTION_ANSWER_TEXT), "utf-8"));
			new WeightCalculation(br,br1);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	
	}
}
