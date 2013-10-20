package huadi.EleInvAccounts.Accounts;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import huadi.EleInvAccounts.DBHelper;
import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Inquiry.CarrierDetail;
import huadi.EleInvAccounts.Inquiry.CarrierHead;
import huadi.EleInvAccounts.Manager.InvoiceListActivity;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import huadi.EleInvAccounts.Social.SocialActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//記帳
public class AccountsActivity extends Activity
{	
	String appID, UUID, cardNo, cardEncrypt;
	final int btnMovePosi = 5; //按鈕位移量
	final int btnMoveNega = -5; //按鈕位移量
	
	boolean isCapture = false;
	String mInvNum = "";
	int detailIndex = 0;
	
	boolean isCarrier = false;
	String[] mInvNumArray = new String[]{};
	int invIndex = 0;
	
	
	SQLiteDatabase db = null;

	int year, month;
	String invPeriod; //對獎發票期別(yyyMM)
	
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting, btn_left, btn_right;
	Button btn_addone, btn_scan, btn_import, btn_income, btn_expend, btn_save, btn_delete, btn_cancel, btn_bg;
	TextView text_total, text_income, text_expenditure, text_balance, text_month;
	TableLayout table;
	LinearLayout popview;
	EditText editText1, editText3, editText5, editText6, editText7, editText14;
	Spinner spinner1, spinner2, spinner3;
	
	boolean isIncome = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		
		Intent intent = getIntent(); 
		isCapture = intent.getBooleanExtra("isCapture", false);
		mInvNum = intent.getStringExtra("invNum");
		
		SharedPreferences ids = getSharedPreferences("IDs", MODE_PRIVATE ); //偏好設定
		appID = ids.getString("appID", "");
		UUID = ids.getString("UUID", "");
		
		SharedPreferences card = getSharedPreferences("CARD", MODE_PRIVATE ); //偏好設定 
		cardNo = card.getString("cardNo", "");
		cardEncrypt = card.getString("cardEncrypt", "");
		
		//Log.e("isCapture", isCapture + ", " +invNum);
		
		DBHelper dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();

