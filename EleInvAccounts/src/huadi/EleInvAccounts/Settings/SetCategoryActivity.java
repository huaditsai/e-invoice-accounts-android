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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
	Button btn_addnewmain, btn_addnewsub, btn_ok, btn_delete, btn_cancel, btn_ok2, btn_delete2, btn_cancel2, btn_bg;
	LinearLayout layout_main, layout_sub, layout_original;
	EditText edit_main, edit_sub;
	TableLayout table;
	Spinner spinner1;
	TextView text_ori, text_category;
	
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
		btn_addnewmain = (Button)findViewById(R.id.button1);
		btn_addnewsub = (Button)findViewById(R.id.button6);
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
		layout_original = (LinearLayout)findViewById(R.id.originalcategory);
		text_ori = (TextView)findViewById(R.id.textView10);
		text_category = (TextView)findViewById(R.id.textView12);
		
		setCategory();
		
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
	
	public void setCategory()
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
					subCursor.close();
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
			mainCateCursor.close();
				
				
//				setCategory(); //以防主分類為空
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetCategoryActivity.this, 
					android.R.layout.simple_spinner_item, mainCategoryItem);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定下拉選單的樣式
				spinner1.setAdapter(adapter); //mainCategory下拉
				
				btn_addnewsub.setText("新增次分類");		
				btn_addnewsub.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						setNewSub();
					}});
			
				btn_addnewmain.setText("新增主分類");		
				btn_addnewmain.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						setNewMain();				
					}});
		
		//主分類清單
		table.removeAllViews();
		
		TableRow tr = new TableRow(this);
		RelativeLayout rl;
		LinearLayout[] l1, l2;
		ImageButton[][] btn;
		TextView[][] name;
		
		
		for (int m = 0; m < mainCateCount; m++){

			Cursor subCursor = db.rawQuery("SELECT sub "
				+ "FROM SubCategory "
				+ "WHERE main = '" + mainCategoryItem[m] + "'", null); //要記得''包起來
				
				int subCateCount = subCursor.getCount()+1; //資料筆數
				String[] subCategory = new String[subCateCount];
				
				if(subCateCount != 0)
				{
					subCursor.moveToFirst();
					for (int i = 1; i < subCateCount; i++)
					{
						subCategory[i] = subCursor.getString(subCursor.getColumnIndex("sub"));
						subCursor.moveToNext(); //移至資料庫下一筆
					}
				}
				subCursor.close();

				rl = new RelativeLayout(this);
				
				name = new TextView[mainCateCount][subCateCount];
				btn = new ImageButton[mainCateCount][subCateCount];
				l1 = new LinearLayout[mainCateCount];
				l2 = new LinearLayout[subCateCount];
			
			for(int s = 0; s < subCateCount; s++){
				
				if(s==0){
					
					l1[m] = new LinearLayout(this);
					l1[m].setOrientation(LinearLayout.VERTICAL);
					name[m][s] = new TextView(this);
					name[m][s].setText(mainCategoryItem[m]); //("主分類");
					name[m][s].setPadding(0, 0, 20, 0);
					name[m][s].getPaint().setFakeBoldText(true);
					l1[m].addView(name[m][s]);
					l1[m].setBackgroundColor(Color.rgb(255, 254, 232));
					DisplayMetrics dm = new DisplayMetrics();	// 建立一個DisplayMetrics物件
					this.getWindowManager().getDefaultDisplay().getMetrics(dm);	// 取得裝置的資訊
					int Width = dm.widthPixels;
					int Height = dm.heightPixels;
					l1[m].setMinimumWidth(Width);
					
					final String item = mainCategoryItem[m];
					btn[m][s] = new ImageButton(this);
					btn[m][s].setBackgroundColor(Color.TRANSPARENT);
					btn[m][s].setMinimumWidth(810);
					btn[m][s].setMinimumHeight(80);
					btn[m][s].setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							editMain(item);
						}});
					
					rl.addView(l1[m]);
					rl.addView(btn[m][s]);
					
					tr.addView(rl);
					table.addView(tr);
					tr = new TableRow(this);
				}else{
					rl = new RelativeLayout(this);
					
					l2[s] = new LinearLayout(this);
					l2[s].setOrientation(LinearLayout.VERTICAL);

					name[m][s] = new TextView(this);
					name[m][s].setText(subCategory[s]); //次分類
					name[m][s].setPadding(50, 0, 20, 0);
					l2[s].addView(name[m][s]);

					final String itemMain = mainCategoryItem[m];
					final String itemSub = subCategory[s];
					btn[m][s] = new ImageButton(this);
					btn[m][s].setBackgroundColor(Color.TRANSPARENT);
					btn[m][s].setMinimumWidth(810);
					btn[m][s].setMinimumHeight(50);
					btn[m][s].setOnClickListener(new OnClickListener(){
	
						@Override
						public void onClick(View arg0) {
							editSub(itemMain, itemSub);
						}});
					
					rl.addView(l2[s]);
					rl.addView(btn[m][s]);
					tr.addView(rl);
					table.addView(tr);
					tr = new TableRow(this);
				}
			}
		}
	}
	
	public void setNewMain(){
		layout_main.setVisibility(View.VISIBLE);
		layout_original.setVisibility(View.GONE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.GONE);

		text_category.setText("分類名稱");
		btn_ok.setText("新增");
		btn_ok.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_ok.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_ok.setBackgroundColor(Color.rgb(50, 179, 226));	
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
//						Log.e("error","1");
						mainCateCursor.close();
//						Log.e("error","2");
					}
									
					layout_main.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
					edit_main.setText("");
