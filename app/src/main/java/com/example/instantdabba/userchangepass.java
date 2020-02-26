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

public class userchangepass extends AppCompatActivity {
    EditText pass,newpass, copass;
    Button Update,back;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchangepass);
        pass = findViewById(R.id.pass);
        newpass = findViewById(R.id.repass);
        copass = findViewById(R.id.copass);
        Update = findViewById(R.id.SignUp);
        back=findViewById(R.id.back);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        final String username=sharedPreferences.getString("userName",null);
        final String number=sharedPreferences.getString("number",null);
        final String mail=sharedPreferences.getString("email",null);
        final String Address=sharedPreferences.getString("address",null);
        final String loc=sharedPreferences.getString("location",null);
        final String passe=sharedPreferences.getString("password",null);
        final String id=sharedPreferences.getString("id",null);
        final String flag=sharedPreferences.getString("flag",null);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("userInfo");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(userchangepass.this,userDash.class);
                startActivity(i);
                finish();

            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString()==null||newpass.getText().toString()==null||copass.getText().toString()==null){
                    if(pass.getText().toString()==null)
                        pass.setError("Please enter old pass");
                    else
                        if(newpass.getText().toString()==null)
                            newpass.setError("Please Enter new pass");
                        else
                            copass.setError("Confirm new password");
                }
                else if(pass.getText().toString().equals(passe)){
                    if(newpass.getText().toString().length()<6){
                        newpass.setError("password too short");
                    }
                    else
                        if(newpass.getText().toString().equals(copass.getText().toString())){
                            UserPojo pojo =new UserPojo();
                            pojo.setName(username);
                            pojo.setAddress(Address);
                            pojo.setEmail(mail);
                            pojo.setMobile(number);
                            pojo.setConpass(newpass.getText().toString());
                            pojo.setPassword(newpass.getText().toString());
                            pojo.setId(id);
                            pojo.setLocationuser(loc);
                            pojo.setFlag(flag);
                            databaseReference.child(id).setValue(pojo);
                            Intent i = new Intent(userchangepass.this,userDash.class);
                            startActivity(i);
                            finish();
                        }
            }}
        });
    }
}