		setUI();
	}

	private void setUI()
	{	
		btn_backfunc = (ImageButton) findViewById(R.id.imageButton1);
		btn_account = (ImageButton) findViewById(R.id.imageButton2);
		btn_manager = (ImageButton) findViewById(R.id.imageButton3);
		btn_social = (ImageButton) findViewById(R.id.imageButton4);
		btn_setting = (ImageButton) findViewById(R.id.imageButton5);
		btn_addone = (Button) findViewById(R.id.button1);
		btn_scan = (Button) findViewById(R.id.button2);
		btn_import = (Button) findViewById(R.id.button3);
		btn_income = (Button) findViewById(R.id.button4);
		btn_expend = (Button) findViewById(R.id.button5);
		btn_save = (Button) findViewById(R.id.button6);
		btn_delete = (Button) findViewById(R.id.button9);
		btn_cancel = (Button) findViewById(R.id.button7);
		text_total = (TextView) findViewById(R.id.textView6);
		text_income = (TextView) findViewById(R.id.textView7);
		text_expenditure = (TextView) findViewById(R.id.textView8);
		text_balance = (TextView) findViewById(R.id.textView9);
		table = (TableLayout) findViewById(R.id.TableLayout1);
		popview = (LinearLayout) findViewById(R.id.LinearLayout1);
		btn_bg = (Button)findViewById(R.id.button8);
		btn_left = (ImageButton) findViewById(R.id.imageButton6); 
		btn_right = (ImageButton) findViewById(R.id.imageButton97); 
		text_month = (TextView) findViewById(R.id.textView77);
		

		//月份選擇----------------------------------------------------
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR) - 1911; //民國
		month = calendar.get(Calendar.MONTH) +1; //Calendar.MONTH 從0開始...
		invPeriod = String.format("%d 年 %02d 月", year, month);
		
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
					
					month -= 1;
					if(month == 0 && year != 0)
					{
						year--;
						month = 12;
					}
					invPeriod = String.format("%d 年 %02d 月", year, month);
					text_month.setText(invPeriod); //月份
					GetChargeList(year+1911, month);
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
					
					month += 1;
					if(month == 14 && year != 0)
					{
						year++;
						month = 2;
					}
					invPeriod = String.format("%d 年 %02d 月", year, month);
					text_month.setText(invPeriod); //月份				
					GetChargeList(year+1911, month);
				}				
				return false;
				}});
		
		//月份選擇----------------------------------------------------

		btn_addone.setOnClickListener(new OnClickListener() //手動記帳
		{
			@Override
			public void onClick(View v)
			{
				isCapture = false;
				popview.setVisibility(View.VISIBLE);
				btn_bg.setVisibility(View.VISIBLE);
				btn_delete.setVisibility(View.GONE);
			}
		});
		btn_scan.setOnClickListener(new OnClickListener() //條碼掃描
		{
			@Override
			public void onClick(View v)
			{
				if(IsInternet())
				{
					isCapture = true;
					Intent intent = new Intent(AccountsActivity.this, CaptureActivity.class);
					startActivity(intent);
				}
				else
				{
					new AlertDialog.Builder(AccountsActivity.this)
					.setTitle("請開啟網路連線功能")
					.setMessage("取得發票資訊需要網路連線")
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
		});
		
//		if(cardNo.length() > 0 && cardEncrypt.length() > 0) //外觀看不出差異...
//			btn_import.setEnabled(true);
//		else
//			btn_import.setEnabled(false);
			
		btn_import.setOnClickListener(new OnClickListener() //TODO 載具匯入
		{
			@Override
			public void onClick(View v)
			{
				if(IsInternet())
				{
					if(cardNo.length() > 0 && cardEncrypt.length() > 0)
					{
						isCarrier = true;
						invIndex = 0;
						detailIndex = 0;
						try
						{
							Map<String, List<String>> head = new CarrierHead().execute("3J0002", cardNo, "N", UUID, appID, cardEncrypt).get();
							mInvNumArray = new String[head.get("invNum").size()];
							
							for (int i = 0; i < head.get("invNum").size(); i++)
							{
								mInvNumArray[i] = head.get("invNum").get(i);
								new CarrierDetail(AccountsActivity.this)
								.execute("3J0002", cardNo, mInvNumArray[i], head.get("invDate").get(i), UUID, appID, cardEncrypt);
								
								//Log.e("ee", cardNo + "," + head.get("invNum").get(i)+ "," + head.get("invDate").get(i)+ "," +UUID+ "," +appID+ "," +cardEncrypt);
							}
							setUI(); //更新記帳列表
							CarrierPopView(invIndex, detailIndex);
						}
						catch (Exception e)
						{
							Toast.makeText(AccountsActivity.this, "資料擷取失敗, \n請檢查網路狀態", Toast.LENGTH_LONG).show();
						}
					}
					else
						new AlertDialog.Builder(AccountsActivity.this)
						.setTitle("需要綁定手機條碼")
						.setMessage("前往綁定手機條碼?")
						.setPositiveButton("確定", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								Intent intent = new Intent(AccountsActivity.this, SettingsActivity.class);
								startActivity(intent);
								AccountsActivity.this.finish();
							}									
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener()	{
							@Override
							public void onClick(DialogInterface dialog, int which){														
							}
						}).show();

				}
				else
				{
					new AlertDialog.Builder(AccountsActivity.this)
					.setTitle("請開啟網路連線功能")
					.setMessage("取得發票資訊需要網路連線")
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
		});

		//popview ---------------------------------
		
		editText1 = (EditText)findViewById(R.id.editText1); //金額
		spinner1 = (Spinner)findViewById(R.id.spinner1); //帳本
		editText14 = (EditText)findViewById(R.id.editText14); //發票編號
		editText3 = (EditText)findViewById(R.id.editText3); //項目
		spinner2 = (Spinner)findViewById(R.id.spinner2); //分類
		spinner3 = (Spinner)findViewById(R.id.spinner3); //分類2
		editText5 = (EditText)findViewById(R.id.editText5); //日期
		editText6 = (EditText)findViewById(R.id.editText6); //商店
		editText7 = (EditText)findViewById(R.id.editText7); //備註
		
		//預設支出
		isIncome = false;
		btn_income.getBackground().setAlpha(60);
		btn_expend.getBackground().setAlpha(255);
		
		
		if(isCapture)
		{
			detailIndex = 0;
			if(mInvNum.length() > 0)
				CapturePopView(detailIndex);
			else
				Toast.makeText(this, "資料擷取失敗, \n請檢查網路狀態", Toast.LENGTH_LONG).show();
		}
		
		//Account
		Cursor accountCursor = db.rawQuery("SELECT * "
			+ "FROM Account ", null); //要記得''包起來
		
		int accountCount = accountCursor.getCount(); //資料筆數
		String[] account = new String[accountCount];
		
		if(accountCount != 0)
		{
			accountCursor.moveToFirst();
			for (int i = 0; i < accountCount; i++)
			{
				account[i] = accountCursor.getString(accountCursor.getColumnIndex("account_name"));
				accountCursor.moveToNext(); //移至資料庫下一筆
			}
		}
		else 
		{
			ContentValues accountCV = new ContentValues();
			accountCV.put("account_name", "wallet01");
			accountCV.put("money", "1000");
			db.insert("Account", null, accountCV);
			account = new String[]{"wallet01"};
		}
		accountCursor.close();
		
		//建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AccountsActivity.this, 
			android.R.layout.simple_spinner_item, account);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定下拉選單的樣式
		spinner1.setAdapter(adapter1); //mainCategory
		
		//mainCategory
		Cursor mainCateCursor = db.rawQuery("SELECT main "
			+ "FROM MainCategory ", null); //要記得''包起來
		
		int mainCateCount = mainCateCursor.getCount(); //資料筆數
		String[] mainCategory = new String[mainCateCount];
		
		if(mainCateCount != 0)
		{
			mainCateCursor.moveToFirst();
			for (int i = 0; i < mainCateCount; i++)
			{
				mainCategory[i] = mainCateCursor.getString(mainCateCursor.getColumnIndex("main"));
				mainCateCursor.moveToNext(); //移至資料庫下一筆
			}
		}
		else 
		{
			ContentValues mainCateCV = new ContentValues();
			mainCateCV.put("main", "其他");
			db.insert("MainCategory", null, mainCateCV);
			
			ContentValues subCateCV = new ContentValues();
			subCateCV.put("main", "其他");
			subCateCV.put("sub", "其他");
			db.insert("SubCategory", null, subCateCV);

			mainCategory = new String[]{"其他", "其他"};
		}
		mainCateCursor.close();
		
		//建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AccountsActivity.this, 
			android.R.layout.simple_spinner_item, mainCategory);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定下拉選單的樣式
		spinner2.setAdapter(adapter2); //mainCategory
		
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() //副分類會跟著主分類選啥
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//SubCategory
				Cursor subCursor = db.rawQuery("SELECT sub "
					+ "FROM SubCategory "
					+ "WHERE main = '" + spinner2.getSelectedItem().toString() + "'", null); //要記得''包起來
				
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
				else 
				{
					ContentValues subCateCV = new ContentValues();
					subCateCV.put("main", spinner2.getSelectedItem().toString());
					subCateCV.put("sub", "其他");
					db.insert("SubCategory", null, subCateCV);
					subCategory = new String[]{"其他"};
				}
				subCursor.close();
				
				ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(AccountsActivity.this, 
					android.R.layout.simple_spinner_item, subCategory);
				adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //設定下拉選單的樣式
				spinner3.setAdapter(adapter3); //SubCategory
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});	
		
		btn_income.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btn_income.getBackground().setAlpha(255);
				btn_expend.getBackground().setAlpha(60);
				isIncome = true;
			}
		});
		btn_expend.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btn_expend.getBackground().setAlpha(255);
				btn_income.getBackground().setAlpha(60);
				isIncome = false;
			}
		});
		
		btn_save.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event){
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					btn_save.setBackgroundColor(Color.rgb(46, 147, 186));
				}
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					btn_save.setBackgroundColor(Color.rgb(50, 179, 226));
					
					if(editText5.getText().toString().length() > 0 && editText1.getText().toString().length() > 0 && editText3.getText().toString().length() > 0 )
					{
						if(editText14.getText().toString().length() > 0) //發票
						{
							Pattern p = Pattern.compile("^[a-zA-Z]{2}[0-9]{8}"); //驗證發票 2英文+8數字
							Matcher m = p.matcher(editText14.getText().toString());
							if(m.find())
							{
								String date = editText5.getText().toString(); // TODO 要規定為yyyyMMdd
				//				String accountName = editText2.getText().toString();
								String accountName = spinner1.getSelectedItem().toString();				
								int money = Integer.parseInt(editText1.getText().toString());
				//					String mainCategory = editText4.getText().toString();
				//					String subCategory = editText4.getText().toString();
								String mainCategory = spinner2.getSelectedItem().toString();
								String subCategory = spinner3.getSelectedItem().toString();
								String item = editText3.getText().toString();
								String sellerName = editText6.getText().toString();
								String invNum = editText14.getText().toString().toUpperCase(); //發票號碼轉大寫
								String remark = editText7.getText().toString();
								
								if(isIncome) //收入為正
									money = money > 0 ? money : -money;
								else
									money = money > 0 ? -money : money;
								
								ContentValues accountCV = new ContentValues();
								accountCV.put("date", date); //日期(yyyyMMdd)
								accountCV.put("accountName", accountName); //記帳帳本
								accountCV.put("money", money); //項目所花的金額
								accountCV.put("mainCategory", mainCategory); //
								accountCV.put("subCategory", subCategory); //
								accountCV.put("item", item); //項目
								accountCV.put("store", sellerName); //商店名稱
								accountCV.put("invNum", invNum); //發票編號
								accountCV.put("remark", remark); //備註
								
								Cursor accountCursor = db.rawQuery("SELECT invNum "
									+ "FROM Charge "
									+ "WHERE item = '" + item + "' "
									+ "AND invNum = '" + invNum + "' ", null); //要記得''包起來
								
								int count = accountCursor.getCount(); //資料筆數
								if(count == 0)
									db.insert("Charge", null, accountCV); //新增一筆至 Invoice
								else
								{
									accountCursor.moveToFirst();
									for (int i = 0; i < count; i++)
									{
										db.update("Charge", accountCV, "invNum = '" + invNum + "'" + "AND item = '" + item + "' ", null);
										accountCursor.moveToNext(); //移至資料庫下一筆
									}
								}
								accountCursor.close();
								
								if(!isCapture && !isCarrier) //手動發票記帳(傳統發票), 若為 發票, 就要存到Invoice,InvDetail
								{					
									int month = Integer.parseInt(date.substring(4, 6)) % 2 == 1?Integer.parseInt(date.substring(4, 6)) + 1 
										: Integer.parseInt(date.substring(4, 6)); //雙數月
									String invPeriod = String.format("%d%02d", Integer.parseInt(date.substring(0, 4))-1911, month);//yyyMM
									ContentValues invoiceCV = new ContentValues();
									invoiceCV.put("invNum", invNum); //發票編號
									invoiceCV.put("invTotalCost", -money); //消費金額
									invoiceCV.put("invDate", date); //發票開立日期(yyyyMMdd)
									invoiceCV.put("sellerName", sellerName); //賣方名稱
									invoiceCV.put("invStatus", ""); //發票狀態(已確認)
									invoiceCV.put("invPeriod", invPeriod); //對獎發票期別(民國年月)
									
//									Log.e("invPeriod", invNum + ", " + invPeriod);
									
									Cursor invoiceCursor = db.rawQuery("SELECT invNum "
										+ "FROM Invoice "
										+ "WHERE invNum = '" + invNum + "'", null); //要記得''包起來
									
									int invCount = invoiceCursor.getCount(); //資料筆數
									if(invCount == 0)
										db.insert("Invoice", null, invoiceCV); //新增一筆至 Invoice
									else
									{
										invoiceCursor.moveToFirst();
										for (int i = 0; i < invCount; i++)
										{
											db.update("Invoice", invoiceCV, "invNum = '" + invNum + "'", null);
											invoiceCursor.moveToNext(); //移至資料庫下一筆
										}
									}
									invoiceCursor.close();
									
									ContentValues invDetailCV = new ContentValues();
									invDetailCV.put("invNum", invNum); //發票編號
									invDetailCV.put("rowNum", "1"); //明細編號(1,2,3...)
									invDetailCV.put("description", item); //品名
									invDetailCV.put("quantity", "1"); //數量
									invDetailCV.put("unitPrice", -money); //單價
									invDetailCV.put("amount", -money); //小記
									
									Cursor invDetailCursor = db.rawQuery("SELECT invNum, rowNum "
										+ "FROM InvDetail "
										+ "WHERE invNum = '" + invNum + "' ", null); //要記得''包起來
									
									int invDetailcount = invDetailCursor.getCount(); //資料筆數
									if(invDetailcount == 0)
										db.insert("InvDetail", null, invDetailCV); //新增一筆至 InvDetail
									else
									{
										invDetailCursor.moveToFirst();
										for (int j = 0; j < invDetailcount; j++)
										{								
											db.update("InvDetail", invDetailCV, "invNum = '" + invNum + "'", null);
											invDetailCursor.moveToNext(); //移至資料庫下一筆
										}
									}
									invDetailCursor.close();
								}
				
								InitPopView();
								
								if(isCapture) //發票掃完會逐一跳出
								{
									detailIndex++;
									CapturePopView(detailIndex);
									//Log.e("detailIndex", "" + detailIndex);
								}
								if(isCarrier) //發票掃完會逐一跳出
								{
									detailIndex++;
									CarrierPopView(invIndex, detailIndex);
									//Log.e("detailIndex", "" + detailIndex);
								}
							}
							else
								Toast.makeText(AccountsActivity.this, "發票號碼錯誤", Toast.LENGTH_SHORT).show();
						}
						else  //不為發票
						{
							String date = editText5.getText().toString(); // TODO 要規定為yyyyMMdd
							String accountName = spinner1.getSelectedItem().toString();				
							int money = Integer.parseInt(editText1.getText().toString());
							String mainCategory = spinner2.getSelectedItem().toString();
							String subCategory = spinner3.getSelectedItem().toString();
							String item = editText3.getText().toString();
							String sellerName = editText6.getText().toString();
							String invNum = ""; //發票號碼轉大寫
							String remark = editText7.getText().toString();
							
							if(isIncome) //收入為正
								money = money > 0 ? money : -money;
							else
								money = money > 0 ? -money : money;
							
							ContentValues accountCV = new ContentValues();
							accountCV.put("date", date); //日期(yyyyMMdd)
							accountCV.put("accountName", accountName); //記帳帳本
							accountCV.put("money", money); //項目所花的金額
							accountCV.put("mainCategory", mainCategory); //
							accountCV.put("subCategory", subCategory); //
							accountCV.put("item", item); //項目
							accountCV.put("store", sellerName); //商店名稱
							accountCV.put("invNum", invNum); //發票編號
							accountCV.put("remark", remark); //備註
							
							Cursor accountCursor = db.rawQuery("SELECT invNum "
								+ "FROM Charge "
								+ "WHERE item = '" + item + "' "
								+ "AND invNum = '" + invNum + "' ", null); //要記得''包起來
							
							int count = accountCursor.getCount(); //資料筆數
							if(count == 0)
								db.insert("Charge", null, accountCV); //新增一筆至 Invoice
							else
							{
								accountCursor.moveToFirst();
								for (int i = 0; i < count; i++)
								{
									db.update("Charge", accountCV, "invNum = '" + invNum + "'" + "AND item = '" + item + "' ", null);
									accountCursor.moveToNext(); //移至資料庫下一筆
								}
							}
							accountCursor.close();
							InitPopView();
						}
					}
					else if(editText1.getText().toString().length() < 1 )
						Toast.makeText(AccountsActivity.this, "金額 不得為空", Toast.LENGTH_SHORT).show();
					else if(editText5.getText().toString().length() < 1 )
						Toast.makeText(AccountsActivity.this, "日期 不得為空", Toast.LENGTH_SHORT).show();				
					else if(editText3.getText().toString().length() < 1 )
						Toast.makeText(AccountsActivity.this, "項目 不得為空", Toast.LENGTH_SHORT).show();
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

					if(isCapture) //發票掃完會逐一跳出
					{
						detailIndex++;
						CapturePopView(detailIndex);
						//Log.e("detailIndex", "" + detailIndex);
					}
					else if(isCarrier)
					{
						detailIndex++;
						CarrierPopView(invIndex, detailIndex);
					}
					else
					{
						//掃完qrCode時, 會先進db
						//db.delete("Invoice", "invNum = '" + invNum + "'", null);
						//db.delete("InvDetail", "invNum = '" + invNum + "'", null);
						
						InitPopView();
					}
				}				
				return false;
				}});
		
		//popview ---------------------------------

		//Side menu -----------------------------------------------
		btn_backfunc.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AccountsActivity.this, MainActivity.class);
				startActivity(intent);
				db.close();
				AccountsActivity.this.finish();
			}
		});
		btn_manager.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AccountsActivity.this, ManagerActivity.class);
				startActivity(intent);
				db.close();
				AccountsActivity.this.finish();
			}
		});
		btn_social.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AccountsActivity.this, SocialActivity.class);
				startActivity(intent);
				db.close();
				AccountsActivity.this.finish();
			}
		});
		btn_setting.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(AccountsActivity.this, SettingsActivity.class);
				startActivity(intent);
				db.close();
				AccountsActivity.this.finish();
			}
		});
		//Side menu -----------------------------------------------		

