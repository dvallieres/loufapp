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

}
