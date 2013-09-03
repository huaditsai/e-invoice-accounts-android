package huadi.EleInvAccounts;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity
{
	String[] method = new String[]
		{
			"/PB2CAPIVAN/invapp/InvApp", //琩高い贱祇布腹絏睲虫, 琩高祇布繷, 灿
			"/PB2CAPIVAN/loveCodeapp/qryLoveCode", //稲み絏琩高
			"/PB2CAPIVAN/invServ/InvServ", //更ㄣ祇布繷琩高, 更ㄣ祇布腹絏灿琩高
			"/PB2CAPIVAN/CarInv/Donate", //更ㄣ祇布秘
			"/PB2CAPIVAN/APIService/generalCarrierRegBlank", //も诀兵絏更ㄣ爹
			"/PB2CAPIVAN/APIService/carrierLinkBlank", //更ㄣ耴め(も诀兵絏)
			"/PB2CAPIVAN/APIService/carrierBankAccBlank", //も诀兵絏竕﹚磕眀め
			"/PB2CAPIVAN/APIService/carrierInvDntBlank" //更ㄣ祇布秘
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.e("id", GetUUID());
		
		Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
		startActivity(intent);
		
		//new InvDetails().execute("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
