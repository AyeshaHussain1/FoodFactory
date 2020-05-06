package fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project.Home_Page;
import com.example.project.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Review extends Fragment {
    private ImageView imageToUploadIV;
    private EditText Comments;
    private Button btn;
    private ProgressBar bar;

    private static final int REQUEST_CODE = 123;

    private Uri imageDataInUriForm;
    private StorageReference objectStorageReference;

    private FirebaseFirestore objectFirebaseFirestore;
    private boolean isImageSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_review, container, false);
        imageToUploadIV = view.findViewById(R.id.imageToUploadIV);
           btn=view.findViewById(R.id.btn);
        bar = view.findViewById(R.id.bar);
        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        Comments = view.findViewById(R.id.Review);
        objectStorageReference = FirebaseStorage.getInstance().getReference("Gallery");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadOurImage();
            }
        });
        imageToUploadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
//
            }
        });
        return view;
    }
    private void openGallery() {
        try {
            Intent objectIntent = new Intent(); //Step 1:create the object of intent
            objectIntent.setAction(Intent.ACTION_GET_CONTENT); //Step 2: You want to get some data


            objectIntent.setType("image/*");//Step 3: Images of all type
            startActivityForResult(objectIntent, REQUEST_CODE);

        } catch (Exception e) {
            Toast.makeText(getActivity(), "openGallery:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                imageDataInUriForm = data.getData();
                Bitmap objectBitmap;

                Context applicationContext = Home_Page.getContextOfApplication();
                applicationContext.getContentResolver();

                objectBitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), imageDataInUriForm);
                isImageSelected = true;

            } else if (requestCode != REQUEST_CODE) {
                Toast.makeText(getActivity(), "Request code doesn't match", Toast.LENGTH_SHORT).show();
            } else if (resultCode != RESULT_OK) {
                Toast.makeText(getActivity(), "Fails to get image", Toast.LENGTH_SHORT).show();
            } else if (data == null) {
                Toast.makeText(getActivity(), "No Image Was Selected", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), "onActivityResult:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadOurImage() {
        try {
            if (imageDataInUriForm != null && !Comments.getText().toString().isEmpty()
                    && isImageSelected) {
                bar.setVisibility(View.VISIBLE);
                //yourName.jpeg
                String imageReview = Comments.getText().toString() + "." + getExtension(imageDataInUriForm);
                final String Review = Comments.getText().toString();

                //FirebaseStorage/BSCSAImagesFolder/yourName.jpeg
                final StorageReference actualImageRef = objectStorageReference.child(imageReview);

                UploadTask uploadTask = actualImageRef.putFile(imageDataInUriForm);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            bar.setVisibility(View.INVISIBLE);
                            throw task.getException();
                        }
                        return actualImageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String url = task.getResult().toString();
                            Map<String, Object> objectMap = new HashMap<>();
                            objectMap.put("URL", url);
                            objectMap.put("Comments", Review);
//
                            objectFirebaseFirestore.collection("Gallery")
                                    .document(Comments.getText().toString())
                                    .set(objectMap)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            bar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getActivity(), "Fails To Upload Image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Comments.setText("");
                                            imageToUploadIV.setVisibility(View.INVISIBLE);
                                            bar.setVisibility(View.INVISIBLE);

                                            Toast.makeText(getActivity(), "Image Successfully Uploaded: ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Fails To Upload Image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (imageDataInUriForm == null) {

                Toast.makeText(getActivity(), "No Image Is Selected", Toast.LENGTH_SHORT).show();
            } else if (Comments.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please First You Need To Enter An Image Name", Toast.LENGTH_SHORT).show();
                Comments.requestFocus();
            } else if (!isImageSelected) {

                Toast.makeText(getActivity(), "Please Select An Image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            Toast.makeText(getActivity(), "uploadOurImage:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private String getExtension(Uri imageDataInUriForm) {
        try {
            Context applicationContext = Home_Page.getContextOfApplication();
            applicationContext.getContentResolver();
            ContentResolver objectContentResolver = applicationContext.getContentResolver();
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();

            String extension = objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(imageDataInUriForm));
            return extension;
        } catch (Exception e) {
            Toast.makeText(getActivity(), "getExtension:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return "";
    }
}
