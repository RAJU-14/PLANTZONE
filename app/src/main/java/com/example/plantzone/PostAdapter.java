package com.example.plantzone;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
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
        ArrayList<String> comments = post.getComments();

        // Display comments
        // Assuming you have a TextView with id textViewComments in your item_post layout
        StringBuilder commentsText = new StringBuilder();
        for (String comment : comments) {
            commentsText.append(comment).append("\n");
        }
        holder.textViewComments.setText(commentsText.toString());

        holder.buttonSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = holder.editTextComment.getText().toString();
                if (!comment.isEmpty()) {
                    post.addComment(context, comment); // Pass only the context and comment
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
        TextView textViewComments; // TextView to display comments
        EditText editTextComment;
        Button buttonSubmitComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewComments = itemView.findViewById(R.id.textViewComments); // Initialize textViewComments with the correct ID
            editTextComment = itemView.findViewById(R.id.editTextComment);
            buttonSubmitComment = itemView.findViewById(R.id.buttonSubmitComment);
        }
    }
}
