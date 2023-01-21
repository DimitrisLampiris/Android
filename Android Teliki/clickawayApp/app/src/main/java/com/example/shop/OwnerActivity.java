package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class OwnerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        mAuth = FirebaseAuth.getInstance();
    }

    public void edit(View view)
    {
        startActivity(new Intent(getApplicationContext(), EditInfoActivity.class));
        finish();
    }

    public void logout(View view)
    {
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }


    public void addProduct(View view)
    {
        startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
        finish();
    }

}