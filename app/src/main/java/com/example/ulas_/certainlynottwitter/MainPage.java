package com.example.ulas_.certainlynottwitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainPage extends AppCompatActivity {
    TextView lbl_mainPage_username;
    SharedPreferences preferences;
    ImageView imageViewAvatar;
    Integer imageViewIndex;
    TextView btn_profile;
    SharedPreferences.Editor editor;
    String username_string;
    DatabaseHelper myDB;
    RecyclerView recyclerView;
    FloatingActionButton btn_float;
    String date;
    private String[] random_words;
    private List<Messages> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        lbl_mainPage_username = findViewById(R.id.lbl_mainPage_username);
        imageViewAvatar = findViewById(R.id.imageView2);
        btn_profile = findViewById(R.id.btn_Profile);
        btn_float = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        messagesList = new ArrayList<>();

        DateFormat df = new SimpleDateFormat("EEE, MMM d");
       date = df.format(Calendar.getInstance().getTime());

        final String[] random_words = {"copper","explain","truck","neat","heat","heap","hate","hall","eat","head","ear","i","love","you","take","on","me","big","in","are","okey","you","window","sound"+
        "rabbit","sofa","shop","toy","think","this","app","is","awesome"};

        myDB = new DatabaseHelper(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();


        username_string = preferences.getString("username","Guest");
        imageViewIndex = preferences.getInt("avatarIndex",0);
        lbl_mainPage_username.setText(username_string);

        Cursor data = myDB.getAllTweeties();

        if(data.getCount() == 0){
            Toast.makeText(MainPage.this,"The database was empty!",Toast.LENGTH_LONG).show();
        }
        else{
            while(data.moveToNext()){
                messagesList.add(new Messages(data.getInt(2),data.getString(0),data.getString(1),data.getInt(3),data.getString(4)));
            }
        }
        SimpleRecyclerAdapter adapter_items = new SimpleRecyclerAdapter(messagesList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
              //  Messages messages = messagesList.get(position);
                Toast.makeText(getApplicationContext(),"Please visit Profile Page for Tweety Details!",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter_items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_profilePage = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent_profilePage);
                finish();
            }
        });
        btn_float.setOnClickListener(new View.OnClickListener() {
            Random random = new Random();
            String words="";
            @Override
            public void onClick(View v) { // Add fake data
                for(int i = 1;i<=3;i++){
                    words="";

                    for(int k = 0;k<=5;k++){
                        words += random_words[random.nextInt(23)] + " ";
                    }
                    AddTweety(words);
                    Log.i("mesaj",words);
                }
                finish();
                startActivity(getIntent());

            }
        });
        switch (imageViewIndex){
            case 0:
                imageViewAvatar.setImageResource(R.drawable.avatar1);
                break;
            case 1:
                imageViewAvatar.setImageResource(R.drawable.avatar2);
                break;
            case 2:
                imageViewAvatar.setImageResource(R.drawable.avatar3);
                break;
            case 3:
                imageViewAvatar.setImageResource(R.drawable.avatar4);
                break;
        }
    }
    public  void AddTweety(String message){

        String user_name = preferences.getString("username","");
        Cursor user_id = myDB.getUserID(user_name);
        int user_id_int=0;

        while(user_id.moveToNext()){
            user_id_int = user_id.getInt(user_id.getColumnIndex("ID"));
        }
        boolean insertTweety = myDB.addTweety(message,user_id_int,date);

        if(insertTweety == true){
            Toast.makeText(MainPage.this,"Success!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainPage.this,"Error!",Toast.LENGTH_LONG).show();
        }

    }
}
