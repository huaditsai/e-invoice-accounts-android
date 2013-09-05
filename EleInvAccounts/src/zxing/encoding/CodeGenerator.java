package zxing.encoding;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class CodeGenerator
{
	Context mContext;
	
	public CodeGenerator(Context context, String content, int desiredWidth, int desiredHeight)
	{
		mContext = context;		
		content = "/XXXXXXX";		
		desiredWidth = 512;
		desiredHeight = 512;
		
		final String imageFileName = "CarrierCode.png";
		
		FileOutputStream fos = null;
		Bitmap bitmap = null;
		try
		{	
			//生成二維碼圖像 BarcodeFormat.QR_CODE / BarcodeFormat.CODE_39
			bitmap = EncodeAsBitmap(content, BarcodeFormat.CODE_39, desiredWidth, desiredHeight);
			if (null != bitmap)
			{
				//將二維碼圖像保存
				File file = new File(Environment.getExternalStorageDirectory(), imageFileName);
				fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != fos)
			{
				try
				{
					fos.close();
				}
				catch (Exception e)
				{
				}
			}
		}
		
		//顯示Code
		if (null != bitmap)
		{
			//ImageView iv = new ImageView(mContext);
			//iv.setImageBitmap(bitmap);
			//iv.setScaleType(ScaleType.FIT_CENTER);
			//setContentView(iv, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}
	
	private static Bitmap EncodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight) throws WriterException
	{
		final int WHITE = 0xFFFFFFFF; //可以指定其他顏色，讓二維碼變成彩色效果
		final int BLACK = 0xFF000000;

		HashMap<EncodeHintType, String> hints = null;
		String encoding = GuessAppropriateEncoding(contents);
		if (encoding != null)
		{
			hints = new HashMap<EncodeHintType, String>(2);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = writer.encode(contents, format, desiredWidth, desiredHeight);//, hints);
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++)
		{
			int offset = y * width;
			for (int x = 0; x < width; x++)
			{
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String GuessAppropriateEncoding(CharSequence contents)
	{
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++)
		{
			if (contents.charAt(i) > 0xFF)
			{
				return "UTF-8";
			}
		}
		return null;
	}
	
	
}
