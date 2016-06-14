package com.example.android.laurentianvrapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by DS android dev on 6/9/2016.
 */
public class queueAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public queueAdapter(Context context, ArrayList<String> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.queue_list_row, null);
        TextView text = (TextView) vi.findViewById(R.id.text1);
        Button deleteButton = (Button) vi.findViewById(R.id.deleteButton);
        //deleteButton.setId();
        text.setText(data.get(position));
        return vi;
    }
}