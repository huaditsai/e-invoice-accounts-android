package huadi.EleInvAccounts.Manager;

import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

//發票
public class ManagerActivity extends Activity
{
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	ImageButton btn_list, btn_analysis, btn_prize, btn_prizelist;
	TextView text_list, text_analysis, text_prize, text_prizelist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
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
}
