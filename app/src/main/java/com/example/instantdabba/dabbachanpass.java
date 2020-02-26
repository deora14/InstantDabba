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

public class dabbachanpass extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    EditText pass,newpass, copass;
    Button Update,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dabbachanpass);
        pass = findViewById(R.id.pass);
        newpass = findViewById(R.id.repass);
        copass = findViewById(R.id.copass);
        Update = findViewById(R.id.SignUp);
        back=findViewById(R.id.back);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("dabbawalaInfo");
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        final String username=sharedPreferences.getString("Ownnam",null);
        final String sernamer=sharedPreferences.getString("Sername",null);
        final String mail=sharedPreferences.getString("email",null);
        final String number=sharedPreferences.getString("number",null);
        final String price=sharedPreferences.getString("price",null);
        final String menu=sharedPreferences.getString("menu",null);
        final String passe=sharedPreferences.getString("password",null);
        final String ig=sharedPreferences.getString("imageUrl",null);
        final String id=sharedPreferences.getString("id",null);
        final String loc=sharedPreferences.getString("location",null);
        final String sas=sharedPreferences.getString("sas",null);
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
                        ServicePojo s = new ServicePojo();
                        s.setMenu(menu);
                        s.setLocation(loc);
                        s.setEmail(mail);
                        s.setPrice(price);
                        s.setMobile(number);
                        s.setOwnnam(username);
                        s.setImageUrl(ig);
                        s.setId(id);
                        s.setSername(sernamer);
                        s.setSas(sas);
                        s.setPass(newpass.getText().toString());
                        s.setConpass(newpass.getText().toString());
                        databaseReference.child(id).setValue(s);
                        Intent intent = new Intent(dabbachanpass.this, DabbawalaLog.class);
                        startActivity(intent);
                        finish();
                    }
                }}
        });
    }
}
