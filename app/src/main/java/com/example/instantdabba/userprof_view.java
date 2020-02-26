package com.example.instantdabba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class userprof_view extends AppCompatActivity {
    TextView name, email, mobile, pass, conpass, address;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprof_view);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        back = findViewById(R.id.SignUp);
        address = findViewById(R.id.address);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        String username=sharedPreferences.getString("userName",null);
        String number=sharedPreferences.getString("number",null);
        String mail=sharedPreferences.getString("email",null);
        String Address=sharedPreferences.getString("address",null);
        name.setText(username);
        email.setText(mail);
        mobile.setText(number);
        address.setText(Address);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(userprof_view.this,userDash.class);
                startActivity(i);
                finish();
            }
        });

    }
}
