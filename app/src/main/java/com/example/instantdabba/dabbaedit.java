package com.example.instantdabba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dabbaedit extends AppCompatActivity {
    EditText ownnam,sername,email,mobile,pass,conpass,rate,include;
    Button back,Update;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dabbaedit);
        ownnam = findViewById(R.id.ownername);
        sername = findViewById(R.id.username);
        email =findViewById(R.id.email);
        mobile =findViewById(R.id.mobile);
        pass =findViewById(R.id.pass);
        conpass = findViewById(R.id.repass);
        include =findViewById(R.id.include);
        rate = findViewById(R.id.price);
        Update= findViewById(R.id.SignUp);
        back= findViewById(R.id.back);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("dabbawalaInfo");
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        String username=sharedPreferences.getString("Ownnam",null);
        String sernamer=sharedPreferences.getString("Sername",null);
        String mail=sharedPreferences.getString("email",null);
        String number=sharedPreferences.getString("number",null);
        String price=sharedPreferences.getString("price",null);
        String menu=sharedPreferences.getString("menu",null);
        final String sas=sharedPreferences.getString("sas",null);
        final String pass=sharedPreferences.getString("password",null);
        final String ig=sharedPreferences.getString("imageUrl",null);
        final String id=sharedPreferences.getString("id",null);
        final String loc=sharedPreferences.getString("location",null);
        ownnam.setText(username);
        sername.setText(sernamer);
        email.setText(mail);
        mobile.setText(number);
        include.setText(menu);
        rate.setText(price);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dabbaedit.this, DabbawalaLog.class);
                startActivity(intent);
                finish();
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicePojo s = new ServicePojo();
                s.setMenu(include.getText().toString());
                s.setLocation(loc);
                s.setPrice(rate.getText().toString());
                s.setMobile(mobile.getText().toString());
                s.setOwnnam(ownnam.getText().toString());
                s.setImageUrl(ig);
                s.setId(id);
                s.setSername(sername.getText().toString());
                s.setMobile(mobile.getText().toString());
                s.setPass(pass);
                s.setConpass(pass);
                s.setSas(sas);
                databaseReference.child(id).setValue(s);
                Intent intent = new Intent(dabbaedit.this, DabbawalaLog.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
