package huadi.EleInvAccounts.Accounts;

import java.util.Calendar;

import huadi.EleInvAccounts.DBHelper;
import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//記帳
public class AccountsActivity extends Activity
{	
	boolean isCapture = false;
	String invNum = "";
	SQLiteDatabase db = null;
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	Button btn_addone, btn_scan, btn_import, btn_income, btn_expend, btn_save, btn_cancel;
	TextView text_total, text_income, text_expenditure, text_balance;
	TableLayout table;
	LinearLayout popview;
	EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7, editText14;
	
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
		// TODO Auto-generated method stub
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

		btn_addone.setOnClickListener(new OnClickListener() //手動記帳
		{
			@Override
			public void onClick(View v)
			{
				isCapture = false;
				popview.setVisibility(View.VISIBLE);
			}
		});
		btn_scan.setOnClickListener(new OnClickListener() //條碼掃描
		{
			@Override
			public void onClick(View v)
			{
				isCapture = true;
				Intent intent = new Intent(AccountsActivity.this, CaptureActivity.class);
				startActivity(intent);
			}
		});

		//popview ---------------------------------
		
		editText1 = (EditText)findViewById(R.id.editText1);
		editText2 = (EditText)findViewById(R.id.editText2); //帳本
		editText14 = (EditText)findViewById(R.id.editText14); //發票編號
		editText3 = (EditText)findViewById(R.id.editText3); //項目
		editText4 = (EditText)findViewById(R.id.editText4); //分類
		editText5 = (EditText)findViewById(R.id.editText5); //日期
		editText6 = (EditText)findViewById(R.id.editText6); //商店
		editText7 = (EditText)findViewById(R.id.editText7); //備註
		
		btn_income.getBackground().setAlpha(60);
		btn_expend.getBackground().setAlpha(60);
		
