package edu.umkc.platescanner;

import android.content.DialogInterface;
import android.hardware.Camera;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;

import edu.umkc.platescanner.scanner.CameraPreview;

public class FragmentScan extends Fragment {

    SurfaceView cameraSurfaceView;
    Camera camera;
    CameraPreview cameraPreview;
    boolean isRunning;
    String ANDROID_DATA_DIR;
    LocationManager locationManager;
    LocationListener locationListener;
    AlertDialog.Builder alertBuilder;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        try {
            cameraSurfaceView = view.findViewById(R.id.cameraSurface);
            cameraPreview = new CameraPreview(getContext(), cameraSurfaceView);
            camera = Camera.open();
            cameraPreview.setCamera(camera);
        } catch (Exception e) {
            alertBuilder.setTitle("Error").setMessage("Camera not detected")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = null;
                            fragmentTransaction = fragmentManager.beginTransaction();

                            FragmentHome fragmentHome = new FragmentHome();
                            fragmentTransaction.hide(FragmentScan.this);
                            fragmentTransaction.add(R.id.main_fragment, fragmentHome);
                            fragmentTransaction.commit();
                        }
                    }).show();
        }

        cameraSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                }, new Camera.PictureCallback() {
                    // raw
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                    }
                }, new Camera.PictureCallback() {
                    // jpeg
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try (FileOutputStream fos = new FileOutputStream(ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "temp_img_file")) {
                            fos.write(data);
                        } catch (Exception e) {

                        }
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            return inflater.inflate(R.layout.fragment_scan, container, false);
        } catch (Exception e) {
            Log.println(Log.ASSERT, "ERROR", e.getMessage());
        }
        return null;
    }
}
