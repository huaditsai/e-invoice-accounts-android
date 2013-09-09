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
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

//發票
public class ManagerActivity extends Activity
{
	String appID, UUID;
	
	int year, month;
	String invPeriod; //對獎發票期別(yyyMM)
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	ImageButton btn_list, btn_analysis, btn_prize, btn_prizelist;
	TextView text_list, text_analysis, text_prize, text_prizelist;
	LinearLayout linear1;
	ImageButton btn_right, btn_left;
	TextView text_month, text_price1, text_price2, text_price3, text_price4;
	Button btn_close;
	
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
			btn_right = (ImageButton)findViewById(R.id.imageButton7);
			btn_left = (ImageButton)findViewById(R.id.imageButton6);
			text_month = (TextView)findViewById(R.id.textView7);
		text_price1 = (TextView)findViewById(R.id.textView11);
		text_price2 = (TextView)findViewById(R.id.textView12);
		text_price3 = (TextView)findViewById(R.id.textView16);
		text_price4 = (TextView)findViewById(R.id.textView17);
		btn_close = (Button)findViewById(R.id.button7);
		
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
	
	private void setPrizelistPop() 
	{
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 1911; //民國
		month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始...
		if(month % 2 == 1)
			month ++;
		invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
		text_month.setText(invPeriod); //月份
		
		btn_right.setOnClickListener(new OnClickListener(){
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
		
		btn_left.setOnClickListener(new OnClickListener(){
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
			}});
		
	}
	
	public void GetWinningList(String _invPeriod)
	{
		try
		{
			Map<String, List<String>> winning = new WinningList().execute(_invPeriod, UUID, appID).get();
			
			String spcPrizeNo = null, firstPrizeNo = null, sixthPrizeNo = null, superPrizeNo = null;
			
			for (String no : winning.get("spcPrizeNo")) //特獎號
				spcPrizeNo += no;
			text_price1.setText(spcPrizeNo);
			
			for (String no : winning.get("firstPrizeNo")) //頭獎號
				firstPrizeNo += no;
			text_price2.setText(firstPrizeNo);
			
			for (String no : winning.get("sixthPrizeNo")) //增開獎號
				sixthPrizeNo += no;
			text_price3.setText(sixthPrizeNo);
			
			for (String no : winning.get("superPrizeNo")) //特別獎號
				superPrizeNo += no;
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
