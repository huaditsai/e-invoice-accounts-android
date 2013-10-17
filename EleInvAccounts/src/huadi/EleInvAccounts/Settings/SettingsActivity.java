package huadi.EleInvAccounts.Settings;

import com.facebook.*;
import com.facebook.model.*;

import java.util.List;
import java.util.Map;

import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Inquiry.CarrierHead;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//設定
public class SettingsActivity extends Activity
{
	String appID, UUID;
	
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	ImageButton btn_setaccount, btn_setcategory, btn_setfb, btn_setphone;
	TextView text_setaccount, text_setcategory, text_setfb, text_setphone;
	LinearLayout setphone, setfb, fbselect;
	Button btn_phoneOK, btn_phoneCancel, btn_fbLogin, btn_fbClose, btn_bg;
	EditText edit_phone, edit_phonecode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		SharedPreferences ids = getSharedPreferences("IDs", MODE_PRIVATE ); //偏好設定
		appID = ids.getString("appID", "");
		UUID = ids.getString("UUID", "");
		
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
		btn_fbLogin = (Button)findViewById(R.id.button8);
		btn_fbClose = (Button)findViewById(R.id.button9);
		btn_bg = (Button)findViewById(R.id.button1);
		fbselect = (LinearLayout)findViewById(R.id.fbselect);
		
		text_setaccount.setText("設定錢包");
		text_setcategory.setText("設定分類");
		text_setphone.setText("綁定手機條碼");
		text_setfb.setText("設定Facebook");
		
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
					Intent intent = new Intent(SettingsActivity.this, SetAccountsActivity.class);
					startActivity(intent);
					SettingsActivity.this.finish();
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
					Intent intent = new Intent(SettingsActivity.this, SetCategoryActivity.class);
					startActivity(intent);
					SettingsActivity.this.finish();
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
				Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, AccountsActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, ManagerActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, SocialActivity.class);
				startActivity(intent);
				SettingsActivity.this.finish();
			}});
	}
	
	public void setPhone(){
		//edit_phone.setText("/XXXXXXX"); // TODO 發佈時刪除
		//edit_phonecode.setText("YOUR_VERIFICATION_CODE");
		edit_phone.setText("/");
		edit_phonecode.setText("");
		setphone.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);

		final SharedPreferences card = getSharedPreferences("CARD", MODE_PRIVATE ); //偏好設定 
		String cardNo, cardEncrypt;
		cardNo = card.getString("cardNo", "");
		cardEncrypt = card.getString("cardEncrypt", "");
		if(cardNo.length() > 0 && cardEncrypt.length() > 0)
		{
			edit_phone.setText(cardNo);;	//手機代碼
			edit_phonecode.setText(cardEncrypt);	//驗證碼
			btn_phoneOK.setText("取消綁定");
			btn_phoneOK.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View v, MotionEvent event){
					if(event.getAction() == MotionEvent.ACTION_DOWN)
					{
						btn_phoneOK.setBackgroundColor(Color.rgb(46, 147, 186));
					}
					if(event.getAction() == MotionEvent.ACTION_UP)
					{
						btn_phoneOK.setBackgroundColor(Color.rgb(50, 179, 226));
						card.edit().remove("cardNo").commit(); //卡片隱碼
						card.edit().remove("cardEncrypt").commit(); //卡片檢驗碼

						Toast.makeText(SettingsActivity.this, "已取消綁定", Toast.LENGTH_SHORT).show();
								
						setphone.setVisibility(View.GONE);
						btn_bg.setVisibility(View.GONE);
					}				
				return false;
				}});
		}
		else
		{
			btn_phoneOK.setText("確定");
			btn_phoneOK.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View v, MotionEvent event){
					if(event.getAction() == MotionEvent.ACTION_DOWN)
					{
						btn_phoneOK.setBackgroundColor(Color.rgb(46, 147, 186));
					}
					if(event.getAction() == MotionEvent.ACTION_UP)
					{
						btn_phoneOK.setBackgroundColor(Color.rgb(50, 179, 226));
						edit_phone.getText().toString();	//手機代碼
						edit_phonecode.getText().toString();	//驗證碼	
						
						try
						{
							Map<String, List<String>> CarrierHeadInfo = new CarrierHead()
								.execute("3J0002", edit_phone.getText().toString(), "Y", UUID, appID, edit_phonecode.getText().toString()).get();
							
							if(CarrierHeadInfo.get("code").get(0).equals("200"))
							{
								card.edit().putString("cardNo", edit_phone.getText().toString()).commit(); //卡片隱碼
								card.edit().putString("cardEncrypt", edit_phonecode.getText().toString()).commit(); //卡片檢驗碼
								
								Toast.makeText(SettingsActivity.this, "成功", Toast.LENGTH_SHORT).show();
								
								setphone.setVisibility(View.GONE);
								btn_bg.setVisibility(View.GONE);
							}
							else
								Toast.makeText(SettingsActivity.this, "條碼/驗證碼 錯誤", Toast.LENGTH_SHORT).show();
						}
						catch (Exception e)
						{
							setphone.setVisibility(View.GONE);
							btn_bg.setVisibility(View.GONE);
						}
					}				
				return false;
				}});
		}
		

		btn_phoneCancel.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_phoneCancel.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_phoneCancel.setBackgroundColor(Color.rgb(50, 179, 226));
					edit_phone.setText("/");
					edit_phonecode.setText("");
					setphone.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
				}				
			return false;
			}});		
	}
	
	public void setFb(){
		
		setfb.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		

		final Session session = Session.getActiveSession();
		Log.e("FBStatus", session+"");
	    if (session != null && session.isOpened()) {
			//logged
	    	btn_fbLogin.setText("登出");
	    	fbselect.setVisibility(View.VISIBLE); //參加排行與否
	    	ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebutton);
			toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			        if (isChecked) {
			            // The toggle is enabled
			        } else {
			            // The toggle is disabled
			        }
			    }
			});
	    } else {
			//not login
	    	btn_fbLogin.setText("登入");
	    	fbselect.setVisibility(View.GONE); //參加排行與否
	    }

		btn_fbLogin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
			    if (session != null && session.isOpened()) {
					//logged
				    Session.getActiveSession().closeAndClearTokenInformation();
					Session.setActiveSession(null);
			    	btn_fbLogin.setText("登入");
			    	fbselect.setVisibility(View.GONE); //參加排行與否
			    } else {
					//not login
			    	fbLogin();
			    }
			}});

		btn_fbClose.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_fbClose.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_fbClose.setBackgroundColor(Color.rgb(50, 179, 226));
					setfb.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
				}				
			return false;
			}});		
	}
	
	private void fbLogin(){
    	Log.e("fbLogin","0");
			// start Facebook Login TODO SSO Login problem
		    Session.openActiveSession(this, true, new Session.StatusCallback() {
		      // callback when session changes state
		      @SuppressWarnings("deprecation")
			@Override
		      public void call(Session session, SessionState state, Exception exception) {
		        if (session.isOpened()) {
			    	Log.e("fbLogin","1");

		          // make request to the /me API
		          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

		            // callback after Graph API response with user object
		            @Override
		            public void onCompleted(GraphUser user, Response response) {
				    	Log.e("fbLogin","2");
		              if (user != null) {
					    	Log.e("fbLogin","3");
			    	    	setFb();
					    	Log.e("fbLogin", "ID:"+user.getId()+";USER NAME:"+user.getName());
		          		}
		            }
		          });
		        }
		      }
		    });
	}
	
	
	//facebook login
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

}
