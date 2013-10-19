package huadi.EleInvAccounts.Manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import huadi.EleInvAccounts.DBHelper;
import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Accounts.CaptureActivity;
import huadi.EleInvAccounts.Inquiry.WinningList;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//發票
public class ManagerActivity extends Activity
{
	String appID, UUID;
	SQLiteDatabase db = null;
	
	final int btnMovePosi = 5; //按鈕位移量
	final int btnMoveNega = -5; //按鈕位移量
	
	int year, month, day;
	String invPeriod; //對獎發票期別(yyyMM)
	
	Map<String, List<String>> winning; //開獎號碼
	ManualAward manualAward; //手動對獎
	boolean isAuto = true;
	Map<String, List<String>> winningAward; //中獎號碼
	AutoAward autoAward; //資料庫對獎
	
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	ImageButton btn_list, btn_analysis, btn_prize, btn_prizelist;
	TextView text_list, text_analysis, text_prize, text_prizelist;
	LinearLayout linear1, linear2, invoice, number, linear3;
	ImageButton btn_right, btn_left, btn_right2, btn_left2;
	TextView text_month, text_price1, text_price2, text_price3, text_price4;
	TextView text_month2, text_input, text_prizeornot;
	Button btn_close, btn_close2, btn_invoice, btn_number, btn_close3;
	TableLayout invoicetable, analysistable;
	Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_clear, btn_backspace, btn_bg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
		SharedPreferences ids = getSharedPreferences("IDs", MODE_PRIVATE ); //偏好設定
		appID = ids.getString("appID", "");
		UUID = ids.getString("UUID", "");
		
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
		text_list = (TextView)findViewById(R.id.textView6);
		text_analysis = (TextView)findViewById(R.id.textView7);
		text_prize = (TextView)findViewById(R.id.textView8);
		text_prizelist = (TextView)findViewById(R.id.textView9);
		btn_list = (ImageButton)findViewById(R.id.imageButton6);
		btn_analysis = (ImageButton)findViewById(R.id.imageButton7);
		btn_prize = (ImageButton)findViewById(R.id.imageButton8);
		btn_prizelist = (ImageButton)findViewById(R.id.imageButton9);
		linear1 = (LinearLayout)findViewById(R.id.LinearLayout1);
		btn_right = (ImageButton)findViewById(R.id.imageButton97);
		btn_left = (ImageButton)findViewById(R.id.imageButton96);
		text_month = (TextView)findViewById(R.id.textView27);
		text_price1 = (TextView)findViewById(R.id.textView11);
		text_price2 = (TextView)findViewById(R.id.textView12);
		text_price3 = (TextView)findViewById(R.id.textView16);
		text_price4 = (TextView)findViewById(R.id.textView17);
		btn_close = (Button)findViewById(R.id.button7);
		linear2 = (LinearLayout)findViewById(R.id.LinearLayout2);
		btn_right2 = (ImageButton)findViewById(R.id.imageButton77);
		btn_left2 = (ImageButton)findViewById(R.id.imageButton76);
		text_month2 = (TextView)findViewById(R.id.textView37);
		btn_invoice = (Button)findViewById(R.id.button1);
		btn_number = (Button)findViewById(R.id.button2);
		invoice = (LinearLayout)findViewById(R.id.invoice);
		number = (LinearLayout)findViewById(R.id.number);
		invoicetable = (TableLayout)findViewById(R.id.invoicetable);
		text_input = (TextView)findViewById(R.id.textView97);
		text_prizeornot = (TextView)findViewById(R.id.textView22);
		btn_0 = (Button)findViewById(R.id.button14);
		btn_1 = (Button)findViewById(R.id.button10);
		btn_2 = (Button)findViewById(R.id.button11);
		btn_3 = (Button)findViewById(R.id.button12);
		btn_4 = (Button)findViewById(R.id.button6);
		btn_5 = (Button)findViewById(R.id.button8);
		btn_6 = (Button)findViewById(R.id.button9);
		btn_7 = (Button)findViewById(R.id.button3);
		btn_8 = (Button)findViewById(R.id.button4);
		btn_9 = (Button)findViewById(R.id.button5);
		btn_clear = (Button)findViewById(R.id.button13);
		btn_backspace = (Button)findViewById(R.id.button15);
		btn_close2 = (Button)findViewById(R.id.button77);
		btn_bg = (Button)findViewById(R.id.button16);
		linear3 = (LinearLayout)findViewById(R.id.LinearLayout3);
		btn_close3 = (Button)findViewById(R.id.button78);
		analysistable = (TableLayout)findViewById(R.id.TableLayout1);
		
