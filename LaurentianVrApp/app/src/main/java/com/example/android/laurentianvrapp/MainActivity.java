package com.example.android.laurentianvrapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

    }

    public void letsJson(View view){
        startTour();
    }


    void startTour(){

        startActivity(new Intent(this, JsonGsonActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


}
