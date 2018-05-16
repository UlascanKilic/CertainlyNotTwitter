package com.example.ulas_.certainlynottwitter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView btn_register,btn_login;
    EditText txt_userName,txt_password;
    String userName_string,password_string;
    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page2);
       // Log.i("LogDeneme", "database create girildi.");
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        txt_userName = findViewById(R.id.txt_userName);
        txt_password = findViewById(R.id.txt_password);
        myDB = new DatabaseHelper(this);

        preferences = PreferenceManager.getDefaultSharedPreferences((getApplicationContext()));
        editor = preferences.edit();

        if(preferences.getBoolean("login",false)) {
            Intent intent_mainPage = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent_mainPage);
            finish();
        }

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent_register = new Intent(getApplicationContext(),RegisterPage.class);
                startActivity(intent_register);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              userName_string = txt_userName.getText().toString();
              password_string = txt_password.getText().toString();

              if(userName_string.matches("") || password_string.matches("")){ //Bilgiler eksik ise
                  AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginPage.this);
                  alertDialog.setTitle("Warning!");
                  alertDialog.setMessage("Fields cannot be blank!");
                  alertDialog.setPositiveButton("Understand", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog,int which) {

                      }
                  });
                  alertDialog.show();
              }
              else{
                  Cursor checkLogin = myDB.checkUser(userName_string,password_string);
                  if(checkLogin.moveToFirst()){
                      editor.putBoolean("login",true);
                      editor.putString("username",userName_string);
                      editor.putInt("avatarIndex",checkLogin.getInt(checkLogin.getColumnIndex("AVATAR")));

                      editor.commit();
                      Intent intent_mainPage =new Intent(getApplicationContext(),MainPage.class);
                      startActivity(intent_mainPage);
                      finish();
                  }
                  else{
                      AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginPage.this);
                      alertDialog.setTitle("Error!");
                      alertDialog.setMessage("Wrong Username or Password!");
                      alertDialog.setPositiveButton("Understand", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog,int which) {

                          }
                      });
                      alertDialog.show();
                  }
              }
            }
        });
    }
}
