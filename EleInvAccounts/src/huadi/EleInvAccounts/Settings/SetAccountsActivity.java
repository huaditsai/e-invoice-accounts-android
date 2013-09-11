package huadi.EleInvAccounts.Settings;

import huadi.EleInvAccounts.DBHelper;
import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	
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
		
		dbHelper = new DBHelper(SetAccountsActivity.this);
		db = dbHelper.getWritableDatabase(); //讓db可寫入
		
		setUI();
	}

	private void setUI() 
	{
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
		
		Cursor accountCursor = db.rawQuery("SELECT * "
			+ "FROM Account ", null); //要記得''包起來
		
		int accountCount = accountCursor.getCount(); //資料筆數
		
		if(accountCount == 0)		 
		{
			ContentValues accountCV = new ContentValues();
			accountCV.put("account_name", "wallet01");
			accountCV.put("money", "1000");
			db.insert("Account", null, accountCV);
			
			Cursor Cursor = db.rawQuery("SELECT * "
				+ "FROM Account ", null); //要記得''包起來
			accountCount = Cursor.getCount();
		}
		
		
		//帳戶清單
		table.removeAllViews();
		//int count = 3; //帳戶總數
		
		TableRow tr = new TableRow(this);
		RelativeLayout rl;
		LinearLayout l1;
		ImageButton[] btn;
		TextView[] name, money;
		
		name = new TextView[accountCount];
		money = new TextView[accountCount];
		btn = new ImageButton[accountCount];
		if(accountCount != 0)
		{
			accountCursor.moveToFirst();
			for (int i = 0; i < accountCount; i++)
			{
				rl = new RelativeLayout(this);
				
				l1 = new LinearLayout(this);
				l1.setOrientation(LinearLayout.HORIZONTAL);
				name[i] = new TextView(this);
				name[i].setText(accountCursor.getString(accountCursor.getColumnIndex("account_name"))); //"錢包");
				name[i].setPadding(0, 0, 20, 0);
				name[i].setMaxEms(7);
				name[i].setEllipsize(TruncateAt.END);
	//			name[i].setMinWidth(500);
				l1.addView(name[i]);
				
				money[i] = new TextView(this);
				money[i].setText(accountCursor.getString(accountCursor.getColumnIndex("money")));
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
				
				accountCursor.moveToNext();
			}
		}
		
		btn_addnew.setOnClickListener(new OnClickListener(){ //新增
			@Override
			public void onClick(View arg0) {
				setNewAccount();				
			}});
		
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetAccountsActivity.this, MainActivity.class);
				startActivity(intent);
				SetAccountsActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetAccountsActivity.this, AccountsActivity.class);
				startActivity(intent);
				SetAccountsActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetAccountsActivity.this, ManagerActivity.class);
				startActivity(intent);
				SetAccountsActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetAccountsActivity.this, SocialActivity.class);
				startActivity(intent);
				SetAccountsActivity.this.finish();
			}});
		btn_setting.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(SetAccountsActivity.this, SettingsActivity.class);
				startActivity(intent);
				SetAccountsActivity.this.finish();
			}
		});
		
	}
	
	public void setNewAccount()
	{		
		layout_addnew.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.GONE);
		
		btn_ok.setText("新增");
		btn_ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) 
			{
//				edit_name.getText().toString();
//				edit_money.getText().toString();
				
				ContentValues AccountCV = new ContentValues();
				AccountCV.put("account_name", edit_name.getText().toString()); //發票編號
				AccountCV.put("money", edit_money.getText().toString()); //消費金額
				
				Cursor InvoiceCursor = db.rawQuery("SELECT name "
					+ "FROM Account "
					+ "WHERE account_name = '" + edit_name.getText().toString() + "'", null); //要記得''包起來
				
				int count = InvoiceCursor.getCount(); //資料筆數
				if(count == 0)
					db.insert("Account", null, AccountCV); //新增一筆至 Invoice
				else
				{
					InvoiceCursor.moveToFirst();
					for (int i = 0; i < count; i++)
					{
						db.update("Account", AccountCV, "account_name = '" + edit_name.getText().toString() + "'", null);
						InvoiceCursor.moveToNext(); //移至資料庫下一筆
					}
				}
				
				layout_addnew.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_name.setText("");
				edit_money.setText("");
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
				edit_name.setText("");
				edit_money.setText("");
			}});
		
		btn_delete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				layout_addnew.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_name.setText("");
				edit_money.setText("");
			}});
		
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_addnew.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_name.setText("");
				edit_money.setText("");
			}});
	}
}
