package com.example.android.laurentianvrapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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

    private CommentsResponse jsonComments;
    Data[] dataArray;
    String[] listOfTourFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_layout);

        jsonComments = new CommentsResponse();
        jsonComments = jsonComments.parseJSON();
        dataArray = new Data[jsonComments.data.size()];

        listOfTourFiles = new String[jsonComments.data.size()];

        for(int i = 0; i<jsonComments.data.size();i++){
            dataArray[i] = jsonComments.data.get(i);
            listOfTourFiles[i] = dataArray[i].getFileUrl();
            Log.i("comment " + i, "" + dataArray[i]);
        }

        String tour = "";
        for(int j = 0; j<listOfTourFiles.length;j++){
            if(j == listOfTourFiles.length-1){
                tour += listOfTourFiles[j] + ";";
            }
            else{tour += listOfTourFiles[j] + ", ";}
        }

        Log.i("Tour Array", tour);
    }

    public void playTour(View view){
        Intent intent = new Intent(this, SimpleVrPanoramaActivityWithInput.class);
        intent.putExtra("panoFileArray", listOfTourFiles);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    class Data{
        private String name;
        private String description;
        private String url;
        private String type;

        //Get Methods
        public String getFileName() {return name;}
        public String getFileDescription() {return description;}
        public String getFileUrl() {return url;}
        public String getFileType() {return type;}

        //Set Methods
        public void setFileName(String name) {this.name = name;}
        public void setFileDescription(String description) {this.description = description;}
        public void setFileUrl(String url) {this.url = url;}
        public void setFileType(String type) {this.type = type;}

        //toString Method to print the object
        @Override
        public String toString() {
            return name + ", " + "\"" + description + "\"" + ", " + url + ", " + type + ";";
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
        public CommentsResponse() {data = new ArrayList<Data>();}

        /**
         * Method to parse a Json String into a Java object using Gson.
         * @return Returns a CommentsResponse object with a ArrayList of the Json data called data.
         */
        public CommentsResponse parseJSON() {
            Gson gson = new GsonBuilder().create();
            CommentsResponse commentsResponse = gson.fromJson(loadJSONFromAsset(), CommentsResponse.class);
            return commentsResponse;
        }
    }
}