		if(isCapture) //若是掃QR code
		{
			//發票應該都是支出
			btn_expend.getBackground().setAlpha(255);
			btn_income.getBackground().setAlpha(60);
			popview.setVisibility(View.VISIBLE);
			
			Cursor invDetailCursor = db.rawQuery("SELECT invNum, description, amount "
				+ "FROM InvDetail "
				+ "WHERE invNum = '" + invNum + "' ", null); //要記得''包起來
			
			int count = invDetailCursor.getCount(); //資料筆數
			//Log.e("count",""+count);
			if(count != 0)
			{
				invDetailCursor.moveToFirst();
				for (int i = 0; i < count; i++)
				{
					int amount = invDetailCursor.getInt(invDetailCursor.getColumnIndex("amount"));
					if(amount < 0) //若(發票)小計<0, 支出就變收入
					{
						amount = -amount;
						isIncome = true;
						btn_expend.getBackground().setAlpha(60);
						btn_income.getBackground().setAlpha(255);
					} else isIncome = false;
					editText1.setText("" + amount);
					editText14.setText(invDetailCursor.getString(invDetailCursor.getColumnIndex("invNum")));
					editText3.setText(invDetailCursor.getString(invDetailCursor.getColumnIndex("description")));
					invDetailCursor.moveToNext();
				}
			}
			
			Cursor invCursor = db.rawQuery("SELECT invDate, sellerName "
				+ "FROM Invoice "
				+ "WHERE invNum = '" + invNum + "' ", null); //要記得''包起來
			
			int count2 = invCursor.getCount(); //資料筆數
			//Log.e("count2",""+count);
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
		
		btn_income.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
				String accountName = editText2.getText().toString(); // TODO 要下啦選擇
				int money = Integer.parseInt(editText1.getText().toString()); // TODO 要規定必填
					String mainCategory = editText4.getText().toString(); // TODO 要下啦選擇
					String subCategory = editText4.getText().toString(); // TODO 要下啦選擇
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
				
				if(!isCapture && invNum != "") //手動發票記帳(傳統發票), 若為 發票, 就要存到Invoice,InvDetail
				{
					int month = Integer.parseInt(date.substring(0, 4)) % 2 == 1?Integer.parseInt(date.substring(0, 4)) + 1 
						: Integer.parseInt(date.substring(0, 4));
					String invPeriod = String.format("%d%02d", Integer.parseInt(date.substring(0, 4))-1911, month);
					ContentValues invoiceCV = new ContentValues();
					invoiceCV.put("invNum", invNum); //發票編號
					invoiceCV.put("invTotalCost", money); //消費金額
					invoiceCV.put("invDate", date); //發票開立日期(yyyyMMdd)
					invoiceCV.put("sellerName", sellerName); //賣方名稱
					invoiceCV.put("invStatus", ""); //發票狀態(已確認)
					invoiceCV.put("invPeriod", invPeriod); //對獎發票期別(民國年月)
					
					Cursor invoiceCursor = db.rawQuery("SELECT invNum "
						+ "FROM Invoice "
						+ "WHERE invNum = '" + invNum + "'", null); //要記得''包起來
					
					int invoiceCVcount = invoiceCursor.getCount(); //資料筆數
					if(count == 0)
						db.insert("Invoice", null, invoiceCV); //新增一筆至 Invoice
					else
					{
						invoiceCursor.moveToFirst();
						for (int i = 0; i < invoiceCVcount; i++)
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
					invDetailCV.put("unitPrice", money); //單價
					invDetailCV.put("amount", money); //小記
					
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
				
				InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)); //隱藏 keyboard
				imm.hideSoftInputFromWindow(AccountsActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				popview.setVisibility(View.GONE);
				editText1.setText("");
				editText2.setText("");
				editText14.setText("");
				editText3.setText("");
				editText4.setText("");
				editText5.setText("");
				editText6.setText("");
				editText7.setText("");
				GetChargeList();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)); //隱藏 keyboard
				imm.hideSoftInputFromWindow(AccountsActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				popview.setVisibility(View.GONE);
				editText1.setText("");
				editText2.setText("");
				editText14.setText("");
				editText3.setText("");
				editText4.setText("");
				editText5.setText("");
				editText6.setText("");
				editText7.setText("");
				GetChargeList();
			}
		});
		//popview ---------------------------------

		//Side menu -----------------------------------------------
		btn_backfunc.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				Intent intent = new Intent(AccountsActivity.this, SettingsActivity.class);
				startActivity(intent);
				db.close();
				AccountsActivity.this.finish();
			}
		});
		//Side menu -----------------------------------------------		
		
		GetChargeList();
	}
	
	public void GetChargeList()
	{
		Calendar calendar = Calendar.getInstance();
		int _year = calendar.get(Calendar.YEAR); //民國
		int _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始
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
		text_expenditure.setText("本月支出 " + expend);
		text_balance.setText("本月結餘 " + balance);

		TableRow tr = new TableRow(this);
		LinearLayout l1, l2, l3;
		TextView[] month, day, item, main, sub, cost, account;
		
		Cursor ChargeListCursor = db.rawQuery("SELECT date, accountName, money, item, mainCategory, subCategory "
			+ "FROM Charge "
			+ "WHERE date >= " + String.format("'%d%02d00' ", _year, _month)
			+ "AND date <= " + String.format("'%d%02d31' ", _year, _month), null);
		
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
				item[i].setMinimumWidth(400);
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
				cost[i] = new TextView(this);
				cost[i].setText(ChargeListCursor.getInt(ChargeListCursor.getColumnIndex("money")) + "NTD");
				cost[i].setMinimumWidth(100);
				cost[i].setPadding(0, 0, 40, 0);
				l1.addView(cost[i]);
				account[i] = new TextView(this);
				account[i].setText(ChargeListCursor.getString(ChargeListCursor.getColumnIndex("accountName")));
				l1.addView(account[i]);
				tr.addView(l1);
				table.addView(tr);
				tr = new TableRow(this);				
				
				ChargeListCursor.moveToNext(); //移至資料庫下一筆
			}
		}
	}

}
