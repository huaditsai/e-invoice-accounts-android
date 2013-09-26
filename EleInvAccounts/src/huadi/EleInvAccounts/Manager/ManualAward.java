package huadi.EleInvAccounts.Manager;

import java.util.List;
import java.util.Map;

import android.util.Log;

//手動對獎(輸入後三碼)
public class ManualAward
{	
	public ManualAward()
	{
		
	}
	
	public String Award(String input, Map<String, List<String>> winning)
	{
		String infoString = "槓龜";
		
		for (String no : winning.get("superPrizeNo"))
		{
			if(input.equals(no.substring(no.length() - 3, no.length())))
				infoString = "有機會中 特獎 一千萬";
			//Log.e("su",no.substring(no.length() - 3, no.length()));
		}
		
		for (String no : winning.get("spcPrizeNo"))
		{
			if(input.equals(no.substring(no.length() - 3, no.length())))
				infoString = "有機會中 特獎 貳百萬";
			//Log.e("sp",no.substring(no.length() - 3, no.length()));
		}
		
		for (String no : winning.get("firstPrizeNo"))
		{
			if(input.equals(no.substring(no.length() - 3, no.length())))
				infoString = "至少中 200 元";
			//Log.e("fir",no.substring(no.length() - 3, no.length()));
		}
		
		for (String no : winning.get("sixthPrizeNo"))
		{
			if(input.equals(no))
				infoString = "中六獎 200 元";
			//Log.e("6", no);
		}
		
		return input + "\r\n" + infoString;		
	}
}
