package Review_Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class StatusAdapterClass extends FirestoreRecyclerAdapter<StatusModelClass, StatusAdapterClass.ViewHolder> {
    public StatusAdapterClass(@NonNull FirestoreRecyclerOptions<StatusModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull StatusModelClass statusModelClass) {
        viewHolder.review.setText(statusModelClass.getreview());
        Glide.with(viewHolder.URL.getContext())
                .load(statusModelClass.getURL())
                .into(viewHolder.URL);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View review_row = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.review_row, parent, false);
        ViewHolder objectViewHolder=new ViewHolder(review_row);
        return objectViewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView URL;
        TextView review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            URL = itemView.findViewById(R.id.Row_image);
            review = itemView.findViewById(R.id.review);
        }

    }

}
