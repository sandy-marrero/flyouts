package com.flyoutsgroup.flyouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private PostsAdapter adapter;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvCreated;
        private TextView tvScreenName;
        private TextView tvTime;
        private ImageView profilePictureIv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.post_tvUserName);
            tvDescription = itemView.findViewById(R.id.post_tvCaption);
            tvScreenName = itemView.findViewById(R.id.post_tvScreenName);
            tvTime = itemView.findViewById(R.id.post_tvTime);
            profilePictureIv = itemView.findViewById(R.id.post_profileIv);
        }


        public void bind(Post post) {
            tvDescription.setText(post.getCaption());
            tvUsername.setText("@" + post.getUser().getUsername());
            tvScreenName.setText(post.getUser().getString("screenname"));
            if (post.getUser().getParseFile("Profilepicture") != null) {
                Glide.with(context).load(post.getUser().getParseFile("Profilepicture").getUrl()).into(profilePictureIv);
            }else {
                Glide.with(context).load("https://i.pinimg.com/1200x/3f/94/70/3f9470b34a8e3f526dbdb022f9f19cf7.jpg").into(profilePictureIv);
            }
            String time = post.getUpdatedAt().toString().substring(0, 11) + " " + post.getUpdatedAt().toString().substring(11, 20);
            System.out.println(time);
            //Testing basic post date implementation
            int x = Integer.parseInt(post.getUpdatedAt().toString().substring(11, 13));
            if (x > 12) {
                x = x - 12;
                time = post.getUpdatedAt().toString().substring(0, 11) + " " + Integer.toString(x) + post.getUpdatedAt().toString().substring(13, 16) + "PM";
            } else {
                time = post.getUpdatedAt().toString().substring(0, 11) + " " + Integer.toString(x) + post.getUpdatedAt().toString().substring(13, 16) + "AM";
            }
            tvTime.setText(time);
        }
    }
}