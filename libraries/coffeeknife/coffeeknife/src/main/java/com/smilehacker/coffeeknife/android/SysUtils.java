package com.smilehacker.coffeeknife.android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

/**
 * Created by kleist on 14-4-2.
 */
public class SysUtils {

    public final static int LOW_MEM_THRESHOLD = 64 * 1024 * 1024;
    public final static double LOW_MEM_IMAGE_CACHE_MULTIPLE = 0.05;
    public final static double NORMAL_MEM_IMAGE_CACHE_MULTIPLE = 0.1;

    public static int calculateMemoryCacheSize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean largeHeap = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
        int memoryClass = am.getMemoryClass();
        if (largeHeap && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            memoryClass = am.getLargeMemoryClass();
        }

        int availableHeap = 1024 * 1024 * memoryClass;
        return (int) (availableHeap < LOW_MEM_THRESHOLD
                ? availableHeap * LOW_MEM_IMAGE_CACHE_MULTIPLE
                : availableHeap * NORMAL_MEM_IMAGE_CACHE_MULTIPLE);
    }


}
