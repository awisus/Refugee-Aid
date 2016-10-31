package de.awisus.refugeeaid.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created on 07.08.16.
 *
 * @author Jens Awisus
 */
public class LocationUtility {

    private LocationUtility() {}

    /*
     * GPS Permissions
     */

    @TargetApi(23)
    public static boolean haveGPSPermission(Activity activity) {
        boolean haveFine =
                activity.checkSelfPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean haveCoarse =
                activity.checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        return haveFine && haveCoarse;
    }

    @TargetApi(23)
    public static void requestGPSPermission(Activity activity) {
        final int MY_PERMISSION_LOCATION = 122;
        final String[] LOCATION_PERMISSTION = new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        activity.requestPermissions(LOCATION_PERMISSTION, MY_PERMISSION_LOCATION);
    }

    /*
     * Address <-> coordinates
     */

    public static LatLng getLocationFromAddress(String strAddress, Context context) throws IOException {
        Geocoder coder = new Geocoder(context);
        List<Address> address;

        address = coder.getFromLocationName(strAddress, 5);
        if (address == null || address.size() < 1) {
            return null;
        }
        Address location = address.get(0);
        location.getLatitude();
        location.getLongitude();

        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static String latlngToAdress(LatLng latLng, Context context) throws IOException {
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

    public static String latlngToStreet(LatLng latLng, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1
        );

        if (addresses != null && addresses.size() > 0) {
            return addresses.get(0).getAddressLine(0);
        }

        return null;
    }

    public static String latlngToCity(LatLng latLng, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1
        );

        if (addresses != null && addresses.size() > 0) {
            String city = addresses.get(0).getLocality();
            String postalCode = addresses.get(0).getPostalCode();

            return postalCode + " " + city;
        }

        return null;
    }
}
