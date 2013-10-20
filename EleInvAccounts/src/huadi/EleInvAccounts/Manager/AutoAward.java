package huadi.EleInvAccounts.Manager;

import huadi.EleInvAccounts.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//手動對獎(輸入後三碼)
public class AutoAward
{
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	Map<String, List<String>> winnerMap = new HashMap<String, List<String>>();

	public AutoAward(Context context)
	{
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase(); //讓db可寫入
	}

	public Map<String, List<String>> Award(String invPeriod, Map<String, List<String>> winningList)
	{
		Cursor InvoiceCursor = db.rawQuery("SELECT invNum, invTotalCost, invDate, sellerName " 
											+ "FROM Invoice " 
											+ "WHERE invPeriod = '" + invPeriod + "'", null); //要記得''包起來

		int count = InvoiceCursor.getCount(); //資料筆數
		
		if (count != 0)
		{
			InvoiceCursor.moveToFirst();

			List<String> prize = new ArrayList<String>();

			for (int i = 0; i < count; i++)
			{
				String invNum = InvoiceCursor.getString(InvoiceCursor.getColumnIndex("invNum"));
				int invTotalCost = InvoiceCursor.getInt(InvoiceCursor.getColumnIndex("invTotalCost"));
				String invDate = InvoiceCursor.getString(InvoiceCursor.getColumnIndex("invDate"));
				invDate = invDate.substring(0,4) + "-" + invDate.substring(4,6) + "-" + invDate.substring(6,8);
				String sellerName = InvoiceCursor.getString(InvoiceCursor.getColumnIndex("sellerName"));
				
				//Log.e("invNum","" + invNum);
				
				String infoString = invDate + "," + invNum + "," + sellerName + "," + invTotalCost;
				
				String input = invNum.substring(2, 10); //前兩個英文			
				
				for (String no : winningList.get("superPrizeNo"))
					if (input.equals(no))
						prize.add("特別獎," + infoString);

				for (String no : winningList.get("spcPrizeNo"))
					if (input.equals(no))
						prize.add("特獎," + infoString);

				for (String no : winningList.get("firstPrizeNo"))
				{
					if (input.equals(no))
						prize.add("頭獎," + infoString);
					else if (input.substring(input.length() - 7, input.length()).equals(no.substring(no.length() - 7, no.length()))) //末7碼 2獎
						prize.add("二獎," + infoString);
					else if (input.substring(input.length() - 6, input.length()).equals(no.substring(no.length() - 6, no.length()))) //末6碼 3獎
						prize.add("三獎," + infoString);
					else if (input.substring(input.length() - 5, input.length()).equals(no.substring(no.length() - 5, no.length()))) //末5碼 4獎
						prize.add("四獎," + infoString);
					else if (input.substring(input.length() - 4, input.length()).equals(no.substring(no.length() - 4, no.length()))) //末4碼 5獎
						prize.add("五獎," + infoString);
					else if (input.substring(input.length() - 3, input.length()).equals(no.substring(no.length() - 3, no.length()))) //末三碼 6獎
						prize.add("六獎," + infoString);
				}

				for (String no : winningList.get("sixthPrizeNo"))
					if (input.substring(input.length() - 3, input.length()).equals(no)) //末三碼
						prize.add("增開六獎," + infoString);	
				
				InvoiceCursor.moveToNext(); //移至資料庫下一筆				
			}
			//Log.e("prize", "" + prize);
			winnerMap.put("prize", prize);
		}
		InvoiceCursor.close();

		return winnerMap;
	}
}
