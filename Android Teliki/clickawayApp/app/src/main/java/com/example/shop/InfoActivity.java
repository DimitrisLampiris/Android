package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {
    DatabaseReference ref;
    FirebaseDatabase db;
    TextView name,address,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        name = findViewById(R.id.textView9);
       address = findViewById(R.id.textView11);
        phone = findViewById(R.id.textView13);
        ref = db.getInstance().getReference().child("Informations");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String n = snapshot.child("name").getValue().toString();
                String a = snapshot.child("address").getValue().toString();
                String p = snapshot.child("phone").getValue().toString();

                name.setText(n);
                address.setText(a);
                phone.setText(p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}