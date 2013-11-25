package huadi.EleInvAccounts.Inquiry;

import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Manager.AutoAward;
import huadi.EleInvAccounts.Manager.ManualAward;

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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//開獎號碼
public class WinningList extends AsyncTask<String, Integer, Map<String, List<String>>> // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
{
	Map<String, List<String>> map = new HashMap<String, List<String>>();

	private ProgressDialog dialog;
	Activity activity;
	Context context;
	String modeString;

	String period = "";

	private final String detailUrl = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp?" + "version=0.2" + "&action=QryWinningList" + "&invTerm={0}" //yyyMM, 必須為雙數月
		+ "&UUID={1}" + "&appID={2}";

	public WinningList(Activity activity, String mode)
	{
		this.activity = activity;
		context = activity;
		modeString = mode;
		dialog = new ProgressDialog(context);
	}

	@Override
	protected Map<String, List<String>> doInBackground(String... params)
	{
		if (params.length < 0)
			return null;

		period = params[0];

		publishProgress(0); //進度

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

			//publishProgress(10); //進度

			if (httpResponse.getStatusLine().getStatusCode() == 200)//判斷網路連接是否成功
			{
				strResult = EntityUtils.toString(httpResponse.getEntity()); //抓下來的資料
				//Log.e("strResult", strResult);

				JSONObject jsonObject = new JSONObject(strResult); //{}為JSONObject

				String v = jsonObject.getString("v"); //版本號碼
				String code = jsonObject.getString("code"); //訊息回應碼
				String msg = jsonObject.getString("msg"); //系統回應訊息
				//Log.e("JSONObject Exception","版本" + v + ", 回應碼 " + code + ", 訊息" + msg);

				//publishProgress(30); //進度

				try
				{
					//String invoYm =  jsonObject.getString("invoYm"); //查詢開獎期別(民國年月)

					List<String> superPrizeNo = new ArrayList<String>(); //千萬特獎號碼
					superPrizeNo.add(jsonObject.getString("superPrizeNo"));

					List<String> spcPrizeNo = new ArrayList<String>(); //特獎號碼(至少1組)
					spcPrizeNo.add(jsonObject.getString("spcPrizeNo"));
					for (int i = 2; i <= 3; i++)
						if (jsonObject.getString("spcPrizeNo" + i).length() > 1)
							spcPrizeNo.add(jsonObject.getString("spcPrizeNo" + i));

					//publishProgress(50); //進度

					List<String> firstPrizeNo = new ArrayList<String>(); //頭獎號碼(至少3組)
					for (int i = 1; i <= 10; i++)
						if (jsonObject.getString("firstPrizeNo" + i).length() > 1)
							firstPrizeNo.add(jsonObject.getString("firstPrizeNo" + i));

					List<String> sixthPrizeNo = new ArrayList<String>(); //增開六獎號碼(至少1組)
					for (int i = 1; i <= 3; i++)
						if (jsonObject.getString("sixthPrizeNo" + i).length() > 1)
							sixthPrizeNo.add(jsonObject.getString("sixthPrizeNo" + i));

					//publishProgress(70); //進度

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

					//publishProgress(90); //進度
				}
				catch (Exception e)
				{
					Log.e("JSONObject Exception", "版本 " + v + ", 回應碼  " + code + ", 訊息 " + msg);
				}

			}
		}
		catch (Exception e)
		{
			Log.e("WinningList", e.toString());
		}
		finally
		{
			//publishProgress(100); //進度
		}

		return map;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		dialog.setMessage("Loading...");
		dialog.setCancelable(false);
		//dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... progress)
	{
		super.onProgressUpdate(progress);
		//dialog.setProgress(progress[0]); //回報進度
	}

	@Override
	protected void onPostExecute(Map<String, List<String>> result)
	{
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss(); //停止

		if (modeString == "Auto")
		{
			Auto(result);
		}

		if (modeString == "GetWinningList")
		{
			GetWinningList(result);
		}

		if (modeString == "Manual")
		{
			Manual(result);
		}
	}

