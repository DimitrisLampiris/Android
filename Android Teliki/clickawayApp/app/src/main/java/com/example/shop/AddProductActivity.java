package com.example.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public ProgressBar bar;
    public EditText id,title,am,price;
    private ImageView image;
    private DatabaseReference ref;
    private StorageReference sref;
    private Uri imageUri;
    private Spinner spinner;
    public String type;
   // private TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        bar = findViewById(R.id.progressBar3);
       image = findViewById(R.id.imageView8);
       id= findViewById(R.id.prdId);
       title = findViewById(R.id.prdTitle);
        am = findViewById(R.id.prdAmmount);
     //   t = findViewById(R.id.type);
        price = findViewById(R.id.prdPrice);
       sref = FirebaseStorage.getInstance().getReference();
       spinner = findViewById(R.id.spinner);
       ArrayList<String> list = new ArrayList<>();
       list.add("Buffan");
       list.add("T-Shirts");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list);
       spinner.setAdapter(adapter);
       spinner.setOnItemSelectedListener(this);
    }

    public void selectImage(View view)
    {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==2 && resultCode == RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            image.setImageURI(imageUri);
        }
    }

    public void addProduct(View view)
    {
        if(imageUri != null)
        {
            uploadToFirebase(imageUri);
        }
        else
            {
                Toast.makeText(AddProductActivity.this,"Please select an image!",Toast.LENGTH_SHORT).show();
            }
    }

    private void uploadToFirebase(Uri uri) {
       ref= FirebaseDatabase.getInstance().getReference().child(type).child(id.getText().toString());

        StorageReference r = sref.child(id + ".png");
        r.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                r.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // ref.child(id.toString()).setValue();
                        HashMap map = new HashMap<>();
                        map.put("amount", am.getText().toString());
                        map.put("id", id.getText().toString());
                        map.put("image", String.valueOf(uri));
                        map.put("price", price.getText().toString());
                        map.put("title", title.getText().toString());

                        ref.setValue(map);

                    }
                });
                Toast.makeText(AddProductActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.INVISIBLE);
                startActivity(new Intent(getApplicationContext(), OwnerActivity.class));
                finish();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                bar.setVisibility(View.VISIBLE);
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(),OwnerActivity.class);


            startActivity(intent);

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       type = parent.getItemAtPosition(position).toString();
      // t.setText(type);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

