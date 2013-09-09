package huadi.EleInvAccounts.Inquiry;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

//開獎號碼
public class WinningList extends AsyncTask<String, String, Map<String, List<String>>> // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
{
	Map<String, List<String>> map = new HashMap<String, List<String>>();
	
	private final String detailUrl = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp?"
		+ "version=0.2"
		+ "&action=QryWinningList"
		+ "&invTerm={0}" //yyyMM, 必須為雙數月
		+ "&UUID={1}"
		+ "&appID={2}";
	
	@Override
	protected Map<String, List<String>> doInBackground(String... params)
	{
		if (params.length < 0)
			return null;
		
		String url = MessageFormat.format(detailUrl, params[0], params[1], params[2]);
		
		HttpGet get = new HttpGet(url);
		String strResult = "";

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
				//Log.e("strResult", strResult);
			
				JSONObject jsonObject = new JSONObject(strResult); //{}為JSONObject
				
				String v =  jsonObject.getString("v"); //版本號碼
				String code =  jsonObject.getString("code"); //訊息回應碼
				String msg =  jsonObject.getString("msg"); //系統回應訊息
				//Log.e("JSONObject Exception","版本" + v + ", 回應碼 " + code + ", 訊息" + msg);
				
				try
				{					
					//String invoYm =  jsonObject.getString("invoYm"); //查詢開獎期別(民國年月)
					
					List<String> superPrizeNo = new ArrayList<String>(); //千萬特獎號碼
					superPrizeNo.add(jsonObject.getString("superPrizeNo"));
					
					List<String> spcPrizeNo = new ArrayList<String>(); //特獎號碼(至少1組)
					spcPrizeNo.add(jsonObject.getString("spcPrizeNo"));
					for (int i = 2; i <= 3; i++)
						if(jsonObject.getString("spcPrizeNo" + i).length() > 1)
							spcPrizeNo.add(jsonObject.getString("spcPrizeNo" + i));					
					
					List<String> firstPrizeNo = new ArrayList<String>(); //頭獎號碼(至少3組)
					for (int i = 1; i <= 10; i++)					
						if(jsonObject.getString("firstPrizeNo" + i).length() > 1)
							firstPrizeNo.add(jsonObject.getString("firstPrizeNo" + i));					
					
					List<String> sixthPrizeNo = new ArrayList<String>(); //增開六獎號碼(至少1組)
					for (int i = 1; i <= 3; i++)
						if(jsonObject.getString("sixthPrizeNo" + i).length() > 1)
							sixthPrizeNo.add(jsonObject.getString("sixthPrizeNo" + i));
					
					List<String> PrizeAmt = new ArrayList<String>(); //金額					
					PrizeAmt.add(jsonObject.getString("superPrizeAmt")); //千萬特獎金額
					PrizeAmt.add(jsonObject.getString("spcPrizeAmt")); //特獎金額
					PrizeAmt.add(jsonObject.getString("firstPrizeAmt")); //頭獎金額
					PrizeAmt.add(jsonObject.getString("secondPrizeAmt")); //二獎金額
					PrizeAmt.add(jsonObject.getString("thirdPrizeAmt")); //三獎金額
					PrizeAmt.add(jsonObject.getString("fourthPrizeAmt")); //四獎金額
					PrizeAmt.add(jsonObject.getString("fifthPrizeAmt")); //五獎金額
					PrizeAmt.add(jsonObject.getString("sixthPrizeAmt")); //六獎金額
					 
					map.put("superPrizeNo", superPrizeNo);
					map.put("spcPrizeNo", spcPrizeNo);
					map.put("firstPrizeNo", firstPrizeNo);
					map.put("sixthPrizeNo", sixthPrizeNo);
					map.put("PrizeAmt", PrizeAmt);
				}
				catch(Exception e)
				{
					Log.e("JSONObject Exception","版本 " + v + ", 回應碼  " + code + ", 訊息 " + msg);
				}			
				
			}			
		}
		catch (Exception e)
		{
			Log.e("WinningList", e.toString());
		}
		
		return map;
	}

	@Override
	protected void onPostExecute( Map<String, List<String>> result)
	{
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(String... params)
	{
		super.onProgressUpdate(params);
	}
	
}
