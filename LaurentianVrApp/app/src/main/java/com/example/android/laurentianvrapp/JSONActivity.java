package com.example.android.laurentianvrapp;

import android.graphics.Movie;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DS android dev on 6/16/2016.
 */
public class JsonActivity extends AppCompatActivity {

    String jsonFile;
    CommentsResponse jsonComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_layout);
        String jsonFile = "{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:58:49+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"Will Bowman\",\n" +
                "        \"id\": \"10205135272291932\"\n" +
                "      },\n" +
                "      \"message\": \"Feminist garbage. Keep it I'll watch the 1st two\",\n" +
                "      \"id\": \"1030457310341603_1030457537008247\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:58:54+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"Joey Cocco\",\n" +
                "        \"id\": \"10200968055242696\"\n" +
                "      },\n" +
                "      \"message\": \"Sexist backlash? You guys just did an awful job in casting.\",\n" +
                "      \"id\": \"1030457310341603_1030457580341576\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:30+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"Brendan Rome\",\n" +
                "        \"id\": \"708005405974266\"\n" +
                "      },\n" +
                "      \"message\": \"In other news; Slime jokes still not funny.\",\n" +
                "      \"id\": \"1030457310341603_1030457733674894\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:39+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"Rico Sandoval\",\n" +
                "        \"id\": \"10202768246543776\"\n" +
                "      },\n" +
                "      \"message\": \"Matthew Sandoval\",\n" +
                "      \"id\": \"1030457310341603_1030457787008222\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:43+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"Robert Mullen\",\n" +
                "        \"id\": \"10203707178544702\"\n" +
                "      },\n" +
                "      \"message\": \"\",\n" +
                "      \"id\": \"1030457310341603_1030457810341553\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:45+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"George Joseph Miller Jr\",\n" +
                "        \"id\": \"10204231241411576\"\n" +
                "      },\n" +
                "      \"message\": \"Think I'll pass on this\",\n" +
                "      \"id\": \"1030457310341603_1030457827008218\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T16:59:53+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"Chris Siers\",\n" +
                "        \"id\": \"682976677774\"\n" +
                "      },\n" +
                "      \"message\": \"\",\n" +
                "      \"id\": \"1030457310341603_1030457870341547\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T17:00:01+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"Paul Michels\",\n" +
                "        \"id\": \"657502154855\"\n" +
                "      },\n" +
                "      \"message\": \"no.\",\n" +
                "      \"id\": \"1030457310341603_1030457903674877\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T17:00:11+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"Manny Peters\",\n" +
                "        \"id\": \"10151937401376792\"\n" +
                "      },\n" +
                "      \"message\": \"The butthurt is strong.\",\n" +
                "      \"id\": \"1030457310341603_1030457963674871\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"created_time\": \"2016-06-16T17:00:23+0000\",\n" +
                "      \"from\": {\n" +
                "        \"name\": \"David Par\",\n" +
                "        \"id\": \"10151954528631666\"\n" +
                "      },\n" +
                "      \"message\": \"It has nothing to do with their gender and everything to do with their lack of talent.\",\n" +
                "      \"id\": \"1030457310341603_1030458167008184\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"paging\": {\n" +
                "    \"cursors\": {\n" +
                "      \"before\": \"WTI5dGJXVnVkRjlqZAFhKemIzSTZANVEF6TURRMU56VXpOekF3T0RJME56b3hORFkyTURrMk16STUZD\",\n" +
                "      \"after\": \"WTI5dGJXVnVkRjlqZAFhKemIzSTZANVEF6TURRMU9ERTJOekF3T0RFNE5Eb3hORFkyTURrMk5ESXoZD\"\n" +
                "    },\n" +
                "    \"next\": \"https://graph.facebook.com/v2.6/152306518156691_1030457310341603/comments?access_token=EAACEdEose0cBAH1GMZCzeWZC6dxkwxUO09Bnau68AfCgcFEZBmIkuQpZBmjG13b3pqNi0pZAzv4FyaPaJ8sHhMioYhZCvGuNvGlZCmvAMRMdeckMOvvelnZCDwE80NTlOKZAd5gRFzE5lKa0bROkOuNodGWnvmvFNnXeUK7AJTH0rTAZDZD&pretty=0&limit=10&after=WTI5dGJXVnVkRjlqZAFhKemIzSTZANVEF6TURRMU9ERTJOekF3T0RFNE5Eb3hORFkyTURrMk5ESXoZD\"\n" +
                "  }\n" +
                "}";
        jsonComments.parseJSON(jsonFile);

    }

    class Comments{
        String created_time;
        String message;
        String id;
        From from;

        public String getCreatedTime() {
            return created_time;
        }
        public String getId() {
            return id;
        }
        public String getMessage() {
            return message;
        }

        public From getFrom(){
            return from;
        }

        public class From{
            String name;
            int id;

            public String getName(){
                return name;
            }

            public int getId() {
                return id;
            }
        }
    }

    public class CommentsResponse {

        List<Comments> comments;

        // public constructor is necessary for collections
        public CommentsResponse() {
            comments = new ArrayList<Comments>();
        }

        public CommentsResponse parseJSON(String response) {
            Gson gson = new GsonBuilder().create();
            CommentsResponse commentsResponse = gson.fromJson(response, CommentsResponse.class);
            return commentsResponse;
        }
    }
}
