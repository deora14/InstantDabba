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


public class userLogin extends AppCompatActivity {
    EditText username,pass;

    Button Login;
    TextView Signup,user;
    FirebaseDatabase firebaseDatabase,fire1;
    DatabaseReference databaseReference,data1;
    DataSnapshot dataSnapshot;
    int flag;
    int fla=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        username= findViewById(R.id.username);
        pass= findViewById(R.id.pass);
        Signup= findViewById(R.id.SignUp);
        Login= findViewById(R.id.Login);
        user= findViewById(R.id.userlog);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("userInfo");
        fire1=FirebaseDatabase.getInstance();
        data1=fire1.getReference("state");
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        Boolean status=sharedPreferences.getBoolean("logInStatus",false);
        if (status==true){
            Intent intent=new Intent(userLogin.this,userDash.class);
            startActivity(intent);
            finish();
        }
        user.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(userLogin.this, DabbawalaLog.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(userLogin.this,location.class);
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
                            UserPojo userPojo=dataSnapshot1.getValue(UserPojo.class);
                            String fUserName=userPojo.getEmail();
                            String fPassword=userPojo.getPassword();
                            if(userName.equals(fUserName)&&password.equals(fPassword)){
                                SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
                                SharedPreferences.Editor editor =sharedPreferences.edit();
                                editor.putString("userName",userPojo.getName());
                                editor.putString("password",userPojo.getPassword());
                                editor.putString("email",userPojo.getEmail());
                                editor.putString("number",userPojo.getMobile());
                                editor.putString("id",userPojo.getId());
                                editor.putString("location",userPojo.getLocationuser());
                                editor.putBoolean("logInStatus",true);
                                editor.putString("address",userPojo.getAddress());
                                editor.putBoolean("status",true);
                                editor.putString("val","user");
                                editor.putString("flag",userPojo.getFlag());
                                editor.apply();
                                fla=1;
                                break;
                            }
                        }
                        if (fla==1){
                            Intent intent=new Intent(userLogin.this,userDash.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(userLogin.this, "EMAIL AND PASSWORD ARE INCORRECT", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(userLogin.this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
