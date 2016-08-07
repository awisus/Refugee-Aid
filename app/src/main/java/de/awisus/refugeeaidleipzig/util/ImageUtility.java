package de.awisus.refugeeaidleipzig.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 07.08.16.
 *
 * @author jens
 */
public class ImageUtility {

    public static final int SCALE = 410;

    private ImageUtility() {}

    public static void setIvImage(ImageView ivImage, String imageData) throws IllegalArgumentException {
        try {
            ivImage.setImageBitmap(stringToImage(imageData));
        } catch (IllegalArgumentException ex) {
            Log.e("Set offer image", ex.getMessage());
        }
    }

    public static Bitmap stringToImage(String imageData) throws IllegalArgumentException {
        if(imageData != null) {
            byte[] byteArray = Base64.decode(imageData, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    public static String imageToString(Bitmap bitmap) throws IllegalArgumentException {
        if(bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else {
            return null;
        }
    }

    public static Bitmap uriToBitmap(Context context, Uri uri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        assert is != null;
        is.close();

        float ratio = Math.min(
                (float) SCALE / bitmap.getWidth(),
                (float) SCALE / bitmap.getHeight());
        int width = Math.round(ratio * bitmap.getWidth());
        int height = Math.round(ratio * bitmap.getHeight());

        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
}
