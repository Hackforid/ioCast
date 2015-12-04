package com.smilehacker.coffeeknife.android;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by kleist on 14-7-30.
 */
public class QRCodeHelper {

    public Bitmap createQRImage(String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        BitMatrix bitMatrix;
        try {
            bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            DLog.d(e.toString());
            return null;
        }
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
            {
                if (bitMatrix.get(x, y))
                {
                    pixels[y * width + x] = 0xff000000;
                }
                else
                {
                    pixels[y * height + x] = 0xffffffff;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

}
