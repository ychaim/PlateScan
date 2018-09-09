package edu.umkc.platescan.camera;

import android.content.Context;
import android.os.Build;

public class CameraModule {

    public CameraModule() {}
    public CameraModule(Context context) {
        context = context;
    }

    private Context context;

    public CameraSupport provideCameraSupport(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (context != null) {
                return new CameraNew(context);
            } else {
                return new CameraOld();
            }
        } else {
            return new CameraOld();
        }
    }
}