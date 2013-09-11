package huadi.EleInvAccounts.Inquiry;

import huadi.EleInvAccounts.DBHelper;

import java.text.MessageFormat;

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

public class CarrierDetail extends AsyncTask<String, String, String> // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
{
	//載具明細
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	
	private final String detailUrl = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/invServ/InvServ?"
		+ "version=0.1"
		+ "&cardType={0}" //卡別, 手機條碼3J0002, 悠遊卡1K0001, iCash 2G0001
		+ "&cardNo={1}" //卡片隱碼
		+ "&expTimeStamp={2}" //有效存續時間戳記
		+ "&action=carrierInvDetail"
		+ "&timeStamp={3}" //時間戳記
		+ "&invNum={4}" //發票號碼
		+ "&invDate={5}" //發票開立日期 (yyyy/MM/dd)
		+ "&uuid={6}" //UUID
		+ "&sellerName=" //開立賣方名稱(非必填)
		+ "&amount=" //金額(非必填)
		+ "&appID={7}" 
		+ "&cardEncrypt={8}"; //卡片檢驗碼(手機條碼驗證碼)

	public CarrierDetail(Context context)
	{
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase(); //讓db可寫入
	}
	
	@Override
	protected String doInBackground(String... params)
	{
		if (params.length < 0)
			return null;
		
		String url = MessageFormat.format(detailUrl, params[0], params[1], params[2], params[3], params[4]
													, params[5], params[6], params[7], params[8]);
		
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
				Log.e("strResult", strResult);
			
				JSONObject jsonObject = new JSONObject(strResult); //{}為JSONObject
				
				String v =  jsonObject.getString("v"); //版本號碼
				String code =  jsonObject.getString("code"); //訊息回應碼
				String msg =  jsonObject.getString("msg"); //系統回應訊息
				
				try
				{
					String invNum =  jsonObject.getString("invNum"); //發票號碼
					String invDate =  jsonObject.getString("invDate"); //發票開立日期(yyyyMMdd)
					String sellerName =  jsonObject.getString("sellerName"); //賣方名稱
					
					String totalAmount =  jsonObject.getString("amount"); //總金額
					
					String invStatus =  jsonObject.getString("invStatus"); //發票狀態(已確認)
					
					JSONArray detailArray = jsonObject.getJSONArray("details"); //[]為JSONArray
					for(int i = 0; i < detailArray.length(); i++)
					{
						String rowNum = detailArray.getJSONObject(i).getString("rowNum"); //明細編號(1,2,3...)
						String description = detailArray.getJSONObject(i).getString("description"); //品名
						String quantity = detailArray.getJSONObject(i).getString("quantity"); //數量
						String unitPrice = detailArray.getJSONObject(i).getString("unitPrice"); //單價
						String amount = detailArray.getJSONObject(i).getString("amount"); //小計						
					}
				}
				catch(Exception e)
				{
					Log.e("JSONObject Exception","版本, " + v + "回應碼, " + code + "訊息, " + msg);
				}			
				
			}			
		}
		catch (Exception e)
		{
			Log.e("CarrierDetail", e.toString());
		}
		
		return null;
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
