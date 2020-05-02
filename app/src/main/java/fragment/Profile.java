package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


import com.example.project.R;

public class Profile extends Fragment {
    private TextView Name, Email;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseAuth;

    private DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container,false);
        Name = view.findViewById(R.id.Name);
        Email = view.findViewById(R.id.Email);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        if( firebaseAuth !=null ){
            Name.setText(firebaseAuth.getDisplayName());
            Email.setText(firebaseAuth.getEmail());
        }

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (signInAccount != null) {
            Name.setText(signInAccount.getDisplayName());
            Email.setText(signInAccount.getEmail());
        }
        return view;
    }
}
