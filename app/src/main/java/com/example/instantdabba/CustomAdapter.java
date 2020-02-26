package com.example.instantdabba;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<ServicePojo> values;
    private Context context;
    String flag;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public CustomAdapter(Context context, ArrayList<ServicePojo> myDataset) {
        values = myDataset;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ServicePojo myPojo = values.get(position);
        holder.name.setText(myPojo.getSername());
        holder.price.setText(myPojo.getPrice());
        holder.menu.setText(myPojo.getMenu());
        AnimationDrawable animationDrawable = (AnimationDrawable) holder.linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Order");
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String s= holder.quantity.getText().toString();
               int i = Integer.parseInt(s);
               int c = i+1;
                String s1 =String.valueOf(c);
                holder.quantity.setText(s1);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= holder.quantity.getText().toString();
                int i = Integer.parseInt(s);
                int c;
                if(i>1)
                 c = i-1;
                else
                    c=i;
                String s1 =String.valueOf(c);
                holder.quantity.setText(s1);
            }
        });



        Glide.with(context).load(myPojo.getImageUrl()).into(holder.icon);
        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("0")) {
                    final Dialog dialog = new Dialog(context);
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.dialog);
                    // Set dialog title
                    dialog.setTitle("Confirmation");
                    dialog.show();
                    Button confirm = (Button) dialog.findViewById(R.id.confirm);
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, UserCart.class);
                            intent.putExtra("Serviceid", myPojo.getId());
                            intent.putExtra("price", myPojo.getPrice());
                            intent.putExtra("Mobile", myPojo.getMobile());
                            intent.putExtra("Quantity", holder.quantity.getText().toString());
                            SharedPreferences sharedPreferences = context.getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            flag="1";
                            editor.putString("flag","1");
                            editor.commit();
                            context.startActivity(intent);
                            ((Activity)context).finish();
                            dialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                else if(flag.equals("1")){
                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            context).create();
                    alertDialog.setTitle("Alert Dialog");
                    alertDialog.setMessage("You have one On going order You can't place another order util the order is not completed ");
                    alertDialog.setIcon(R.drawable.tick);


                    alertDialog.setButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            dialog.dismiss();
                        }
                    });


                    // Showing Alert Message
                    alertDialog.show();
                }
            }
        });
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name,menu,price,quantity;
        public ImageView icon,plus,minus;
        public Button order;
        public LinearLayout linearLayout;
        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            icon = v.findViewById(R.id.icon);
            order =v.findViewById(R.id.add);
            price =v.findViewById(R.id.price);
            menu =v.findViewById(R.id.menu);
            quantity =v.findViewById(R.id.quantity);
            plus =v.findViewById(R.id.plus);
            minus =v.findViewById(R.id.minus);
            linearLayout = v.findViewById(R.id.linearlayout);
            SharedPreferences sharedPreferences=context.getSharedPreferences("data",MODE_PRIVATE);
            flag = sharedPreferences.getString("flag",null);

        }
    }


}