package huadi.EleInvAccounts.Settings;

import com.facebook.*;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Inquiry.CarrierHead;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//設定
public class SettingsActivity extends Activity
{
	String appID, UUID;
	String fbappID = "1421440268083722";
	String fbappSecret = "YOUR_FACEBOOK_APP_SECRET";
	private Facebook facebook = new Facebook(fbappID);
	private SharedPreferences fb;
	
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	ImageButton btn_setaccount, btn_setcategory, btn_setfb, btn_setphone;
	TextView text_setaccount, text_setcategory, text_setfb, text_setphone, username;
	ImageView userpic;
	LinearLayout setphone, setfb, fbselect;
	Button btn_phoneOK, btn_phoneCancel, btn_fbLogin, btn_fbClose, btn_bg;
	EditText edit_phone, edit_phonecode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	        
	    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
	    		.detectDiskWrites().detectNetwork().penaltyLog().build());
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
	    		.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		
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
		userpic = (ImageView)findViewById(R.id.userpic);
		username = (TextView)findViewById(R.id.username);
		
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
	
	@SuppressWarnings("deprecation")
	public void setFb(){
		setfb.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		username.setVisibility(View.GONE);
		userpic.setVisibility(View.GONE);
		
    	fb = getSharedPreferences("FaceBook", MODE_PRIVATE ); //偏好設定 
    	final String access_token = fb.getString("access_token", null); 
    	final Long expires = fb.getLong("access_expires", -1);
		
    	if (access_token != null && expires != -1){
    		facebook.setAccessToken(access_token);
    	    facebook.setAccessExpires(expires);
    		username.setVisibility(View.VISIBLE);
    		userpic.setVisibility(View.VISIBLE);
			String name = fb.getString("fbname", "");
			String id = fb.getString("fbid", "");
			String profile_picture = "https://graph.facebook.com/"+id+"/picture?type=square";
    		username.setText(name+"你好!");
    		
    		String filePath = Environment.getExternalStorageDirectory() + "/EleInvAccounts/" + id + ".png";
    		File file = new File(filePath);
    		if (!file.exists())
    			userpic.setImageBitmap(getFBpic(profile_picture, id));
    		
    		else{
        		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        		userpic.setImageBitmap(bitmap);}
    		
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
				if (access_token != null && expires != -1){
					//logged
					fb.edit().remove("access_expires").commit();
					fb.edit().remove("access_token").commit();
					fb.edit().remove("fbid").commit();
					fb.edit().remove("fbname").commit();
					Toast.makeText(SettingsActivity.this, "已登出Facebook", Toast.LENGTH_SHORT).show();
			    	btn_fbLogin.setText("登入");
			    	fbselect.setVisibility(View.GONE); //參加排行與否
			    	setFb();
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
	
	@SuppressWarnings("deprecation")
	private void fbLogin(){
    	Log.e("fbLogin","0");
    	facebook.authorize(this, new String[]{"user_about_me"}, Facebook.FORCE_DIALOG_AUTH, new DialogListener(){

			@Override
			public void onComplete(Bundle values) {
			  	try {
				  	String token = facebook.getAccessToken();
				  	long token_expires = facebook.getAccessExpires();
					String about_me = facebook.request("me"); //json string
					
					JSONObject jb1 = new JSONObject(about_me);
					String id = jb1.getString("id");
					String name = jb1.getString("name");
					String profile_picture = "https://graph.facebook.com/"+id+"/picture?type=square";
					username.setText(name+"你好!");
					userpic.setImageBitmap(getFBpic(profile_picture, id));
					
			        SharedPreferences.Editor editor = fb.edit();
			        editor.putLong("access_expires", token_expires);
			        editor.putString("access_token", token);
			        editor.putString("fbid", id);
			        editor.putString("fbname", name);
			        editor.commit();
			        setFb();
					Toast.makeText(SettingsActivity.this, name+"已登入Facebook", Toast.LENGTH_SHORT).show();
				} catch (MalformedURLException e) {e.printStackTrace();} 
			  	catch (IOException e) {e.printStackTrace();} 
			  	catch (JSONException e) {e.printStackTrace();}
			}

			@Override
			public void onFacebookError(FacebookError e) {Log.e("fbLogin","FacebookError:"+e);}
			@Override
			public void onError(DialogError e) {Log.e("fbLogin","Error:"+e);}
			@Override
			public void onCancel() {}});
	}
	
	private Bitmap getFBpic(String profile_picture, String id){ //儲存FB User大頭照
		Bitmap bitmap = null;
		try {
			URL url = new URL(profile_picture);URLConnection conn = url.openConnection();
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        BitmapFactory.Options options=new BitmapFactory.Options();
	        bitmap = BitmapFactory.decodeStream(is,null,options);
	
			File folder = new File(Environment.getExternalStorageDirectory(), "EleInvAccounts");
			if(!folder.exists())
				folder.mkdir();
			File file = new File(Environment.getExternalStorageDirectory() + "/EleInvAccounts/", id + ".png");
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
		}
		catch (MalformedURLException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		
		return bitmap;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume(){
		super.onResume();
		facebook.extendAccessTokenIfNeeded(this, null);
	}
	
	//facebook login
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

}
