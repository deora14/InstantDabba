package com.example.instantdabba;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class usersignup extends AppCompatActivity {
    EditText name, email, mobile, pass, conpass, address;
    String location;
    TextView login;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Button signup;
    AppLocationService appLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersignup);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        pass = findViewById(R.id.pass);
        conpass = findViewById(R.id.repass);
        signup = findViewById(R.id.SignUp);
        address = findViewById(R.id.address);
        login =findViewById(R.id.login);
        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("userInfo");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(usersignup.this,userLogin.class);
                startActivity(i);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signupdata();
            }
        });
        }

    public void signupdata() {
        final String Name = name.getText().toString();
        final String Email = email.getText().toString();
        final String Mobile = mobile.getText().toString();
        final String password = pass.getText().toString();
        final String retype = conpass.getText().toString();
        final String id = databaseReference.push().getKey();
        boolean invalid = false;
        if (name.equals("")) {
            invalid = true;
            Toast.makeText(getApplicationContext(), "Enter your name",
                    Toast.LENGTH_SHORT).show();
            name.setError("Enter name");
        } else {

            if (Mobile.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Enter your number",
                        Toast.LENGTH_SHORT).show();
                mobile.setError("enter number");
            } else if (Email.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Enter your mail",
                        Toast.LENGTH_SHORT).show();
                email.setError("enter mail");
            } else if (password.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Enter Password",
                        Toast.LENGTH_SHORT).show();
                pass.setError("enter password");
            } else if (retype.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Re-type Password",
                        Toast.LENGTH_SHORT).show();
                conpass.setError("Re enter password");
            } else if (!pass.getText().toString().equals(conpass.getText().toString())) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "password not match", Toast.LENGTH_SHORT).show();
                conpass.setError("password not match");
            } else if (pass.length() < 6) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Please enter atleast 6 digit password",
                        Toast.LENGTH_SHORT).show();
                pass.setError("enter atleast 6 digit password");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "enter valid mail", Toast.LENGTH_SHORT).show();
                email.setError("enter valid email");
            } else if (!Patterns.PHONE.matcher(Mobile).matches()) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "enter valid number", Toast.LENGTH_SHORT).show();
                mobile.setError("enter valid no.");
            } else
                if(address.getText().toString().equals("")){
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "enter valid address", Toast.LENGTH_SHORT).show();
                    mobile.setError("enter valid address");

                }
                else{

                UserPojo userPojo = new UserPojo();
                userPojo.setEmail(Email);
                userPojo.setId(id);
                userPojo.setName(Name);
                userPojo.setPassword(password);
                userPojo.setConpass(password);
                userPojo.setMobile(Mobile);
                userPojo.setLocationuser(location);
                userPojo.setAddress(address.getText().toString());
                userPojo.setFlag("0");
                databaseReference.child(id).setValue(userPojo);
                Intent intent = new Intent(usersignup.this, userLogin.class);
                startActivity(intent);
                finish();
            }
        }
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            address.setText(locationAddress);
        }
    }


}