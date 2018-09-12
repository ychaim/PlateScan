package edu.umkc.platescan.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class PermissionUtilities {
    public static boolean hasPermission(Context context, String... allPermissionsNeeded) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && allPermissionsNeeded != null)
            for (String permission : allPermissionsNeeded) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        return true;
    }
}
