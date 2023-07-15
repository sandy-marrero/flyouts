package com.flyoutsgroup.flyouts.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.flyoutsgroup.flyouts.Post;
import com.flyoutsgroup.flyouts.PostsAdapter;
import com.flyoutsgroup.flyouts.R;
import com.flyoutsgroup.flyouts.RegistrationActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static String TAG = "ProfileFragment";
    protected List<Post> allItems;
    protected PostsAdapter itemsAdapter;
    SwipeRefreshLayout swiperefreshprofile;
    private TextView tvUserName;
    private TextView tvUserScreenName;
    private TextView tvUserBio;
    private ImageView tvUserImage;
    private RecyclerView rvnews;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            Intent i = new Intent(getContext(), RegistrationActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserScreenName = view.findViewById(R.id.tvUserScreenName);
        tvUserBio = view.findViewById(R.id.tvUserBio);
        tvUserImage = view.findViewById(R.id.tvUserImage);

        tvUserName.setText("@" + ParseUser.getCurrentUser().getUsername());
        if (ParseUser.getCurrentUser().getString("bio") != null) {
            tvUserBio.setText(ParseUser.getCurrentUser().getString("bio"));
        } else {
            tvUserBio.setText("No Bio has been added");
        }
        tvUserScreenName.setText(ParseUser.getCurrentUser().getString("screenname"));
        if (ParseUser.getCurrentUser().getParseFile("Profilepicture") != null) {
            Glide.with(getContext()).load(ParseUser.getCurrentUser().getParseFile("Profilepicture").getUrl()).into(tvUserImage);
        } else {
            Glide.with(getContext()).load("https://i.pinimg.com/1200x/3f/94/70/3f9470b34a8e3f526dbdb022f9f19cf7.jpg").into(tvUserImage);
        }

        allItems = new ArrayList<Post>();
        itemsAdapter = new PostsAdapter(getContext(), allItems);
        rvnews = view.findViewById(R.id.rvNews);
        rvnews.setAdapter(itemsAdapter);
        rvnews.setLayoutManager(new LinearLayoutManager(getContext()));
        queryNewsItems();


        swiperefreshprofile = view.findViewById(R.id.swiperefreshprofile);
        swiperefreshprofile.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swiperefreshprofile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allItems.clear();
                queryNewsItems();
                swiperefreshprofile.setRefreshing(false);
            }
        });
    }

    private void queryNewsItems() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_CAPTION);
        query.include(Post.KEY_USER);
        query.setLimit(25);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> items, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting Items", e);
                    return;
                }
                for (Post item : items) {
                    Log.i(TAG, "Item title: " + item.getCaption() + ", username: " + item.getUser().getUsername());
                }
                allItems.addAll(items);
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }
}