		text_list.setText("發票清單");
		text_analysis.setText("消費分析");
		text_prize.setText("發票對獎");
		text_prizelist.setText("各期獎號");

		btn_list.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_list.setX(btn_list.getX() + btnMovePosi);
					btn_list.setY(btn_list.getY() + btnMovePosi);
					text_list.setX(text_list.getX() + btnMovePosi);
					text_list.setY(text_list.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_list.setX(btn_list.getX() + btnMoveNega);
					btn_list.setY(btn_list.getY() + btnMoveNega);
					text_list.setX(text_list.getX() + btnMoveNega);
					text_list.setY(text_list.getY() + btnMoveNega);
					Intent intent = new Intent(ManagerActivity.this, InvoiceListActivity.class);
					startActivity(intent);
					ManagerActivity.this.finish();
				}				
				return false;
				}});
		
		btn_analysis.setOnTouchListener(new OnTouchListener(){ //消費分析
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_analysis.setX(btn_analysis.getX() + btnMovePosi);
					btn_analysis.setY(btn_analysis.getY() + btnMovePosi);
					text_analysis.setX(text_analysis.getX() + btnMovePosi);
					text_analysis.setY(text_analysis.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_analysis.setX(btn_analysis.getX() + btnMoveNega);
					btn_analysis.setY(btn_analysis.getY() + btnMoveNega);
					text_analysis.setX(text_analysis.getX() + btnMoveNega);
					text_analysis.setY(text_analysis.getY() + btnMoveNega);
					setAnalysis();
				}
				return false;
			}});
		
		btn_prize.setOnTouchListener(new OnTouchListener(){ //發票對獎
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_prize.setX(btn_prize.getX() + btnMovePosi);
					btn_prize.setY(btn_prize.getY() + btnMovePosi);
					text_prize.setX(text_prize.getX() + btnMovePosi);
					text_prize.setY(text_prize.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_prize.setX(btn_prize.getX() + btnMoveNega);
					btn_prize.setY(btn_prize.getY() + btnMoveNega);
					text_prize.setX(text_prize.getX() + btnMoveNega);
					text_prize.setY(text_prize.getY() + btnMoveNega);
					
					if(IsInternet()) //判斷網路狀態
					{
						linear2.setVisibility(View.VISIBLE);
						setPrizePop(); //發票對獎
					}
					else
					{
						new AlertDialog.Builder(ManagerActivity.this)
						.setTitle("請開啟網路連線功能")
						.setMessage("取得對獎資訊需要網路連線")
						.setPositiveButton("確定", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));							
							}									
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener()	{
							@Override
							public void onClick(DialogInterface dialog, int which){														
							}
						}).show();
					}
				}				
				return false;				
			}});
		
		btn_prizelist.setOnTouchListener(new OnTouchListener(){ //各期獎號
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_prizelist.setX(btn_prizelist.getX() + btnMovePosi);
					btn_prizelist.setY(btn_prizelist.getY() + btnMovePosi);
					text_prizelist.setX(text_prizelist.getX() + btnMovePosi);
					text_prizelist.setY(text_prizelist.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_prizelist.setX(btn_prizelist.getX() + btnMoveNega);
					btn_prizelist.setY(btn_prizelist.getY() + btnMoveNega);
					text_prizelist.setX(text_prizelist.getX() + btnMoveNega);
					text_prizelist.setY(text_prizelist.getY() + btnMoveNega);
					
					if(IsInternet()) //判斷網路狀態
					{
						linear1.setVisibility(View.VISIBLE);
						setPrizelistPop(); //各期獎號
					}
					else
					{
						new AlertDialog.Builder(ManagerActivity.this)
						.setTitle("請開啟網路連線功能")
						.setMessage("取得對獎資訊需要網路連線")
						.setPositiveButton("確定", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));							
							}									
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener()	{
							@Override
							public void onClick(DialogInterface dialog, int which){														
							}
						}).show();
					}					
				}
				return false;								
			}});
			
		//slide--------------------------------------------------------------
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManagerActivity.this, MainActivity.class);
				startActivity(intent);
				ManagerActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManagerActivity.this, AccountsActivity.class);
				startActivity(intent);
				ManagerActivity.this.finish();
			}});
		btn_social.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManagerActivity.this, SocialActivity.class);
				startActivity(intent);
				ManagerActivity.this.finish();
			}});
		btn_setting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManagerActivity.this, SettingsActivity.class);
				startActivity(intent);
				ManagerActivity.this.finish();
			}});
		//slide-----------------------------------------------------------------
	}
	
	public void setAnalysis()
	{ //消費分析
		
		Cursor mainCat = db.rawQuery("SELECT main "
			+ "FROM MainCategory ", null); //要記得''包起來 ASC小-大 DESC大-小
		
		int count = mainCat.getCount(); //資料筆數
		if(count > 0 )
			mainCat.moveToFirst();
		
		List<Integer> moneyList = new ArrayList<Integer>();
		
		for (int i = 0; i < count; i++)
		{
			Cursor charge = db.rawQuery("SELECT money "
				+ "FROM Charge "
				+ "WHERE mainCategory = '" + mainCat.getString(mainCat.getColumnIndex("main")) + "'", null);
			
			int tmp = 0;
			if(charge.getCount() > 0)
				charge.moveToFirst();
			for (int j = 0; j < charge.getCount(); j++)
			{
				tmp += charge.getInt(charge.getColumnIndex("money"));
				charge.moveToNext();
			}
			moneyList.add(tmp);
			mainCat.moveToNext();
		}
		
		float total = 0;
		for (int i = 0; i < moneyList.size(); i++)
		{
			total += moneyList.get(i);			
		}
		
		linear3.setVisibility(View.VISIBLE);
		btn_bg.setVisibility(View.VISIBLE);
		analysistable.removeAllViews();
		TableRow tr = new TableRow(this);
		LinearLayout l1;
		TextView[] item, percent, cost;
//		int count = 5;

		item = new TextView[count];
		percent = new TextView[count];
		cost = new TextView[count];
		
		DisplayMetrics dm = new DisplayMetrics();	// 建立一個DisplayMetrics物件
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);	// 取得裝置的資訊
		int Width = dm.widthPixels;
		int Height = dm.heightPixels;
		
		if(count > 0 )
			mainCat.moveToFirst();
		
		for(int i=0;i<count;i++)
		{
			l1 = new LinearLayout(this);
			item[i] = new TextView(this);
			item[i].setText(mainCat.getString(mainCat.getColumnIndex("main")));//("分類");
			item[i].setMinWidth(Width/4);
			percent[i] = new TextView(this);
			percent[i].setText(((float)moneyList.get(i)/total)*100 + " %");
			percent[i].setMinWidth(Width/4);
			cost[i] = new TextView(this);
			cost[i].setText(moneyList.get(i) + " NTD");
			cost[i].setMinWidth(Width/4);

			l1.addView(item[i]);
			l1.addView(percent[i]);
			l1.addView(cost[i]);
			tr.addView(l1);
			analysistable.addView(tr);
			tr = new TableRow(this);
			
			mainCat.moveToNext();
		}
		
		btn_close3.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_close3.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_close3.setBackgroundColor(Color.rgb(50, 179, 226));
					linear3.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
				}				
			return false;
			}});
	}
	
	private void setPrizePop(){ //發票對獎
		btn_bg.setVisibility(View.VISIBLE);
		
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 1911; //民國
		month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始...
		day = calendar.get(Calendar.DAY_OF_MONTH);
		if(month % 2 == 1)
		{
			if(day < 25)
				month -= 3; //若現在為9月, 還沒到25號, 只能看56月
			else 
				month--; //若現在為9月, 減減來看78月
		}
		else
			month -= 2; //若現在為10月, 減2來看78月
		
		invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
		text_month2.setText(invPeriod); //月份
		

		btn_left2.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_left2.setX(btn_left2.getX() + btnMovePosi);
					btn_left2.setY(btn_left2.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_left2.setX(btn_left2.getX() + btnMoveNega);
					btn_left2.setY(btn_left2.getY() + btnMoveNega);

					month -= 2;
					if(month == 0 && year != 0)
					{
						year--;
						month = 12;
					}
					invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
					text_month2.setText(invPeriod); //月份
					if(isAuto) Auto();
					else Manual();		
				}				
				return false;
				}});
		

		
		btn_right2.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_right2.setX(btn_right2.getX() + btnMovePosi);
					btn_right2.setY(btn_right2.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_right2.setX(btn_right2.getX() + btnMoveNega);
					btn_right2.setY(btn_right2.getY() + btnMoveNega);

					month += 2;
					if(month == 14 && year != 0)
					{
						year++;
						month = 2;
					}
					invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
					text_month2.setText(invPeriod); //月份
					if(isAuto) Auto();
					else Manual();
				}				
				return false;
				}});
		
		btn_invoice.setOnClickListener(new OnClickListener(){ //發票對獎
			@Override
			public void onClick(View v) {
				invoice.setVisibility(View.VISIBLE);
				number.setVisibility(View.GONE);
				isAuto = true;
				Auto(); //自動對獎
			}});
		
		btn_number.setOnClickListener(new OnClickListener(){ //三碼對獎
			@Override
			public void onClick(View v) {
				invoice.setVisibility(View.GONE);
				number.setVisibility(View.VISIBLE);
				isAuto = false;
				Manual(); //手動對獎
			}});
		
		btn_close2.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_close2.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_close2.setBackgroundColor(Color.rgb(50, 179, 226));
					linear2.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
				}				
			return false;
			}});
		
		if(isAuto) Auto(); //自動對獎
		else Manual(); //手動對獎
	}
	
	private void Auto() //自動對獎
	{
		int count = 0; //中獎筆數
		try
		{
			winning = new WinningList(this).execute(String.format("%d%02d", year, month), UUID, appID).get();
			autoAward = new AutoAward(ManagerActivity.this);

			winningAward = autoAward.Award(String.format("%d%02d", year, month), winning);
			count = winningAward.size(); 
		}
		catch(Exception e)
		{			
			//e.printStackTrace();
		}
		
		invoicetable.removeAllViews();
		
		//Log.e("count", "" + count);
		
		TableRow tr = new TableRow(this);
		LinearLayout l1;
		TextView[] prize;
		final TextView[] number;
		TextView[] date, store, cost;
		
		if(count > 0)
		{
			List<String> prizeInfoList = winningAward.get("prize");
			
			prize = new TextView[prizeInfoList.size()];
			number = new TextView[prizeInfoList.size()];
			date = new TextView[prizeInfoList.size()];
			store = new TextView[prizeInfoList.size()];
			cost = new TextView[prizeInfoList.size()];
			
			invoicetable.removeAllViews();
			
			
			//Log.e("prizeInfoList", "" + prizeInfoList);
			for (int i = 0; i < prizeInfoList.size(); i++)
			{
				//prizeName, invDate, invNum, sellerName, invTotalCost
				String prizeName = prizeInfoList.get(i).split(",")[0]; //獎項				
				String invDate = prizeInfoList.get(i).split(",")[1]; //日期
				String invNum = prizeInfoList.get(i).split(",")[2]; //發票號碼
				String sellerName = prizeInfoList.get(i).split(",")[3]; //消費商店
				String invTotalCost =prizeInfoList.get(i).split(",")[4] + " NTD"; //金額
					
				l1 = new LinearLayout(this);
				l1.setOrientation(LinearLayout.HORIZONTAL);
	
				prize[i] = new TextView(this);
				prize[i].setText(prizeName); //獎項
				prize[i].setPadding(0, 0, 20, 0);
				prize[i].setTextColor(Color.RED);
				prize[i].setMinimumWidth(150);
				prize[i].setGravity(Gravity.CENTER);
				date[i] = new TextView(this);
				date[i].setText(invDate); //日期
				date[i].setPadding(0, 0, 20, 0);
				date[i].setMinimumWidth(100);
				date[i].setGravity(Gravity.CENTER);
				number[i] = new TextView(this);
				number[i].setText(invNum); //發票號碼
				number[i].setPadding(0, 0, 20, 0);
				number[i].setMinimumWidth(150);
				number[i].setGravity(Gravity.CENTER);
				store[i] = new TextView(this);
				store[i].setText(sellerName); //消費商店
				store[i].setMaxEms(6);
				store[i].setLines(1);
				store[i].setPadding(0, 0, 20, 0);
				store[i].setMinimumWidth(100);
				store[i].setGravity(Gravity.CENTER);
				cost[i] = new TextView(this);
				cost[i].setText(invTotalCost); //金額
				cost[i].setMinimumWidth(100);
				cost[i].setGravity(Gravity.CENTER);
				
				l1.addView(prize[i]);
				l1.addView(date[i]);
				l1.addView(number[i]);
				l1.addView(store[i]);
				l1.addView(cost[i]);
				
				tr.addView(l1);
				invoicetable.addView(tr);
				tr = new TableRow(this);
			}
		}
		
	}

	private void setPrizelistPop() //開獎號碼列表popView
	{
		btn_bg.setVisibility(View.VISIBLE);
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 1911; //民國
		month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始...
		day = calendar.get(Calendar.DAY_OF_MONTH);
		if(month % 2 == 1)
		{
			if(day < 25)
				month -= 3; //若現在為9月, 還沒到25號, 只能看56月
			else 
				month--; //若現在為9月, 減減來看78月
		}
		else
			month -= 2; //若現在為10月, 減2來看78月
		
		invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
		text_month.setText(invPeriod); //月份
		

		btn_left.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_left.setX(btn_left.getX() + btnMovePosi);
					btn_left.setY(btn_left.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_left.setX(btn_left.getX() + btnMoveNega);
					btn_left.setY(btn_left.getY() + btnMoveNega);

					month -= 2;
					if(month == 0 && year != 0)
					{
						year--;
						month = 12;
					}
					invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
					text_month.setText(invPeriod); //月份
					GetWinningList(String.format("%d%02d", year, month));
				}				
				return false;
				}});
		

		
		btn_right.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_right.setX(btn_right.getX() + btnMovePosi);
					btn_right.setY(btn_right.getY() + btnMovePosi);
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_right.setX(btn_right.getX() + btnMoveNega);
					btn_right.setY(btn_right.getY() + btnMoveNega);

					month += 2;
					if(month == 14 && year != 0)
					{
						year++;
						month = 2;
					}
					invPeriod = String.format("%d 年 %02d - %02d 月", year, month-1, month);
					text_month.setText(invPeriod); //月份
					GetWinningList(String.format("%d%02d", year, month));
				}				
				return false;
				}});
		
		GetWinningList(String.format("%d%02d", year, month));
		

		btn_close.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_close.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_close.setBackgroundColor(Color.rgb(50, 179, 226));
					linear1.setVisibility(View.GONE);
					btn_bg.setVisibility(View.GONE);
				}				
			return false;
			}});		
	}
	
	public void GetWinningList(String _invPeriod) //取得開獎號碼
	{
		try
		{
			Map<String, List<String>> winning = new WinningList(this).execute(_invPeriod, UUID, appID).get();
			
			String spcPrizeNo = "", firstPrizeNo = "", sixthPrizeNo = "", superPrizeNo = "";
			
			for (String no : winning.get("spcPrizeNo")) //特獎號
				spcPrizeNo += no + "\n";
			text_price1.setText(spcPrizeNo.substring(0, spcPrizeNo.length()-1));
			
			for (String no : winning.get("firstPrizeNo")) //頭獎號
				firstPrizeNo += no + "\n";
			text_price2.setText(firstPrizeNo.substring(0, firstPrizeNo.length()-1));
			
			for (String no : winning.get("sixthPrizeNo")) //增開獎號
				sixthPrizeNo += no + "\n";
			text_price3.setText(sixthPrizeNo.substring(0, sixthPrizeNo.length()-1));
			
			for (String no : winning.get("superPrizeNo")) //特別獎號
				superPrizeNo += no + "\n";
			text_price4.setText(superPrizeNo.substring(0, superPrizeNo.length()-1));
		}
		catch (Exception e)
		{
			text_price1.setText("無此期別資料");
			text_price2.setText("無此期別資料");
			text_price3.setText("無此期別資料");
			text_price4.setText("無此期別資料");
			e.printStackTrace();
		}		
	}
	public void Manual() //手動對獎
	{
		try
		{
			winning = new WinningList(this).execute(String.format("%d%02d", year, month), UUID, appID).get();
			manualAward = new ManualAward();
			//Log.e("winning", "" + manualAward.Award("516", winning));		
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
		
		text_input.setText("請輸入號碼");
		text_prizeornot.setText("未中獎");
		
		btn_0.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("0");
			}});
		
		btn_1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("1");
			}});
		
		btn_2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("2");
			}});
		
		btn_3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("3");
			}});
		
		btn_4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("4");
			}});
		
		btn_5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("5");
			}});
		
		btn_6.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("6");
			}});
		
		btn_7.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("7");
			}});
		
		btn_8.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("8");
			}});
		
		btn_9.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AwardInput("9");
			}});
		
		btn_clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				text_input.setText("請輸入號碼");
			}});
		
		btn_backspace.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String currInput = text_input.getText().toString();
				if(currInput.length() == 1) //只有一個就清空
					text_input.setText("請輸入號碼");
				else if(currInput.length() == 2) //兩個就減一, 三個已經對獎了
					text_input.setText(currInput.substring(0, 1));
			}});		
	}
	
	public void AwardInput(String InputNumString) //判斷手動對獎的輸入
	{
		if(!text_input.getText().toString().equals("請輸入號碼")) //為數字
		{
			text_input.setText(text_input.getText().toString() + InputNumString);
			if(text_input.getText().toString().length() == 3)
			{
				try
				{
					text_prizeornot.setText(manualAward.Award(text_input.getText().toString(), winning));
				}
				catch (Exception e)
				{
					//e.printStackTrace();
					text_prizeornot.setText("無此期別資料");
				}
				text_input.setText("請輸入號碼");
			}
		}
		else					
			text_input.setText(InputNumString);
	}
	
	private boolean IsInternet()
	{
		ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);//先取得此service
		NetworkInfo networInfo = conManager.getActiveNetworkInfo(); //在取得相關資訊
		
		if (networInfo == null || !networInfo.isAvailable()) //沒網路
			return false;
		else
			return true;
	}
	
}
