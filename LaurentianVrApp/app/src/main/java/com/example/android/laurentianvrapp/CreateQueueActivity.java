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

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by DS android dev on 6/7/2016.
 */
public class CreateQueueActivity extends AppCompatActivity {


    private ListView listview;
    private queueAdapter queueAdap;
    private ArrayList<String> arrayList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_queue_layout);

        arrayList = new ArrayList<>();
        arrayList.add("data1");
        arrayList.add("data2");
        arrayList.add("data3");
        arrayList.add("data4");
        arrayList.add("data5");
        arrayList.add("data6");

        queueAdap = new queueAdapter(this, arrayList);

        listview = (ListView) findViewById(R.id.listview_tour);
        listview.setAdapter(queueAdap);
    }

    public void add(View view){
        startActivity(new Intent(this, ChooseMedia.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void addItem(View view){
        arrayList.add("testing");
        Log.i("added Item", "Item has been added to arraylist");
        Toast.makeText(CreateQueueActivity.this, "Item has been added.", Toast.LENGTH_SHORT).show();
        queueAdap.notifyDataSetChanged();
    }

    public void removeItem(View view){
        if(arrayList.size()>0)
        {
            arrayList.remove(arrayList.size() - 1);
            Log.i("added Item", "Item has been removed from arraylist");
            Toast.makeText(CreateQueueActivity.this, "Item has been removed.", Toast.LENGTH_SHORT).show();
            queueAdap.notifyDataSetChanged();
        }
        else {
            Toast.makeText(CreateQueueActivity.this, "There are no items to remove.", Toast.LENGTH_SHORT).show();
        }
    }

}
