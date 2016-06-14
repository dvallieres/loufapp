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


public class ChooseMedia extends AppCompatActivity {


    ListView listview;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_meda);
    }

    EditText tEdit;
    int imageArraySize = 10;
    int videoArraySize = 10;
    private String[] imageArray = new String[imageArraySize];
    private String[] videoArray = new String[videoArraySize];


    public void add(View v){
        tEdit = (EditText)findViewById(R.id.edittext);
        Log.v("images: ", "This is " + tEdit.getText().toString() + ".");
        addImageItem(tEdit.getText().toString());
    }

    public void remove(View v){
        tEdit = (EditText)findViewById(R.id.edittext);
        Log.v("images: ", "This is " + tEdit.getText().toString() + ".");
        removeImageItem(tEdit.getText().toString());
    }

    public void printArray(View v) {
        printImageArray();
    }

    public void insertItem(View v) {
        insertImageItem("poopy" , 2);
    }

    // Adds Image to Image Array if there is room.
    boolean addImageItem(String item){
        for(int i = 0; i < imageArraySize; i++){
            if(imageArray[i] == null){
                imageArray[i] = item;
                return true;
            }
        }
        return false;
    }

    // Adds Video to Video Array if there is room.
    boolean addVideoItem(String item){
        for(int i = 0; i < videoArraySize; i++){
            if(videoArray[i] == null){
                videoArray[i] = item;
                return true;
            }
        }
        return false;
    }

    // item is the item that is being reinserted, arraySlot is the position it is being reinserted to.
    // x is the original index of the item being reinserted.
    boolean insertImageItem(String item, int newIndex){
        int oldIndex = -1;
        for(int i = 0; i < imageArraySize; i++){
            if (imageArray[i]!=null){
                if(imageArray[i].equals(item)){
                    oldIndex = i;
                }
            }
            Log.v("tag", "oldIndex: " + oldIndex);
        }

        String tempItem;

        if (oldIndex == -1){
            Log.v("tag", "oldIndex-1: " + oldIndex);
            return false;
        }
        else if (oldIndex > newIndex){

            for(int i = oldIndex; i > newIndex; i-- ){
                Log.v("tag", "oldIndex>: " + oldIndex);
                imageArray[i] = imageArray[i-1];
            }
            imageArray[newIndex] = item;
            return true;
        }
        else if (oldIndex < newIndex){
            for(int i = oldIndex; i < newIndex; i++ ){
                Log.v("tag", "oldIndex<: " + oldIndex);
                imageArray[i] = imageArray[i+1];
            }
            imageArray[newIndex] = item;
            return true;
        }
        else {
            Log.v("tag", "oldIndexelse: " + oldIndex);
            return false;
        }

    }


    boolean removeImageItem(String item){
        for(int i = 0; i < imageArraySize; i++){
            if (imageArray[i]!=null){
                if(imageArray[i].equals(item)){
                    imageArray[i] = null;
                    for(int j = i; j < imageArraySize-1; j++){
                        imageArray[j] = imageArray[j+1];
                    }
                    return true;
                }
            }
        }
        return false;
    }

    boolean removeVideoItem(String item){
        for(int i = 0; i < videoArraySize; i++){
            if(videoArray[i].equals(item)){
                videoArray[i] = null;

                for(int j = i; j < videoArraySize-1; j++){
                    videoArray[j] = videoArray[j+1];
                }
                return true;
            }
        }
        return false;
    }

    void printImageArray(){
        for (int i = 0; i<imageArraySize; i++){
            Log.v("images: ", "This is image " + i + ", which is " + imageArray[i] + ".");
        }
    }

    void printVideoArray(){
        for (int i = 0; i<videoArraySize; i++){
            Log.v("images: ", "This is image " + i + ", which is " + videoArray[i] + ".");
        }
    }
}