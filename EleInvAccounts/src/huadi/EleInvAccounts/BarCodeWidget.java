package huadi.EleInvAccounts;

import java.io.File;

import zxing.encoding.CodeGenerator;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.widget.RemoteViews;

//桌面小工具
public class BarCodeWidget extends AppWidgetProvider
{
	RemoteViews views;
	public static final String WidgetOpen = "huadi.EleInvAccounts.WidgetOpen";
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		SharedPreferences card = context.getSharedPreferences("CARD", Context.MODE_PRIVATE ); //偏好設定
		
		views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
		views.setTextColor(R.id.weget_textView, Color.BLACK);
		
		if(card.getString("cardNo", "").length() > 0)
			GetBarCode(context, "EleInvAccounts", card.getString("cardNo", ""), 500, 150); //
		else
			views.setTextViewText(R.id.weget_textView, "");
        
	    Intent configIntent = new Intent(context, MainActivity.class);
	    PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
	    views.setOnClickPendingIntent(R.id.widgetLayout, configPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, views);
	}

	public void GetBarCode(Context context, String folderName, String content, int desiredWidth, int desiredHeight)
	{
		String filePath = Environment.getExternalStorageDirectory() + "/" + folderName + "/" + content + ".png";
		File file = new File(filePath);

		if (!file.exists()) //沒有檔案就產生吧
			new CodeGenerator(folderName, content, desiredWidth, desiredHeight);

		try //顯示		
		{
			Bitmap bitmap = BitmapFactory.decodeFile(filePath);
			views.setImageViewBitmap(R.id.weget_imageView, bitmap);
			
			views.setTextViewText(R.id.weget_textView, content);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
