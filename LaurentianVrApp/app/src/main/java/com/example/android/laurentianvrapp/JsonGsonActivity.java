package com.example.android.laurentianvrapp;

import android.graphics.Movie;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DS android dev on 6/16/2016.
 */
public class JsonGsonActivity extends AppCompatActivity {

    private String jsonFile;
    private CommentsResponse jsonComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_layout);
        jsonComments = new CommentsResponse();
        jsonComments = jsonComments.parseJSON(jsonFile);
        Data d = jsonComments.data.get(0);
        Log.i("comment new ", "" + d.getMessage());
    }

    class Data{
        String created_time;
        String message;
        String id;

        public String getCreatedTime() {return created_time;}
        public String getId() {
            return id;
        }
        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return id + " " + message + " " + created_time;
        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public class CommentsResponse {

        List<Data> data;

        // public constructor is necessary for collections
        public CommentsResponse() {
            data = new ArrayList<Data>();
        }

        public CommentsResponse parseJSON(String response) {
            Gson gson = new GsonBuilder().create();

            CommentsResponse commentsResponse = gson.fromJson(loadJSONFromAsset(), CommentsResponse.class);
            return commentsResponse;
        }
    }
}
