package huadi.EleInvAccounts.Accounts;

import huadi.EleInvAccounts.R;
import huadi.EleInvAccounts.Inquiry.InvDetails;
import huadi.EleInvAccounts.R.id;
import huadi.EleInvAccounts.R.layout;
import huadi.EleInvAccounts.R.raw;

import java.io.IOException;
import java.util.Vector;

import zxing.camera.CameraManager;
import zxing.decoding.CaptureActivityHandler;
import zxing.decoding.InactivityTimer;
import zxing.view.ViewfinderView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public class CaptureActivity extends Activity implements Callback
{
	String appID, UUID;

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);

		SharedPreferences ids = getSharedPreferences("IDs", MODE_PRIVATE ); //偏好設定
		appID = ids.getString("appID", "");
		UUID = ids.getString("UUID", "");

		// CameraManager
		CameraManager.init(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		txtResult = (TextView) findViewById(R.id.txtResult);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface)
		{
			initCamera(surfaceHolder);
		}
		else
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
		{
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (handler != null)
		{
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy()
	{
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder)
	{
		try
		{
			CameraManager.get().openDriver(surfaceHolder);
		}
		catch (IOException ioe)
		{
			return;
		}
		catch (RuntimeException e)
		{
			return;
		}
		if (handler == null)
		{
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if (!hasSurface)
		{
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView()
	{
		return viewfinderView;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void drawViewfinder()
	{
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode)
	{
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();

		parseInvoice(obj.getBarcodeFormat().toString(), obj.getText()); //將結果分析
		//txtResult.setText(obj.getBarcodeFormat().toString() + ":" + obj.getText()); // 顯示結果
	}

	private void parseInvoice(String type, String content) //分析條碼資訊
	{
		if (type.equals("QR_CODE") && content.matches("^[A-Za-z]{2}[0-9]{8}[0-9]{3}[0-9]{2}[0-9]{2}[0-9]{4}.*"))
		{
			type = "QRCode";

			String invNum = content.substring(0, 10); //發票號碼(含英文)

			int year = Integer.parseInt(content.substring(10, 13)) + 1911; //民國(3)(轉西元)
			String month = content.substring(13, 15);
			String day = content.substring(15, 17);

			String invDate = year + "/" + month + "/" + day;

			String randomCode = content.substring(17, 21); //發票上隨機碼四碼
			int sales = Integer.parseInt(content.substring(21, 29), 16); //未稅之金額8碼(16轉10進位)
			int totlal = Integer.parseInt(content.substring(29, 37), 16); //含稅之金額8碼(16轉10進位)
			String purchaserID = content.substring(37, 45); //買方統一編號 (一般消費者則以 00000000)
			String sellerID = content.substring(45, 53); //商家統一編號
			String encrypt = content.substring(53, 77); //加密驗證資訊

			new InvDetails(CaptureActivity.this).execute(type, invNum, "", invDate, encrypt, sellerID, UUID, randomCode, appID, "" + totlal);			

			//Log.e("",format + "\n" + code + "\n" + date + "\n" + encryptedAuthentication + "\n" + vendorUniformNumbers + "\n" + randomCode);
			//txtResult.setText(format + "\n" + code + "\n" + date + "\n" + encryptedAuthentication + "\n" + vendorUniformNumbers + "\n" + randomCode);

		}
		else if (type.equals("CODE_39") && content.matches("^[0-9]{3}[0-9]{2}[A-Za-z]{2}[0-9]{8}[0-9]{4}.*"))
		{
			type = "Barcode";

			String invTerm = content.substring(0, 5); //發票期別(民國年月)
			String year = content.substring(0, 3); //民國(3)(轉西元)
			String month = content.substring(3, 5); //月(2)
			String invDate = year + "/" + month;

			String invNum = content.substring(5, 15);
			String randomCode = content.substring(15, 19); //隨機碼(4)

			//txtResult.setText(format + "\n" + code + "\n" + date + "\n" + randomCode);

			new InvDetails(CaptureActivity.this).execute(type, invNum, invTerm, invDate, "", "", UUID, randomCode, appID);
		}
		else
		{
			Toast.makeText(CaptureActivity.this, "非發票條碼", Toast.LENGTH_LONG).show();
		}
		
		Intent intent = new Intent(CaptureActivity.this, AccountsActivity.class);
		startActivity(intent);
	}

	private void initBeepSound() //掃到了就叫一聲
	{
		if (playBeep && mediaPlayer == null)
		{
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try
			{
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			}
			catch (IOException e)
			{
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate()
	{
		if (playBeep && mediaPlayer != null)
		{
			mediaPlayer.start();
		}
		if (vibrate)
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mediaPlayer)
		{
			mediaPlayer.seekTo(0);
		}
	};

}