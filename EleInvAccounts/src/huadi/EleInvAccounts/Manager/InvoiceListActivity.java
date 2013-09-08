package huadi.EleInvAccounts.Manager;

import java.util.Calendar;

import huadi.EleInvAccounts.DBHelper;
import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//發票清單
public class InvoiceListActivity extends Activity
{
	SQLiteDatabase db = null;
	int year, month;
	String[] invNum; //發票編號
	String invPeriod; //對獎發票期別(yyyMM)
	String[] sellerName; //賣方名稱
	String[] invDate; //發票開立日期(yyyyMMdd)
	int[] invTotalCost; //消費金額
	
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	ImageButton btn_left, btn_right;
	TextView text_month;
	TextView text_invoiceno, text_invoicemonth, text_store, text_date, text_cost;
	TableLayout table, table_pop;
	LinearLayout popview;
	Button popclose, popdelete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoicelist);
		
		DBHelper dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();
		
		setUI();		
	}

	private void setUI() {
		btn_backfunc = (ImageButton)findViewById(R.id.imageButton1);
		btn_account = (ImageButton)findViewById(R.id.imageButton2);
		btn_manager = (ImageButton)findViewById(R.id.imageButton3);
		btn_social = (ImageButton)findViewById(R.id.imageButton4);
		btn_setting = (ImageButton)findViewById(R.id.imageButton5);
		btn_left = (ImageButton)findViewById(R.id.imageButton6);
		btn_right = (ImageButton)findViewById(R.id.imageButton7);
		text_month = (TextView)findViewById(R.id.textView7);
		table = (TableLayout)findViewById(R.id.TableLayout);
		popview = (LinearLayout)findViewById(R.id.LinearLayout1);
		text_invoiceno = (TextView)findViewById(R.id.textView8);
		text_invoicemonth = (TextView)findViewById(R.id.textView9);
		text_store = (TextView)findViewById(R.id.textView10);
		text_date = (TextView)findViewById(R.id.textView11);
		text_cost = (TextView)findViewById(R.id.textView16);
		table_pop = (TableLayout)findViewById(R.id.TablePop);
		popclose = (Button)findViewById(R.id.button6);
		popdelete = (Button)findViewById(R.id.button7);
		
		//月份選擇----------------------------------------------------
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 1911; //民國
		month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始...
		if(month % 2 == 1)
			month ++;
		invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
		
		text_month.setText(invPeriod); //月份
		btn_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				month -= 2;
				if(month == 0 && year != 0)
				{
					year--;
					month = 12;
				}
				invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
				text_month.setText(invPeriod); //月份
				GetInvList(); //發票清單
			}});
		btn_right.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				month += 2;
				if(month == 14 && year != 0)
				{
					year++;
					month = 2;
				}
				invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
				text_month.setText(invPeriod); //月份
				GetInvList(); //發票清單
			}});
		//月份選擇----------------------------------------------------
		
		GetInvList(); //發票清單, popview			
				
		//Side menu -----------------------------------------------
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InvoiceListActivity.this, MainActivity.class);
				startActivity(intent);
				db.close();
				InvoiceListActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InvoiceListActivity.this, AccountsActivity.class);
				startActivity(intent);
				db.close();
				InvoiceListActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(InvoiceListActivity.this, ManagerActivity.class);
				startActivity(intent);
				db.close();
				InvoiceListActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InvoiceListActivity.this, SocialActivity.class);
				startActivity(intent);
				db.close();
				InvoiceListActivity.this.finish();
			}});
		btn_setting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InvoiceListActivity.this, SettingsActivity.class);
				startActivity(intent);
				db.close();
				InvoiceListActivity.this.finish();
			}});
		//Side menu -----------------------------------------------
	}
	
	public void GetInvList()
	{		
		Cursor invListCursor = db.rawQuery("SELECT invNum, invTotalCost, invDate, sellerName "
			+ "FROM Invoice "
			+ "WHERE invPeriod = " + String.format("'%d%02d'", year, month), null); //要記得''包起來
		
		int count = invListCursor.getCount(); //資料筆數
		//Log.e("count", "" + count);

		table.removeAllViews();
		
		if(count != 0)
		{
			invListCursor.moveToFirst(); //移至資料庫第一筆
			
			TableRow tr = new TableRow(this);
			RelativeLayout rl;
			LinearLayout l1;
			ImageButton[] btn;
			TextView[] date, store, cost;
			
			date = new TextView[count];
			store = new TextView[count];
			cost = new TextView[count];
			btn = new ImageButton[count];
			invNum = new String[count];
			invTotalCost = new int[count];
			invDate = new String[count];
			sellerName = new String[count];

			for (int i = 0; i < count; i++)
			{
				final int j = i;
				rl = new RelativeLayout(this);
				
				invNum[i] = invListCursor.getString(invListCursor.getColumnIndex("invNum"));
				invTotalCost[i] = invListCursor.getInt(invListCursor.getColumnIndex("invTotalCost"));
				
				invDate[i] = invListCursor.getString(invListCursor.getColumnIndex("invDate"));
				invDate[i] = invDate[i].substring(0,4) + "-" + invDate[i].substring(4,6) + "-" + invDate[i].substring(6,8);
				
				sellerName[i] = invListCursor.getString(invListCursor.getColumnIndex("sellerName"));
//				Log.e("inv", invNum[i]+":"+invPeriod+":"+sellerName[i]+":"+invDate[i]+":"+invTotalCost[i]);
				
				l1 = new LinearLayout(this);
				l1.setOrientation(LinearLayout.HORIZONTAL);
				date[i] = new TextView(this);
				date[i].setText(invDate[i]);
				date[i].setPadding(0, 0, 20, 0);
				l1.addView(date[i]);				
				
				store[i] = new TextView(this);
				store[i].setText(sellerName[i]);
				store[i].setMaxEms(7);
				store[i].setLines(1);
				store[i].setEllipsize(TruncateAt.END);
				store[i].setPadding(0, 0, 20, 0);
				l1.addView(store[i]);				
				
				cost[i] = new TextView(this);
				cost[i].setText(invTotalCost[i] + " NTD");
				l1.addView(cost[i]);
				
				btn[i] = new ImageButton(this);
				btn[i].setBackgroundColor(Color.TRANSPARENT);
				btn[i].setMinimumWidth(810);
				btn[i].setMinimumHeight(100);
				btn[i].setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View arg0) {
						popview.setVisibility(View.VISIBLE);
						PopViewInfo(invNum[j], invPeriod, sellerName[j], invDate[j], invTotalCost[j]);
					}});
	
				rl.addView(l1);
				rl.addView(btn[i]);
				tr.addView(rl);
				table.addView(tr);
				tr = new TableRow(this);

