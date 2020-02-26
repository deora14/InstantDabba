package com.example.instantdabba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DabbawalaLog extends AppCompatActivity {
    EditText username,pass;

    Button Login;
    TextView SignUp,user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DataSnapshot dataSnapshot;
    int flag=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dabbawala_log);
        username = findViewById(R.id.username);
        pass= findViewById(R.id.pass);
        SignUp= findViewById(R.id.SignUp);
        Login= findViewById(R.id.Login);
        user= findViewById(R.id.userlog);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("dabbawalaInfo");
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        Boolean status=sharedPreferences.getBoolean("logInStatus",false);
        if (status==true){
            Intent intent=new Intent(DabbawalaLog.this,DabbawalaDash.class);
            startActivity(intent);
            finish();
        }
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DabbawalaLog.this,userLogin.class);
                startActivity(intent);
                finish();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(DabbawalaLog.this,Dabbaloc.class);
                startActivity(i);
                finish();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName=username.getText().toString();
                final String password=pass.getText().toString();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            ServicePojo userPojo=dataSnapshot1.getValue(ServicePojo.class);
                            String fUserName=userPojo.getEmail();
                            String fPassword=userPojo.getPass();
                            if(userName.equals(fUserName)&&password.equals(fPassword)){
                                SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
                                SharedPreferences.Editor editor =sharedPreferences.edit();
                                editor.putString("Ownnam",userPojo.getOwnnam());
                                editor.putString("Sername",userPojo.getSername());
                                editor.putString("password",userPojo.getPass());
                                editor.putString("email",userPojo.getEmail());
                                editor.putString("number",userPojo.getMobile());
                                editor.putString("imageUrl",userPojo.getImageUrl());
                                editor.putString("price",userPojo.getPrice());
                                editor.putString("menu",userPojo.getMenu());
                                editor.putString("id",userPojo.getId());
                                editor.putString("location",userPojo.getLocation());
                                editor.putString("sas",userPojo.getSas());
                                editor.putString("us",null);
                                editor.putBoolean("status",true);
                                editor.putString("val","Dabbawala");

                                editor.putBoolean("logInStatus",true);
                                editor.apply();
                                flag=1;
                                break;
                            }
                        }
                        if (flag==1){
                            Intent intent=new Intent(DabbawalaLog.this,DabbawalaDash.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(DabbawalaLog.this, "EMAIL AND PASSWORD ARE INCORRECT", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DabbawalaLog.this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
