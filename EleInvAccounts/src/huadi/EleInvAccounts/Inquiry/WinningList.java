package huadi.EleInvAccounts.Inquiry;

import huadi.EleInvAccounts.MySSLSocketFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class WinningList extends AsyncTask<String, String, String> // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
{
	//開獎號碼
	
	private final String detailUrl = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp?"
		+ "version=0.2"
		+ "&action=QryWinningList"
		+ "&invTerm={0}" //yyyMM, 必須為雙數月
		+ "&UUID={1}"
		+ "&appID={2}";

	public WinningList(Context context)
	{
		
	}
	
	@Override
	protected String doInBackground(String... params)
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
					String invoYm =  jsonObject.getString("invoYm"); //查詢開獎期別(民國年月)
					String superPrizeNo =  jsonObject.getString("superPrizeNo"); //千萬特獎號碼
					
					List<String> spcPrizeNo = new ArrayList<String>(); //特獎號碼(至少1組)
					spcPrizeNo.add(jsonObject.getString("spcPrizeNo"));
					for (int i = 2; i <= 3; i++)
						if(jsonObject.getString("spcPrizeNo") != "")
							spcPrizeNo.add(jsonObject.getString("spcPrizeNo" + i));					
					
					List<String> firstPrizeNo = new ArrayList<String>(); //頭獎號碼(至少3組)
					for (int i = 1; i <= 10; i++)
					{
						if(jsonObject.getString("firstPrizeNo" + i) != "")
							firstPrizeNo.add(jsonObject.getString("firstPrizeNo" + i));
					}
					
					List<String> sixthPrizeNo = new ArrayList<String>(); //六獎號碼(至少1組)
					for (int i = 1; i <= 3; i++)
						if(jsonObject.getString("sixthPrizeNo") != "")
							spcPrizeNo.add(jsonObject.getString("sixthPrizeNo" + i));
					
					String superPrizeAmt = jsonObject.getString("superPrizeAmt"); //千萬特獎金額
					String spcPrizeAmt = jsonObject.getString("spcPrizeAmt"); //特獎金額
					String firstPrizeAmt = jsonObject.getString("firstPrizeAmt"); //頭獎金額
					String secondPrizeAmt = jsonObject.getString("secondPrizeAmt"); //二獎金額
					String thirdPrizeAmt = jsonObject.getString("thirdPrizeAmt"); //三獎金額
					String fourthPrizeAmt = jsonObject.getString("fourthPrizeAmt"); //四獎金額
					String fifthPrizeAmt = jsonObject.getString("fifthPrizeAmt"); //五獎金額
					String sixthPrizeAmt = jsonObject.getString("sixthPrizeAmt"); //六獎金額
					
				}
				catch(Exception e)
				{
					Log.e("JSONObject Exception","版本, " + v + "回應碼, " + code + "訊息, " + msg);
				}			
				
			}			
		}
		catch (Exception e)
		{
			Log.e("WinningList", e.toString());
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
