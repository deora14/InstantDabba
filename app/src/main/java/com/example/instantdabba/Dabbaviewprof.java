package com.example.instantdabba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Dabbaviewprof extends AppCompatActivity {
    TextView ownnam,sername,email,mobile,pass,conpass,rate,include;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dabbaviewprof);
        ownnam = findViewById(R.id.ownername);
        sername = findViewById(R.id.username);
        email =findViewById(R.id.email);
        mobile =findViewById(R.id.mobile);
        pass =findViewById(R.id.pass);
        conpass = findViewById(R.id.repass);
        include =findViewById(R.id.include);
        rate = findViewById(R.id.price);
        back= findViewById(R.id.SignUp);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        String username=sharedPreferences.getString("Ownnam",null);
        String sernamer=sharedPreferences.getString("Sername",null);
        String mail=sharedPreferences.getString("email",null);
        String number=sharedPreferences.getString("number",null);
        String price=sharedPreferences.getString("price",null);
        String menu=sharedPreferences.getString("menu",null);
        ownnam.setText(username);
       sername.setText(sernamer);
       email.setText(mail);
       mobile.setText(number);
       include.setText(menu);
       rate.setText(price);
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i =new Intent(Dabbaviewprof.this,DabbawalaDash.class);
               startActivity(i);
               finish();
           }
       });
    }
}
