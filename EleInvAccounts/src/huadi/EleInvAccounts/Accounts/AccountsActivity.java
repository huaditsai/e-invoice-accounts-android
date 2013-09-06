package huadi.EleInvAccounts.Accounts;

import java.util.UUID;

import huadi.EleInvAccounts.CaptureActivity;
import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//記帳
public class AccountsActivity extends Activity
{
	
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	Button btn_addone, btn_scan, btn_import, btn_income, btn_expend, btn_save, btn_cancel;
	TextView text_total, text_income, text_expenditure, text_balance;
	TableLayout table;
	LinearLayout popview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		
		setUI();
	}

	private void setUI() {
		// TODO Auto-generated method stub
		btn_backfunc = (ImageButton)findViewById(R.id.imageButton1);
		btn_account = (ImageButton)findViewById(R.id.imageButton2);
		btn_manager = (ImageButton)findViewById(R.id.imageButton3);
		btn_social = (ImageButton)findViewById(R.id.imageButton4);
		btn_setting = (ImageButton)findViewById(R.id.imageButton5);
		btn_addone = (Button)findViewById(R.id.button1);
		btn_scan = (Button)findViewById(R.id.button2);
		btn_import = (Button)findViewById(R.id.button3);
		btn_income = (Button)findViewById(R.id.button4);
		btn_expend = (Button)findViewById(R.id.button5);
		btn_save = (Button)findViewById(R.id.button6);
		btn_cancel = (Button)findViewById(R.id.button7);
		text_total = (TextView)findViewById(R.id.textView6);
		text_income = (TextView)findViewById(R.id.textView7);
		text_expenditure = (TextView)findViewById(R.id.textView8);
		text_balance = (TextView)findViewById(R.id.textView9);
		table = (TableLayout)findViewById(R.id.TableLayout1);
		popview = (LinearLayout)findViewById(R.id.LinearLayout1);
		
		btn_addone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popview.setVisibility(View.VISIBLE);
			}});
		btn_scan.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AccountsActivity.this, CaptureActivity.class);
				intent.putExtra("UUID", GetUUID());
				intent.putExtra("appID", "YOUR_EINVOICE_APP_ID");
				startActivity(intent);
			}});
		
		btn_income.getBackground().setAlpha(60);
		btn_expend.getBackground().setAlpha(60);
		btn_income.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_income.getBackground().setAlpha(255);
				btn_expend.getBackground().setAlpha(60);
			}});
		btn_expend.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_expend.getBackground().setAlpha(255);
				btn_income.getBackground().setAlpha(60);
			}});
		btn_save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}});
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popview.setVisibility(View.GONE);
			}});
				
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AccountsActivity.this, MainActivity.class);
				startActivity(intent);
				AccountsActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AccountsActivity.this, ManagerActivity.class);
				startActivity(intent);
				AccountsActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AccountsActivity.this, SocialActivity.class);
				startActivity(intent);
				AccountsActivity.this.finish();
			}});
		btn_setting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AccountsActivity.this, SettingsActivity.class);
				startActivity(intent);
				AccountsActivity.this.finish();
			}});
		
		text_total.setText("總資產");
		text_income.setText("本月收入");
		text_expenditure.setText("本月支出");
		text_balance.setText("本月結餘");
		
		TableRow tr = new TableRow(this);
		LinearLayout l1, l2, l3;
		TextView[] month, day, item, main, sub, cost, account;
		
		int count = 10; //資料筆數
		month = new TextView[count];
		day = new TextView[count];
		item = new TextView[count];
		main = new TextView[count];
		sub = new TextView[count];
		cost = new TextView[count];
		account = new TextView[count];
		
		for(int i=0;i<count;i++){
			l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.HORIZONTAL);
			month[i] = new TextView(this);
			month[i].setText("月");
			month[i].setTextSize(20);
			TextPaint tp = month[i].getPaint();
            tp.setFakeBoldText(true);
			l1.addView(month[i]);
			day[i] = new TextView(this);
			day[i].setText("/日");
			day[i].setPadding(0, 0, 20, 0);
			l1.addView(day[i]);
			l2 = new LinearLayout(this);
			l2.setOrientation(LinearLayout.VERTICAL);
			item[i] = new TextView(this);
			item[i].setText("項目");
			l2.addView(item[i]);
			l3 = new LinearLayout(this);
			l3.setOrientation(LinearLayout.HORIZONTAL);
			main[i] = new TextView(this);
			main[i].setText("主-");
			l3.addView(main[i]);
			sub[i] = new TextView(this);
			sub[i].setText("次分類");
			sub[i].setPadding(0, 0, 40, 0);
			l3.addView(sub[i]);
			l2.addView(l3);
			l1.addView(l2);
			cost[i] = new TextView(this);
			cost[i].setText("金額");
			cost[i].setPadding(0, 0, 40, 0);
			l1.addView(cost[i]);
			account[i] = new TextView(this);
			account[i].setText("帳本");
			l1.addView(account[i]);
			tr.addView(l1);
			table.addView(tr);
			tr = new TableRow(this);
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