//		Calendar calendar = Calendar.getInstance();
		int _year = calendar.get(Calendar.YEAR); //民國
		int _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始
		GetChargeList(_year, _month);
	}

	public void GetChargeList(final int _year, final int _month) //一進去的列表
	{
//		Calendar calendar = Calendar.getInstance();
//		int _year = calendar.get(Calendar.YEAR); //民國
//		int _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始
		Cursor invListCursor = db.rawQuery("SELECT money "
			+ "FROM Charge "
			+ "WHERE date >= " + String.format("'%d%02d00' ", _year, _month)
			+ "AND date <= " + String.format("'%d%02d31' ", _year, _month), null);
		int costCount = invListCursor.getCount(); //資料筆數
		int income = 0, expend = 0, balance = 0;
		if(costCount != 0)
		{
			invListCursor.moveToFirst(); //移至資料庫第一筆
			for (int i = 0; i < costCount; i++)
			{
				int money = invListCursor.getInt(invListCursor.getColumnIndex("money"));
				balance += money;
				
				if(money < 0)
					expend += money;
				else 
					income += money;
				
				invListCursor.moveToNext(); //移至資料庫下一筆
			}
		}
		invListCursor.close();
		
		text_total.setText("總資產");
		text_income.setText("本月收入 " + income);
		text_expenditure.setText("本月支出 " + -expend);
		text_balance.setText("本月結餘 " + balance);

		TableRow tr = new TableRow(this);
		LinearLayout l1, l2, l3, l4;
		TextView[] month, day, item, main, sub, cost, account;
		
		final Cursor ChargeListCursor = db.rawQuery("SELECT date, accountName, money, mainCategory, subCategory, item, store, invNum, remark "
			+ "FROM Charge "
			+ "WHERE date >= " + String.format("'%d%02d00' ", _year, _month)
			+ "AND date <= " + String.format("'%d%02d31' ", _year, _month)
			+ "ORDER BY date ASC", null);
		
		int ChargeListCount = ChargeListCursor.getCount(); //資料筆數
		month = new TextView[ChargeListCount];
		day = new TextView[ChargeListCount];
		item = new TextView[ChargeListCount];
		main = new TextView[ChargeListCount];
		sub = new TextView[ChargeListCount];
		cost = new TextView[ChargeListCount];
		account = new TextView[ChargeListCount];

		table.removeAllViews();

		if(ChargeListCount != 0)
		{
			ChargeListCursor.moveToFirst(); //移至資料庫第一筆
			for (int i = 0; i < ChargeListCount; i++)
			{
				final String date = ChargeListCursor.getString(ChargeListCursor.getColumnIndex("date"));
				final String accountName = ChargeListCursor.getString(ChargeListCursor.getColumnIndex("accountName"));
				final String money = ChargeListCursor.getString(ChargeListCursor.getColumnIndex("money"));
				final String mainCategory = ChargeListCursor.getString(ChargeListCursor.getColumnIndex("mainCategory"));
				final String subCategory = ChargeListCursor.getString(ChargeListCursor.getColumnIndex("subCategory"));
				final String itemName = ChargeListCursor.getString(ChargeListCursor.getColumnIndex("item"));
				final String store = ChargeListCursor.getString(ChargeListCursor.getColumnIndex("store"));
				final String invNum = ChargeListCursor.getString(ChargeListCursor.getColumnIndex("invNum"));
				final String remark = ChargeListCursor.getString(ChargeListCursor.getColumnIndex("remark"));
				
				l1 = new LinearLayout(this);
				l1.setOrientation(LinearLayout.HORIZONTAL);
				if(i%2==0){
					l1.setBackgroundColor(Color.rgb(255, 254, 232));
				}
				l1.setOnClickListener(new OnClickListener(){	//POPVIEW 資料綁定
					@Override
					public void onClick(View arg0) {
						popview.setVisibility(View.VISIBLE);
						btn_bg.setVisibility(View.VISIBLE);
						btn_delete.setVisibility(View.VISIBLE);
						
						editText1.setText(money); //金額
						
						ArrayAdapter<String> accountAdap = (ArrayAdapter<String>) spinner1.getAdapter(); //帳本
						spinner1.setSelection(accountAdap.getPosition(accountName), true);

						editText14.setText(invNum); //發票編號
						editText3.setText(itemName); //項目
						
						ArrayAdapter<String> mainCategoryAdap = (ArrayAdapter<String>) spinner2.getAdapter(); //分類
						spinner2.setSelection(mainCategoryAdap.getPosition(mainCategory), true);
						
						if(spinner2.isSelected())
						{
							ArrayAdapter<String> subCategoryAdap = (ArrayAdapter<String>) spinner3.getAdapter(); //分類
							spinner3.setSelection(subCategoryAdap.getPosition(subCategory), true);
						}

						editText5.setText(date); //日期
						editText6.setText(store); //商店
						editText7.setText(remark); //備註
						
						btn_save.setText("修改");
						
						btn_save.setOnTouchListener(new OnTouchListener(){
							@Override
							public boolean onTouch(View v, MotionEvent event){
								if(event.getAction() == MotionEvent.ACTION_DOWN)
								{
									btn_save.setBackgroundColor(Color.rgb(46, 147, 186));
								}
								if(event.getAction() == MotionEvent.ACTION_UP)
								{
									btn_save.setBackgroundColor(Color.rgb(50, 179, 226));

									if(editText5.getText().toString().length() > 0 && editText1.getText().toString().length() > 0
										&& editText3.getText().toString().length() > 0 )
									{
										if(editText14.getText().toString().length() > 0) //當為發票時
										{
											Pattern p = Pattern.compile("^[a-zA-Z]{2}[0-9]{8}"); //驗證發票 2英文+8數字
											Matcher m = p.matcher(editText14.getText().toString());
											if(m.find())
											{
												String date = editText5.getText().toString(); // TODO 要規定為yyyyMMdd
												String accountName = spinner1.getSelectedItem().toString();				
												int money = Integer.parseInt(editText1.getText().toString());

												String mainCategory = spinner2.getSelectedItem().toString();
												String subCategory = spinner3.getSelectedItem().toString();
												String item = editText3.getText().toString();
												String sellerName = editText6.getText().toString();
												String invNum = editText14.getText().toString().toUpperCase(); //發票號碼轉大寫
												String remark = editText7.getText().toString();
												
												if(isIncome) //收入為正
													money = money > 0 ? money : -money;
												else
													money = money > 0 ? -money : money;
												
												ContentValues accountCV = new ContentValues();
												accountCV.put("date", date); //日期(yyyyMMdd)
												accountCV.put("accountName", accountName); //記帳帳本
												accountCV.put("money", money); //項目所花的金額
												accountCV.put("mainCategory", mainCategory); //
												accountCV.put("subCategory", subCategory); //
												accountCV.put("item", item); //項目
												accountCV.put("store", sellerName); //商店名稱
												accountCV.put("invNum", invNum); //發票編號
												accountCV.put("remark", remark); //備註
												
												db.update("Charge", accountCV, "item = '" + item + "' "
													+ "AND invNum = '" + invNum + "' ", null);
												
												InitPopView();
												GetChargeList(_year, _month);
												btn_save.setText("新增");
											}
											else
												Toast.makeText(AccountsActivity.this, "發票號碼錯誤", Toast.LENGTH_SHORT).show();
										}
										else //不為發票
										{
											String date = editText5.getText().toString(); // TODO 要規定為yyyyMMdd
											String accountName = spinner1.getSelectedItem().toString();				
											int money = Integer.parseInt(editText1.getText().toString());

											String mainCategory = spinner2.getSelectedItem().toString();
											String subCategory = spinner3.getSelectedItem().toString();
											String item = editText3.getText().toString();
											String sellerName = editText6.getText().toString();
											String invNum = ""; //發票號碼轉大寫
											String remark = editText7.getText().toString();
											
											if(isIncome) //收入為正
												money = money > 0 ? money : -money;
											else
												money = money > 0 ? -money : money;
											
											ContentValues accountCV = new ContentValues();
											accountCV.put("date", date); //日期(yyyyMMdd)
											accountCV.put("accountName", accountName); //記帳帳本
											accountCV.put("money", money); //項目所花的金額
											accountCV.put("mainCategory", mainCategory); //
											accountCV.put("subCategory", subCategory); //
											accountCV.put("item", item); //項目
											accountCV.put("store", sellerName); //商店名稱
											accountCV.put("invNum", invNum); //發票編號
											accountCV.put("remark", remark); //備註
											
											db.update("Charge", accountCV, "item = '" + item + "' "
												+ "AND invNum = '" + invNum + "' ", null);	
											
											InitPopView();
											GetChargeList(_year, _month);
											btn_save.setText("新增");
										}
									}
									else if(editText1.getText().toString().length() < 1 )
										Toast.makeText(AccountsActivity.this, "金額 不得為空", Toast.LENGTH_SHORT).show();
									else if(editText5.getText().toString().length() < 1 )
										Toast.makeText(AccountsActivity.this, "日期 不得為空", Toast.LENGTH_SHORT).show();				
									else if(editText3.getText().toString().length() < 1 )
										Toast.makeText(AccountsActivity.this, "項目 不得為空", Toast.LENGTH_SHORT).show();
									
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


									db.delete("Charge", "item = '" + itemName + "' "
										+ "AND invNum = '" + invNum + "' "
										+ "AND remark = '" + remark + "' "
										+ "AND date = '" + date + "' ", null);
									InitPopView();
									GetChargeList(_year, _month);
									btn_save.setText("新增");
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

									InitPopView();
									GetChargeList(_year, _month);
									btn_save.setText("新增");
								}				
								return false;
								}});
					}});
				month[i] = new TextView(this);
				month[i].setText(date.substring(4,6));
				month[i].setTextSize(20);
				month[i].setPadding(30, 0, 0, 0);
				TextPaint tp = month[i].getPaint();
				tp.setFakeBoldText(true);
				l1.addView(month[i]);
				day[i] = new TextView(this);
				day[i].setText(" / " + date.substring(6,8));
				day[i].setPadding(0, 0, 20, 0);
				l1.addView(day[i]);
				l2 = new LinearLayout(this);
				l2.setOrientation(LinearLayout.VERTICAL);
				item[i] = new TextView(this);
				item[i].setText(itemName);
				item[i].setTypeface(null, Typeface.BOLD);
				DisplayMetrics dm = new DisplayMetrics();	// 建立一個DisplayMetrics物件
				this.getWindowManager().getDefaultDisplay().getMetrics(dm);	// 取得裝置的資訊
				int Width = dm.widthPixels;
				int Height = dm.heightPixels;
				item[i].setMinimumWidth(Width/3);
//				item[i].setMinimumWidth(300);
				item[i].setMaxEms(7);
				item[i].setEllipsize(TruncateAt.END);
				item[i].setLines(1);
				l2.addView(item[i]);
				l3 = new LinearLayout(this);
				l3.setOrientation(LinearLayout.HORIZONTAL);
				main[i] = new TextView(this);
				main[i].setText(mainCategory + " - ");
				l3.addView(main[i]);
				sub[i] = new TextView(this);
				sub[i].setText(subCategory);
				sub[i].setPadding(0, 0, 40, 0);
				l3.addView(sub[i]);
				l2.addView(l3);
				l1.addView(l2);
				l4 = new LinearLayout(this);
				l4.setOrientation(LinearLayout.VERTICAL);
				cost[i] = new TextView(this);
				cost[i].setText(money + "NTD");
				cost[i].setPadding(0, 0, 40, 0);
				if((cost[i].getText().toString()).contains("-")){
					cost[i].setTextColor(Color.RED);
				}else{
					cost[i].setTextColor(Color.GREEN);
				}
				l4.addView(cost[i]);
				account[i] = new TextView(this);
				account[i].setText(accountName);
				l4.addView(account[i]);
				l1.addView(l4);
				tr.addView(l1);
				table.addView(tr);
				tr = new TableRow(this);				
				
				ChargeListCursor.moveToNext(); //移至資料庫下一筆
			}
		}
		ChargeListCursor.close();
	}
	
	private void InitPopView()
	{
		//InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)); //隱藏 keyboard
		//imm.hideSoftInputFromWindow(AccountsActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		popview.setVisibility(View.GONE);
		btn_bg.setVisibility(View.GONE);
		btn_delete.setVisibility(View.GONE);
		editText1.setText("");
//		editText2.setText("");
		editText14.setText("");
		editText3.setText("");
//		editText4.setText("");
		editText5.setText("");
		editText6.setText("");
		editText7.setText("");
		
		Calendar calendar = Calendar.getInstance();
		int _year = calendar.get(Calendar.YEAR); //民國
		int _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH 從0開始
		GetChargeList(_year, _month);
	}
	
	private void CapturePopView(int _detailIndex)
	{
		Cursor invDetailCursor = db.rawQuery("SELECT invNum, description, amount "
			+ "FROM InvDetail "
			+ "WHERE invNum = '" + mInvNum + "' ", null); //要記得''包起來
		
		int count = invDetailCursor.getCount(); //資料筆數
		
		if(detailIndex + 1 > count)
			isCapture = false;
		
		if(isCapture) //若是掃QR code
		{
			//發票應該都是支出
			isIncome = false;
			btn_expend.getBackground().setAlpha(255);
			btn_income.getBackground().setAlpha(60);			
			
			btn_delete.setVisibility(View.GONE);
			
			//Log.e("count",""+_count);
			if(count != 0)
			{
				popview.setVisibility(View.VISIBLE);
				btn_bg.setVisibility(View.VISIBLE);				

				invDetailCursor.moveToPosition(_detailIndex);
				int amount = invDetailCursor.getInt(invDetailCursor.getColumnIndex("amount"));
				if(amount < 0) //若(發票)小計<0, 支出就變收入
				{
					amount = -amount;
					isIncome = true;
					btn_expend.getBackground().setAlpha(60);
					btn_income.getBackground().setAlpha(255);
				} 
				editText1.setText("" + amount);
				editText14.setText(invDetailCursor.getString(invDetailCursor.getColumnIndex("invNum")));
				editText3.setText(invDetailCursor.getString(invDetailCursor.getColumnIndex("description")));
				//invDetailCursor.moveToNext();				
			}
			
			Cursor invCursor = db.rawQuery("SELECT invDate, sellerName "
				+ "FROM Invoice "
				+ "WHERE invNum = '" + mInvNum + "' ", null); //要記得''包起來
			
			int count2 = invCursor.getCount(); //資料筆數
			//Log.e("count2",""+count2);
			if(count2 != 0)
			{
				invCursor.moveToFirst();
				for (int i = 0; i < count2; i++)
				{			
					editText5.setText(invCursor.getString(invCursor.getColumnIndex("invDate")));
					editText6.setText(invCursor.getString(invCursor.getColumnIndex("sellerName")));
					invCursor.moveToNext();
				}
			}			
			
		}
		invDetailCursor.close();
	}
	
	private void CarrierPopView(int _invIndex, int _detailIndex)
	{
		Cursor invDetailCursor = db.rawQuery("SELECT invNum, description, amount "
			+ "FROM InvDetail "
			+ "WHERE invNum = '" + mInvNumArray[_invIndex] + "' ", null); //要記得''包起來
		
		int count = invDetailCursor.getCount(); //資料筆數			
		
		if(isCarrier) //若是掃QR code
		{
			//發票應該都是支出
			isIncome = false;
			btn_expend.getBackground().setAlpha(255);
			btn_income.getBackground().setAlpha(60);			
			
			btn_delete.setVisibility(View.GONE);
			
			if(detailIndex + 1 > count)
				invIndex++;
			if(invIndex + 1 > mInvNumArray.length)
				isCarrier = false;
			
//			Log.e("count",""+count);
			if(count != 0)
			{
				popview.setVisibility(View.VISIBLE);
				btn_bg.setVisibility(View.VISIBLE);				

				invDetailCursor.moveToPosition(_detailIndex);
				int amount = invDetailCursor.getInt(invDetailCursor.getColumnIndex("amount"));
				if(amount < 0) //若(發票)小計<0, 支出就變收入
				{
					amount = -amount;
					isIncome = true;
					btn_expend.getBackground().setAlpha(60);
					btn_income.getBackground().setAlpha(255);
				} 
				editText1.setText("" + amount);
				editText14.setText(invDetailCursor.getString(invDetailCursor.getColumnIndex("invNum")));
				editText3.setText(invDetailCursor.getString(invDetailCursor.getColumnIndex("description")));
				//invDetailCursor.moveToNext();				
			}
			
			Cursor invCursor = db.rawQuery("SELECT invDate, sellerName "
				+ "FROM Invoice "
				+ "WHERE invNum = '" + mInvNumArray[_invIndex] + "' ", null); //要記得''包起來
			
			int count2 = invCursor.getCount(); //資料筆數
			//Log.e("count2",""+count2);
			if(count2 != 0)
			{
				invCursor.moveToFirst();
				for (int i = 0; i < count2; i++)
				{			
					editText5.setText(invCursor.getString(invCursor.getColumnIndex("invDate")));
					editText6.setText(invCursor.getString(invCursor.getColumnIndex("sellerName")));
					invCursor.moveToNext();
				}
			}
			
		}
		invDetailCursor.close();
		
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
