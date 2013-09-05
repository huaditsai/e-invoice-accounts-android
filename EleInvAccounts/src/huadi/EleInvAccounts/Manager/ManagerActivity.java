package huadi.EleInvAccounts.Manager;

import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ManagerActivity extends Activity
{
	//UI«Å§i
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
		setUI();
	}

	private void setUI() {
		// TODO Auto-generated method stub
		btn_backfunc = (ImageButton)findViewById(R.id.imageButton1);
		btn_account = (ImageButton)findViewById(R.id.imageButton2);
		btn_manager = (ImageButton)findViewById(R.id.imageButton3);
		btn_social = (ImageButton)findViewById(R.id.imageButton4);
		btn_setting = (ImageButton)findViewById(R.id.imageButton5);
				
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
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ManagerActivity.this, ManagerActivity.class);
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
