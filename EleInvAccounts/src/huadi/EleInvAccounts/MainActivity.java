package huadi.EleInvAccounts;

import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Inquiry.InvDetails;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;

import java.util.UUID;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity
{
	//UI
	public ImageButton btn_account, btn_invoice, btn_social, btn_setting;
	public TextView text_account, text_invoice, text_social, text_setting;
	
	final static String appID = "YOUR_EINVOICE_APP_ID";
//	String[] method = new String[]
//		{
//			"/PB2CAPIVAN/invapp/InvApp", //琩高い贱祇布腹絏睲虫, 琩高祇布繷, 灿
//			"/PB2CAPIVAN/loveCodeapp/qryLoveCode", //稲み絏琩高
//			"/PB2CAPIVAN/invServ/InvServ", //更ㄣ祇布繷琩高, 更ㄣ祇布腹絏灿琩高
//			"/PB2CAPIVAN/CarInv/Donate", //更ㄣ祇布秘
//			"/PB2CAPIVAN/APIService/generalCarrierRegBlank", //も诀兵絏更ㄣ爹
//			"/PB2CAPIVAN/APIService/carrierLinkBlank", //更ㄣ耴め(も诀兵絏)
//			"/PB2CAPIVAN/APIService/carrierBankAccBlank", //も诀兵絏竕﹚磕眀め
//			"/PB2CAPIVAN/APIService/carrierInvDntBlank" //更ㄣ祇布秘
//		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.e("id", GetUUID());
		
		//new InvDetails().execute("QRCode","VX20070106","","2013/07/13","g7Y1WPAG8PE1PbEIebrTQg==","01802112",GetUUID(),"9035",appID);
		
		setUI(); //砞﹚UI
	}

	private void setUI() {
		// TODO Auto-generated method stub
		text_account = (TextView)findViewById(R.id.text_1);
		text_invoice = (TextView)findViewById(R.id.text_2);
		text_social = (TextView)findViewById(R.id.text_3);
		text_setting = (TextView)findViewById(R.id.text_4);
		btn_account = (ImageButton)findViewById(R.id.btn_1);
		btn_invoice = (ImageButton)findViewById(R.id.btn_2);
		btn_social = (ImageButton)findViewById(R.id.btn_3);
		btn_setting = (ImageButton)findViewById(R.id.btn_4);
		
		text_account.setText("癘眀");
		text_invoice.setText("祇布恨瞶");
		text_social.setText("Θ碞");
		text_setting.setText("砞﹚");
		
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_account.setX(btn_account.getX()+5);
				btn_account.setY(btn_account.getY()+5);
				text_account.setX(text_account.getX()+5);
				text_account.setY(text_account.getY()+5);
				
				Intent intent = new Intent(MainActivity.this, AccountsActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}});
		
		btn_invoice.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_invoice.setX(btn_invoice.getX()+5);
				btn_invoice.setY(btn_invoice.getY()+5);
				text_invoice.setX(text_invoice.getX()+5);
				text_invoice.setY(text_invoice.getY()+5);
				
				Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}});
		
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_social.setX(btn_social.getX()+5);
				btn_social.setY(btn_social.getY()+5);
				text_social.setX(text_social.getX()+5);
				text_social.setY(text_social.getY()+5);
				
				Intent intent = new Intent(MainActivity.this, SocialActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}});
		
		btn_setting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_setting.setX(btn_setting.getX()+5);
				btn_setting.setY(btn_setting.getY()+5);
				text_setting.setX(text_setting.getX()+5);
				text_setting.setY(text_setting.getY()+5);
				
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
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
				Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
				intent.putExtra("UUID", GetUUID());
				intent.putExtra("appID", appID);
				startActivity(intent);
				//new AlertDialog.Builder(this).setTitle("闽").setMessage("huadi73@gmail.com").show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
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
