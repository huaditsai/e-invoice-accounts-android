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
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//發票清單
public class InvoiceListActivity extends Activity
{
	SQLiteDatabase db = null;
	int year, month;
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	ImageButton btn_left, btn_right;
	TextView text_month;
	TextView text_invoiceno, text_invoicemonth, text_store, text_date, text_cost;
	TableLayout table, table_pop;
	LinearLayout popview;
	Button popclose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoicelist);
		
		DBHelper dbHelper = new DBHelper(this);
		
		db = dbHelper.getWritableDatabase();
		setUI();
		db.close();
	}

	private void setUI() {
		// TODO Auto-generated method stub
		
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
		
		//月份選擇----------------------------------------------------
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 1911; //民國
		month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始...
		if(month % 2 == 1)
			month ++;
		
		text_month.setText(String.format("%d 年 %02d - %02d 月", year, month-1, month)); //月份
		btn_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				month -= 2;
				if(month == 0 && year != 0)
				{
					year--;
					month = 12;
				}
				text_month.setText(String.format("%d 年 %02d - %02d 月", year, month-1, month)); //月份
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
				text_month.setText(String.format("%d 年 %02d - %02d 月", year, month-1, month)); //月份
			}});
		//月份選擇----------------------------------------------------
		
		//發票清單----------------------------------------------------
		TableRow tr = new TableRow(this);
		RelativeLayout rl;
		LinearLayout l1;
		ImageButton[] btn;
		TextView[] date, store, cost;

		int count = 20; //資料筆數
		date = new TextView[count];
		store = new TextView[count];
		cost = new TextView[count];
		btn = new ImageButton[count];
		
//		Cursor invListCursor = db.rawQuery("SELECT invNum, invTotalCost, invDate "
//			+ "FROM Invoice "
//			+ "WHERE invPeriod = " + String.format("%d%02d", year, month), null);

		for (int i = 0; i < count; i++)
		{
			rl = new RelativeLayout(this);
			
			l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.HORIZONTAL);
			date[i] = new TextView(this);
			date[i].setText("2013-XX-XX");
			date[i].setPadding(0, 0, 20, 0);
			l1.addView(date[i]);
			
			store[i] = new TextView(this);
			store[i].setText("誠品生活股份有限公司");
			store[i].setMaxEms(7);
			store[i].setLines(1);
			store[i].setEllipsize(TruncateAt.END);
			store[i].setPadding(0, 0, 20, 0);
			l1.addView(store[i]);
			
			cost[i] = new TextView(this);
			cost[i].setText("300NTD");
			l1.addView(cost[i]);
			
			btn[i] = new ImageButton(this);
			btn[i].setBackgroundColor(Color.TRANSPARENT);
			btn[i].setMinimumWidth(810);
			btn[i].setMinimumHeight(100);
			btn[i].setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 自動產生的方法 Stub
					popview.setVisibility(View.VISIBLE);
				}});

			rl.addView(l1);
			rl.addView(btn[i]);
			tr.addView(rl);
			table.addView(tr);
			tr = new TableRow(this);
		}
		//發票清單----------------------------------------------------

		//popview--------------------------------------------------
		text_invoiceno.setText("VP56080808");
		text_invoicemonth.setText("2013年7-8月");
		text_store.setText("統一超商股份有限公司");
		text_date.setText("2013-07-29");
		text_cost.setText("39NTD");
		
		TableRow tr2 = new TableRow(this);
		LinearLayout l2;
		TextView[] item, cost2;

		int count2 = 5; //資料筆數
		item = new TextView[count2];
		cost2 = new TextView[count2];

		for (int i = 0; i < count2; i++)
		{			
			l2 = new LinearLayout(this);
			l2.setOrientation(LinearLayout.HORIZONTAL);
			item[i] = new TextView(this);
			item[i].setText("純喫茶綠茶650ml(盒)");
			item[i].setPadding(20, 0, 20, 0);
			item[i].setMaxEms(20);
			item[i].setLines(1);
			item[i].setEllipsize(TruncateAt.END);
			item[i].setMinimumWidth(700);
			l2.addView(item[i]);
			
			cost2[i] = new TextView(this);
			cost2[i].setText("25");
			cost2[i].setMinimumWidth(100);
			cost2[i].setGravity(Gravity.RIGHT);
			l2.addView(cost2[i]);
			
			tr2.addView(l2);
			table_pop.addView(tr2);
			tr2 = new TableRow(this);
		}
		
		popclose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				popview.setVisibility(View.GONE);
			}});
		
		//popview--------------------------------------------------
				
		//Side menu -----------------------------------------------
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InvoiceListActivity.this, MainActivity.class);
				startActivity(intent);
				InvoiceListActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InvoiceListActivity.this, AccountsActivity.class);
				startActivity(intent);
				InvoiceListActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Intent intent = new Intent(InvoiceListActivity.this, ManagerActivity.class);
				startActivity(intent);
				InvoiceListActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InvoiceListActivity.this, SocialActivity.class);
				startActivity(intent);
				InvoiceListActivity.this.finish();
			}});
		btn_setting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InvoiceListActivity.this, SettingsActivity.class);
				startActivity(intent);
				InvoiceListActivity.this.finish();
			}});
		//Side menu -----------------------------------------------
	}
}
