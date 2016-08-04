package de.awisus.refugeeaidleipzig.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public class Utility {

    public static final Utility INSTANCE = new Utility();

    private Utility() {}
    public static Utility getInstance() {
        return INSTANCE;
    }

    public ProgressDialog zeigeLadebalken(Activity activity, String nachricht) {
        return ProgressDialog.show(activity, null, nachricht, true, false);
    }

    public Bitmap stringToImage(String imageData) throws IllegalArgumentException {
        if(imageData != null) {
            byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            return null;
        }
    }

    public String latlngToAdress(LatLng latLng, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1
        );

        if (addresses != null && addresses.size() > 0) {
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String postalCode = addresses.get(0).getPostalCode();

            return address + "\n" + postalCode + " " + city;
        }

        return null;
    }
}
