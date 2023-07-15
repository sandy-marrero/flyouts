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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flyoutsgroup.flyouts.BulletinComposeActivity;
import com.flyoutsgroup.flyouts.Flyer;
import com.flyoutsgroup.flyouts.FlyersAdapter;
import com.flyoutsgroup.flyouts.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link BulletinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BulletinFragment extends Fragment {

    public static String TAG = "BulletinBoard";
    protected List<Flyer> allFlyers;
    protected FlyersAdapter flyersAdapter;
    SwipeRefreshLayout swipeContainer;
    private RecyclerView rvBoard;


    public BulletinFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bulletin_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionbar_flyer_create) {
            Intent intent = new Intent(getContext(), BulletinComposeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bulletin, container, false);
    }

    //widgets go here
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allFlyers = new ArrayList<>();
        flyersAdapter = new FlyersAdapter(getContext(), allFlyers);
        rvBoard = view.findViewById(R.id.rvBoard);
        rvBoard.setAdapter(flyersAdapter);
        rvBoard.setLayoutManager(new LinearLayoutManager(getContext()));
        queryFlyers();


        swipeContainer = view.findViewById(R.id.swiperefresh);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryFlyers();
                swipeContainer.setRefreshing(false);
            }

        });

    }


    protected void queryFlyers() {
        ParseQuery<Flyer> query = ParseQuery.getQuery(Flyer.class);
        query.include(Flyer.KEY_AUTHOR);
        query.addDescendingOrder(Flyer.KEY_CREATED);
        query.findInBackground(new FindCallback<Flyer>() {
            @Override
            public void done(List<Flyer> flyers, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting Flyers", e);
                    return;
                }
                for (Flyer flyer : flyers) {
                    Log.i(TAG, "Flyer title: " + flyer.getTitle() + "Flyer: " + flyer.getDescription() + ", username: " + flyer.getAuthor().getUsername());
                }
                if (allFlyers.size() > 0) {
                    allFlyers.removeAll(allFlyers);
                }
                allFlyers.addAll(flyers);
                flyersAdapter.notifyDataSetChanged();
            }
        });
    }
}