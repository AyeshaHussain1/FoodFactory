package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_Page extends AppCompatActivity {
private EditText Email;
private EditText Pwd;
private Button Login,googlesignin;
private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private FirebaseAuth objectFirebaseAuth;

    private ProgressBar bar;
    private final static int RC_SIGN_IN = 111;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), Home_Page.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);
        connectionOfXml();
    }
    public void change(View View)
    {
        Intent intent = new Intent(Login_Page.this, New_User.class);
        startActivity(intent);
    }

    public void connectionOfXml(){
        try {
            Email=findViewById(R.id.Email_login);
            Pwd=findViewById(R.id.password_login);
            Login=findViewById(R.id.btn1_login);
            bar=findViewById(R.id.bar);
            googlesignin = findViewById(R.id.btn3_Google);
            mAuth = FirebaseAuth.getInstance();
            objectFirebaseAuth = FirebaseAuth.getInstance();
           Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Login_User();
                }
            });

            createRequest();
            googlesignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                    bar.setVisibility(View.VISIBLE);
                }
            });
        }
        catch(Exception e) {
            Toast.makeText(this, "Connect-With-XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        //**********************LOGIN***********************//
        public void Login_User() {
            try {
                bar.setVisibility(View.VISIBLE);
                if (!Email.getText().toString().isEmpty() && !Pwd.getText().toString().isEmpty()) {
                    if (objectFirebaseAuth.getCurrentUser() != null) {
                        objectFirebaseAuth.signOut();
                        Login.setEnabled(false);

                        bar.setVisibility(View.INVISIBLE);
                        Toast.makeText(this, "User Logged Out Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        objectFirebaseAuth.signInWithEmailAndPassword(Email.getText().toString(),
                                Pwd.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Login.setEnabled(true);
                                        bar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(Login_Page.this, "User Logged In", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login_Page.this, Home_Page.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Login.setEnabled(true);
                                Email.requestFocus();

                                bar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Login_Page.this, "Fails To Sig-in User: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else if (Email.getText().toString().isEmpty()) {
                    Login.setEnabled(true);
                   Email.requestFocus();

                    bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "Please Enter The Email", Toast.LENGTH_SHORT).show();
                } else if (Pwd.getText().toString().isEmpty()) {
                    Login.setEnabled(true);
                    Pwd.requestFocus();

                    bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "Please Enter The Password", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {

                Login.setEnabled(true);
                Email.requestFocus();

                bar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Logging In Error" + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        //***************************GOOGLE_Login********************************************//
        private void createRequest() {
            //Creating a send request to open a Pop-up so that user can Log-in
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }
    private void signIn() {
        //Intent in which you can select your Google account
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bar.setVisibility(View.VISIBLE);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                bar.setVisibility(View.INVISIBLE);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                bar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Login Failed: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login_Page.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), Home_Page.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login_Page.this, "signInWithCredential:failure" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
