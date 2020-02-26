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

public class useredit extends AppCompatActivity {
    EditText name, email, mobile,address;
    Button Update,back;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useredit);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        Update = findViewById(R.id.SignUp);
        back = findViewById(R.id.back);
        address = findViewById(R.id.address);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        String username=sharedPreferences.getString("userName",null);
        String number=sharedPreferences.getString("number",null);
        String mail=sharedPreferences.getString("email",null);
        String Address=sharedPreferences.getString("address",null);
        final String loc=sharedPreferences.getString("location",null);
        final String pass=sharedPreferences.getString("password",null);
        final String id=sharedPreferences.getString("id",null);
        final String flag=sharedPreferences.getString("flag",null);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("userInfo");
        name.setText(username);
        email.setText(mail);
        mobile.setText(number);
        address.setText(Address);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(useredit.this,userDash.class);
                startActivity(i);
                finish();

            }
        });
       Update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               UserPojo pojo =new UserPojo();
               pojo.setName(name.getText().toString());
               pojo.setAddress(address.getText().toString());
               pojo.setEmail(email.getText().toString());
               pojo.setMobile(mobile.getText().toString());
               pojo.setConpass(pass);
               pojo.setPassword(pass);
               pojo.setId(id);
               pojo.setFlag(flag);
               pojo.setLocationuser(loc);
               databaseReference.child(id).setValue(pojo);
               Intent i = new Intent(useredit.this,DabbawalaDash.class);
               startActivity(i);
               finish();
           }
       });
    }
}
