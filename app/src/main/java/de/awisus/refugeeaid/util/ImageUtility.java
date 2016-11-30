package de.awisus.refugeeaid.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 07.08.16.
 *
 * @author Jens Awisus
 */
public class ImageUtility {

    private static final int SCALE = 512;

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

        // get orientation of bitmap and get degrees for rotation

        int rotation = getOrientation(context, uri);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Bitmap rotatedBitmap = null;

        // if rotation > 0 -> rotate bitmap

        if (rotation != 0) {
            // we may be needing the bitmap to rotate
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            rotatedBitmap = Bitmap.createBitmap(
                    scaledBitmap , 0, 0,
                    scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(),
                    matrix, true);

            scaledBitmap.recycle();
        }

        return rotatedBitmap == null ? scaledBitmap : rotatedBitmap;
    }

    private static int getOrientation(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
                null, null, null);

        int result = -1;
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        }

        return result;
    }
}
