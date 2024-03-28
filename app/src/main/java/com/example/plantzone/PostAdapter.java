package com.example.plantzone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Post> posts;
    private String userEmail; // Store the user's email address

    public PostAdapter(Context context, ArrayList<Post> posts, String userEmail) {
        this.context = context;
        this.posts = posts;
        this.userEmail = userEmail; // Assign the user's email address
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.textViewDescription.setText(post.getDescription());
        Glide.with(context).load(post.getImageUri()).into(holder.imageViewPost);

        // Display comments
        StringBuilder commentsText = new StringBuilder();
        ArrayList<String> comments = post.getComments();
        for (String comment : comments) {
            commentsText.append(comment).append("\n");
        }
        holder.textViewComments.setText(commentsText.toString());

        holder.buttonSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = holder.editTextComment.getText().toString();
                if (!comment.isEmpty()) {
                    // Pass the comment to the post object for adding
                    post.addComment(context, comment, userEmail, post.getPostId(), new Date());
                    notifyDataSetChanged();
                    holder.editTextComment.setText("");
                } else {
                    Toast.makeText(context, "Please enter a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPost;
        TextView textViewDescription;
        TextView textViewComments;
        EditText editTextComment;
        Button buttonSubmitComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewComments = itemView.findViewById(R.id.textViewComments);
            editTextComment = itemView.findViewById(R.id.editTextComment);
            buttonSubmitComment = itemView.findViewById(R.id.buttonSubmitComment);
        }
    }
}
