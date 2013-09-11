package huadi.EleInvAccounts.Social;

import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//成就
public class SocialActivity extends Activity
{
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	TableLayout table;
	Button btn_personal, btn_facebook;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social);
		
		setUI();
	}

	private void setUI() {
		// TODO Auto-generated method stub
		btn_backfunc = (ImageButton)findViewById(R.id.imageButton1);
		btn_account = (ImageButton)findViewById(R.id.imageButton2);
		btn_manager = (ImageButton)findViewById(R.id.imageButton3);
		btn_social = (ImageButton)findViewById(R.id.imageButton4);
		btn_setting = (ImageButton)findViewById(R.id.imageButton5);
		table = (TableLayout)findViewById(R.id.TableLayout1);
		btn_personal = (Button)findViewById(R.id.button1);
		btn_facebook = (Button)findViewById(R.id.button2);
		
		btn_personal.getBackground().setAlpha(255);
		btn_facebook.getBackground().setAlpha(80);
		
		table.removeAllViews();
		TableRow tr = new TableRow(this);
		LinearLayout container, right_container;
		RelativeLayout rl;
		ImageView facebook, crown;
		TextView item, idname;
		
		int count = 10;
		
		for(int i=0;i<count;i++){
			container = new LinearLayout(this);
			container.setOrientation(LinearLayout.HORIZONTAL);
			rl = new RelativeLayout(this);
			facebook = new ImageView(this);
			crown = new ImageView(this);
			
			facebook.setImageResource(R.drawable.fb_person);
			facebook.setPadding(10, 10, 10, 10);
			crown.setImageResource(R.drawable.crown);
			
			rl.addView(facebook);
			rl.addView(crown);
			container.addView(rl);
			
			right_container = new LinearLayout(this);
			right_container.setOrientation(LinearLayout.VERTICAL);
			right_container.setGravity(Gravity.CENTER);
			item = new TextView(this);
			idname = new TextView(this);
			item.setText("每月儲蓄王");
			item.setMinHeight(50);
			idname.setText("王小明");
			idname.setMinHeight(50);
			right_container.addView(item);
			right_container.addView(idname);
			container.addView(right_container);
			tr.addView(container);
			table.addView(tr);
			tr = new TableRow(this);
		}
		
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SocialActivity.this, MainActivity.class);
				startActivity(intent);
				SocialActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SocialActivity.this, AccountsActivity.class);
				startActivity(intent);
				SocialActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SocialActivity.this, ManagerActivity.class);
				startActivity(intent);
				SocialActivity.this.finish();
			}});
		btn_setting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SocialActivity.this, SettingsActivity.class);
				startActivity(intent);
				SocialActivity.this.finish();
			}});
	}
}