//				PopViewInfo(invNum[i], invPeriod, sellerName[i], invDate[i], invTotalCost[i]);
				
				invListCursor.moveToNext(); //移至資料庫下一筆
			}
			
		}
		
	}
	
	public void PopViewInfo(String invNum, String invPeriod, String sellerName, String invDate, int invTotalCost)
	{
//		Log.e("invNum", invNum);
//		Log.e("sellerName", sellerName);
//		Log.e("invDate", invDate);
//		Log.e("invTotalCost", "" + invTotalCost);
		
		text_invoiceno.setText(invNum);
		text_invoicemonth.setText(invPeriod);
		text_store.setText(sellerName);
		text_date.setText(invDate);
		text_cost.setText(invTotalCost + "NTD");
		
		TableRow tr2 = new TableRow(this);
		LinearLayout l2;
		TextView[] item, cost2;
		
		Cursor invDetailCursor = db.rawQuery("SELECT description, quantity, unitPrice, amount "
			+ "FROM InvDetail "
			+ "WHERE invNum = '" + invNum + "'", null);

		int count = invDetailCursor.getCount(); //資料筆數
		//Log.e("count2", "" + count);
			
		item = new TextView[count];
		cost2 = new TextView[count];
		
		table_pop.removeAllViews();

		if(count != 0)
		{	
			invDetailCursor.moveToFirst(); //移至資料庫第一筆
			for (int i = 0; i < count; i++)
			{			
				l2 = new LinearLayout(this);
				l2.setOrientation(LinearLayout.HORIZONTAL);
				item[i] = new TextView(this);
				item[i].setText(invDetailCursor.getString(invDetailCursor.getColumnIndex("description")));
				item[i].setPadding(20, 0, 20, 0);
				item[i].setMaxEms(10);
				item[i].setLines(1);
				item[i].setEllipsize(TruncateAt.END);
				item[i].setMinimumWidth(680);
				l2.addView(item[i]);
				
				cost2[i] = new TextView(this);
				cost2[i].setText(invDetailCursor.getInt(invDetailCursor.getColumnIndex("amount")) + "NTD");
				cost2[i].setMinimumWidth(100);
				cost2[i].setGravity(Gravity.RIGHT);
				l2.addView(cost2[i]);
				
				tr2.addView(l2);
				table_pop.addView(tr2);
				tr2 = new TableRow(this);
				
				invDetailCursor.moveToNext(); //移至資料庫下一筆
			}
			
			popclose.setOnClickListener(new OnClickListener(){	
				@Override
				public void onClick(View v) {
					popview.setVisibility(View.GONE);
				}});
			
			popdelete.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					//刪除
					popview.setVisibility(View.GONE);
				}});
		}
	}
	
	
}
