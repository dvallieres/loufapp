package com.example.android.laurentianvrapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void letsVR(View view){

        outsideFunction();

    }

    public void letsVideo(View view){

        outsideFunction2();

    }

    public void letsJson(View view){

        outsideFunction3();

    }

    class imageTimerTask extends TimerTask {

        public void run(){
            outsideFunction();
        }
    }


    void outsideFunction(){

        startActivity(new Intent(this, SimpleVrPanoramaActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    void outsideFunction2(){

        startActivity(new Intent(this, SimpleVrVideoActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    void outsideFunction3(){

        startActivity(new Intent(this, JsonActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


}
