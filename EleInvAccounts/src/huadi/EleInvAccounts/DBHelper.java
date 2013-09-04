package huadi.EleInvAccounts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	private final static int DBVersion = 1; // 版本
	private final static String DBName = "EleInvAccounts.db";
	String[] tableName = new String[]{"Account", "MainCategory", "SubCategory", "Invoice", "Charge"};

	public DBHelper(Context context)
	{
		super(context, DBName, null, DBVersion);// TODO 自動產生的建構子 Stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) //Android 載入時找不到生成的資料庫時觸發
	{		
		String SQL = "";
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[0] //帳戶（錢包、郵局、iCash…）
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "account_name NTEXT" //帳戶名稱
			+ ");";
		db.execSQL(SQL);
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[1]	//主分類（食、衣、住、行、育、樂、收入、其他）
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "main_category NTEXT"
			+ ");";
		db.execSQL(SQL);
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[2]	//次分類
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "sub_category NTEXT"
			+ ");";
		db.execSQL(SQL);
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[3]	//發票
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "invoice_no VARCHAR(10),"	//發票編號
			+ "invoice_month VARCHAR(2),"	//發票月份
			+ "invoice_cost INTEGER"	//消費總金額
			+ ");";
		db.execSQL(SQL);
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[4]	//記帳
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "account_date VARCHAR(10),"	//記帳日期
			+ "account_name NTEXT,"	//記帳帳本
			+ "account_cost INTEGER," //項目所花的金額
			+ "main_category NTEXT,"
			+ "sub_category NTEXT,"
			+ "item NTEXT," //項目
			+ "store NTEXT," //商店名稱
			+ "invoice_no VARCHAR(10)"	//發票編號
			+ ");";
		db.execSQL(SQL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		for (String name : tableName)
		{
			db.execSQL("DROP TABLE IF EXISTS " + name);
		}		
		onCreate(db);
	}

}
