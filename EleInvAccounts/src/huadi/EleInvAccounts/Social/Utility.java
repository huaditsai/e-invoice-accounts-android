package huadi.EleInvAccounts.Social;

import huadi.EleInvAccounts.DBHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

public class Utility {

	static String webname = "http://www.artist-wu.com/WebSite3/"; //TODO 待網站上線後更改
	
	private static void connectWeb()
	{
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()     
	    .detectDiskReads()     
	    .detectDiskWrites()     
	    .detectNetwork()   // or .detectAll() for all detectable problems     
	    .penaltyLog()     
	    .build());
		
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()     
	    .detectLeakedSqlLiteObjects()
	    .penaltyLog()     
	    .penaltyDeath()     
	    .build());
	}
	
	static public String getJson(Context context,String function)
	{
		connectWeb();
		String url = null;
		
		Calendar calendar = Calendar.getInstance();
		int _year = calendar.get(Calendar.YEAR); //民國
		int _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor invListCursor = db.rawQuery("SELECT money "
				+ "FROM Charge "
				+ "WHERE date >= " + String.format("'%d%02d00' ", _year, _month)
				+ "AND date <= " + String.format("'%d%02d31' ", _year, _month), null);
			int costCount = invListCursor.getCount(); //資料筆數
			int income = 0, expend = 0, balance = 0;
			if(costCount != 0)
			{
				invListCursor.moveToFirst(); //移至資料庫第一筆
				for (int i = 0; i < costCount; i++)
				{
					int money = invListCursor.getInt(invListCursor.getColumnIndex("money"));
					balance += money;
					
					if(money < 0)
						expend += money;
					else 
						income += money;
					
					invListCursor.moveToNext(); //移至資料庫下一筆
				}
			}
		invListCursor.close();
		
		SharedPreferences fb = context.getApplicationContext().getSharedPreferences("FaceBook", Context.MODE_PRIVATE); //偏好設定 
		String user_id = fb.getString("fbid", "");
		String user_name = fb.getString("fbname", "");
		String join = fb.getString("join", "");
		String money = String.valueOf(balance);
		String access_token = fb.getString("access_token", "");
		
		if(function.equals("update"))
			url = webname+"Default.aspx?function="+function
					+"&user_id="+user_id+"&user_name="+user_name+"&join="+join+"&money="+money;
        if (function.equals("friend"))
			url = webname+"Default.aspx?function="+function +"&access_token="+access_token;
		
        url = url.replace(" ", "%20");
        
        String result = "";
        HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
        HttpPost httppost = new HttpPost(url);
        HttpResponse response;
		try {
			response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        InputStream is = entity.getContent();
	    
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf8"),9999999);
	        StringBuilder sb = new StringBuilder();
	        String line = null;

	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        is.close();
	        result = sb.toString();
		} catch (ClientProtocolException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		
        return result;
	}
	
	static public Bitmap getFBpic(String profile_picture, String id){ //儲存FB User大頭照
		Bitmap bitmap = null;
		try {
			URL url = new URL(profile_picture);
			URLConnection conn = url.openConnection();
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        BitmapFactory.Options options=new BitmapFactory.Options();
	        bitmap = BitmapFactory.decodeStream(is,null,options);
	
			File folder = new File(Environment.getExternalStorageDirectory(), "EleInvAccounts");
			if(!folder.exists())
				folder.mkdir();
			File file = new File(Environment.getExternalStorageDirectory() + "/EleInvAccounts/", id + ".png");
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
		}
		catch (MalformedURLException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		
		return bitmap;
	}	
}
