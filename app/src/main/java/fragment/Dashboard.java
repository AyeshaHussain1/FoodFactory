package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.Food_Ordering;
import com.example.project.My_Reviews;
import com.example.project.R;


public class Dashboard extends Fragment {
    private ImageView Card2,card4;
    public static Context contextOfApplication;
    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dashboard,container,false);

       ImageView Card2=(ImageView) view.findViewById(R.id.imageView2);
        ImageView Card4=(ImageView) view.findViewById(R.id.imageView4);

        Card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(getActivity(), Food_Ordering.class);
                startActivity(intent);
            }
        });
        Card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity() , My_Reviews.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    }