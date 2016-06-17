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
        String jsonFile = "{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:58:49+0000\",\n" +
                "      \"message\": \"Feminist garbage. Keep it I'll watch the 1st two\",\n" +
                "      \"id\": \"1030457310341603_1030457537008247\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:58:54+0000\",\n" +
                "      \"message\": \"Sexist backlash? You guys just did an awful job in casting.\",\n" +
                "      \"id\": \"1030457310341603_1030457580341576\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:30+0000\",\n" +
                "      \"message\": \"In other news; Slime jokes still not funny.\",\n" +
                "      \"id\": \"1030457310341603_1030457733674894\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:39+0000\",\n" +
                "      \"message\": \"Matthew Sandoval\",\n" +
                "      \"id\": \"1030457310341603_1030457787008222\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:43+0000\",\n" +
                "      \"message\": \"\",\n" +
                "      \"id\": \"1030457310341603_1030457810341553\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:45+0000\",\n" +
                "      \"message\": \"Think I'll pass on this\",\n" +
                "      \"id\": \"1030457310341603_1030457827008218\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:53+0000\",\n" +
                "      \"message\": \"\",\n" +
                "      \"id\": \"1030457310341603_1030457870341547\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T17:00:01+0000\",\n" +
                "      \"message\": \"no.\",\n" +
                "      \"id\": \"1030457310341603_1030457903674877\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T17:00:11+0000\",\n" +
                "      \"message\": \"The butthurt is strong.\",\n" +
                "      \"id\": \"1030457310341603_1030457963674871\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T17:00:23+0000\",\n" +
                "      \"message\": \"It has nothing to do with their gender and everything to do with their lack of talent.\",\n" +
                "      \"id\": \"1030457310341603_1030458167008184\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        jsonComments = new CommentsResponse();
        jsonComments = jsonComments.parseJSON(jsonFile);
        Data d = jsonComments.data.get(0);
        Log.i("comment ", "" + d.getMessage());
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

    public class CommentsResponse {

        List<Data> data;

        // public constructor is necessary for collections
        public CommentsResponse() {
            data = new ArrayList<Data>();
        }

        public CommentsResponse parseJSON(String response) {
            Gson gson = new GsonBuilder().create();
            CommentsResponse commentsResponse = gson.fromJson(response, CommentsResponse.class);
            return commentsResponse;
        }
    }
}
