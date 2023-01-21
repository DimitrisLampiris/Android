package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import Model.Data;
import Model.Order;

public class BuffanDetails extends AppCompatActivity {

    private ImageView img;
    private TextView title,price,posot,am;

    private int count;
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private String mamount;
    private String mtitle;
    private String mimage;
    private String mprice;
    private String id;
    private Order order;
    private ArrayList<Order> orderList=null;
    private Map<String, Integer> rest_amount ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffan_details);
        Bundle bundle = getIntent().getExtras();
        order=new Order();
        orderList = bundle.getParcelableArrayList("orderlist");
        img=findViewById(R.id.image);
        title=findViewById(R.id.title_dt);
        price=findViewById(R.id.price_dt);
        posot=findViewById(R.id.textView6);
        am=findViewById(R.id.posotita);
        db = FirebaseDatabase.getInstance();

        count=0;

        Intent intent = getIntent();
         mtitle=intent.getStringExtra("title");
         mprice=intent.getStringExtra("price");
        mamount=intent.getStringExtra("amount");
         mimage=intent.getStringExtra("image");
        id=intent.getStringExtra("productId");
        rest_amount= (Map<String, Integer>) getIntent().getSerializableExtra("amountHash");
        title.setText(mtitle);
        price.setText(mprice);
        if (rest_amount.containsKey(id)){
            posot.setText(rest_amount.get(id).toString());
        }
        else
            posot.setText(mamount);


        Picasso.get().load(mimage).networkPolicy(NetworkPolicy.OFFLINE).into(img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

                Picasso.get().load(mimage).into(img);

            }
        });


    }

    public void addToCart(View view)
    {
        int total_amount;
        if (rest_amount.containsKey(id)){
            total_amount=rest_amount.get(id);
        }
        else
            total_amount=Integer.parseInt(mamount);
        int price=Integer.parseInt(  mprice.substring(0, mprice.length() - 1))*count;
        //Intent intent = getIntent();
        //String uid=intent.getStringExtra("id");
        //Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_LONG).show();
        total_amount=total_amount-Integer.parseInt((String) am.getText());
        rest_amount.put(id,total_amount);
        order.setAmount((String) am.getText());
        order.setPrice(String.valueOf(price));
        order.setTitle(mtitle);
        order.setProductId(id);
        Intent intent2 = new Intent(getApplicationContext(),MainActivity3.class);
        Bundle bundle = new Bundle();
        orderList.add(order);

        bundle.putParcelableArrayList("orderlist",  orderList);
        intent2.putExtras(bundle);
        intent2.putExtra("amountHash", (Serializable) rest_amount);
        startActivity(intent2);
    }


    public void addProduct(View view)
    {
        if(count<Integer.parseInt((String) posot.getText())) {
            count = count + 1;
            String a = String.valueOf(count);
            am.setText(a);
        }
    }
    public void removeProduct(View view)
    {
        if(count>0) {
            count = count - 1;
            String a = String.valueOf(count);
            am.setText(a);
        }
    }
}