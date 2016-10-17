package com.example.ramrooter.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Ram Rooter on 10/15/2016.
 */

public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment =  fm.findFragmentById(R.id.fragment_movie_list);
        if(fragment == null){
            fm.beginTransaction()
                    .add(R.id.fragment_movie_list, fragment)
                    .commit();
        }
    }
}
