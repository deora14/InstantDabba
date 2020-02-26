package com.example.instantdabba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userDash extends AppCompatActivity {
    ImageView imageView;
    TextView Name;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<ServicePojo> arrayList = new ArrayList<>();
    FirebaseDatabase database,data;
    DatabaseReference databaseReference,data1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("dabbawalaInfo");
        data =FirebaseDatabase.getInstance();
        data1 = data.getReference("userInfo");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getDataFromFirebase();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
                String username=sharedPreferences.getString("userName",null);
                String number=sharedPreferences.getString("number",null);
                String mail=sharedPreferences.getString("email",null);
                String Address=sharedPreferences.getString("address",null);
                final String loc=sharedPreferences.getString("location",null);
                final String pass=sharedPreferences.getString("password",null);
                final String id=sharedPreferences.getString("id",null);
                final String flag=sharedPreferences.getString("flag",null);
                UserPojo pojo =new UserPojo();
                pojo.setName(username);
                pojo.setAddress(Address);
                pojo.setEmail(mail);
                pojo.setMobile(number);
                pojo.setConpass(pass);
                pojo.setPassword(pass);
                pojo.setId(id);
                pojo.setFlag("0");
                pojo.setLocationuser(loc);
                data1.child(id).setValue(pojo);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(userDash.this, userLogin.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_view:

                Intent intent1 = new Intent(userDash.this,userprof_view.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.menu_edit:

                Intent intent2 = new Intent(userDash.this,useredit.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.menu_pass:

                Intent intent3 = new Intent(userDash.this,userchangepass.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.menu_refresh:
                startActivity(getIntent());
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getDataFromFirebase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    ServicePojo userPojo = dataSnapshot1.getValue(ServicePojo.class);
                        arrayList.add(userPojo);
                }
                progressDialog.cancel();
                CustomAdapter customAdapter = new CustomAdapter(userDash.this, arrayList);
                recyclerView.setAdapter(customAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(userDash.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
