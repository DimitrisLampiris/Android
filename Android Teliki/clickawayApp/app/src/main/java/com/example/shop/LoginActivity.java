package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView text;
    Button bu;
    EditText email;
    EditText password;
    ProgressBar bar;
    FirebaseAuth auth;
    FirebaseFirestore store;
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        store = FirebaseFirestore.getInstance();
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        text = findViewById(R.id.textView4);
        bu = findViewById(R.id.button2);
        email = findViewById(R.id.editTextTextPersonName2);
        password = findViewById(R.id.editTextTextPersonName4);
        bar = findViewById(R.id.progressBar2);
        auth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        String e = email.getText().toString();
        String p = password.getText().toString();

        if (e.isEmpty() || e == null) {
            email.setError("Email is required!");
            return;
        }


        if (p.length() < 6) {
            password.setError("Password must be at least 6 characters!");
            return;
        }
        bar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String user_id = auth.getCurrentUser().getUid();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("id", user_id);
                    editor.apply();
                    check(user_id);
                   // Toast.makeText(LoginActivity.this, "Login Successfully" , Toast.LENGTH_SHORT).show();
                   // startActivity(new Intent(getApplicationContext(), MainActivity3.class));
                   // finish();

                } else {
                    Toast.makeText(getApplicationContext(),
                            task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

   public void check(String uid)
    {
        DocumentReference ref = store.collection("Users").document(uid);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("Type").equals("Owner"))
                {
                    startActivity(new Intent(getApplicationContext(), OwnerActivity.class));
                    finish();
                }
                else
                    {
                        startActivity(new Intent(getApplicationContext(), MainActivity3.class));
                        finish();
                    }
            }
        });
    }


    public void gotoregister(View view)
    {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
        finish();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}