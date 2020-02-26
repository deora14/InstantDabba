package com.example.instantdabba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DabbawalaDash extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<OrderPojo> arrayList = new ArrayList<>();
    FirebaseDatabase database,data;
    DatabaseReference databaseReference,data1;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dabbawala_dash);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        LinearLayout linearLayout =findViewById(R.id.linear_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Order");
        data =FirebaseDatabase.getInstance();
        data1 = data.getReference("dabbawalaInfo");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getDataFromFirebase();
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        id =sharedPreferences.getString("id",null);


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
                ServicePojo s = new ServicePojo();
                s.setMenu(menu);
                s.setLocation(loc);
                s.setPrice(price);
                s.setMobile(number);
                s.setOwnnam(username);
                s.setImageUrl(ig);
                s.setId(id);
                s.setSername(sernamer);
                s.setEmail(mail);
                s.setSas(sas);
                s.setPass(passe);
                s.setConpass(passe);
                data1.child(id).setValue(s);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(DabbawalaDash.this, DabbawalaLog.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_view:

                Intent intent1 = new Intent(DabbawalaDash.this,Dabbaviewprof.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.menu_edit:

                Intent intent2 = new Intent(DabbawalaDash.this,dabbaedit.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.menu_pass:

                Intent intent3 = new Intent(DabbawalaDash.this,dabbachanpass.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.menu_refresh:
                startActivity(getIntent());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getDataFromFirebase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    OrderPojo userPojo = dataSnapshot1.getValue(OrderPojo.class);
                    if((userPojo.getServiceid().equals(id))) {
                        arrayList.add(userPojo);
                    }
                }
                progressDialog.cancel();
                Custom_Adapter customAdapter = new Custom_Adapter(DabbawalaDash.this, arrayList);
                recyclerView.setAdapter(customAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(DabbawalaDash.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
