package huadi.EleInvAccounts.Inquiry;

import huadi.EleInvAccounts.DBHelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class CarrierHead extends AsyncTask<String, String, Map<String, List<String>>> // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
{
	//載具明細
	
	Map<String, List<String>> map = new HashMap<String, List<String>>();
	
	private final String detailUrl = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/invServ/InvServ?"
		+ "version=0.1"
		+ "&cardType={0}" //卡別, 手機條碼3J0002, 悠遊卡1K0001, iCash 2G0001
		+ "&cardNo={1}" //卡片隱碼(/2345782)
		+ "&expTimeStamp={2}" //有效存續時間戳記
		+ "&action=carrierInvChk"
		+ "&timeStamp={3}" //時間戳記
		+ "&startDate={4}" //查詢起始時間(yyyy/MM/dd)
		+ "&endDate={5}" //查詢結束時間(yyyy/MM/dd), 開始及結束查詢時間 "相同月份"
		+ "&onlyWinningInv={6}" //僅回傳中獎資訊 (Y/N)
		+ "&uuid={7}" //UUID
		+ "&appID={8}" 
		+ "&cardEncrypt={9}"; //卡片檢驗碼(手機條碼驗證碼)
	
	@Override
	protected Map<String, List<String>> doInBackground(String... params)
	{
		if (params.length < 0)
			return null;
		
		String strResult = "";

		try
		{
			String nowTime = "" + (System.currentTimeMillis() / 1000); //new GetNTP().execute("").get();
			String timeStamp = "" + (Integer.parseInt(nowTime) + 100);
			String expTimeStamp = "" + (Integer.parseInt(timeStamp) + 100000);
			Calendar calendar = Calendar.getInstance();
			String startDate = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/01";
			String endDate = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/30";
			
			String url = MessageFormat.format(detailUrl, params[0], params[1], expTimeStamp, timeStamp, startDate
														, endDate, params[2], params[3], params[4], params[5]);
			
			HttpGet get = new HttpGet(url);
			Log.e("url", url);
			
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
				
				List<String> codeList = new ArrayList<String>();
				String code =  jsonObject.getString("code"); //訊息回應碼
				codeList.add(code);
				map.put("code", codeList);
				
				String msg =  jsonObject.getString("msg"); //系統回應訊息				
				String onlyWinningInv =  jsonObject.getString("onlyWinningInv"); //僅回傳中獎資訊 (Y/N)				
								
				try
				{
					List<String> invNumList = new ArrayList<String>();
					List<String> invDateList = new ArrayList<String>();
					JSONArray detailArray = jsonObject.getJSONArray("details"); //[]為JSONArray
					for(int i = 0; i < detailArray.length(); i++)
					{
//							String rowNum = detailArray.getJSONObject(i).getString("rowNum"); //明細編號(1,2,3...)
						String invNum =  detailArray.getJSONObject(i).getString("invNum"); //發票號碼
						invNumList.add(invNum);
//							String cardType =  detailArray.getJSONObject(i).getString("cardType"); //卡別
//							String cardNo =  detailArray.getJSONObject(i).getString("cardNo"); //卡片（載具）隱碼						
//							String sellerName =  detailArray.getJSONObject(i).getString("sellerName"); //賣方名稱
//							String invStatus =  detailArray.getJSONObject(i).getString("invStatus"); //2
//							String invDonatable =  detailArray.getJSONObject(i).getString("invDonatable"); //發票是否捐贈
						
						
						JSONObject invDateObject =  detailArray.getJSONObject(i).getJSONObject("invDate"); //發票開立時間
						String year = invDateObject.getString("year"); //yyy
						String month = invDateObject.getString("month"); //m
						String date = invDateObject.getString("date"); //d
						
						String invDateString = String.format("%d/%02d/%02d", Integer.parseInt(year)+1911, Integer.parseInt(month), Integer.parseInt(date));
						invDateList.add(invDateString);
//							String day = invDateObject.getString("day"); //星期1234
//							String hours = invDateObject.getString("hours"); //
//							String minutes = invDateObject.getString("minutes"); //
//							String seconds = invDateObject.getString("seconds"); //
//							String time = invDateObject.getString("time"); //時間戳記
//							String timezoneOffset = invDateObject.getString("timezoneOffset"); //時區
					}
					
					map.put("invNum", invNumList);
					map.put("invDate", invDateList);
				}
				catch(Exception e)
				{
					Log.e("JSONObject CarrierHead","版本, " + v + "回應碼, " + code + "訊息, " + msg);
				}
			}
				
						
		}
		catch (Exception e)
		{
			Log.e("CarrierHead", e.toString());
		}
		
		return map;
	}

	@Override
	protected void onPostExecute(Map<String, List<String>> result)
	{
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(String... params)
	{
		super.onProgressUpdate(params);
	}
	
}
