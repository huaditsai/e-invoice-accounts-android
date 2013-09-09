package huadi.EleInvAccounts.Manager;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Inquiry.WinningList;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//發票
public class ManagerActivity extends Activity
{
	String appID, UUID;
	
	int year, month, day;
	String invPeriod; //對獎發票期別(yyyMM)
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	ImageButton btn_list, btn_analysis, btn_prize, btn_prizelist;
	TextView text_list, text_analysis, text_prize, text_prizelist;
	LinearLayout linear1, linear2, invoice, number;
	ImageButton btn_right, btn_left, btn_right2, btn_left2;
	TextView text_month, text_price1, text_price2, text_price3, text_price4;
	TextView text_month2, text_input, text_prizeornot;
	Button btn_close, btn_close2, btn_invoice, btn_number;
	TableLayout invoicetable;
	Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_clear, btn_backspace, btn_bg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
		SharedPreferences ids = getSharedPreferences("IDs", MODE_PRIVATE ); //偏好設定
		appID = ids.getString("appID", "");
		UUID = ids.getString("UUID", "");
		
		setUI();
	}

	private void setUI() {
		// TODO Auto-generated method stub
		final int btnMovePosi = 5; //按鈕位移量
		final int btnMoveNega = -5; //按鈕位移量
		
		btn_backfunc = (ImageButton)findViewById(R.id.imageButton1);
		btn_account = (ImageButton)findViewById(R.id.imageButton2);
		btn_manager = (ImageButton)findViewById(R.id.imageButton3);
		btn_social = (ImageButton)findViewById(R.id.imageButton4);
		btn_setting = (ImageButton)findViewById(R.id.imageButton5);
		text_list = (TextView)findViewById(R.id.textView6);
		text_analysis = (TextView)findViewById(R.id.textView7);
		text_prize = (TextView)findViewById(R.id.textView8);
		text_prizelist = (TextView)findViewById(R.id.textView9);
		btn_list = (ImageButton)findViewById(R.id.imageButton6);
		btn_analysis = (ImageButton)findViewById(R.id.imageButton7);
		btn_prize = (ImageButton)findViewById(R.id.imageButton8);
		btn_prizelist = (ImageButton)findViewById(R.id.imageButton9);
		linear1 = (LinearLayout)findViewById(R.id.LinearLayout1);
		btn_right = (ImageButton)findViewById(R.id.imageButton97);
		btn_left = (ImageButton)findViewById(R.id.imageButton96);
		text_month = (TextView)findViewById(R.id.textView27);
		text_price1 = (TextView)findViewById(R.id.textView11);
		text_price2 = (TextView)findViewById(R.id.textView12);
		text_price3 = (TextView)findViewById(R.id.textView16);
		text_price4 = (TextView)findViewById(R.id.textView17);
		btn_close = (Button)findViewById(R.id.button7);
		linear2 = (LinearLayout)findViewById(R.id.LinearLayout2);
		btn_right2 = (ImageButton)findViewById(R.id.imageButton77);
		btn_left2 = (ImageButton)findViewById(R.id.imageButton76);
		text_month2 = (TextView)findViewById(R.id.textView37);
		btn_invoice = (Button)findViewById(R.id.button1);
		btn_number = (Button)findViewById(R.id.button2);
		invoice = (LinearLayout)findViewById(R.id.invoice);
		number = (LinearLayout)findViewById(R.id.number);
		invoicetable = (TableLayout)findViewById(R.id.invoicetable);
		text_input = (TextView)findViewById(R.id.textView97);
		text_prizeornot = (TextView)findViewById(R.id.textView22);
		btn_0 = (Button)findViewById(R.id.button14);
		btn_1 = (Button)findViewById(R.id.button10);
		btn_2 = (Button)findViewById(R.id.button11);
		btn_3 = (Button)findViewById(R.id.button12);
		btn_4 = (Button)findViewById(R.id.button6);
		btn_5 = (Button)findViewById(R.id.button8);
		btn_6 = (Button)findViewById(R.id.button9);
		btn_7 = (Button)findViewById(R.id.button3);
		btn_8 = (Button)findViewById(R.id.button4);
		btn_9 = (Button)findViewById(R.id.button5);
		btn_clear = (Button)findViewById(R.id.button13);
		btn_backspace = (Button)findViewById(R.id.button15);
		btn_close2 = (Button)findViewById(R.id.button77);
		btn_bg = (Button)findViewById(R.id.button16);
		
		text_list.setText("發票清單");
		text_analysis.setText("消費分析");
		text_prize.setText("發票對獎");
		text_prizelist.setText("各期獎號");

		btn_list.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_list.setX(btn_list.getX() + btnMovePosi);
					btn_list.setY(btn_list.getY() + btnMovePosi);
					text_list.setX(text_list.getX() + btnMovePosi);
					text_list.setY(text_list.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_list.setX(btn_list.getX() + btnMoveNega);
					btn_list.setY(btn_list.getY() + btnMoveNega);
					text_list.setX(text_list.getX() + btnMoveNega);
					text_list.setY(text_list.getY() + btnMoveNega);
					Intent intent = new Intent(ManagerActivity.this, InvoiceListActivity.class);
					startActivity(intent);
					ManagerActivity.this.finish();
				}				
				return false;
				}});
		
		btn_analysis.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_analysis.setX(btn_analysis.getX() + btnMovePosi);
					btn_analysis.setY(btn_analysis.getY() + btnMovePosi);
					text_analysis.setX(text_analysis.getX() + btnMovePosi);
					text_analysis.setY(text_analysis.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_analysis.setX(btn_analysis.getX() + btnMoveNega);
					btn_analysis.setY(btn_analysis.getY() + btnMoveNega);
					text_analysis.setX(text_analysis.getX() + btnMoveNega);
					text_analysis.setY(text_analysis.getY() + btnMoveNega);
				}
				return false;
			}});
		
		btn_prize.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_prize.setX(btn_prize.getX() + btnMovePosi);
					btn_prize.setY(btn_prize.getY() + btnMovePosi);
					text_prize.setX(text_prize.getX() + btnMovePosi);
					text_prize.setY(text_prize.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_prize.setX(btn_prize.getX() + btnMoveNega);
					btn_prize.setY(btn_prize.getY() + btnMoveNega);
					text_prize.setX(text_prize.getX() + btnMoveNega);
					text_prize.setY(text_prize.getY() + btnMoveNega);
					
					linear2.setVisibility(View.VISIBLE);
					setPrizePop();
				}				
				return false;				
			}});
		
		btn_prizelist.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_prizelist.setX(btn_prizelist.getX() + btnMovePosi);
					btn_prizelist.setY(btn_prizelist.getY() + btnMovePosi);
					text_prizelist.setX(text_prizelist.getX() + btnMovePosi);
					text_prizelist.setY(text_prizelist.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_prizelist.setX(btn_prizelist.getX() + btnMoveNega);
					btn_prizelist.setY(btn_prizelist.getY() + btnMoveNega);
					text_prizelist.setX(text_prizelist.getX() + btnMoveNega);
					text_prizelist.setY(text_prizelist.getY() + btnMoveNega);
					
					linear1.setVisibility(View.VISIBLE);
					setPrizelistPop();
				}
				return false;								
			}});
				
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ManagerActivity.this, MainActivity.class);
				startActivity(intent);
				ManagerActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ManagerActivity.this, AccountsActivity.class);
				startActivity(intent);
				ManagerActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ManagerActivity.this, SocialActivity.class);
				startActivity(intent);
				ManagerActivity.this.finish();
			}});
		btn_setting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ManagerActivity.this, SettingsActivity.class);
				startActivity(intent);
				ManagerActivity.this.finish();
			}});
	}
	
	private void setPrizePop(){
		btn_bg.setVisibility(View.VISIBLE);
		btn_right2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				text_month2.setText("月份");
			}});
		
		btn_left2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				text_month2.setText("月份");
			}});
		
		btn_invoice.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				invoice.setVisibility(View.VISIBLE);
				number.setVisibility(View.GONE);
			}});
		
		btn_number.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				invoice.setVisibility(View.GONE);
				number.setVisibility(View.VISIBLE);
			}});
		
		btn_close2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				linear2.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		//invoicetable
		int count=5; //中獎筆數
		
		TableRow tr = new TableRow(this);
		LinearLayout l1;
		TextView[] prize;
		final TextView[] number;
		TextView[] date, store, cost;
		
		prize = new TextView[count];
		number = new TextView[count];
		date = new TextView[count];
		store = new TextView[count];
		cost = new TextView[count];
		
		invoicetable.removeAllViews();
		
		for (int i = 0; i < count; i++)
		{
			l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.HORIZONTAL);

			prize[i] = new TextView(this);
			prize[i].setText("獎項");
			prize[i].setPadding(0, 0, 20, 0);
			prize[i].setTextColor(Color.RED);
			prize[i].setMinimumWidth(150);
			prize[i].setGravity(Gravity.CENTER);
			date[i] = new TextView(this);
			date[i].setText("日期");
			date[i].setPadding(0, 0, 20, 0);
			date[i].setMinimumWidth(100);
			date[i].setGravity(Gravity.CENTER);
			number[i] = new TextView(this);
			number[i].setText("發票號碼");
			number[i].setPadding(0, 0, 20, 0);
			number[i].setMinimumWidth(150);
			number[i].setGravity(Gravity.CENTER);
			store[i] = new TextView(this);
			store[i].setText("消費商店");
			store[i].setMaxEms(6);
			store[i].setLines(1);
			store[i].setPadding(0, 0, 20, 0);
			store[i].setMinimumWidth(100);
			store[i].setGravity(Gravity.CENTER);
			cost[i] = new TextView(this);
			cost[i].setText("金額");
			cost[i].setMinimumWidth(100);
			cost[i].setGravity(Gravity.CENTER);
			
			l1.addView(prize[i]);
			l1.addView(date[i]);
			l1.addView(number[i]);
			l1.addView(store[i]);
			l1.addView(cost[i]);
			
			tr.addView(l1);
			invoicetable.addView(tr);
			tr = new TableRow(this);
		}
		
		text_input.setText("輸入的號碼");
		text_prizeornot.setText("未中獎");
		
		btn_0.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_6.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_7.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_8.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_9.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
		
		btn_backspace.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}});
	}
	
	private void setPrizelistPop() 
	{
		btn_bg.setVisibility(View.VISIBLE);
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 1911; //民國
		month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始...
		day = calendar.get(Calendar.DAY_OF_MONTH);
		if(month % 2 == 1)
		{
			if(day < 25)
				month -= 3; //若現在為9月, 還沒到25號, 只能看56月
			else 
				month--; //若現在為9月, 減減來看78月
		}
		else
			month -= 2; //若現在為10月, 減2來看78月
		invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
		text_month.setText(invPeriod); //月份
		
		btn_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				month -= 2;
				if(month == 0 && year != 0)
				{
					year--;
					month = 12;
				}
				invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
				text_month.setText(invPeriod); //月份
				GetWinningList(String.format("%d%02d", year, month));
			}});
		
		btn_right.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				month += 2;
				if(month == 14 && year != 0)
				{
					year++;
					month = 2;
				}
				invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
				text_month.setText(invPeriod); //月份
				GetWinningList(String.format("%d%02d", year, month));
			}});
		
		GetWinningList(String.format("%d%02d", year, month));
		
		btn_close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				linear1.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
	}
	
	public void GetWinningList(String _invPeriod)
	{
		try
		{
			Map<String, List<String>> winning = new WinningList().execute(_invPeriod, UUID, appID).get();
			
			String spcPrizeNo = "", firstPrizeNo = "", sixthPrizeNo = "", superPrizeNo = "";
			
			for (String no : winning.get("spcPrizeNo")) //特獎號
				spcPrizeNo += no + "\n";
			text_price1.setText(spcPrizeNo);
			
			for (String no : winning.get("firstPrizeNo")) //頭獎號
				firstPrizeNo += no + "\n";
			text_price2.setText(firstPrizeNo);
			
			for (String no : winning.get("sixthPrizeNo")) //增開獎號
				sixthPrizeNo += no + "\n";
			text_price3.setText(sixthPrizeNo);
			
			for (String no : winning.get("superPrizeNo")) //特別獎號
				superPrizeNo += no + "\n";
			text_price4.setText(superPrizeNo);
		}
		catch (Exception e)
		{
			text_price1.setText("無此期別資料");
			text_price2.setText("無此期別資料");
			text_price3.setText("無此期別資料");
			text_price4.setText("無此期別資料");
			e.printStackTrace();
		}
		
	}
}
