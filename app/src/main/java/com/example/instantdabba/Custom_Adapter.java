package com.example.instantdabba;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Custom_Adapter extends RecyclerView.Adapter<Custom_Adapter.ViewHolder> {

    private ArrayList<OrderPojo> values;
    private Context context;
    FirebaseDatabase database;
    DatabaseReference databaseReference,databaseReference1;
    String us,id,sas;
    Button confirm,cancel;
    Dialog dialog;

    public Custom_Adapter(Context context, ArrayList<OrderPojo> myDataset) {
        values = myDataset;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_order, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderPojo myPojo = values.get(position);
        holder.name.setText(myPojo.getUserid());
        holder.totalprice.setText(myPojo.getTotalprice());
        holder.quantity.setText(myPojo.getQuantity());
        AnimationDrawable animationDrawable = (AnimationDrawable) holder.linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        us = myPojo.getUserid();
        SharedPreferences sharedPreferences=context.getSharedPreferences("data",MODE_PRIVATE);
        id=sharedPreferences.getString("id",null);
        sas=sharedPreferences.getString("sas",null);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Order");
        databaseReference1 = database.getReference("Orderstatus");

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            if ("pending".equals(sas)){
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Confirmation");
                    dialog.show();
                    confirm = dialog.findViewById(R.id.confirm);
                    cancel = dialog.findViewById(R.id.cancel);
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            OrderPojo userPojo = new OrderPojo();
                            userPojo.setUserid(myPojo.getUserid());
                            userPojo.setServiceid(myPojo.getServiceid());
                            userPojo.setTotalprice(myPojo.getTotalprice());
                            userPojo.setPrice(myPojo.getPrice());
                            userPojo.setMobile(myPojo.getMobile());
                            userPojo.setQuantity(myPojo.getQuantity());
                            databaseReference.child(myPojo.getServiceid()).setValue(userPojo);
                            statusPojo n = new statusPojo();
                            n.setUserid(myPojo.getUserid());
                            n.setStatus("confirmed");
                            n.setSerid(myPojo.getServiceid());
                            databaseReference1.child(myPojo.getServiceid()).setValue(n);
                            Intent intent = new Intent(context, UserInfo.class);
                            intent.putExtra("userid", holder.name.getText().toString());
                            intent.putExtra("srid", myPojo.getServiceid());
                            context.startActivity(intent);
                            SharedPreferences sharedPreferences=context.getSharedPreferences("data",MODE_PRIVATE);
                            SharedPreferences.Editor editor =sharedPreferences.edit();
                            editor.putString("sas","confirmed");
                            editor.commit();
                            dialog.dismiss();
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    database = FirebaseDatabase.getInstance();
                                    databaseReference = database.getReference("Order");
                                    OrderPojo userPojo = new OrderPojo();
                                    userPojo.setUserid(myPojo.getUserid());
                                    userPojo.setServiceid(myPojo.getServiceid());
                                    userPojo.setTotalprice(myPojo.getTotalprice());
                                    userPojo.setStatus("cancelled");
                                    userPojo.setMobile(myPojo.getMobile());
                                    userPojo.setQuantity(myPojo.getQuantity());
                                    databaseReference.child(myPojo.getServiceid()).setValue(userPojo);
                                    statusPojo n = new statusPojo();
                                    n.setUserid(myPojo.getUserid());
                                    n.setStatus("cancelled");
                                    n.setSerid(myPojo.getServiceid());
                                    databaseReference1.child(myPojo.getServiceid()).setValue(n);
                                    SharedPreferences sharedPreferences=context.getSharedPreferences("data",MODE_PRIVATE);
                                    SharedPreferences.Editor editor =sharedPreferences.edit();
                                    editor.putString("sas","cancelled");
                                    editor.commit();
                                    dialog.dismiss();
                                    Intent i = new Intent(context, DabbawalaDash.class);
                                    context.startActivity(i);
                                    ((Activity)context).finish();
                                }
                            });
                        }
                    });
                }
                            else {
                                Intent intent = new Intent(context, UserInfo.class);
                                intent.putExtra("userid", myPojo.getUserid());
                                intent.putExtra("f", 0);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                }

        });
    }
    @Override
    public int getItemCount() {
        return values.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,quantity,totalprice;
        public LinearLayout linearLayout;
        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.userid);
            totalprice =v.findViewById(R.id.price);
            quantity=v.findViewById(R.id.quantity);
            linearLayout = v.findViewById(R.id.linearlayout);

        }
    }
}