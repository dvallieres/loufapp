package com.example.android.laurentianvrapp;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by DS android dev on 6/7/2016.
 */
public class CreateQueueActivity extends AppCompatActivity {


    ListView listview;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_queue_layout);
        listview = (ListView) findViewById(R.id.listview_tour);
        listview.setAdapter(new queueAdapter(this, new String[] { "data1",
                "data2",
                "data1",
                "data2",
                "data1",
                "data2",
                "data1",
                "data2",
                "data2",
                "data1",
                "data2"}));
    }

    EditText tEdit;
    int imageArraySize = 10;
    int videoArraySize = 10;
    private String[] imageArray = new String[imageArraySize];
    private String[] videoArray = new String[videoArraySize];



    public void add(View view){
        startActivity(new Intent(this, ChooseMedia.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}
