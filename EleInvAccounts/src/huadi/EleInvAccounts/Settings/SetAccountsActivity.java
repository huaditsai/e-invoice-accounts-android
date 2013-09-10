package huadi.EleInvAccounts.Settings;

import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//設定
public class SetAccountsActivity extends Activity
{
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	Button btn_addnew, btn_ok, btn_delete, btn_cancel, btn_bg;
	LinearLayout layout_addnew;
	EditText edit_name, edit_money;
	TableLayout table;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setaccount);
		
		setUI();
	}

	private void setUI() {
		btn_backfunc = (ImageButton)findViewById(R.id.imageButton1);
		btn_account = (ImageButton)findViewById(R.id.imageButton2);
		btn_manager = (ImageButton)findViewById(R.id.imageButton3);
		btn_social = (ImageButton)findViewById(R.id.imageButton4);
		btn_setting = (ImageButton)findViewById(R.id.imageButton5);
		btn_addnew = (Button)findViewById(R.id.button1);
		btn_bg = (Button)findViewById(R.id.button2);
		layout_addnew = (LinearLayout)findViewById(R.id.LinearLayout1);
		btn_ok = (Button)findViewById(R.id.button8);
		btn_delete = (Button)findViewById(R.id.button3);
		btn_cancel = (Button)findViewById(R.id.button9);
		edit_name = (EditText)findViewById(R.id.editText1);
		edit_money = (EditText)findViewById(R.id.editText2);
		table = (TableLayout)findViewById(R.id.TableLayout);
		
		//帳戶清單
		table.removeAllViews();
		int count = 3; //帳戶總數
		
		TableRow tr = new TableRow(this);
		RelativeLayout rl;
		LinearLayout l1;
		ImageButton[] btn;
		TextView[] name, money;
		
		name = new TextView[count];
		money = new TextView[count];
		btn = new ImageButton[count];
		
		for (int i = 0; i < count; i++){
			rl = new RelativeLayout(this);
			
			l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.HORIZONTAL);
			name[i] = new TextView(this);
			name[i].setText("錢包");
			name[i].setPadding(0, 0, 20, 0);
			name[i].setMinWidth(500);
			l1.addView(name[i]);
			
			money[i] = new TextView(this);
			money[i].setText("2,000NTD");
			money[i].setPadding(0, 0, 20, 0);
			l1.addView(money[i]);
			
			btn[i] = new ImageButton(this);
			btn[i].setBackgroundColor(Color.TRANSPARENT);
			btn[i].setMinimumWidth(810);
			btn[i].setMinimumHeight(100);
			btn[i].setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					editAccount();
				}});

			rl.addView(l1);
			rl.addView(btn[i]);
			tr.addView(rl);
			table.addView(tr);
			tr = new TableRow(this);
		}
		
		btn_addnew.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				setNewAccount();				
			}});
		
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetAccountsActivity.this, MainActivity.class);
				startActivity(intent);
				SetAccountsActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetAccountsActivity.this, AccountsActivity.class);
				startActivity(intent);
				SetAccountsActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetAccountsActivity.this, ManagerActivity.class);
				startActivity(intent);
				SetAccountsActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetAccountsActivity.this, SocialActivity.class);
				startActivity(intent);
				SetAccountsActivity.this.finish();
			}});
	}
	
	public void setNewAccount(){

		layout_addnew.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.GONE);
		
		btn_ok.setText("新增");
		btn_ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				edit_name.getText().toString();
				edit_money.getText().toString();
				
				layout_addnew.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_addnew.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
	}
	
	public void editAccount(){

		layout_addnew.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.VISIBLE);
		
		btn_ok.setText("修改");
		btn_ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				edit_name.getText().toString();
				edit_money.getText().toString();
				
				layout_addnew.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		btn_delete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				layout_addnew.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_addnew.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
	}
}
