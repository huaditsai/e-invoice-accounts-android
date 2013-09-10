package huadi.EleInvAccounts.Accounts;

import java.util.Calendar;

import huadi.EleInvAccounts.DBHelper;
import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//記帳
public class AccountsActivity extends Activity
{	
	boolean isCapture = false;
	int index = 0;
	boolean isCapturePopView = false;
	
	String invNum = "";
	SQLiteDatabase db = null;

	int year, month;
	String invPeriod; //對獎發票期別(yyyMM)
	
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting, btn_left, btn_right;
	Button btn_addone, btn_scan, btn_import, btn_income, btn_expend, btn_save, btn_cancel, btn_bg;
	TextView text_total, text_income, text_expenditure, text_balance, text_month;
	TableLayout table;
	LinearLayout popview;
	EditText editText1, editText3, editText5, editText6, editText7, editText14;
	Spinner spinner1, spinner2, spinner3;
	
	boolean isIncome = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		
		Intent intent = getIntent(); 
		isCapture = intent.getBooleanExtra("isCapture", false);
		invNum = intent.getStringExtra("invNum");
		
		//Log.e("isCapture", isCapture + ", " +invNum);
		
		DBHelper dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();

		setUI();		
	}

	private void setUI()
	{
		btn_backfunc = (ImageButton) findViewById(R.id.imageButton1);
		btn_account = (ImageButton) findViewById(R.id.imageButton2);
		btn_manager = (ImageButton) findViewById(R.id.imageButton3);
		btn_social = (ImageButton) findViewById(R.id.imageButton4);
		btn_setting = (ImageButton) findViewById(R.id.imageButton5);
		btn_addone = (Button) findViewById(R.id.button1);
		btn_scan = (Button) findViewById(R.id.button2);
		btn_import = (Button) findViewById(R.id.button3);
		btn_income = (Button) findViewById(R.id.button4);
		btn_expend = (Button) findViewById(R.id.button5);
		btn_save = (Button) findViewById(R.id.button6);
		btn_cancel = (Button) findViewById(R.id.button7);
		text_total = (TextView) findViewById(R.id.textView6);
		text_income = (TextView) findViewById(R.id.textView7);
		text_expenditure = (TextView) findViewById(R.id.textView8);
		text_balance = (TextView) findViewById(R.id.textView9);
		table = (TableLayout) findViewById(R.id.TableLayout1);
		popview = (LinearLayout) findViewById(R.id.LinearLayout1);
		btn_bg = (Button)findViewById(R.id.button8);
		btn_left = (ImageButton) findViewById(R.id.imageButton6); 
		btn_right = (ImageButton) findViewById(R.id.imageButton97); 
		text_month = (TextView) findViewById(R.id.textView77);
		

		//月份選擇----------------------------------------------------
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 1911; //民國
		month = calendar.get(Calendar.MONTH) +1; //Calendar.MONTH 從0開始...
		invPeriod = String.format("%d 年 %02d 月", year, month);
		
		text_month.setText(invPeriod); //月份
		btn_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				month -= 1;
				if(month == 0 && year != 0)
				{
					year--;
					month = 12;
				}
				invPeriod = String.format("%d 年 %02d 月", year, month);
				text_month.setText(invPeriod); //月份
				GetChargeList(year+1911, month);
			}});
		btn_right.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				month += 1;
				if(month == 14 && year != 0)
				{
					year++;
					month = 2;
				}
				invPeriod = String.format("%d 年 %02d 月", year, month);
				text_month.setText(invPeriod); //月份				
				GetChargeList(year+1911, month);
			}});
		//月份選擇----------------------------------------------------

		btn_addone.setOnClickListener(new OnClickListener() //手動記帳
		{
			@Override
			public void onClick(View v)
			{
				isCapture = false;
				popview.setVisibility(View.VISIBLE);
				btn_bg.setVisibility(View.VISIBLE);
			}
		});
		btn_scan.setOnClickListener(new OnClickListener() //條碼掃描
		{
			@Override
			public void onClick(View v)
			{
				if(IsInternet())
				{
					isCapture = true;
					Intent intent = new Intent(AccountsActivity.this, CaptureActivity.class);
					startActivity(intent);
				}
				else
				{
					new AlertDialog.Builder(AccountsActivity.this)
					.setTitle("請開啟網路連線功能")
					.setMessage("取得發票資訊需要網路連線")
					.setPositiveButton("確定", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));							
						}									
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener()	{
						@Override
						public void onClick(DialogInterface dialog, int which){														
						}
					}).show();
				}
			}
		});

		//popview ---------------------------------
		
		editText1 = (EditText)findViewById(R.id.editText1);
		spinner1 = (Spinner)findViewById(R.id.spinner1); //帳本
		editText14 = (EditText)findViewById(R.id.editText14); //發票編號
		editText3 = (EditText)findViewById(R.id.editText3); //項目
		spinner2 = (Spinner)findViewById(R.id.spinner2); //分類
		spinner3 = (Spinner)findViewById(R.id.spinner3); //分類2
		editText5 = (EditText)findViewById(R.id.editText5); //日期
		editText6 = (EditText)findViewById(R.id.editText6); //商店
		editText7 = (EditText)findViewById(R.id.editText7); //備註
		
		//預設支出
		isIncome = false;
		btn_income.getBackground().setAlpha(60);
		btn_expend.getBackground().setAlpha(255);
				
		index = 0;
		if(isCapture)
		{
			isCapturePopView = true;
			CapturePopView(index);
		}
		
		//Account
		Cursor accountCursor = db.rawQuery("SELECT * "
			+ "FROM Account ", null); //要記得''包起來
		
		int accountCount = accountCursor.getCount(); //資料筆數
		String[] account = new String[accountCount];
		
		if(accountCount != 0)
		{
			accountCursor.moveToFirst();
			for (int i = 0; i < accountCount; i++)
			{
				account[i] = accountCursor.getString(accountCursor.getColumnIndex("account_name"));
				accountCursor.moveToNext(); //移至資料庫下一筆
			}
		}
		else 
		{
			ContentValues accountCV = new ContentValues();
			accountCV.put("account_name", "test");
			db.insert("Account", null, accountCV);
		}
		
		//建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AccountsActivity.this, 
			android.R.layout.simple_spinner_item, account);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定下拉選單的樣式
		spinner1.setAdapter(adapter1); //mainCategory
		
		//mainCategory
		Cursor mainCateCursor = db.rawQuery("SELECT mainCategory "
			+ "FROM MainCategory ", null); //要記得''包起來
		
		int mainCateCount = mainCateCursor.getCount(); //資料筆數
		String[] mainCategory = new String[mainCateCount];
		
		if(mainCateCount != 0)
		{
			mainCateCursor.moveToFirst();
			for (int i = 0; i < mainCateCount; i++)
			{
				mainCategory[i] = mainCateCursor.getString(mainCateCursor.getColumnIndex("mainCategory"));
				mainCateCursor.moveToNext(); //移至資料庫下一筆
			}
		}
		else 
		{
			ContentValues mainCateCV = new ContentValues();
			mainCateCV.put("mainCategory", "食");
			db.insert("MainCategory", null, mainCateCV);
			mainCateCV.put("mainCategory", "衣");
			db.insert("MainCategory", null, mainCateCV);
		}
		
		//建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AccountsActivity.this, 
			android.R.layout.simple_spinner_item, mainCategory);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定下拉選單的樣式
		spinner2.setAdapter(adapter2); //mainCategory
		
		//SubCategory
		Cursor subCursor = db.rawQuery("SELECT subCategory "
			+ "FROM SubCategory ", null); //要記得''包起來
		
		int subCateCount = subCursor.getCount(); //資料筆數
		String[] subCategory = new String[subCateCount];
		
		if(subCateCount != 0)
		{
			subCursor.moveToFirst();
			for (int i = 0; i < subCateCount; i++)
			{
				subCategory[i] = subCursor.getString(subCursor.getColumnIndex("subCategory"));
				subCursor.moveToNext(); //移至資料庫下一筆
			}
		}
		else 
		{
			ContentValues subCateCV = new ContentValues();
			subCateCV.put("subCategory", "飲料");
			db.insert("SubCategory", null, subCateCV);
			subCateCV.put("subCategory", "T-shirt");
			db.insert("SubCategory", null, subCateCV);
		}
		
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(AccountsActivity.this, 
			android.R.layout.simple_spinner_item, subCategory);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定下拉選單的樣式
		spinner3.setAdapter(adapter3); //SubCategory
		
		
		btn_income.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btn_income.getBackground().setAlpha(255);
				btn_expend.getBackground().setAlpha(60);
				isIncome = true;
			}
		});
		btn_expend.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btn_expend.getBackground().setAlpha(255);
				btn_income.getBackground().setAlpha(60);
				isIncome = false;
			}
		});
		btn_save.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{				
				String date = editText5.getText().toString(); // TODO 要規定為yyyyMMdd
//				String accountName = editText2.getText().toString();
				String accountName = spinner1.getSelectedItem().toString();				
				int money = Integer.parseInt(editText1.getText().toString()); // TODO 要規定必填, 數字
//					String mainCategory = editText4.getText().toString();
//					String subCategory = editText4.getText().toString();
				String mainCategory = spinner2.getSelectedItem().toString();
				String subCategory = spinner3.getSelectedItem().toString();
				String item = editText3.getText().toString(); // TODO 要規定必填
				String sellerName = editText6.getText().toString();
				String invNum = editText14.getText().toString();
				String remark = editText7.getText().toString();
				
				if(isIncome) //收入為正
					money = money > 0 ? money : -money;
				else
					money = money > 0 ? -money : money;
				
				ContentValues accountCV = new ContentValues();
				accountCV.put("date", date); //日期(yyyyMMdd)
				accountCV.put("accountName", accountName); //記帳帳本
				accountCV.put("money", money); //項目所花的金額
				accountCV.put("mainCategory", mainCategory); //
				accountCV.put("subCategory", subCategory); //
				accountCV.put("item", item); //項目
				accountCV.put("store", sellerName); //商店名稱
				accountCV.put("invNum", invNum); //發票編號
				accountCV.put("remark", remark); //備註
				
				Cursor accountCursor = db.rawQuery("SELECT invNum "
					+ "FROM Charge "
					+ "WHERE item = '" + item + "' "
					+ "AND invNum = '" + invNum + "' ", null); //要記得''包起來
				
				int count = accountCursor.getCount(); //資料筆數
				if(count == 0)
					db.insert("Charge", null, accountCV); //新增一筆至 Invoice
				else
				{
					accountCursor.moveToFirst();
					for (int i = 0; i < count; i++)
					{
						db.update("Charge", accountCV, "invNum = '" + invNum + "'" + "AND item = '" + item + "' ", null);
						accountCursor.moveToNext(); //移至資料庫下一筆
					}
				}
				
				if(!isCapture && invNum.length() > 0) //手動發票記帳(傳統發票), 若為 發票, 就要存到Invoice,InvDetail
				{					
					int month = Integer.parseInt(date.substring(4, 6)) % 2 == 1?Integer.parseInt(date.substring(4, 6)) + 1 
						: Integer.parseInt(date.substring(4, 6)); //雙數月
					String invPeriod = String.format("%d%02d", Integer.parseInt(date.substring(0, 4))-1911, month);//yyyMM
					ContentValues invoiceCV = new ContentValues();
					invoiceCV.put("invNum", invNum); //發票編號
					invoiceCV.put("invTotalCost", -money); //消費金額
					invoiceCV.put("invDate", date); //發票開立日期(yyyyMMdd)
					invoiceCV.put("sellerName", sellerName); //賣方名稱
					invoiceCV.put("invStatus", ""); //發票狀態(已確認)
					invoiceCV.put("invPeriod", invPeriod); //對獎發票期別(民國年月)
					
					//Log.e("invPeriod", invNum + ", " + invPeriod);
					
					Cursor invoiceCursor = db.rawQuery("SELECT invNum "
						+ "FROM Invoice "
						+ "WHERE invNum = '" + invNum + "'", null); //要記得''包起來
					
					int invCount = invoiceCursor.getCount(); //資料筆數
					if(invCount == 0)
						db.insert("Invoice", null, invoiceCV); //新增一筆至 Invoice
					else
					{
						invoiceCursor.moveToFirst();
						for (int i = 0; i < invCount; i++)
						{
							db.update("Invoice", invoiceCV, "invNum = '" + invNum + "'", null);
							invoiceCursor.moveToNext(); //移至資料庫下一筆
						}
					}
					
					ContentValues invDetailCV = new ContentValues();
					invDetailCV.put("invNum", invNum); //發票編號
					invDetailCV.put("rowNum", "1"); //明細編號(1,2,3...)
					invDetailCV.put("description", item); //品名
					invDetailCV.put("quantity", "1"); //數量
					invDetailCV.put("unitPrice", -money); //單價
					invDetailCV.put("amount", -money); //小記
					
					Cursor invDetailCursor = db.rawQuery("SELECT invNum, rowNum "
						+ "FROM InvDetail "
						+ "WHERE invNum = '" + invNum + "' ", null); //要記得''包起來
					
					int invDetailcount = invDetailCursor.getCount(); //資料筆數
					if(invDetailcount == 0)
						db.insert("InvDetail", null, invDetailCV); //新增一筆至 InvDetail
					else
					{
						invDetailCursor.moveToFirst();
						for (int j = 0; j < invDetailcount; j++)
						{								
							db.update("InvDetail", invDetailCV, "invNum = '" + invNum + "'", null);
							invDetailCursor.moveToNext(); //移至資料庫下一筆
						}
					}
				}

				InitPopView();
				
				if(isCapturePopView) //發票掃完會逐一跳出
				{
					index++;
					CapturePopView(index);
					//Log.e("index", "" + index);
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//掃完qrCode時, 會先進db
				//db.delete("Invoice", "invNum = '" + invNum + "'", null);
				//db.delete("InvDetail", "invNum = '" + invNum + "'", null);
				
				InitPopView();
			}
		});
		//popview ---------------------------------

		//Side menu -----------------------------------------------
		btn_backfunc.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AccountsActivity.this, MainActivity.class);
				startActivity(intent);
				db.close();
				AccountsActivity.this.finish();
			}
		});
		btn_manager.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AccountsActivity.this, ManagerActivity.class);
				startActivity(intent);
				db.close();
				AccountsActivity.this.finish();
			}
		});
		btn_social.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AccountsActivity.this, SocialActivity.class);
				startActivity(intent);
				db.close();
				AccountsActivity.this.finish();
			}
		});
		btn_setting.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AccountsActivity.this, SettingsActivity.class);
				startActivity(intent);
				db.close();
				AccountsActivity.this.finish();
			}
		});
		//Side menu -----------------------------------------------		

