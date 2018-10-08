package edu.umkc.platescanner;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment mFragment;
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mFragment = new FragmentHome();
                    break;
                case R.id.navigation_scan:
                    mFragment = new FragmentScan();
                    break;
                case R.id.navigation_rewards:
                    mFragment = new FragmentRewards();
                    break;
                default:
                    return false;
            }
            fragmentTransaction.replace(R.id.main_fragment, mFragment);
            fragmentTransaction.commit();
            return true;
        }
    };
}
