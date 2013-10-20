package huadi.EleInvAccounts.Social;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import huadi.EleInvAccounts.MainActivity;
import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Accounts.AccountsActivity;
import huadi.EleInvAccounts.Manager.ManagerActivity;
import huadi.EleInvAccounts.Settings.SettingsActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import android.widget.Toast;

//成就
public class SocialActivity extends Activity
{
	//UI宣告
	ImageButton btn_backfunc, btn_account, btn_manager, btn_social, btn_setting;
	TableLayout table;
	Button btn_personal, btn_facebook;
	private ProgressDialog mProgressDialog;
	String json;
	int count=0;
	String data[][] = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social);
		
		SharedPreferences fb = getSharedPreferences("FaceBook", MODE_PRIVATE ); //偏好設定 
    	final String access_token = fb.getString("access_token", null); 
    	final Long expires = fb.getLong("access_expires", -1);
		
    	if(access_token != null && expires != -1)
    	{
    		//更新後台資料
            Utility.getJson(SocialActivity.this, "update");
    		setUI();
    	}
    	else
    	{
    		Builder MyAlertDialog = new AlertDialog.Builder(this);
    		MyAlertDialog.setTitle("尚未登入Facebook");
    		MyAlertDialog.setMessage("請登入Facebook以便啟用本功能!");
    		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener()
    		{
    			public void onClick(DialogInterface dialog, int which) {
    				Intent intent = new Intent(SocialActivity.this, SettingsActivity.class);
    				startActivity(intent);
    				SocialActivity.this.finish();
    				}
    		};
    		MyAlertDialog.setNeutralButton("確定",OkClick );
    		MyAlertDialog.show();
    	}
	}

	private void setUI() {
		
		btn_backfunc = (ImageButton)findViewById(R.id.imageButton1);
		btn_account = (ImageButton)findViewById(R.id.imageButton2);
		btn_manager = (ImageButton)findViewById(R.id.imageButton3);
		btn_social = (ImageButton)findViewById(R.id.imageButton4);
		btn_setting = (ImageButton)findViewById(R.id.imageButton5);
		table = (TableLayout)findViewById(R.id.TableLayout1);
		btn_personal = (Button)findViewById(R.id.button1);
		btn_facebook = (Button)findViewById(R.id.button2);

//		btn_personal.getBackground().setAlpha(255);
//		btn_facebook.getBackground().setAlpha(80);
		btn_personal.setVisibility(View.GONE);
		btn_facebook.setVisibility(View.GONE);
		
		//抓取後台資料
		new LoadingDataAsyncTask().execute();
//		String json = Utility.getJson(SocialActivity.this, "friend");
		
		
		btn_backfunc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SocialActivity.this, MainActivity.class);
				startActivity(intent);
				SocialActivity.this.finish();
			}});
		btn_account.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SocialActivity.this, AccountsActivity.class);
				startActivity(intent);
				SocialActivity.this.finish();
			}});
		btn_manager.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SocialActivity.this, ManagerActivity.class);
				startActivity(intent);
				SocialActivity.this.finish();
			}});
		btn_setting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SocialActivity.this, SettingsActivity.class);
				startActivity(intent);
				SocialActivity.this.finish();
			}});
	}
	
	private void setTableLayout()
	{
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int Width = dm.widthPixels;
        int Height = dm.heightPixels;
        
		table.removeAllViews();
		TableRow tr = new TableRow(this);
		LinearLayout container, right_container, last;
		RelativeLayout rl;
		ImageView facebook, crown;
		TextView item, idname, lasttxt;
		
		for(int i=0;i<count;i++){
			container = new LinearLayout(this);
			container.setOrientation(LinearLayout.HORIZONTAL);
			rl = new RelativeLayout(this);
			facebook = new ImageView(this);
			crown = new ImageView(this);

//			facebook.setImageResource(R.drawable.fb_person);
			facebook.setImageBitmap(Utility.getFBpic("https://graph.facebook.com/"+data[i][1]+"/picture?width="+Width/20*3+"&height="+Width/20*3, data[i][1]));
			facebook.setPadding(30, 30, 10, 10);
			crown.setImageResource(R.drawable.crown);
			
			rl.addView(facebook);
			rl.addView(crown);
			container.addView(rl);
			
			right_container = new LinearLayout(this);
			right_container.setOrientation(LinearLayout.VERTICAL);
			right_container.setGravity(Gravity.CENTER);
			item = new TextView(this);
			idname = new TextView(this);
			item.setText("第"+(i+1)+"名");
			item.setMinHeight(50);
			item.setPadding(30, Width/15, 0, 0);
			idname.setText(data[i][2]);
			idname.setMinHeight(50);
			idname.setPadding(30, 0, 0, 0);
			right_container.addView(item);
			right_container.addView(idname);
			container.addView(right_container);
			tr.addView(container);
			table.addView(tr);
			tr = new TableRow(this);
		}
		last = new LinearLayout(this);
		lasttxt = new TextView(this);
		lasttxt.setText("推薦更多朋友一起來用e記帳!");
		lasttxt.setMinimumWidth(Width/10*7);
		lasttxt.setMinHeight(150);
		lasttxt.setGravity(Gravity.CENTER);
		last.addView(lasttxt);
		tr.addView(last);
		table.addView(tr);
		tr = new TableRow(this);
	}
    
	class LoadingDataAsyncTask extends AsyncTask<String, Integer, Integer>{

		@Override
		protected Integer doInBackground(String... param) {
			json = Utility.getJson(SocialActivity.this, "friend");
			JSONArray array;
			try {
				array = new JSONArray(json);
				count = array.length();
				data = new String[count][6];
				for(int i=0; i<array.length(); i++)
				{
					JSONObject jb = array.getJSONObject(i);
					data[i][0] = jb.getString("id");
					data[i][1] = jb.getString("user_id");
					data[i][2] = jb.getString("user_name");
					data[i][3] = jb.getString("join");
					data[i][4] = jb.getString("money");
					data[i][5] = jb.getString("pic");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			setTableLayout();
			mProgressDialog.dismiss();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(SocialActivity.this);
			mProgressDialog.setMessage("資料載入中，請稍後");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

	}
}
