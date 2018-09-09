package edu.umkc.platescan;

import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import edu.umkc.platescan.camera.CameraModule;
import edu.umkc.platescan.camera.CameraNew;
import edu.umkc.platescan.camera.CameraOld;
import edu.umkc.platescan.camera.CameraSupport;

public class ScannerActivity extends AppCompatActivity {

    public CameraSupport getCameraInstance(){
        CameraModule cameraModule = new CameraModule(this.getApplicationContext());
        return cameraModule.provideCameraSupport();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        CameraSupport camera = getCameraInstance();


    }
}
