package com.example.dohahamdy.movieappstage2;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);


        MainFragment fragment=new MainFragment();
        Fragment fragment1=getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);

        if(fragment1==null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_main, fragment, MainFragment.TAG)
                    .commit();

        }
    }

}
