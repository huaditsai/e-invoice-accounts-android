package huadi.EleInvAccounts.Inquiry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import android.os.AsyncTask;
import android.util.Log;

public class GetNTP extends AsyncTask<String, String, String> // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
{
	//時間戳記 CarrierDetail用
	private final String url = "http://120.127.14.60/HuadiNTP/time/NowTime/?format=json";	
	
	@Override
	protected String doInBackground(String... params)
	{
		if (params.length < 0)
			return null;	
		
		HttpGet get = new HttpGet(url);
		String strResult = "";
		String NowTime = "";

		try
		{
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			HttpClient httpClient = SslSocketFactory.createMyHttpClient(); //new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = null;
			httpResponse = httpClient.execute(get);

			if (httpResponse.getStatusLine().getStatusCode() == 200)//判斷網路連接是否成功
			{
				strResult = EntityUtils.toString(httpResponse.getEntity()); //抓下來的資料
				Log.e("strResult", strResult);
			
				JSONArray jsonArray = new JSONArray(strResult); //{}為JSONObject []為JSONArray
				
				NowTime =  jsonArray.getJSONObject(0).getString("NowTime"); //ntp
				//String DateTime =  jsonArray.getJSONObject(0).getString("DateTime"); //yyyyMMdd ...
			}			
		}
		catch (Exception e)
		{
			Log.e("CarrierDetail", e.toString());
		}
		
		return NowTime;
	}

	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(String... params)
	{
		super.onProgressUpdate(params);
	}
	
}
