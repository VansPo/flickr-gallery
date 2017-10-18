package com.ipvans.flickrgallery.utils;

import android.content.Context;

public class DeviceUtils {

    public static int getOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

}
