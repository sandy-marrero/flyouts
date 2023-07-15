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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flyoutsgroup.flyouts.NewsfeedComposeActivity;
import com.flyoutsgroup.flyouts.Post;
import com.flyoutsgroup.flyouts.PostsAdapter;
import com.flyoutsgroup.flyouts.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedFragment extends Fragment {

    public static String TAG = "NewsFeed";
    protected List<Post> allItems;
    protected PostsAdapter itemsAdapter;
    SwipeRefreshLayout swiperefreshnews;
    private RecyclerView rvnews;


    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.newsfeed_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_newsfeed_compose) {
            Toast.makeText(getActivity(), "Compose Post!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), NewsfeedComposeActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_compose_refresh) {
            allItems.clear();
            queryNewsItems();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

    //widgets go here
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allItems = new ArrayList<Post>();
        itemsAdapter = new PostsAdapter(getContext(), allItems);
        rvnews = view.findViewById(R.id.rvNews);
        rvnews.setAdapter(itemsAdapter);
        rvnews.setLayoutManager(new LinearLayoutManager(getContext()));
        queryNewsItems();


        swiperefreshnews = view.findViewById(R.id.swiperefreshnews);
        swiperefreshnews.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swiperefreshnews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allItems.clear();
                queryNewsItems();
                swiperefreshnews.setRefreshing(false);
            }
        });
    }


    private void queryNewsItems() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_CAPTION);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_UPDATED_AT);
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