	private void Auto(Map<String, List<String>> winning) //自動對獎
	{
		Map<String, List<String>> winningAward = null;
		int count = 0; //中獎筆數

		TextView textView23 = (TextView) activity.findViewById(R.id.textView23);
		textView23.setText("中獎清單");

		//winning = new WinningList(this).execute(String.format("%d%02d", year, month), UUID, appID).get();
		AutoAward autoAward = new AutoAward(context);

		winningAward = autoAward.Award(period, winning);
		count = winningAward.size();

		TableLayout invoicetable = (TableLayout) activity.findViewById(R.id.invoicetable);
		invoicetable.removeAllViews();

		//Log.e("count", "" + count);

		TableRow tr = new TableRow(context);
		LinearLayout l1;
		TextView[] prize;
		final TextView[] number;
		TextView[] date, store, cost;

		if (count > 0)
		{
			List<String> prizeInfoList = winningAward.get("prize");

			prize = new TextView[prizeInfoList.size()];
			number = new TextView[prizeInfoList.size()];
			date = new TextView[prizeInfoList.size()];
			store = new TextView[prizeInfoList.size()];
			cost = new TextView[prizeInfoList.size()];

			invoicetable.removeAllViews();

			//Log.e("prizeInfoList", "" + prizeInfoList);
			for (int i = 0; i < prizeInfoList.size(); i++)
			{
				//prizeName, invDate, invNum, sellerName, invTotalCost
				String prizeName = prizeInfoList.get(i).split(",")[0]; //獎項				
				String invDate = prizeInfoList.get(i).split(",")[1]; //日期
				String invNum = prizeInfoList.get(i).split(",")[2]; //發票號碼
				String sellerName = prizeInfoList.get(i).split(",")[3]; //消費商店
				String invTotalCost = prizeInfoList.get(i).split(",")[4] + " NTD"; //金額

				l1 = new LinearLayout(context);
				l1.setOrientation(LinearLayout.HORIZONTAL);

				prize[i] = new TextView(context);
				prize[i].setText(prizeName); //獎項
				prize[i].setPadding(0, 0, 20, 0);
				prize[i].setTextColor(Color.RED);
				prize[i].setMinimumWidth(150);
				prize[i].setGravity(Gravity.CENTER);
				date[i] = new TextView(context);
				date[i].setText(invDate); //日期
				date[i].setPadding(0, 0, 20, 0);
				date[i].setMinimumWidth(100);
				date[i].setGravity(Gravity.CENTER);
				number[i] = new TextView(context);
				number[i].setText(invNum); //發票號碼
				number[i].setPadding(0, 0, 20, 0);
				number[i].setMinimumWidth(150);
				number[i].setGravity(Gravity.CENTER);
				store[i] = new TextView(context);
				store[i].setText(sellerName); //消費商店
				store[i].setMaxEms(6);
				store[i].setLines(1);
				store[i].setPadding(0, 0, 20, 0);
				store[i].setMinimumWidth(100);
				store[i].setGravity(Gravity.CENTER);
				cost[i] = new TextView(context);
				cost[i].setText(invTotalCost); //金額
				cost[i].setMinimumWidth(100);
				cost[i].setGravity(Gravity.CENTER);

				l1.addView(prize[i]);
				l1.addView(date[i]);
				l1.addView(number[i]);
				l1.addView(store[i]);
				l1.addView(cost[i]);

				tr.addView(l1);
				invoicetable.addView(tr);
				tr = new TableRow(context);
			}
		}
		else
		{
			textView23.setText("無發票中獎");
		}

	}

	public void GetWinningList(Map<String, List<String>> winning) //取得開獎號碼
	{
		TextView text_price1, text_price2, text_price3, text_price4;
		text_price1 = (TextView) activity.findViewById(R.id.textView11);
		text_price2 = (TextView) activity.findViewById(R.id.textView12);
		text_price3 = (TextView) activity.findViewById(R.id.textView16);
		text_price4 = (TextView) activity.findViewById(R.id.textView17);

		if (winning.size() > 0)
		{
			//Map<String, List<String>> winning = new WinningList(this).execute(_invPeriod, UUID, appID).get();

			String spcPrizeNo = "", firstPrizeNo = "", sixthPrizeNo = "", superPrizeNo = "";

			//特獎號
			for (String no : winning.get("spcPrizeNo"))
				spcPrizeNo += no + "\n";
			text_price1.setText(spcPrizeNo.substring(0, spcPrizeNo.length() - 1));

			//頭獎號
			for (String no : winning.get("firstPrizeNo"))
				firstPrizeNo += no + "\n";
			text_price2.setText(firstPrizeNo.substring(0, firstPrizeNo.length() - 1));

			//增開獎號
			for (String no : winning.get("sixthPrizeNo"))
				sixthPrizeNo += no + "\n";
			text_price3.setText(sixthPrizeNo.substring(0, sixthPrizeNo.length() - 1));

			//特別獎號
			for (String no : winning.get("superPrizeNo"))
				superPrizeNo += no + "\n";
			text_price4.setText(superPrizeNo.substring(0, superPrizeNo.length() - 1));
		}
		else
		{
			text_price1.setText("無此期別資料");
			text_price2.setText("無此期別資料");
			text_price3.setText("無此期別資料");
			text_price4.setText("無此期別資料");
		}
	}

	public void Manual(final Map<String, List<String>> winning) //手動對獎
	{
		final TextView text_input;
		final TextView text_prizeornot;
		text_input = (TextView) activity.findViewById(R.id.textView97);
		text_prizeornot = (TextView) activity.findViewById(R.id.textView22);

		Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_clear, btn_backspace;
		btn_0 = (Button) activity.findViewById(R.id.button14);
		btn_1 = (Button) activity.findViewById(R.id.button10);
		btn_2 = (Button) activity.findViewById(R.id.button11);
		btn_3 = (Button) activity.findViewById(R.id.button12);
		btn_4 = (Button) activity.findViewById(R.id.button6);
		btn_5 = (Button) activity.findViewById(R.id.button8);
		btn_6 = (Button) activity.findViewById(R.id.button9);
		btn_7 = (Button) activity.findViewById(R.id.button3);
		btn_8 = (Button) activity.findViewById(R.id.button4);
		btn_9 = (Button) activity.findViewById(R.id.button5);
		btn_clear = (Button) activity.findViewById(R.id.button13);
		btn_backspace = (Button) activity.findViewById(R.id.button15);

		//winning = new WinningList(this).execute(String.format("%d%02d", year, month), UUID, appID).get();
		final ManualAward manualAward = new ManualAward();
		//Log.e("winning", "" + manualAward.Award("516", winning));		

		text_input.setText("請輸入號碼");
		text_prizeornot.setText("未中獎");

		btn_0.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("0", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("1", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("2", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("3", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_4.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("4", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_5.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("5", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_6.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("6", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_7.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("7", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_8.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("8", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_9.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AwardInput("9", manualAward, winning, text_input, text_prizeornot);
			}
		});

		btn_clear.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				text_input.setText("請輸入號碼");
			}
		});

		btn_backspace.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String currInput = text_input.getText().toString();
				if (currInput.length() == 1) //只有一個就清空
					text_input.setText("請輸入號碼");
				else if (currInput.length() == 2) //兩個就減一, 三個已經對獎了
					text_input.setText(currInput.substring(0, 1));
			}
		});
	}

	public void AwardInput(String InputNumString, ManualAward manualAward, Map<String, List<String>> winning, TextView text_input, TextView text_prizeornot) //判斷手動對獎的輸入
	{
		if (!text_input.getText().toString().equals("請輸入號碼")) //為數字
		{
			text_input.setText(text_input.getText().toString() + InputNumString);
			if (text_input.getText().toString().length() == 3)
			{
				try
				{
					text_prizeornot.setText(manualAward.Award(text_input.getText().toString(), winning));
				}
				catch (Exception e)
				{
					//e.printStackTrace();
					text_prizeornot.setText("無此期別資料");
				}
				text_input.setText("請輸入號碼");
			}
		}
		else
			text_input.setText(InputNumString);
	}

}
