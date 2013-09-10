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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

//設定
public class SetCategoryActivity extends Activity
{
	SQLiteDatabase db = null;
	String[] mainCategoryItem;
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
		
		DBHelper dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();
		
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
		
		//side---------------------------------------------
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetCategoryActivity.this, MainActivity.class);
				startActivity(intent);
				db.close();
				SetCategoryActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetCategoryActivity.this, AccountsActivity.class);
				startActivity(intent);
				db.close();
				SetCategoryActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetCategoryActivity.this, ManagerActivity.class);
				startActivity(intent);
				db.close();
				SetCategoryActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetCategoryActivity.this, SocialActivity.class);
				startActivity(intent);
				db.close();
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
		});//side------------------------------------------------------
	}
	
	public void setMain()
	{
		Cursor mainCateCursor = db.rawQuery("SELECT main "
			+ "FROM MainCategory ", null); //要記得''包起來
		
		int mainCateCount = mainCateCursor.getCount(); //資料筆數
		mainCategoryItem = new String[mainCateCount];
		
		if(mainCateCount != 0)
		{
			mainCateCursor.moveToFirst();
			for (int i = 0; i < mainCateCount; i++)
			{
				mainCategoryItem[i] = mainCateCursor.getString(mainCateCursor.getColumnIndex("main"));
				
				Cursor subCursor = db.rawQuery("SELECT sub "
					+ "FROM SubCategory "
					+ "WHERE main = '" + mainCategoryItem[i] + "'", null); //要記得''包起來
				
				if(subCursor.getCount() == 0) 
				{
					ContentValues subCateCV = new ContentValues();
					subCateCV.put("main", mainCategoryItem[i]);
					subCateCV.put("sub", "其他");
					db.insert("SubCategory", null, subCateCV);
				}				
				mainCateCursor.moveToNext(); //移至資料庫下一筆
			}
		}
		else 
		{
			ContentValues mainCateCV = new ContentValues();
			mainCateCV.put("main", "食");
			db.insert("MainCategory", null, mainCateCV);
			mainCateCV.put("main", "衣");
			db.insert("MainCategory", null, mainCateCV);
			
			ContentValues subCateCV = new ContentValues();
			subCateCV.put("main", "食");
			subCateCV.put("sub", "飲料");
			db.insert("SubCategory", null, subCateCV);
			subCateCV.put("main", "衣");
			subCateCV.put("sub", "襯衫");
			db.insert("SubCategory", null, subCateCV);
			
			mainCategoryItem = new String[]{"食", "衣"};
		}
				
		btn_main.setEnabled(false);
		btn_sub.setEnabled(true);
		btn_main.getBackground().setAlpha(60);
		btn_sub.getBackground().setAlpha(255);
		
		btn_addnew.setText("新增主分類");		
		btn_addnew.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				setNewMain();				
			}});
		
		//主分類清單
		table.removeAllViews();
		
		//int count = mainCategory.length; //主分類總數
		
		TableRow tr = new TableRow(this);
		RelativeLayout rl;
		LinearLayout l1;
		ImageButton[] btn;
		TextView[] name;
		
		name = new TextView[mainCateCount];
		btn = new ImageButton[mainCateCount];
		
		for (int i = 0; i < mainCateCount; i++){
			rl = new RelativeLayout(this);
			
			l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.HORIZONTAL);
			name[i] = new TextView(this);
			name[i].setText(mainCategoryItem[i]); //("主分類");
			name[i].setPadding(0, 0, 20, 0);
			name[i].setMinWidth(500);
			l1.addView(name[i]);
			
			final String item = mainCategoryItem[i];
			btn[i] = new ImageButton(this);
			btn[i].setBackgroundColor(Color.TRANSPARENT);
			btn[i].setMinimumWidth(810);
			btn[i].setMinimumHeight(100);
			btn[i].setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					editMain(item);
				}});

			rl.addView(l1);
			rl.addView(btn[i]);
			tr.addView(rl);
			table.addView(tr);
			tr = new TableRow(this);
		}
	}
	
	public void setNewMain(){

		layout_main.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.GONE);
		
		btn_ok.setText("新增");
		btn_ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) 
			{
				//edit_main.getText().toString();
				if(edit_main.getText().toString().length() > 0)
				{
					Cursor mainCateCursor = db.rawQuery("SELECT main "
						+ "FROM MainCategory "
						+ "Where main = '" + edit_main.getText().toString() + "'", null); //要記得''包起來
					
					int count = mainCateCursor.getCount(); //資料筆數				
					if(count == 0) 
					{
						ContentValues mainCateCV = new ContentValues();
						mainCateCV.put("main", edit_main.getText().toString());
						db.insert("MainCategory", null, mainCateCV);
					}
				}
								
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_main.setText("");
				setMain();
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_main.setText("");
			}});
	}
	
	public void editMain(final String item){

		layout_main.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.VISIBLE);
		
		btn_ok.setText("修改");
		btn_ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//edit_main.getText().toString();
				if(edit_main.getText().toString().length() > 0)
				{
//					Cursor mainCateCursor = db.rawQuery("SELECT main "
//						+ "FROM MainCategory "
//						+ "Where main = '" + item + "'", null); //要記得''包起來
					
					//int mainCount = mainCateCursor.getCount(); //資料筆數 = 0 就點不到啦
					
					ContentValues mainCateCV = new ContentValues();
					mainCateCV.put("main", edit_main.getText().toString());
					db.update("MainCategory", mainCateCV, "main = '" + item + "'", null);
					
					//SubCategory中的也要跟著更新
					Cursor subCateCursor = db.rawQuery("SELECT main " 
						+ "FROM SubCategory "
						+ "Where main = '" + item + "'", null); //要記得''包起來
					
					int subCount = subCateCursor.getCount(); //資料筆數				
					if(subCount > 0)
					{
						db.update("SubCategory", mainCateCV, "main = '" + item + "'", null);
					}
				}				
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_main.setText("");
				setMain();
			}});
		
		btn_delete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) 
			{				
				db.delete("MainCategory", "main = '" + item + "'", null);
				
				//SubCategory中的也要跟著刪除
				Cursor subCateCursor = db.rawQuery("SELECT main "
					+ "FROM SubCategory "
					+ "Where main = '" + item + "'", null); //要記得''包起來
				
				int subCount = subCateCursor.getCount(); //資料筆數				
				if(subCount > 0)
				{
					db.delete("SubCategory", "main = '" + item + "'", null);
				}
				
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_main.setText("");
				setMain();
			}});
		
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_main.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_main.setText("");
			}});
	}

	public void setSub()
	{
		Cursor subCursor = db.rawQuery("SELECT sub "
			+ "FROM SubCategory ", null); //要記得''包起來
		
		int subCateCount = subCursor.getCount(); //資料筆數
		String[] subCategory = new String[subCateCount];
		
		if(subCateCount != 0)
		{
			subCursor.moveToFirst();
			for (int i = 0; i < subCateCount; i++)
			{
				subCategory[i] = subCursor.getString(subCursor.getColumnIndex("sub"));
				subCursor.moveToNext(); //移至資料庫下一筆
			}
		}
		
		setMain(); //以防主分類為空
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetCategoryActivity.this, 
			android.R.layout.simple_spinner_item, mainCategoryItem);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定下拉選單的樣式
		spinner1.setAdapter(adapter); //mainCategory下拉
		
		btn_sub.setEnabled(false);
		btn_main.setEnabled(true);
		btn_sub.getBackground().setAlpha(60);
		btn_main.getBackground().setAlpha(255);
		
		btn_addnew.setText("新增次分類");		
		btn_addnew.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				setNewSub();
			}});
		

		//次分類清單
		table.removeAllViews();
		//int count = 3; //次分類總數
		
		TableRow tr = new TableRow(this);
		RelativeLayout rl;
		LinearLayout l1;
		ImageButton[] btn;
		TextView[] name;
		
		name = new TextView[subCateCount];
		btn = new ImageButton[subCateCount];
		
		for (int i = 0; i < subCateCount; i++){
			rl = new RelativeLayout(this);
			
			l1 = new LinearLayout(this);
			l1.setOrientation(LinearLayout.HORIZONTAL);
			name[i] = new TextView(this);
			name[i].setText(subCategory[i]); //次分類
			name[i].setPadding(0, 0, 20, 0);
			name[i].setMinWidth(500);
			l1.addView(name[i]);
			
			final String item = subCategory[i];
			btn[i] = new ImageButton(this);
			btn[i].setBackgroundColor(Color.TRANSPARENT);
			btn[i].setMinimumWidth(810);
			btn[i].setMinimumHeight(100);
			btn[i].setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					editSub(item);
				}});

			rl.addView(l1);
			rl.addView(btn[i]);
			tr.addView(rl);
			table.addView(tr);
			tr = new TableRow(this);
		}
	}
	
	public void setNewSub(){

		layout_sub.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete2.setVisibility(View.GONE);
		
		btn_ok2.setText("新增");
		btn_ok2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{				
				if(edit_sub.getText().toString().length() > 0)
				{
					//SubCategory
					Cursor subCursor = db.rawQuery("SELECT sub "
						+ "FROM SubCategory "
						+ "WHERE sub = '" + edit_sub.getText().toString() + "'", null); //要記得''包起來
					
					int subCateCount = subCursor.getCount(); //資料筆數	
					if(subCateCount == 0)
					{
						ContentValues subCateCV = new ContentValues();
						subCateCV.put("main", spinner1.getSelectedItem().toString());
						subCateCV.put("sub", edit_sub.getText().toString());
						db.insert("SubCategory", null, subCateCV);
					}
				}				
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_sub.setText("");
				setSub();
			}});
		
		btn_cancel2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_sub.setText("");
			}});
	}
	
	public void editSub(final String item)
	{
		edit_sub.setText(item);
		Cursor subCursor = db.rawQuery("SELECT main "
			+ "FROM SubCategory "
			+ "WHERE sub = '" + item + "'", null); //要記得''包起來
		
		//final int subCateCount = subCursor.getCount(); //資料筆數=0就點不到啦!
		subCursor.moveToFirst();
		String mainString = subCursor.getString(subCursor.getColumnIndex("main"));
		ArrayAdapter<String> myAdap = (ArrayAdapter<String>) spinner1.getAdapter();
		spinner1.setSelection(myAdap.getPosition(mainString), true);
		
		layout_sub.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete2.setVisibility(View.VISIBLE);
		
		btn_ok2.setText("修改");
		btn_ok2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(edit_sub.getText().toString().length() > 0)
				{		
					ContentValues subCateCV = new ContentValues();
					subCateCV.put("main", spinner1.getSelectedItem().toString());
					subCateCV.put("sub", edit_sub.getText().toString());
					db.update("SubCategory", subCateCV, "sub = '" + item + "'", null);
				}
				
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_sub.setText("");
				setSub();
			}
		});
		
		btn_delete2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				db.delete("SubCategory", "sub = '" + item + "'", null);
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_sub.setText("");
				setSub();
			}});
		
		btn_cancel2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layout_sub.setVisibility(View.GONE);
				btn_bg.setVisibility(View.GONE);
				edit_sub.setText("");
			}});
	}
}
