package huadi.EleInvAccounts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	private final static int DBVersion = 1; // 版本
	private final static String DBName = "EleInvAccounts.db";
	String[] tableName = new String[]{"Account", "MainCategory", "SubCategory", "Invoice", "InvDetail", "Charge"};

	public DBHelper(Context context)
	{
		super(context, DBName, null, DBVersion);// TODO 自動產生的建構子 Stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) //Android 載入時找不到生成的資料庫時觸發
	{		
		String SQL = "";
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[0] //Account 帳戶（錢包、郵局、iCash…）
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "account_name NTEXT" //帳戶名稱
			+ ");";
		db.execSQL(SQL);
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[1]	//MainCategory 主分類（食、衣、收入、其他）
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "main_category NTEXT"
			+ ");";
		db.execSQL(SQL);
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[2]	//SubCategory 次分類
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "sub_category NTEXT"
			+ ");";
		db.execSQL(SQL);
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[3]	//Invoice 發票
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "invNum VARCHAR(10),"	//發票編號
			+ "invTotalCost INTEGER,"	//消費金額
			+ "invDate VARCHAR(8),"	//發票開立日期(yyyyMMdd)
			+ "sellerName NTEXT,"	//賣方名稱
			+ "invStatus NTEXT,"	//發票狀態(已確認)
			+ "invPeriod VARCHAR(5)"	//對獎發票期別(民國年月yyyMM)
			+ ");";
		db.execSQL(SQL);
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[4]	//InvDetail 發票明細
			+ "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "invNum VARCHAR(10),"	//發票編號
			+ "rowNum INTEGER,"	//明細編號(1,2,3...)
			+ "description NTEXT,"	//品名
			+ "quantity INTEGER,"	//數量
			+ "unitPrice INTEGER,"	//單價
			+ "amount INTEGER"	//小記
			+ ");";
		db.execSQL(SQL);
		
		SQL = "CREATE TABLE IF NOT EXISTS " + tableName[5]	//Charge 記帳
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
