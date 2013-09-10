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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//設定
public class SetCategoryActivity extends Activity
{
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	Button btn_main, btn_sub, btn_addnew, btn_income, btn_expend, btn_ok, btn_delete, btn_cancel, btn_ok2, btn_delete2, btn_cancel2, btn_bg;
	LinearLayout layout_main, layout_sub;
	EditText edit_main, edit_sub;
	TableLayout table;
	Spinner spinner1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setcategory);
		
		setUI();
	}

	private void setUI() {
		btn_backfunc = (ImageButton)findViewById(R.id.imageButton1);
		btn_account = (ImageButton)findViewById(R.id.imageButton2);
		btn_manager = (ImageButton)findViewById(R.id.imageButton3);
		btn_social = (ImageButton)findViewById(R.id.imageButton4);
		btn_setting = (ImageButton)findViewById(R.id.imageButton5);
		btn_main = (Button)findViewById(R.id.button7);
		btn_sub = (Button)findViewById(R.id.button6);
		btn_addnew = (Button)findViewById(R.id.button1);
		btn_income = (Button)findViewById(R.id.button4);
		btn_expend = (Button)findViewById(R.id.button5);
		btn_bg = (Button)findViewById(R.id.button2);
		layout_main = (LinearLayout)findViewById(R.id.LinearLayout1);
		layout_sub = (LinearLayout)findViewById(R.id.LinearLayout2);
		btn_ok = (Button)findViewById(R.id.button8);
		btn_delete = (Button)findViewById(R.id.button3);
		btn_cancel = (Button)findViewById(R.id.button9);
		edit_main = (EditText)findViewById(R.id.editText1);
		table = (TableLayout)findViewById(R.id.TableLayout);
		edit_sub = (EditText)findViewById(R.id.editText2);
		spinner1 = (Spinner)findViewById(R.id.spinner1);
		btn_ok2 = (Button)findViewById(R.id.button10);
		btn_delete2 = (Button)findViewById(R.id.button11);
		btn_cancel2 = (Button)findViewById(R.id.button12);
		
		setMain();
		
		btn_main.setEnabled(false);
		btn_main.getBackground().setAlpha(60);
		btn_sub.getBackground().setAlpha(255);
		btn_main.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				setMain();				
			}});
		
		btn_sub.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				setSub();				
			}});
		
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetCategoryActivity.this, MainActivity.class);
				startActivity(intent);
				SetCategoryActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetCategoryActivity.this, AccountsActivity.class);
				startActivity(intent);
				SetCategoryActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetCategoryActivity.this, ManagerActivity.class);
				startActivity(intent);
				SetCategoryActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetCategoryActivity.this, SocialActivity.class);
				startActivity(intent);
				SetCategoryActivity.this.finish();
			}});
		btn_setting.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(SetCategoryActivity.this, SettingsActivity.class);
				startActivity(intent);
				SetCategoryActivity.this.finish();
			}
		});
	}
	
	public void setMain(){

		btn_main.setEnabled(false);
		btn_sub.setEnabled(true);
		btn_main.getBackground().setAlpha(60);
		btn_sub.getBackground().setAlpha(255);
		
		btn_addnew.setText("新增主分類");		
		btn_addnew.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				setNewAccount();				
			}});
		
		//主分類清單
		table.removeAllViews();
		int count = 3; //主分類總數
		
		TableRow tr = new TableRow(this);
		RelativeLayout rl;
		LinearLayout l1;
		ImageButton[] btn;
		TextView[] name;
		
		name = new TextView[count];
		btn = new ImageButton[count];
		
		for (int i = 0; i < count; i++){
			rl = new RelativeLayout(this);
			
			l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.HORIZONTAL);
			name[i] = new TextView(this);
			name[i].setText("主分類");
			name[i].setPadding(0, 0, 20, 0);
			name[i].setMinWidth(500);
			l1.addView(name[i]);
			
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
	}
	

	public void setSub(){

		btn_sub.setEnabled(false);
		btn_main.setEnabled(true);
		btn_sub.getBackground().setAlpha(60);
		btn_main.getBackground().setAlpha(255);
		
		btn_addnew.setText("新增次分類");		
		btn_addnew.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				setNewCategory();
			}});
		

		//次分類清單
		table.removeAllViews();
		int count = 3; //次分類總數
		
		TableRow tr = new TableRow(this);
		RelativeLayout rl;
		LinearLayout l1;
		ImageButton[] btn;
		TextView[] name;
		
		name = new TextView[count];
		btn = new ImageButton[count];
		
		for (int i = 0; i < count; i++){
			rl = new RelativeLayout(this);
			
			l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.HORIZONTAL);
			name[i] = new TextView(this);
			name[i].setText("次分類");
			name[i].setPadding(0, 0, 20, 0);
			name[i].setMinWidth(500);
			l1.addView(name[i]);
			
			btn[i] = new ImageButton(this);
			btn[i].setBackgroundColor(Color.TRANSPARENT);
			btn[i].setMinimumWidth(810);
			btn[i].setMinimumHeight(100);
			btn[i].setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					editCategory();
				}});

			rl.addView(l1);
			rl.addView(btn[i]);
			tr.addView(rl);
			table.addView(tr);
			tr = new TableRow(this);
		}
	}
	
	
	public void setNewAccount(){

		layout_main.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.GONE);
		
		btn_income.getBackground().setAlpha(60);
		btn_expend.getBackground().setAlpha(255);
		
		btn_income.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btn_income.getBackground().setAlpha(255);
				btn_expend.getBackground().setAlpha(60);
			}
		});
		btn_expend.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btn_expend.getBackground().setAlpha(255);
				btn_income.getBackground().setAlpha(60);
			}
		});
		
		btn_ok.setText("新增");
		btn_ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				edit_main.getText().toString();
				
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
	}
	
	public void editAccount(){

		layout_main.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.VISIBLE);
		
		btn_ok.setText("修改");
		btn_ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				edit_main.getText().toString();
				
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		btn_delete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
	}
	
	public void setNewCategory(){

		layout_sub.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete2.setVisibility(View.GONE);
		
		btn_ok2.setText("新增");
		btn_ok2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
//				spinner1.getSelectedItem().toString();
				edit_sub.getText().toString();
				
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		btn_cancel2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
	}
	
	public void editCategory(){

		layout_sub.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete2.setVisibility(View.VISIBLE);
		
		btn_ok2.setText("修改");
		btn_ok2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
//				spinner1.getSelectedItem().toString();
				edit_sub.getText().toString();
				
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		btn_delete2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
		
		btn_cancel2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
			}});
	}
}
