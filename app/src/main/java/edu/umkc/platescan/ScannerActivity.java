package edu.umkc.platescan;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import edu.umkc.platescan.overlay.OverlayWithHoleImageView;
import edu.umkc.platescan.scanner.CameraPreview;

public class ScannerActivity extends AppCompatActivity {

    SurfaceView cameraSurfaceView;
    CameraPreview cameraPreview;

    AlertDialog.Builder alertBuilder;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertBuilder = new AlertDialog.Builder(ScannerActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertBuilder = new AlertDialog.Builder(ScannerActivity.this);
        }

        cameraSurfaceView = findViewById(R.id.cameraSurfaceView);
        try {
            cameraPreview = new CameraPreview(ScannerActivity.this, cameraSurfaceView);
            cameraPreview.setCamera(Camera.open());
        } catch (Exception e) {
            alertBuilder.setTitle("Error").setMessage("Camera not detected")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent mainIntent = new Intent(ScannerActivity.this, MainActivity.class);
                            ScannerActivity.this.startActivity(mainIntent);
                        }}).show();
        }
    }
}
