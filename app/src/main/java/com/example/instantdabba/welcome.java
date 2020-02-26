package com.example.instantdabba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class welcome extends AppCompatActivity {
    Button user,dabba;
    boolean status=false;
    String val=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        user = findViewById(R.id.user);
        dabba = findViewById(R.id.dabbawala);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        status=sharedPreferences.getBoolean("status",false);
        val=sharedPreferences.getString("val",null);
       if (status == true) {
            if (val.equals("user")) {
                Intent intent = new Intent(welcome.this, userLogin.class);
                intent.putExtra("val", "user");
                welcome.this.startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(welcome.this, DabbawalaLog.class);
                intent.putExtra("val", "Dabbawala");
                welcome.this.startActivity(intent);
                finish();


            }
        }
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(welcome.this, userLogin.class);
                    startActivity(intent);
                    finish();

                }
            });
            dabba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(welcome.this, DabbawalaLog.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }