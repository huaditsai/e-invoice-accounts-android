package huadi.EleInvAccounts.Inquiry;

import huadi.EleInvAccounts.MySSLSocketFactory;

import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class InvDetails extends AsyncTask<String, String, String> // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
{
	private final String detailUrl = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp?"
		+ "version=0.2"
		+ "&type={0}" //QRCode, Barcode
		+ "&invNum={1}" //發票號碼
		+ "&action=qryInvDetail"
		+ "&generation=V2"
		+ "&invTerm={2}" //yyyMM, Type為Barcode時為必填
		+ "&invDate={3}" //發票開立日期 (yyyy/MM/dd)
		+ "&encrypt={4}" //發票檢驗碼, Type為QRCode時為必填
		+ "&sellerID={5}" //商家統編, Type為QRCode時為必填
		+ "&UUID={6}"
		+ "&randomNumber={7}" //4位隨機碼
		+ "&appID={8}";

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
			HttpClient httpClient = MySSLSocketFactory.createMyHttpClient(); //new DefaultHttpClient(httpParameters);

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
					String invStatus =  jsonObject.getString("invStatus"); //發票狀態(已確認)
					String invPeriod =  jsonObject.getString("invPeriod"); //對獎發票期別(民國年月)
					
					JSONArray detailObject = jsonObject.getJSONArray("details"); //[]為JSONArray
					for(int i = 0; i < detailObject.length(); i++)
					{
						String rowNum = detailObject.getJSONObject(i).getString("rowNum"); //明細編號(1,2,3...)
						String description = detailObject.getJSONObject(i).getString("description"); //品名
						String quantity = detailObject.getJSONObject(i).getString("quantity"); //數量
						String unitPrice = detailObject.getJSONObject(i).getString("unitPrice"); //單價
						String amount = detailObject.getJSONObject(i).getString("amount"); //小計
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
			Log.e("E", e.toString());
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
	
	public static HttpClient createHttpClient()
	{
	    HttpParams params = new BasicHttpParams();
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
	    HttpProtocolParams.setUseExpectContinue(params, true);

	    SchemeRegistry schReg = new SchemeRegistry();
	    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
	    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

	    return new DefaultHttpClient(conMgr, params);
	}

}
