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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterPage extends AppCompatActivity {
    TextView btn_register,btn_goto_login;
    EditText txt_username,txt_password;
    RadioGroup radioGroupAvatar;
    String username_string,password_string;
    Integer selectedAvatar_int;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    DatabaseHelper myDB;
    ImageView imageViewAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

       imageViewAvatar = findViewById(R.id.imageView);
       btn_register = findViewById(R.id.btn_registerPage);
       btn_goto_login  = findViewById(R.id.btn_goto_login);
       txt_username = findViewById(R.id.txt_register_userName);
       txt_password = findViewById(R.id.txt_register_password);
       radioGroupAvatar = findViewById(R.id.radioGroupAvatar);

        myDB = new DatabaseHelper(this);

       btn_register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               username_string = txt_username.getText().toString();
               password_string = txt_password.getText().toString();
               selectedAvatar_int = radioGroupAvatar.indexOfChild(findViewById(radioGroupAvatar.getCheckedRadioButtonId()));

               if(username_string.matches("") || password_string.matches("")){
                   AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterPage.this);
                   alertDialog.setTitle("Warning!");
                   alertDialog.setMessage("Fields cannot be blank!");
                   alertDialog.setPositiveButton("Understand", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog,int which) {

                       }
                   });
                   alertDialog.show();
               }
               else{
                   preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                   editor = preferences.edit();
                   Cursor checkLogin = myDB.checkUser(username_string,password_string);
                   if(checkLogin.moveToFirst()){
                       AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterPage.this);
                       alertDialog.setTitle("Warning!");
                       alertDialog.setMessage("This username is already in use!");
                       alertDialog.setPositiveButton("Understand", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog,int which) {

                           }
                       });
                       alertDialog.show();
                   }
                   else{
                       editor.putString("username",username_string);
                       editor.putString("password",password_string);
                       editor.putInt("avatarIndex",selectedAvatar_int);
                       editor.putBoolean("login",true);
                       boolean result =  myDB.addUserData(username_string,password_string,selectedAvatar_int);

                       if(result){
                           Toast.makeText(RegisterPage.this,"Success!",Toast.LENGTH_LONG).show();
                           editor.commit();
                       }
                       else{
                           Toast.makeText(RegisterPage.this,"Error!",Toast.LENGTH_LONG).show();
                       }
                       
                       Intent intent_mainPage = new Intent(getApplicationContext(),MainPage.class);
                       startActivity(intent_mainPage);
                       finish();
                   }

               }


           }
       });
        radioGroupAvatar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedAvatar_int = radioGroupAvatar.indexOfChild(findViewById(radioGroupAvatar.getCheckedRadioButtonId()));
                switch (selectedAvatar_int){
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
        });

        btn_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent_login);
                finish();
            }
        });

    }
}
