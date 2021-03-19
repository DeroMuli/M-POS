package com.muli.m_pos.Utility;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public final class Utility {

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
