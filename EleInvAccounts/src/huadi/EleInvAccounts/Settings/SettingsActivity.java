package huadi.EleInvAccounts.Settings;

import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

//設定
public class SettingsActivity extends Activity
{
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	ImageButton btn_setaccount, btn_setcategory, btn_setfb, btn_setphone;
	TextView text_setaccount, text_setcategory, text_setfb, text_setphone;
	LinearLayout setphone, setfb;
	Button btn_phoneOK, btn_phoneCancel, btn_fbOK, btn_fbCancel;
	EditText edit_phone, edit_phonecode, edit_fbuser, edit_fbpw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		setUI();
	}

	private void setUI() {
		
		final int btnMovePosi = 5; //按鈕位移量
		final int btnMoveNega = -5; //按鈕位移量
		
		btn_backfunc = (ImageButton)findViewById(R.id.imageButton1);
		btn_account = (ImageButton)findViewById(R.id.imageButton2);
		btn_manager = (ImageButton)findViewById(R.id.imageButton3);
		btn_social = (ImageButton)findViewById(R.id.imageButton4);
		btn_setting = (ImageButton)findViewById(R.id.imageButton5);
		btn_setaccount = (ImageButton)findViewById(R.id.imageButton6);
		btn_setcategory = (ImageButton)findViewById(R.id.imageButton7);
		btn_setfb = (ImageButton)findViewById(R.id.imageButton9);
		btn_setphone = (ImageButton)findViewById(R.id.imageButton8);
		text_setaccount = (TextView)findViewById(R.id.textView6);
		text_setcategory = (TextView)findViewById(R.id.textView7);
		text_setfb = (TextView)findViewById(R.id.textView9);
		text_setphone = (TextView)findViewById(R.id.textView8);
		setphone = (LinearLayout)findViewById(R.id.setphone);
		btn_phoneOK = (Button)findViewById(R.id.button6);
		btn_phoneCancel = (Button)findViewById(R.id.button7);
		edit_phone = (EditText)findViewById(R.id.editText1);
		edit_phonecode = (EditText)findViewById(R.id.editText2);
		setfb = (LinearLayout)findViewById(R.id.setfb);
		btn_fbOK = (Button)findViewById(R.id.button8);
		btn_fbCancel = (Button)findViewById(R.id.button9);
		edit_fbuser = (EditText)findViewById(R.id.editText3);
		edit_fbpw = (EditText)findViewById(R.id.editText4);
		
		text_setaccount.setText("設定帳戶");
		text_setcategory.setText("設定分類");
		text_setphone.setText("綁定手機條碼");
		text_setfb.setText("綁定Facebook");
		
		btn_setaccount.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_setaccount.setX(btn_setaccount.getX() + btnMovePosi);
					btn_setaccount.setY(btn_setaccount.getY() + btnMovePosi);
					text_setaccount.setX(text_setaccount.getX() + btnMovePosi);
					text_setaccount.setY(text_setaccount.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_setaccount.setX(btn_setaccount.getX() + btnMoveNega);
					btn_setaccount.setY(btn_setaccount.getY() + btnMoveNega);
					text_setaccount.setX(text_setaccount.getX() + btnMoveNega);
					text_setaccount.setY(text_setaccount.getY() + btnMoveNega);
				}				
				return false;
				}});
		
		btn_setcategory.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_setcategory.setX(btn_setcategory.getX() + btnMovePosi);
					btn_setcategory.setY(btn_setcategory.getY() + btnMovePosi);
					text_setcategory.setX(text_setcategory.getX() + btnMovePosi);
					text_setcategory.setY(text_setcategory.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_setcategory.setX(btn_setcategory.getX() + btnMoveNega);
					btn_setcategory.setY(btn_setcategory.getY() + btnMoveNega);
					text_setcategory.setX(text_setcategory.getX() + btnMoveNega);
					text_setcategory.setY(text_setcategory.getY() + btnMoveNega);
				}				
				return false;
				}});
		
		btn_setfb.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_setfb.setX(btn_setfb.getX() + btnMovePosi);
					btn_setfb.setY(btn_setfb.getY() + btnMovePosi);
					text_setfb.setX(text_setfb.getX() + btnMovePosi);
					text_setfb.setY(text_setfb.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_setfb.setX(btn_setfb.getX() + btnMoveNega);
					btn_setfb.setY(btn_setfb.getY() + btnMoveNega);
					text_setfb.setX(text_setfb.getX() + btnMoveNega);
					text_setfb.setY(text_setfb.getY() + btnMoveNega);
					setFb();
				}				
				return false;
				}});
		
		btn_setphone.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_setphone.setX(btn_setphone.getX() + btnMovePosi);
					btn_setphone.setY(btn_setphone.getY() + btnMovePosi);
					text_setphone.setX(text_setphone.getX() + btnMovePosi);
					text_setphone.setY(text_setphone.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_setphone.setX(btn_setphone.getX() + btnMoveNega);
					btn_setphone.setY(btn_setphone.getY() + btnMoveNega);
					text_setphone.setX(text_setphone.getX() + btnMoveNega);
					text_setphone.setY(text_setphone.getY() + btnMoveNega);
					setPhone();
				}				
				return false;
				}});
				
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingsActivity.this, AccountsActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingsActivity.this, ManagerActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingsActivity.this, SocialActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
			}});
	}
	
	public void setPhone(){
		setphone.setVisibility(View.VISIBLE);
		
		btn_phoneOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				edit_phone.getText().toString();	//手機代碼
				edit_phonecode.getText().toString();	//驗證碼
				setphone.setVisibility(View.GONE);
			}});
		btn_phoneCancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setphone.setVisibility(View.GONE);
			}});
		
	}
	
	public void setFb(){
		setfb.setVisibility(View.VISIBLE);
		
		btn_fbOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				edit_fbuser.getText().toString();	//FB帳號
				edit_fbpw.getText().toString();	//FB密碼
				setfb.setVisibility(View.GONE);
			}});
		btn_fbCancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setfb.setVisibility(View.GONE);
			}});
		
	}
}
