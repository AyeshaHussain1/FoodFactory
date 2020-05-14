package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends Fragment {
    private TextView Name, Email;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore objectFirebaseFireStore;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseAuth;
    private DatabaseReference reference;
    private DocumentReference objectDocumentReference;
    private static final String CollectionName = "Users";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile, container,false);
        Name = view.findViewById(R.id.Name);
        Email = view.findViewById(R.id.Email);

        mAuth = FirebaseAuth.getInstance();
        objectFirebaseFireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();

        if( firebaseAuth !=null ){
           Name.setText(firebaseAuth.getDisplayName());
            Email.setText(firebaseAuth.getEmail());
            objectDocumentReference =  objectFirebaseFireStore.collection(CollectionName)
                .document(Name.getText().toString());

        objectDocumentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            Toast.makeText(getActivity(),"No values retrieved",Toast.LENGTH_SHORT).show();
                            return;
                        }

                            String name = documentSnapshot.getString("Name");

                    }
                });
        }
        else
        {
            Toast.makeText(getContext(),"NO record",Toast.LENGTH_SHORT).show();
        }
//        objectDocumentReference =  objectFirebaseFireStore.collection(CollectionName)
//                .document(Name.getText().toString());
//
//        objectDocumentReference.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (!documentSnapshot.exists()) {
//                            Toast.makeText(getActivity(),"No values retrieved",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                            String name = documentSnapshot.getString("Name");
//
//                    }
//                });


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (signInAccount != null) {
            Name.setText(signInAccount.getDisplayName());
            Email.setText(signInAccount.getEmail());
        }
        return view;
    }
}
