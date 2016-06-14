package com.example.android.laurentianvrapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Timer;
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


    void queuePage(View view) {

        Intent intent = new Intent(this, CreateQueueActivity.class);
        startActivity(intent);
    }

    public void queueTime(View view){

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

        startActivity(new Intent(this, CreateQueueActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


}
