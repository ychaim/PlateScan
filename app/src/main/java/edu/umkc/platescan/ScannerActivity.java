package edu.umkc.platescan;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.hardware.Camera;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.openalpr.OpenALPR;
import org.openalpr.model.Results;
import org.openalpr.model.ResultsError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import edu.umkc.platescan.scanner.CameraPreview;
import edu.umkc.platescan.util.CapturePhotoUtils;

public class ScannerActivity extends AppCompatActivity {

    SurfaceView cameraSurfaceView;
    Camera camera;
    CameraPreview cameraPreview;
    boolean isRunning;
    String ANDROID_DATA_DIR;
    LocationManager locationManager;
    LocationListener locationListener;


    AlertDialog.Builder alertBuilder;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scanner);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertBuilder = new AlertDialog.Builder(ScannerActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertBuilder = new AlertDialog.Builder(ScannerActivity.this);
        }

        camera = Camera.open();
        cameraSurfaceView = findViewById(R.id.cameraSurfaceView);
        try {
            cameraPreview = new CameraPreview(ScannerActivity.this, cameraSurfaceView);
            cameraPreview.setCamera(camera);
        } catch (Exception e) {
            alertBuilder.setTitle("Error").setMessage("Camera not detected")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent mainIntent = new Intent(ScannerActivity.this, MainActivity.class);
                            ScannerActivity.this.startActivity(mainIntent);
                        }}).show();
        }
        isRunning = false;
        ANDROID_DATA_DIR = "data/data/edu.umkc.platescan";

        final Camera.PictureCallback mPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, final Camera camera) {
                Log.println(Log.ASSERT, "UPDT", "Taking photo...");
                final File pictureFile = getOutputMediaFile();
                try {
                    Log.println(Log.ASSERT, "UPDT", "Trying to save photo to your gallery...");
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    Log.println(Log.ASSERT, "UPDT", "Saved!");
                } catch (Exception e) {
                    Log.println(Log.ASSERT, "ERR", "Dang, I failed. Sorry bro.");
                }

                if (pictureFile != null) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 10;

                            final String result = OpenALPR.Factory.create(ScannerActivity.this, ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("us", "mo", pictureFile.getAbsolutePath(), openAlprConfFile, 10);

                            try {
                                final Results results = new Gson().fromJson(result, Results.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (results == null || results.getResults() == null || results.getResults().size() == 0) {
                                            //Toast.makeText(MainActivity.this, "It was not possible to detect the licence plate.", Toast.LENGTH_LONG).show();
                                            deleteImageFile(pictureFile.getAbsolutePath());
                                        } else {
                                            Log.println(Log.ASSERT, "Size", getString(results.getResults().size()));
                                            for (int i = 0; i < results.getResults().size(); i += 1)
                                            {
                                                Log.println(Log.ASSERT, "ALPR", "Confidence: " + results.getResults().get(i).getConfidence());
                                                if (results.getResults().get(i).getConfidence() >= 90.0)
                                                {

                                                }
                                                else {
                                                    //deleteImageFile(pictureFile.getAbsolutePath());
                                                }
                                            }
                                        }
                                    }
                                });

                            } catch (JsonSyntaxException exception) {
                                final ResultsError resultsError = new Gson().fromJson(result, ResultsError.class);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }
                    });
                }
                isRunning = false;
            }
        };
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    camera.takePicture(null, null, mPicture);
                } catch (Exception e) {
                    Log.println(Log.ASSERT, "ERR", e.getMessage());
                }
            }
        };

        cameraSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    isRunning = true;
                    handler.removeCallbacks(runnable);
                    handler.post(runnable);
                } else {

                }
            }
        });
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("LALA", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private void deleteImageFile(final String filePath){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
            Log.println(Log.ASSERT, "UPDT", String.format("Deleted file %s", filePath));
        }
    }
}
