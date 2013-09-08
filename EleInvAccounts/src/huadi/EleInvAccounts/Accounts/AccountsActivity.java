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
	SQLiteDatabase db = null;
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	Button btn_addone, btn_scan, btn_import, btn_income, btn_expend, btn_save, btn_cancel;
	TextView text_total, text_income, text_expenditure, text_balance;
	TableLayout table;
	LinearLayout popview;
	EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		
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
				// TODO Auto-generated method stub
				popview.setVisibility(View.VISIBLE);
			}
		});
		btn_scan.setOnClickListener(new OnClickListener() //條碼掃描
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(AccountsActivity.this, CaptureActivity.class);
				startActivity(intent);
			}
		});

		//popview ---------------------------------
		
		editText1 = (EditText)findViewById(R.id.editText1);
		editText2 = (EditText)findViewById(R.id.editText2); //帳本
		editText3 = (EditText)findViewById(R.id.editText3); //項目
		editText4 = (EditText)findViewById(R.id.editText4); //分類
		editText5 = (EditText)findViewById(R.id.editText5); //日期
		editText6 = (EditText)findViewById(R.id.editText6); //商店
		editText7 = (EditText)findViewById(R.id.editText7); //備註
		
		btn_income.getBackground().setAlpha(60);
		btn_expend.getBackground().setAlpha(60);
		btn_income.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				btn_income.getBackground().setAlpha(255);
				btn_expend.getBackground().setAlpha(60);
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
			}
		});
		btn_save.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String date = editText5.getText().toString();
				String accountName = editText2.getText().toString();
				String cost = editText1.getText().toString();
					String mainCategory = editText4.getText().toString();
					String subCategory = editText4.getText().toString();
				String item = editText3.getText().toString();
				String store = editText6.getText().toString();
					String invNum = editText5.getText().toString();
				String remark = editText7.getText().toString();
				
				ContentValues accountCV = new ContentValues();
				accountCV.put("date", date); //日期(yyyyMMdd)
				accountCV.put("accountName", accountName); //記帳帳本
				accountCV.put("cost", cost); //項目所花的金額
				accountCV.put("mainCategory", mainCategory); //
				accountCV.put("subCategory", subCategory); //
				accountCV.put("item", item); //項目
				accountCV.put("store", store); //商店名稱
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
				popview.setVisibility(View.GONE);
				editText1.setText("");
				editText2.setText("");
				editText3.setText("");
				editText4.setText("");
				editText5.setText("");
				editText6.setText("");
				editText7.setText("");
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				popview.setVisibility(View.GONE);
				editText1.setText("");
				editText2.setText("");
				editText3.setText("");
				editText4.setText("");
				editText5.setText("");
				editText6.setText("");
				editText7.setText("");
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
				AccountsActivity.this.finish();
			}
		});
		//Side menu -----------------------------------------------
		
		Calendar calendar = Calendar.getInstance();
		int _year = calendar.get(Calendar.YEAR); //民國
		int _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始
		Cursor invListCursor = db.rawQuery("SELECT cost "
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
				int money = invListCursor.getInt(invListCursor.getColumnIndex("cost"));
				balance += money;
				
				if(money > 0)
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

		int count = 10; //資料筆數
		month = new TextView[count];
		day = new TextView[count];
		item = new TextView[count];
		main = new TextView[count];
		sub = new TextView[count];
		cost = new TextView[count];
		account = new TextView[count];

		table.removeAllViews();
		
		for (int i = 0; i < count; i++)
		{
			l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.HORIZONTAL);
			month[i] = new TextView(this);
			month[i].setText("月");
			month[i].setTextSize(20);
			TextPaint tp = month[i].getPaint();
			tp.setFakeBoldText(true);
			l1.addView(month[i]);
			day[i] = new TextView(this);
			day[i].setText("/日");
			day[i].setPadding(0, 0, 20, 0);
			l1.addView(day[i]);
			l2 = new LinearLayout(this);
			l2.setOrientation(LinearLayout.VERTICAL);
			item[i] = new TextView(this);
			item[i].setText("項目");
			item[i].setMinimumWidth(400);
			item[i].setMaxEms(5);
			item[i].setEllipsize(TruncateAt.END);
			item[i].setLines(1);
			l2.addView(item[i]);
			l3 = new LinearLayout(this);
			l3.setOrientation(LinearLayout.HORIZONTAL);
			main[i] = new TextView(this);
			main[i].setText("主-");
			l3.addView(main[i]);
			sub[i] = new TextView(this);
			sub[i].setText("次分類");
			sub[i].setPadding(0, 0, 40, 0);
			l3.addView(sub[i]);
			l2.addView(l3);
			l1.addView(l2);
			cost[i] = new TextView(this);
			cost[i].setText("金額");
			cost[i].setMinimumWidth(100);
			cost[i].setPadding(0, 0, 40, 0);
			l1.addView(cost[i]);
			account[i] = new TextView(this);
			account[i].setText("帳本");
			l1.addView(account[i]);
			tr.addView(l1);
			table.addView(tr);
			tr = new TableRow(this);
		}
	}

}
