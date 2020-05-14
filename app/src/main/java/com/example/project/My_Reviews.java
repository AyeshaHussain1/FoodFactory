package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Review_Recycler.StatusAdapterClass;
import Review_Recycler.StatusModelClass;

public class My_Reviews extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView review;
    private ImageView image;
    private Button back;
    private RecyclerView rcv;
    private FirebaseFirestore objectFirebaseAuth;
    private StatusAdapterClass objectStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__reviews);
        rcv = findViewById(R.id.RCV);
        back=findViewById(R.id.back_btn);
        review = findViewById(R.id.review);
        image = findViewById(R.id.Row_image);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
            }
        });
        connection();
    }

    public void connection() {

        try {

            objectFirebaseAuth = FirebaseFirestore.getInstance();
            addValuestoRV();

        } catch (Exception ex) {

            Toast.makeText(My_Reviews.this, "connection: " + ex.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void addValuestoRV() {
        try {
            Query objectQuery = objectFirebaseAuth.collection("Reviews");

            FirestoreRecyclerOptions<StatusModelClass> options =
                    new FirestoreRecyclerOptions.Builder<StatusModelClass>().setQuery(objectQuery, StatusModelClass.class).build();

            objectStatusAdapter = new StatusAdapterClass(options);


            rcv.setLayoutManager(new LinearLayoutManager(this));

            rcv.setAdapter(objectStatusAdapter);

        } catch (Exception ex) {
            Toast.makeText(My_Reviews.this, "AddValuesToRV: " + ex.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onStart() {

        super.onStart();

        objectStatusAdapter.startListening();

    }

    @Override
    public void onStop() {

        super.onStop();

        objectStatusAdapter.stopListening();

    }
}
