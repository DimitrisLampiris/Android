package com.example.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import Model.Order;

public class CartActivity extends AppCompatActivity {
    private ArrayList<Order> orderList;
    private  TextView total;
    private  ListView list;
   private  int productprice;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ArrayList<String> m= new ArrayList<String>();
    private  ArrayAdapter<String> adapter;
    private String checkeditem;
    private Map<String, Integer> rest_amount ;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        list=findViewById(R.id.listView);
        total = findViewById(R.id.textView5);
        rest_amount= (Map<String, Integer>) getIntent().getSerializableExtra("amountHash");
        orderList = bundle.getParcelableArrayList("orderlist");
        if (orderList.size()>0){
            Toast.makeText(getApplicationContext(), "list to buy", Toast.LENGTH_LONG).show();
        }
        for (Order item:orderList)
        {

            String fullorder = item.getTitle() +" "+"x"+ item.getAmount() +" "+item.getPrice();
            String[] arrSplit =fullorder.split(" ");
            productprice = productprice + Integer.parseInt(arrSplit[arrSplit.length-1]);
            m.add(fullorder);

        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,m);
        list.setAdapter(adapter);
       total.setText(String.valueOf(productprice) + String.valueOf("$"));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p, long id)
            {
                p = list.getCheckedItemPosition();
                 position = p;
                 checkeditem = m.get(p).toString();
            }
        });

    }

   public void complete_purchase(View view){
       String currentTime = Calendar.getInstance().getTime().toString();


       int i=1;
       for (Order item:orderList) {
           myRef = database.getReference("Orders").child(user.getUid()).child(currentTime).child("item"+String.valueOf(i));

           myRef.child("Type").setValue(item.getTitle());
           myRef.child("Amount").setValue(item.getAmount());



           myRef=database.getReference(item.getProductId());
           myRef.child("amount").setValue(rest_amount.get(item.getProductId()).toString());
           i++;
       }
       Toast.makeText(getApplicationContext(), "Your order is completed", Toast.LENGTH_LONG).show();
      orderList.removeAll(orderList);
       rest_amount.clear();
       Intent intent2 = new Intent(getApplicationContext(),MainActivity3.class);
       Bundle bundle = new Bundle();
       intent2.putExtra("amountHash", (Serializable) rest_amount);
       bundle.putParcelableArrayList("orderlist",  orderList);
       intent2.putExtras(bundle);
       startActivity(intent2);
   }

   public void delete(View view)
    {
        String[] Split =checkeditem.split(" ");
        int currentprice =  Integer.parseInt(Split[Split.length-1]);
        productprice = productprice - currentprice;
        total.setText(String.valueOf(productprice) + String.valueOf("$"));
        m.remove(position);
        Order order=orderList.get(position);
        rest_amount.put(order.getProductId(),rest_amount.get(order.getProductId())-Integer.parseInt(order.getAmount()));
        orderList.remove(position);

        adapter.notifyDataSetChanged();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(),MainActivity3.class);
            intent.putExtra("orderlist",orderList);
            intent.putExtra("amountHash", (Serializable) rest_amount);
            startActivity(intent);

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}