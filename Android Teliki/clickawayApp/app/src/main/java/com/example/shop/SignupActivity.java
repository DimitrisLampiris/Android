package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText email,password;
    Button b;
    TextView login;
    ProgressBar bar;
    FirebaseAuth auth;
    FirebaseFirestore store;
    RadioButton owner,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        b =findViewById(R.id.button);
        login = findViewById(R.id.textView);
        bar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        owner = findViewById(R.id.radioButton);
        user = findViewById(R.id.radioButton2);
    }

    public void register(View view)
    {
        String e = email.getText().toString();
        String p = password.getText().toString();

        if(e.isEmpty() || e==null )
        {
            email.setError("Email is required!");
            return;
        }

        if(p.length() < 6)
        {
            password.setError("Password must be at least 6 characters!");
            return;
        }
        bar.setVisibility(View.VISIBLE);


        auth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser usr = auth.getCurrentUser();
                if(task.isSuccessful())
                {
                    if(owner.isChecked())
                    {
                        DocumentReference r = store.collection("Users").document(usr.getUid());
                        Map<String,Object> userType = new HashMap<>();
                        userType.put("Type",owner.getText().toString());
                        r.set(userType);
                        Toast.makeText(SignupActivity.this,"User Created!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                    else if(user.isChecked())
                    {
                        DocumentReference r = store.collection("Users").document(usr.getUid());
                        Map<String,Object> userType = new HashMap<>();
                        userType.put("Type",user.getText().toString());
                        r.set(userType);
                        Toast.makeText(SignupActivity.this,"User Created!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();

                    }
                    else Toast.makeText(SignupActivity.this,"You must select the type",Toast.LENGTH_SHORT);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    // //Αν πατησει στο textView Login here τοτε μεταφερεται στο MainActivity2 και κλεινει αυτο το activity

    public void login(View view)
    {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}