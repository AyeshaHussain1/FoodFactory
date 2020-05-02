package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class New_User extends AppCompatActivity {
private EditText Name;
    private EditText Email;
    private EditText Pwd;
    private TextView Login;
    private FirebaseAuth objectFirebaseAuth;
    private static final String CollectionName = "Users";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar bar;
    private Button Signup;
    private FirebaseFirestore  objectFirebaseFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__user);
        connectionOfXml();
    }
    public void connectionOfXml(){
        try{
        Name=findViewById(R.id.Name_ET1);
        Email=findViewById(R.id.Email_ET2);
        Pwd=findViewById(R.id.Pwd_ET3);
        Signup=findViewById(R.id.btn_user);
        Login=findViewById(R.id.LoginTV);
        objectFirebaseAuth = FirebaseAuth.getInstance();
        bar = findViewById(R.id.bar);
        objectFirebaseFireStore = FirebaseFirestore.getInstance();
            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(New_User.this, MainActivity.class));
                    finish();
                }
            });
            Signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExistingUser();
                }
            });

    }
        catch(Exception ex) {
            Toast.makeText(this, "Connect-With-XML: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void ExistingUser() {
        try {
            bar.setVisibility(View.VISIBLE);
            if (!Email.getText().toString().isEmpty()) {
                if (objectFirebaseAuth != null) {
                    Signup.setEnabled(false);
                    objectFirebaseAuth.fetchSignInMethodsForEmail(Email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    boolean check = task.getResult().getSignInMethods().isEmpty();
                                    if (!check) {
                                        Signup.setEnabled(true);

                                        bar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(New_User.this, "User Already Exists", Toast.LENGTH_SHORT).show();

                                    } else if (check) {
                                        bar.setVisibility(View.INVISIBLE);

                                        Signup.setEnabled(true);
                                        Signup();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            bar.setVisibility(View.INVISIBLE);
                            Signup.setEnabled(true);

                            Toast.makeText(New_User.this, "Fails To Check If User Exists" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Email.requestFocus();
                Signup.setEnabled(true);

                bar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Email and Password is Empty", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            bar.setVisibility(View.INVISIBLE);
            Signup.setEnabled(true);

            Toast.makeText(this, "Check User Error" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Signup() {
        try {
            bar.setVisibility(View.VISIBLE);
            if (!Email.getText().toString().isEmpty()
                    &&
                    !Pwd.getText().toString().isEmpty()) {
                if (objectFirebaseAuth != null) {
                    bar.setVisibility(View.INVISIBLE);
                    Signup.setEnabled(false);

                    objectFirebaseAuth.createUserWithEmailAndPassword(Email.getText().toString(),
                            Pwd.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            objectFirebaseFireStore.collection(CollectionName).document(Name.getText().toString()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.getResult().exists()) {
                                                bar.setVisibility(View.INVISIBLE);
                                                Name.requestFocus();
                                                Toast.makeText(New_User.this, "Document Already Exists", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Map<String, Object> objectMap = new HashMap<>();
                                                objectMap.put("Username", Name.getText().toString());
                                                objectMap.put("Email", Email.getText().toString());
                                                objectFirebaseFireStore.collection(CollectionName)
                                                        .document(Name.getText().toString()).set(objectMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                bar.setVisibility(View.INVISIBLE);
                                                                Toast.makeText(New_User.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                bar.setVisibility(View.INVISIBLE);
                                                                Toast.makeText(New_User.this, "Fails to add data", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                            if (authResult.getUser() != null) {

                                objectFirebaseAuth.signOut();
                                Email.requestFocus();

                                Signup.setEnabled(true);
                                bar.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(New_User.this, MainActivity.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Signup.setEnabled(true);
                            Email.requestFocus();

                            bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(New_User.this, "Failed To Create User" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else if (Email.getText().toString().isEmpty()) {
                Signup.setEnabled(true);
                bar.setVisibility(View.INVISIBLE);

                Email.requestFocus();
                Toast.makeText(this, "Please Enter The Email", Toast.LENGTH_SHORT).show();
            } else if (Pwd.getText().toString().isEmpty()) {
                Signup.setEnabled(true);
                bar.setVisibility(View.INVISIBLE);

                Pwd.requestFocus();
                Toast.makeText(this, "Please Enter The Password", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {

            Signup.setEnabled(true);
            bar.setVisibility(View.INVISIBLE);

            Email.requestFocus();
            Toast.makeText(this, "Signup Error" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
