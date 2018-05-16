package com.example.ulas_.certainlynottwitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailOfTweety extends AppCompatActivity {
    TextView lbl_username,lbl_message;
    ImageView imageViewDetail;
    EditText txt_update_text;
    TextView btn_delete,btn_update,btn_send_update;
    SharedPreferences preferences;
    int imageViewIndex,control;
    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_tweety);

        lbl_username = findViewById(R.id.txtUserNameDetail);
        lbl_message = findViewById(R.id.txtMessageDetail);
        imageViewDetail = findViewById(R.id.imageViewDetail);
        txt_update_text = findViewById(R.id.txt_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);
        btn_send_update = findViewById(R.id.btn_send_update);
        myDB = new DatabaseHelper(this);
        btn_send_update.setVisibility(View.GONE);
        txt_update_text.setVisibility(View.GONE);
        control = 0;

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        imageViewIndex = preferences.getInt("avatarIndex",0);

        Bundle bundle = getIntent().getExtras();
        String id = String.valueOf(bundle.getInt("messageID"));
        final int intID = Integer.parseInt(id);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control == 0){
                    btn_send_update.setVisibility(View.VISIBLE);
                    txt_update_text.setVisibility(View.VISIBLE);
                    control = 1;
                }
                else if(control == 1){
                    btn_send_update.setVisibility(View.GONE);
                    txt_update_text.setVisibility(View.GONE);
                    control = 0;
                }

            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailOfTweety.this);
                alertDialog.setTitle("Delete!");
                alertDialog.setMessage("Are you sure you want to delete the message ?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        myDB.DeleteMessage(intID);
                        Intent intent_mainPage = new Intent(getApplicationContext(), MainPage.class);
                        startActivity(intent_mainPage);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });
                alertDialog.show();


            }
        });
        btn_send_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_update_text.length() !=0){
                    Cursor data = myDB.checkMessage(intID);
                    if(data.moveToNext()){
                        myDB.UpdateMessage(intID,txt_update_text.getText().toString());
                        Intent intent_mainPage = new Intent(getApplicationContext(), MainPage.class);
                        startActivity(intent_mainPage);
                        finish();
                    }
                    else{
                        Toast.makeText(DetailOfTweety.this,"Error!",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(DetailOfTweety.this,"You must put something in the text field!",Toast.LENGTH_LONG).show();
                }

            }
        });

        if(bundle != null){
            lbl_username.setText(bundle.getString("username"));
            lbl_message.setText(bundle.getString("message"));
        }
        switch (imageViewIndex){
            case 0:
                imageViewDetail.setImageResource(R.drawable.avatar1);
                break;
            case 1:
                imageViewDetail.setImageResource(R.drawable.avatar2);
                break;
            case 2:
                imageViewDetail.setImageResource(R.drawable.avatar3);
                break;
            case 3:
                imageViewDetail.setImageResource(R.drawable.avatar4);
                break;
        }



    }
}