//					Log.e("error","3");
					setCategory();
//					Log.e("error","4");
				}				
			return false;
			}});
		btn_cancel.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_cancel.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_cancel.setBackgroundColor(Color.rgb(50, 179, 226));	
					layout_main.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
					edit_main.setText("");
				}				
			return false;
			}});
	}
	
	public void editMain(final String item){

		layout_main.setVisibility(View.VISIBLE);
		layout_original.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete.setVisibility(View.VISIBLE);
		
		text_ori.setText(item);
		text_category.setText("修改名稱");
		
		btn_ok.setText("修改");
		btn_ok.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_ok.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_ok.setBackgroundColor(Color.rgb(50, 179, 226));	
					//edit_main.getText().toString();
					if(edit_main.getText().toString().length() > 0)
					{
//						Cursor mainCateCursor = db.rawQuery("SELECT main "
//							+ "FROM MainCategory "
//							+ "Where main = '" + item + "'", null); //要記得''包起來
						
						//int mainCount = mainCateCursor.getCount(); //資料筆數 = 0 就點不到啦
						
						ContentValues mainCateCV = new ContentValues();
						mainCateCV.put("main", edit_main.getText().toString());
						db.update("MainCategory", mainCateCV, "main = '" + item + "'", null);
						
						//SubCategory中的也要跟著更新
						db.update("SubCategory", mainCateCV, "main = '" + item + "'", null);
						
						//記帳裡也要改
						ContentValues chargeCV = new ContentValues();
						chargeCV.put("mainCategory", edit_main.getText().toString());
						db.update("Charge", chargeCV, "mainCategory = '" + item + "'", null);
						
					}				
					layout_main.setVisibility(View.GONE);
					layout_original.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
					edit_main.setText("");
					setCategory();
				}				
			return false;
			}});

		btn_delete.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_delete.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_delete.setBackgroundColor(Color.rgb(50, 179, 226));	
					db.delete("MainCategory", "main = '" + item + "'", null);
					
					//SubCategory中的也要跟著刪除
					Cursor subCateCursor = db.rawQuery("SELECT main "
						+ "FROM SubCategory "
						+ "Where main = '" + item + "'", null); //要記得''包起來
					
					int subCount = subCateCursor.getCount(); //資料筆數
					subCateCursor.close();
					if(subCount > 0)
					{
						db.delete("SubCategory", "main = '" + item + "'", null);
					}
					
					layout_main.setVisibility(View.GONE);
					layout_original.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
					edit_main.setText("");
					setCategory();
				}				
			return false;
			}});
		
		btn_cancel.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_cancel.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_cancel.setBackgroundColor(Color.rgb(50, 179, 226));	
					layout_main.setVisibility(View.GONE);
					layout_original.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
					edit_main.setText("");
				}				
			return false;
			}});
	}
	
	public void setNewSub(){

		layout_sub.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete2.setVisibility(View.GONE);
		
		btn_ok2.setText("新增");
		btn_ok2.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_ok2.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_ok2.setBackgroundColor(Color.rgb(50, 179, 226));
					if(edit_sub.getText().toString().length() > 0)
					{
						//SubCategory
						Cursor subCursor = db.rawQuery("SELECT sub "
							+ "FROM SubCategory "
							+ "WHERE sub = '" + edit_sub.getText().toString() + "'", null); //要記得''包起來
						
						int subCateCount = subCursor.getCount(); //資料筆數
						subCursor.close();
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
					setCategory();
				}				
			return false;
			}});
		btn_cancel2.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_cancel2.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_cancel2.setBackgroundColor(Color.rgb(50, 179, 226));
					layout_sub.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
					edit_sub.setText("");
				}				
			return false;
			}});
	}
	
	public void editSub(final String mainItem, final String subItem)
	{
		edit_sub.setText(subItem);
//		Cursor subCursor = db.rawQuery("SELECT main "
//			+ "FROM SubCategory "
//			+ "WHERE sub = '" + subItem + "'", null); //要記得''包起來
//		
//		//final int subCateCount = subCursor.getCount(); //資料筆數=0就點不到啦!
//		subCursor.moveToFirst();
//		String mainString = subCursor.getString(subCursor.getColumnIndex("main"));
		ArrayAdapter<String> myAdap = (ArrayAdapter<String>) spinner1.getAdapter();
		spinner1.setSelection(myAdap.getPosition(mainItem), true);
		
		layout_sub.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		btn_delete2.setVisibility(View.VISIBLE);
		
		btn_ok2.setText("修改");
		btn_ok2.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_ok2.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_ok2.setBackgroundColor(Color.rgb(50, 179, 226));
					if(edit_sub.getText().toString().length() > 0)
					{
//						Cursor subCursor = db.rawQuery("SELECT main "
//							+ "FROM SubCategory "
//							+ "WHERE main = '" + mainItem + "'" 
//							+ "AND sub = '" + subItem + "'", null); //要記得''包起來

						ContentValues subCateCV = new ContentValues();
						subCateCV.put("main", spinner1.getSelectedItem().toString());
						subCateCV.put("sub", edit_sub.getText().toString());
						db.update("SubCategory", subCateCV,"main = '" + mainItem + "'" + "AND sub = '" + subItem + "'", null);
						
						//記帳裡也要改
						ContentValues chargeCV = new ContentValues();
						chargeCV.put("mainCategory", spinner1.getSelectedItem().toString());
						chargeCV.put("subCategory", edit_sub.getText().toString());
						db.update("Charge", chargeCV, "mainCategory = '" + mainItem + "'" + "AND subCategory = '" + subItem + "'", null);

//						subCursor.close();
					}
					
					layout_sub.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
					edit_sub.setText("");
					setCategory();
				}				
			return false;
			}});

		btn_delete2.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_delete2.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_delete2.setBackgroundColor(Color.rgb(50, 179, 226));
					db.delete("SubCategory", "main = '" + mainItem + "'" + "AND sub = '" + subItem + "'", null);
					layout_sub.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
					edit_sub.setText("");
					setCategory();
				}				
			return false;
			}});

		btn_cancel2.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_cancel2.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_cancel2.setBackgroundColor(Color.rgb(50, 179, 226));
					layout_sub.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
					edit_sub.setText("");
				}				
			return false;
			}});
	}
}
