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

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class CarrierDetail extends AsyncTask<String, Integer, String> // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
{
	//載具明細
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	
	private ProgressDialog dialog;
	Context mContext;
	
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
		
		mContext = context;
		dialog = new ProgressDialog(context);
	}
	
	@Override
	protected String doInBackground(String... params)
	{
		if (params.length < 0)
			return null;
		
		publishProgress(0); //進度
		
		String nowTime = "" + (System.currentTimeMillis() / 1000); //new GetNTP().execute("").get();
		String timeStamp = "" + (Integer.parseInt(nowTime) + 100);
		String expTimeStamp = "" + (Integer.parseInt(timeStamp) + 100000);
		
		String url = MessageFormat.format(detailUrl, params[0], params[1], expTimeStamp, timeStamp, params[2]
													, params[3], params[4], params[5], params[6]);
		
		HttpGet get = new HttpGet(url);
		String strResult = "";

		try
		{
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			HttpClient httpClient = SslSocketFactory.createMyHttpClient(); //new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = null;
			httpResponse = httpClient.execute(get);
			
			publishProgress(10); //進度

			if (httpResponse.getStatusLine().getStatusCode() == 200)//判斷網路連接是否成功
			{
				strResult = EntityUtils.toString(httpResponse.getEntity()); //抓下來的資料
//				Log.e("CarrierDetail", strResult);
			
				JSONObject jsonObject = new JSONObject(strResult); //{}為JSONObject
				
				String v =  jsonObject.getString("v"); //版本號碼
				String code =  jsonObject.getString("code"); //訊息回應碼
				String msg =  jsonObject.getString("msg"); //系統回應訊息
				
				publishProgress(30); //進度
				
				try
				{
					String invNum =  jsonObject.getString("invNum"); //發票號碼
					String invDate = params[3].replace("/", ""); //jsonObject.getString("invDate"); //發票開立日期(yyyyMMdd) 文件騙人啦！
					String sellerName =  jsonObject.getString("sellerName"); //賣方名稱					
					String totalAmount =  jsonObject.getString("amount"); //總金額					
					String invStatus =  jsonObject.getString("invStatus"); //發票狀態(已確認)
					int month = Integer.parseInt(invDate.substring(4,6));
					if(month % 2 == 1)
						month++;
					String invPeriod = String.format("%d%02d", Integer.parseInt(invDate.substring(0,4))-1911, month);
					
					ContentValues InvoiceCV = new ContentValues();
					InvoiceCV.put("invNum", invNum); //發票編號
					InvoiceCV.put("invTotalCost", totalAmount); //消費金額
					InvoiceCV.put("invDate", invDate); //發票開立日期(yyyyMMdd)
					InvoiceCV.put("sellerName", sellerName); //賣方名稱
					InvoiceCV.put("invStatus", invStatus); //發票狀態(已確認)
					InvoiceCV.put("invPeriod", invPeriod); //對獎發票期別(民國年月)
					
					publishProgress(50); //進度
					
					Cursor InvoiceCursor = db.rawQuery("SELECT invNum "
						+ "FROM Invoice "
						+ "WHERE invNum = '" + invNum + "'", null); //要記得''包起來
					
					int count = InvoiceCursor.getCount(); //資料筆數
					if(count == 0)
						db.insert("Invoice", null, InvoiceCV); //新增一筆至 Invoice
					else
					{
						InvoiceCursor.moveToFirst();
						for (int i = 0; i < count; i++)
						{
							db.update("Invoice", InvoiceCV, "invNum = '" + invNum + "'", null);
							InvoiceCursor.moveToNext(); //移至資料庫下一筆
						}
					}
					
					publishProgress(70); //進度
					
					JSONArray detailArray = jsonObject.getJSONArray("details"); //[]為JSONArray
					for(int i = 0; i < detailArray.length(); i++)
					{
						String rowNum = detailArray.getJSONObject(i).getString("rowNum"); //明細編號(1,2,3...)
						String description = detailArray.getJSONObject(i).getString("description"); //品名
						String quantity = detailArray.getJSONObject(i).getString("quantity"); //數量
						String unitPrice = detailArray.getJSONObject(i).getString("unitPrice"); //單價
						String amount = detailArray.getJSONObject(i).getString("amount"); //小計	
						
						ContentValues InvDetailCV = new ContentValues();
						InvDetailCV.put("invNum", invNum); //發票編號
						InvDetailCV.put("rowNum", rowNum); //明細編號(1,2,3...)
						InvDetailCV.put("description", description); //品名
						InvDetailCV.put("quantity", quantity); //數量
						InvDetailCV.put("unitPrice", unitPrice); //單價
						InvDetailCV.put("amount", amount); //小記
						
						publishProgress(70 + i); //進度
						
						Cursor invDetailCursor = db.rawQuery("SELECT invNum, rowNum "
							+ "FROM InvDetail "
							+ "WHERE invNum = '" + invNum + "' "
							+ "AND rowNum = '" + rowNum + "' ", null); //要記得''包起來
						
						int count2 = invDetailCursor.getCount(); //資料筆數
						if(count2 == 0)
						{
							db.insert("InvDetail", null, InvDetailCV); //新增一筆至 InvDetail
//							Log.e("db.insert", "" + count2 + ", " + rowNum);
						}
						else
						{
							invDetailCursor.moveToFirst();
							for (int j = 0; j < count2; j++)
							{								
								db.update("InvDetail", InvDetailCV, "invNum = '" + invNum + "'" + "AND rowNum = '" + rowNum + "' ", null);
								invDetailCursor.moveToNext(); //移至資料庫下一筆
//								Log.e("db.update", "" + count2 + ", " + rowNum);
							}
						}
					}
					publishProgress(90); //進度
				}
				catch(Exception e)
				{
					Log.e("JSONObject CarrierDetail","版本, " + v + "回應碼, " + code + "訊息, " + msg);
				}			
				
			}			
		}
		catch (Exception e)
		{
			Log.e("CarrierDetail", e.toString());
		}
		finally
		{
			publishProgress(100); //進度
		}
		
		return null;
	}
	
	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
    }
	
	@Override
	protected void onProgressUpdate(Integer... progress)
	{
		super.onProgressUpdate(progress);
		dialog.setProgress(progress[0]); //回報進度
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss(); //停止
	}

	
}
