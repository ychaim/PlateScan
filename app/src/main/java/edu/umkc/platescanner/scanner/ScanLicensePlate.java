package edu.umkc.platescanner.scanner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

// the OpenALPR library
import org.openalpr.OpenALPR;
import org.openalpr.model.Results;
import org.openalpr.model.ResultsError;

import edu.umkc.platescanner.MainActivity;

public class ScanLicensePlate {

    private static String ANDROID_DATA_DIR = "data/data/edu.umkc.platescanner";
    
    
    public static boolean isLicensePlate(File pictureFile) {

        if (pictureFile == null) {
            return false;
        }

        boolean isRunning = false;
        ArrayList flaggedNumbersList = new ArrayList();
        final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;

        final String result = OpenALPR.Factory.create(MainActivity.this, ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("us", "mo", pictureFile.getAbsolutePath(), openAlprConfFile, 10);

        try {
            final Results results = new Gson().fromJson(result, Results.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (results == null || results.getResults() == null || results.getResults().size() == 0) {
                        //Toast.makeText(MainActivity.this, "It was not possible to detect the licence plate.", Toast.LENGTH_LONG).show();
                        resultTextView.setText(resultTextView.getText() + "It was not possible to detect the licence plate." + "\n");
                        deleteImageFile(pictureFile.getAbsolutePath().toString());
                    } else {
                        Log.d("LALA", "" + results.getResults().size());
                        for (int i = 0; i < results.getResults().size(); i += 1)
                        {
                            Log.d("LALA", "" + results.getResults().get(i));
                            if (results.getResults().get(i).getConfidence() >= 90.0)
                            {
                                resultTextView.setText(resultTextView.getText() + "Plate: " + results.getResults().get(i).getPlate()
                                    // Trim confidence to two decimal places
                                    + " Confidence: " + String.format("%.2f", results.getResults().get(i).getConfidence()) + "%"
                                    // Convert processing time to seconds and trim to two decimal places
                                    + " Processing time: " + String.format("%.2f", ((results.getProcessingTimeMs() / 1000.0) % 60)) + " seconds" + "\n");

                                if (flaggedNumbersList.contains(results.getResults().get(i).getPlate().toUpperCase()))
                                {
                                    resultTextView.setText(resultTextView.getText() + "Found flagged number" + "\n");
                                }
                            }
                            else {
                                resultTextView.setText(resultTextView.getText() + "Confidence not high enough\n");
                                deleteImageFile(pictureFile.getAbsolutePath().toString());
                            }
                        }
                    }

                    return true;
                }
            });

        } catch (JsonSyntaxException exception) {
            final ResultsError resultsError = new Gson().fromJson(result, ResultsError.class);
        }
    }
}
