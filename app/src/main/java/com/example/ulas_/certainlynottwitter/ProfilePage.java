package com.example.ulas_.certainlynottwitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class ProfilePage extends AppCompatActivity {
    TextView btn_sendTweety,btn_profile_mainPage,btn_exit;
    ImageView imageViewAvatar;
    TextView lbl_profile_name;
    EditText txt_tweety;
    Integer imageViewIndex;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    DatabaseHelper myDB;
    FloatingActionButton btn_float;
    SimpleRecyclerAdapter adapter_items;
    private List<Messages> messagesList;
    String date;


    String profile_name,string_tweety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        txt_tweety = findViewById(R.id.txt_tweety);
        btn_profile_mainPage = findViewById(R.id.btn_profile_mainPage);

        btn_sendTweety = findViewById(R.id.btn_sendTweety);
        btn_exit = findViewById(R.id.btn_exit);
        lbl_profile_name = findViewById(R.id.lbl_profile_name);
        imageViewAvatar = findViewById(R.id.imageView3);

        DateFormat df = new SimpleDateFormat("EEE, MMM d");
        date = df.format(Calendar.getInstance().getTime());

        myDB = new DatabaseHelper(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        profile_name = preferences.getString("username","Guest");
        imageViewIndex = preferences.getInt("avatarIndex",0);

        recyclerView = findViewById(R.id.recViewProfile);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        messagesList = new ArrayList<>();

        lbl_profile_name.setText(profile_name);

        Cursor data = myDB.getUserTweety(profile_name);


        if(data.getCount() == 0){
            Toast.makeText(ProfilePage.this,"The database was empty!",Toast.LENGTH_LONG).show();
        }
        else{
            while(data.moveToNext()){
                messagesList.add(new Messages(data.getInt(2),data.getString(0),data.getString(1),data.getInt(3),data.getString(4)));
            }
        }
          adapter_items = new SimpleRecyclerAdapter(messagesList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
              //  Log.d("position", "Tıklanan pos : "+position);
                Messages messages = messagesList.get(position);
            //    Toast.makeText(getApplicationContext(),"pozisyon:"+" "+position+" "+"Ad:"+messages.getID(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfilePage.this,DetailOfTweety.class);
                intent.putExtra("username",messages.getUsername());
                intent.putExtra("message",messages.getMessage());
                intent.putExtra("avatarIndex",messages.getAvatarIndex());
                intent.putExtra("messageID",messages.getID());
                startActivity(intent);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter_items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btn_profile_mainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_mainPage = new Intent(getApplicationContext(), MainPage.class);
                startActivity(intent_mainPage);
                finish();
            }
        });



        btn_sendTweety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string_tweety = txt_tweety.getText().toString();
                if(string_tweety.length() !=0){
                    txt_tweety.setText("");
                    AddTweety(string_tweety);
                    messagesList.clear();
                    Cursor data = myDB.getUserTweety(profile_name);
                    while(data.moveToNext()){
                        messagesList.add(new Messages(data.getInt(2),data.getString(0),data.getString(1),data.getInt(3),data.getString(4)));
                    }

                    recyclerView.setAdapter(adapter_items);

                }
                else{
                    Toast.makeText(ProfilePage.this,"You must put something in the text field!",Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("login",false);
                editor.commit();
                Intent intent_loginPage = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent_loginPage);
                finish();
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
            Toast.makeText(ProfilePage.this,"Success!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(ProfilePage.this,"Error!",Toast.LENGTH_LONG).show();
        }

    }

}
