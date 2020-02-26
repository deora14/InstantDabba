package com.example.instantdabba;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class UserInfo extends AppCompatActivity {
    TextView name,mobile,email,address;
    FirebaseDatabase database,database1;
    DatabaseReference databaseReference,databaseReference1,data2;
    String id,result,f;
    ProgressDialog progressDialog;
    Button back,location,delivered;
    String loc,serid;
    UserPojo userPojo;
    String us,sr,mb,p,q,s,t,usid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        back=findViewById(R.id.back);
        address=findViewById(R.id.address);
        location=findViewById(R.id.location);
        delivered=findViewById(R.id.delivered);
        Intent intent = getIntent();
        id = intent.getStringExtra("userid");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("userInfo");
        database1 = FirebaseDatabase.getInstance();
        databaseReference1 = database1.getReference("Orderstatus");
        data2=database1.getReference("Order");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        loc=sharedPreferences.getString("location",null);
        serid=sharedPreferences.getString("id",null);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            checkLocationPermission();
        }
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] splitString = result.split(" ");
                Double lat,longi;
                lat = Double.parseDouble(splitString[0]);
                longi = Double.parseDouble(splitString[1]);
                String location;
                String[] split = loc.split(" ");
                double la = Double.parseDouble(split[0]);
                double lon = Double.parseDouble(split[1]);
             /*   location="geo:"+la+","+lon+"&"+lat+","+longi;*/
                location= "http://maps.google.com/maps?f=d&hl=en&saddr="+la+","+lon+"&daddr="+lat+","+longi;
                Uri gmmIntentUri = Uri.parse(location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            }
        });
        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putString("sas","pending");
                editor.commit();
                UserPojo s = new UserPojo();
                s.setPassword(userPojo.getPassword());
                s.setConpass(userPojo.getPassword());
                s.setAddress(userPojo.getAddress());
                s.setId(userPojo.getId());
                s.setLocationuser(userPojo.getLocationuser());
                s.setMobile(userPojo.getMobile());
                s.setFlag("0");
                s.setEmail(userPojo.getEmail());
                s.setName(userPojo.getName());
                databaseReference.child(id).setValue(s);
                databaseReference1.child(serid).removeValue();
                data2.child(serid).removeValue();
                Intent i = new Intent(UserInfo.this,DabbawalaDash.class);
                startActivity(i);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserInfo.this,DabbawalaDash.class);
                startActivity(i);
                finish();
            }
        });
        usid =sharedPreferences.getString("id",null);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    userPojo = dataSnapshot1.getValue(UserPojo.class);
                    if((userPojo.getId()).equals(id)){
                        name.setText(userPojo.getName());
                        mobile.setText(userPojo.getMobile());
                        email.setText(userPojo.getEmail());
                        address.setText(userPojo.getAddress());
                        result = userPojo.getLocationuser();
                        progressDialog.cancel();
                        break;
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(UserInfo.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });




    }
    public static final int MY_PERMISSION_REQUEST_LOCATION=99;
    public  boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }
            return  false;
        }
        else{
            return true;
        }

    }
    public void OnRequestPermissionResult(int requestCode,String permissions[],int[] grantResults){
        switch (requestCode){
            case MY_PERMISSION_REQUEST_LOCATION:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }
                else {
                    Toast.makeText(this,"permission denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}

