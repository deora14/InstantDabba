package com.example.instantdabba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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

public class dabbawalaSignup extends AppCompatActivity {
    EditText ownnam,sername,email,mobile,pass,conpass,rate,include;
    ImageView serphoto;
    SelectImageHelper selectImageHelper;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView login;
    Button signup;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dabbawala_signup);
        ownnam = findViewById(R.id.ownername);
        sername = findViewById(R.id.username);
        email =findViewById(R.id.email);
        mobile =findViewById(R.id.mobile);
        login =findViewById(R.id.login);
        pass =findViewById(R.id.pass);
        conpass = findViewById(R.id.repass);
        include =findViewById(R.id.include);
        rate = findViewById(R.id.price);
        serphoto = findViewById(R.id.l);
        signup = findViewById(R.id.SignUp);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Intent intent = getIntent();
        location = intent.getStringExtra("location");

        // Instance for Database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("dabbawalaInfo");

        selectImageHelper = new SelectImageHelper(this, serphoto);

        serphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImageHelper.selectImageOption();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(dabbawalaSignup.this,DabbawalaLog.class);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        selectImageHelper.handleResult(requestCode, resultCode, result);  // call this helper class method
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        selectImageHelper.handleGrantedPermisson(requestCode, grantResults);   // call this helper class method
    }
    public void signupdata(){
        final ProgressDialog dialog = new ProgressDialog(dabbawalaSignup.this);
        final String name = ownnam.getText().toString();
        final String Service = sername.getText().toString();
        final String Email = email.getText().toString();
        final String Mobile = mobile.getText().toString();
        final String password = pass.getText().toString();
        final String retype= conpass.getText().toString();
        final String price = rate.getText().toString();
        final String menu= include.getText().toString();
        final String id = databaseReference.push().getKey();
        boolean invalid = false;
        if (name.equals("")) {
            invalid = true;
            Toast.makeText(getApplicationContext(), "Enter your name",
                    Toast.LENGTH_SHORT).show();
            ownnam.setError("Enter name");
        } else
            if(Service.equals("")) {
                invalid = true;
                Toast.makeText(getApplicationContext(), "Enter your name",
                        Toast.LENGTH_SHORT).show();
                sername.setError("Enter name");
            }
            else{

        if (Mobile.equals("")) {
            invalid = true;
            Toast.makeText(getApplicationContext(), "Enter your number",
                    Toast.LENGTH_SHORT).show();
            mobile.setError("enter number");
        } else

        if (Email.equals("")) {
            invalid = true;
            Toast.makeText(getApplicationContext(), "Enter your mail",
                    Toast.LENGTH_SHORT).show();
            email.setError("enter mail");
        } else
        if(price.equals("")) {
            invalid = true;
            Toast.makeText(getApplicationContext(), "Enter price",
                    Toast.LENGTH_SHORT).show();
            rate.setError("enter price");

        }  else
        if(menu.equals("")) {
            invalid = true;
            Toast.makeText(getApplicationContext(), "Enter menu",
                    Toast.LENGTH_SHORT).show();
            include.setError("enter menu");

        }  else
        if (password.equals("")) {
            invalid = true;
            Toast.makeText(getApplicationContext(), "Enter Password",
                    Toast.LENGTH_SHORT).show();
            pass.setError("enter password");
        }
            else

        if (retype.equals("")) {
            invalid = true;
            Toast.makeText(getApplicationContext(), "Re-type Password",
                    Toast.LENGTH_SHORT).show();
            conpass.setError("Re enter password");
        }else

        if(!pass.getText().toString().equals(conpass.getText().toString())){
            invalid = true;
            Toast.makeText(getApplicationContext(),"password not match", Toast.LENGTH_SHORT).show();
            conpass.setError("password not match");
        }else

        if(pass.length()<6){
            invalid=true;
            Toast.makeText(getApplicationContext(), "Please enter atleast 6 digit password",
                    Toast.LENGTH_SHORT).show();
            pass.setError("enter atleast 6 digit password");
        }else

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            invalid = true;
            Toast.makeText(getApplicationContext(), "enter valid mail", Toast.LENGTH_SHORT).show();
            email.setError("enter valid email");
        }else
        if(!Patterns.PHONE.matcher(Mobile).matches()){
            invalid = true;
            Toast.makeText(getApplicationContext(), "enter valid number", Toast.LENGTH_SHORT).show();
            mobile.setError("enter valid no.");
        }
        else{
            Uri uri = selectImageHelper.getURI_FOR_SELECTED_IMAGE();
            if (uri != null) {
                final StorageReference reference = storageReference.child("/Image/" + email);
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("1234", "Download Url" + uri.toString());

                                ServicePojo userPojo = new ServicePojo();
                                userPojo.setEmail(Email);
                                userPojo.setId(id);
                                userPojo.setOwnnam(name);
                                userPojo.setSername(Service);
                                userPojo.setPass(password);
                                userPojo.setConpass(password);
                                userPojo.setMobile(Mobile);
                                userPojo.setPrice(price);
                                userPojo.setMenu(menu);
                                userPojo.setImageUrl(uri.toString());
                                userPojo.setLocation(location);
                                userPojo.setSas("pending");
                                dialog.cancel();
                                databaseReference.child(id).setValue(userPojo);
                                Intent intent = new Intent(dabbawalaSignup.this, DabbawalaLog.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(dabbawalaSignup.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }}}}}