//		Calendar calendar = Calendar.getInstance();
		int _year = calendar.get(Calendar.YEAR); //民國
		int _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始
		GetChargeList(_year, _month);
	}
	
	public void GetChargeList(int _year, int _month) //一進去的列表
	{
//		Calendar calendar = Calendar.getInstance();
//		int _year = calendar.get(Calendar.YEAR); //民國
//		int _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始
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
		
		text_total.setText("總資產");
		text_income.setText("本月收入 " + income);
		text_expenditure.setText("本月支出 " + -expend);
		text_balance.setText("本月結餘 " + balance);

		TableRow tr = new TableRow(this);
		LinearLayout l1, l2, l3, l4;
		TextView[] month, day, item, main, sub, cost, account;
		
		Cursor ChargeListCursor = db.rawQuery("SELECT date, accountName, money, item, mainCategory, subCategory "
			+ "FROM Charge "
			+ "WHERE date >= " + String.format("'%d%02d00' ", _year, _month)
			+ "AND date <= " + String.format("'%d%02d31' ", _year, _month)
			+ "ORDER BY date ASC", null);
		
		int ChargeListCount = ChargeListCursor.getCount(); //資料筆數
		month = new TextView[ChargeListCount];
		day = new TextView[ChargeListCount];
		item = new TextView[ChargeListCount];
		main = new TextView[ChargeListCount];
		sub = new TextView[ChargeListCount];
		cost = new TextView[ChargeListCount];
		account = new TextView[ChargeListCount];

		table.removeAllViews();

		if(ChargeListCount != 0)
		{
			ChargeListCursor.moveToFirst(); //移至資料庫第一筆
			for (int i = 0; i < ChargeListCount; i++)
			{				
				l1 = new LinearLayout(this);
				l1.setOrientation(LinearLayout.HORIZONTAL);
				month[i] = new TextView(this);
				month[i].setText(ChargeListCursor.getString(ChargeListCursor.getColumnIndex("date")).substring(4,6));
				month[i].setTextSize(20);
				TextPaint tp = month[i].getPaint();
				tp.setFakeBoldText(true);
				l1.addView(month[i]);
				day[i] = new TextView(this);
				day[i].setText(" / " + ChargeListCursor.getString(ChargeListCursor.getColumnIndex("date")).substring(6,8));
				day[i].setPadding(0, 0, 20, 0);
				l1.addView(day[i]);
				l2 = new LinearLayout(this);
				l2.setOrientation(LinearLayout.VERTICAL);
				item[i] = new TextView(this);
				item[i].setText(ChargeListCursor.getString(ChargeListCursor.getColumnIndex("item")));
				item[i].setTypeface(null, Typeface.BOLD);
				item[i].setMinimumWidth(430);
				item[i].setMaxEms(5);
				item[i].setEllipsize(TruncateAt.END);
				item[i].setLines(1);
				l2.addView(item[i]);
				l3 = new LinearLayout(this);
				l3.setOrientation(LinearLayout.HORIZONTAL);
				main[i] = new TextView(this);
				main[i].setText(ChargeListCursor.getString(ChargeListCursor.getColumnIndex("mainCategory")) + " - ");
				l3.addView(main[i]);
				sub[i] = new TextView(this);
				sub[i].setText(ChargeListCursor.getString(ChargeListCursor.getColumnIndex("subCategory")));
				sub[i].setPadding(0, 0, 40, 0);
				l3.addView(sub[i]);
				l2.addView(l3);
				l1.addView(l2);
				l4 = new LinearLayout(this);
				l4.setOrientation(LinearLayout.VERTICAL);
				cost[i] = new TextView(this);
				cost[i].setText(ChargeListCursor.getInt(ChargeListCursor.getColumnIndex("money")) + "NTD");
				cost[i].setPadding(0, 0, 40, 0);
				l4.addView(cost[i]);
				account[i] = new TextView(this);
				account[i].setText(ChargeListCursor.getString(ChargeListCursor.getColumnIndex("accountName")));
				l4.addView(account[i]);
				l1.addView(l4);
				tr.addView(l1);
				table.addView(tr);
				tr = new TableRow(this);				
				
				ChargeListCursor.moveToNext(); //移至資料庫下一筆
			}
		}
	}
	
	private void InitPopView()
	{
		InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)); //隱藏 keyboard
		imm.hideSoftInputFromWindow(AccountsActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		popview.setVisibility(View.GONE);
		btn_bg.setVisibility(View.GONE);
		editText1.setText("");
//		editText2.setText("");
		editText14.setText("");
		editText3.setText("");
//		editText4.setText("");
		editText5.setText("");
		editText6.setText("");
		editText7.setText("");
		
		Calendar calendar = Calendar.getInstance();
		int _year = calendar.get(Calendar.YEAR); //民國
		int _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始
		GetChargeList(_year, _month);
	}
	
	private void CapturePopView(int _index)
	{
		Cursor invDetailCursor = db.rawQuery("SELECT invNum, description, amount "
			+ "FROM InvDetail "
			+ "WHERE invNum = '" + invNum + "' ", null); //要記得''包起來
		
		int count = invDetailCursor.getCount(); //資料筆數
		if(index >= count)
			isCapturePopView = false;
		
		if(isCapturePopView) //若是掃QR code
		{
			//發票應該都是支出
			isIncome = false;
			btn_expend.getBackground().setAlpha(255);
			btn_income.getBackground().setAlpha(60);
			popview.setVisibility(View.VISIBLE);
			btn_bg.setVisibility(View.VISIBLE);
			
			if(count == 0) //當網路慢, 更新ui會比爬資料快
			{
				Intent intent = new Intent(AccountsActivity.this, AccountsActivity.class);
				intent.putExtra("isCapture", true);
				intent.putExtra("invNum", invNum);
				startActivity(intent);
				finish();
			}
			//Log.e("count",""+count);
			if(count != 0)
			{
				if (_index < count)
				{
					invDetailCursor.moveToPosition(_index);
					int amount = invDetailCursor.getInt(invDetailCursor.getColumnIndex("amount"));
					if(amount < 0) //若(發票)小計<0, 支出就變收入
					{
						amount = -amount;
						isIncome = true;
						btn_expend.getBackground().setAlpha(60);
						btn_income.getBackground().setAlpha(255);
					} 
					editText1.setText("" + amount);
					editText14.setText(invDetailCursor.getString(invDetailCursor.getColumnIndex("invNum")));
					editText3.setText(invDetailCursor.getString(invDetailCursor.getColumnIndex("description")));
					//invDetailCursor.moveToNext();
				}
			}
			
			Cursor invCursor = db.rawQuery("SELECT invDate, sellerName "
				+ "FROM Invoice "
				+ "WHERE invNum = '" + invNum + "' ", null); //要記得''包起來
			
			int count2 = invCursor.getCount(); //資料筆數
			//Log.e("count2",""+count2);
			if(count2 != 0)
			{
				invCursor.moveToFirst();
				for (int i = 0; i < count2; i++)
				{			
					editText5.setText(invCursor.getString(invCursor.getColumnIndex("invDate")));
					editText6.setText(invCursor.getString(invCursor.getColumnIndex("sellerName")));
					invCursor.moveToNext();
				}
			}
		}
	}
	
	private boolean IsInternet()
	{
		ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);//先取得此service
		NetworkInfo networInfo = conManager.getActiveNetworkInfo(); //在取得相關資訊
		
		if (networInfo == null || !networInfo.isAvailable()) //沒網路
			return false;
		else
			return true;
	}

}
