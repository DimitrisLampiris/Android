package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;

public class EditInfoActivity extends AppCompatActivity {

    EditText name,address,phone;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        database = FirebaseDatabase.getInstance();

        name = findViewById(R.id.editTextTextPersonName);
        address = findViewById(R.id.editTextTextPersonName3);
        phone = findViewById(R.id.editTextTextPersonName5);

        myRef2 = database.getInstance().getReference().child("Informations");
        myRef2.addValueEventListener(new ValueEventListener() {
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

    public void editinfo(View view) {
        myRef = database.getReference("Informations");
        String n = name.getText().toString();
        String a = address.getText().toString();
        String p = phone.getText().toString();

        if (!a.equals("") && !n.equals("") && !p.equals("")) {
            HashMap hashmap = new HashMap();
            hashmap.put("name", n);
            hashmap.put("address", a);
            hashmap.put("phone", p);

            myRef.updateChildren(hashmap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(EditInfoActivity.this, "Your data is syccessfully updated", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    address.setText("");
                    phone.setText("");

                    startActivity(new Intent(getApplicationContext(), OwnerActivity.class));
                    finish();

                }
            });

        }
        else Toast.makeText(EditInfoActivity.this,"Please complete all the fields!",Toast.LENGTH_SHORT).show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(),OwnerActivity.class);


            startActivity(intent);

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}