package com.example.instantdabba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.graphics.Color.WHITE;

public class UserCart extends AppCompatActivity {
    TextView quantity,price,mobile,totalprice,ok,cncl;
    FirebaseDatabase database;
    DatabaseReference databaseReference,databaseReference1;
    SharedPreferences sharedPreferences;
    String userid,serviceid,amount;
    ImageView pending,confirmed,cancelled;
    Button back;
    int c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        quantity= findViewById(R.id.quantity);
        price=findViewById(R.id.itemprices);
        mobile=findViewById(R.id.Mobile);
        totalprice=findViewById(R.id.totalprice);
        pending=findViewById(R.id.circle_pending);
        confirmed=findViewById(R.id.circle_confirm);
        cancelled=findViewById(R.id.circle_cancel);
        pending=findViewById(R.id.circle_pending);
        back=findViewById(R.id.back);
        cncl=findViewById(R.id.cncl);
        Intent intent = getIntent();
        String amount = intent.getStringExtra("Quantity");
        String rate = intent.getStringExtra("price");
        final String contact = intent.getStringExtra("Mobile");
        serviceid = intent.getStringExtra("Serviceid");
        int i = Integer.parseInt(amount)*Integer.parseInt(rate)+20;
        String s= String.valueOf(i);
        totalprice.setText(s);
        quantity.setText(amount);
        mobile.setText(contact);
        price.setText(rate);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Order");
        databaseReference1 = database.getReference("Orderstatus");
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        userid = sharedPreferences.getString("id",null);
        String mob =sharedPreferences.getString("number",null);
        final OrderPojo userPojo = new OrderPojo();
        userPojo.setUserid(userid);
        userPojo.setServiceid(serviceid);
        userPojo.setTotalprice(totalprice.getText().toString());
        userPojo.setQuantity(amount);
        userPojo.setPrice(rate);
        userPojo.setMobile(mob);
        databaseReference.child(serviceid).setValue(userPojo);
        statusPojo se =new statusPojo();
        se.setSerid(userPojo.getServiceid());
        se.setStatus("pending");
        se.setUserid(userPojo.getUserid());
        databaseReference1.child(serviceid).setValue(se);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserCart.this,userDash.class);
                startActivity(i);
                finish();
            }
        });

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    statusPojo userPojo = dataSnapshot1.getValue(statusPojo.class);
                    if ((userPojo.getSerid()).equals(serviceid)) {
                        if (userPojo.getStatus().equals("pending")) {
                            pending.setImageResource(R.drawable.green);
                            confirmed.setImageResource(R.drawable.circle);
                            cncl.setBackground(getDrawable(R.drawable.shape2));
                            cncl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    database = FirebaseDatabase.getInstance();
                                    databaseReference = database.getReference("Order");
                                    databaseReference.child(serviceid).removeValue();
                                    databaseReference1 = database.getReference("Orderstatus");
                                    databaseReference1.child(serviceid).removeValue();
                                    SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("flag","0");
                                    editor.commit();
                                    Intent i = new Intent(UserCart.this, userDash.class);
                                    startActivity(i);
                                    finish();
                                }
                            });

                        } else {
                            if (userPojo.getStatus().equals("confirmed")) {
                                pending.setImageResource(R.drawable.circle);
                                confirmed.setImageResource(R.drawable.green);
                                cncl.setBackground(getDrawable(R.drawable.shape3));
                            }
                            if (userPojo.getStatus().equals("cancelled")) {
                                pending.setImageResource(R.drawable.circle);
                                confirmed.setImageResource(R.drawable.circle);
                                cancelled.setImageResource(R.drawable.red);
                                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("flag", 0);
                                editor.commit();

                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserCart.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
