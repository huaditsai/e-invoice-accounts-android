package huadi.EleInvAccounts;

import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;

import java.io.File;
import java.util.UUID;

import zxing.encoding.CodeGenerator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	//UI宣告
	public ImageButton btn_account, btn_invoice, btn_social, btn_setting;
	public TextView text_account, text_invoice, text_social, text_setting;
	
	final static String appID = "YOUR_EINVOICE_APP_ID";
//	String[] method = new String[]
//		{
//			"/PB2CAPIVAN/invapp/InvApp", //查詢中獎發票號碼清單, 查詢發票表頭, 明細
//			"/PB2CAPIVAN/loveCodeapp/qryLoveCode", //愛心碼查詢
//			"/PB2CAPIVAN/invServ/InvServ", //載具發票表頭查詢, 載具發票號碼明細查詢
//			"/PB2CAPIVAN/CarInv/Donate", //載具發票捐贈
//			"/PB2CAPIVAN/APIService/generalCarrierRegBlank", //手機條碼載具註冊
//			"/PB2CAPIVAN/APIService/carrierLinkBlank", //載具歸戶(手機條碼)
//			"/PB2CAPIVAN/APIService/carrierBankAccBlank", //手機條碼綁定金融帳戶
//			"/PB2CAPIVAN/APIService/carrierInvDntBlank" //載具發票捐贈
//		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences ids = getSharedPreferences("IDs", MODE_PRIVATE ); //偏好設定 
		ids.edit().putString("appID", appID).commit(); //寫入appID
		ids.edit().putString("UUID", GetUUID()).commit(); //寫入 uuid
		
		SharedPreferences card = getSharedPreferences("CARD", MODE_PRIVATE ); //偏好設定 
//		card.edit().putString("cardNo", "").commit(); //卡片隱碼
//		card.edit().putString("cardEncrypt", "").commit(); //卡片檢驗碼
		//Log.e("id", GetUUID());
		
//		new InvDetails(this).execute("QRCode","VX20070106","","2013/07/13","g7Y1WPAG8PE1PbEIebrTQg==","01802112",GetUUID(),"9035",appID, "40");
		
		setUI(); //設定UI		
		
		if(card.getString("cardNo", "").length() > 0 )
			GetBarCode("EleInvAccounts", card.getString("cardNo", ""), 540, 200); //360.85
		
//		try
//		{
//			Map<String, List<String>> winning = new WinningList().execute("10206", GetUUID(), appID).get();
//			ManualAward anualAward = new ManualAward();
//			Log.e("winning", "" + manualAward.Award("516", winning));
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
		
		
		//Log.e("now", "" + System.currentTimeMillis()/1000);
	}
		
	private void setUI() 
	{
		final int btnMovePosi = 5; //按鈕位移量
		final int btnMoveNega = -5; //按鈕位移量
		
		text_account = (TextView)findViewById(R.id.text_1);
		text_invoice = (TextView)findViewById(R.id.text_2);
		text_social = (TextView)findViewById(R.id.text_3);
		text_setting = (TextView)findViewById(R.id.text_4);
		btn_account = (ImageButton)findViewById(R.id.btn_1);
		btn_invoice = (ImageButton)findViewById(R.id.btn_2);
		btn_social = (ImageButton)findViewById(R.id.btn_3);
		btn_setting = (ImageButton)findViewById(R.id.btn_4);
		
		text_account.setText("記帳");
		text_invoice.setText("發票管理");
		text_social.setText("成就");
		text_setting.setText("設定");
		
		btn_account.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_account.setX(btn_account.getX() + btnMovePosi);
					btn_account.setY(btn_account.getY() + btnMovePosi);
					text_account.setX(text_account.getX() + btnMovePosi);
					text_account.setY(text_account.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_account.setX(btn_account.getX() + btnMoveNega);
					btn_account.setY(btn_account.getY() + btnMoveNega);
					text_account.setX(text_account.getX() + btnMoveNega);
					text_account.setY(text_account.getY() + btnMoveNega);
					Intent intent = new Intent(MainActivity.this, AccountsActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
				}				
				return false;
				}});
		
		btn_invoice.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_invoice.setX(btn_invoice.getX() + btnMovePosi);
					btn_invoice.setY(btn_invoice.getY() + btnMovePosi);
					text_invoice.setX(text_invoice.getX() + btnMovePosi);
					text_invoice.setY(text_invoice.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_invoice.setX(btn_invoice.getX() + btnMoveNega);
					btn_invoice.setY(btn_invoice.getY() + btnMoveNega);
					text_invoice.setX(text_invoice.getX() + btnMoveNega);
					text_invoice.setY(text_invoice.getY() + btnMoveNega);
					Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
				}
				return false;
			}});
		
		btn_social.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_social.setX(btn_social.getX() + btnMovePosi);
					btn_social.setY(btn_social.getY() + btnMovePosi);
					text_social.setX(text_social.getX() + btnMovePosi);
					text_social.setY(text_social.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_social.setX(btn_social.getX() + btnMoveNega);
					btn_social.setY(btn_social.getY() + btnMoveNega);
					text_social.setX(text_social.getX() + btnMoveNega);
					text_social.setY(text_social.getY() + btnMoveNega);				
					Intent intent = new Intent(MainActivity.this, SocialActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
				}				
				return false;				
			}});
		
		btn_setting.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_setting.setX(btn_setting.getX() + btnMovePosi);
					btn_setting.setY(btn_setting.getY() + btnMovePosi);
					text_setting.setX(text_setting.getX() + btnMovePosi);
					text_setting.setY(text_setting.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_setting.setX(btn_setting.getX() + btnMoveNega);
					btn_setting.setY(btn_setting.getY() + btnMoveNega);
					text_setting.setX(text_setting.getX() + btnMoveNega);
					text_setting.setY(text_setting.getY() + btnMoveNega);			
					Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
				}
				return false;								
			}});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.action_settings:				
				//new AlertDialog.Builder(this).setTitle("關於").setMessage("huadi73@gmail.com").show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void GetBarCode(String folderName, String content, int desiredWidth, int desiredHeight)
	{		
		String filePath = Environment.getExternalStorageDirectory() + "/" + folderName + "/" + content + ".png";		
		File file = new File(filePath);
		//file.delete(); //TODO 發佈時要記得註解掉
        if( !file.exists() ) //沒有檔案就產生吧
        	new CodeGenerator(folderName, content, desiredWidth, desiredHeight);
        
		try //顯示
		{
			Bitmap bitmap = BitmapFactory.decodeFile(filePath);
			ImageView imageView = (ImageView)findViewById(R.id.imageView2);
			imageView.setImageBitmap(bitmap);
			
			TextView textView = (TextView)findViewById(R.id.textView1);
			textView.setText(content);
		}  
		catch (Exception e)  
		{  
			e.printStackTrace();   
		}  
	}
	
	public String GetUUID()
	{
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		
		String android_id = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID); 

		String tmDevice = "" + tm.getDeviceId();
		String tmSerial = "" + tm.getSimSerialNumber();

		UUID deviceUuid = new UUID(android_id.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode()); 

		return deviceUuid.toString();
	}

}
