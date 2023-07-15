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

import com.flyoutsgroup.flyouts.Item;
import com.flyoutsgroup.flyouts.ItemsAdapter;
import com.flyoutsgroup.flyouts.MarketComposeActivity;
import com.flyoutsgroup.flyouts.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MarketFragment extends Fragment {

    public static String TAG = "MarketBoard";
    protected List<Item> allItems;
    protected ItemsAdapter itemsAdapter;
    SwipeRefreshLayout swiperefreshmarket;
    private RecyclerView rvmarket;


    public MarketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.market_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionbar_item_create) {
            Intent intent = new Intent(getContext(), MarketComposeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market, container, false);
    }

    //widgets go here
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allItems = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(getContext(), allItems);
        rvmarket = view.findViewById(R.id.rvMarket);
        rvmarket.setAdapter(itemsAdapter);
        rvmarket.setLayoutManager(new LinearLayoutManager(getContext()));
        queryMarketItems();


        swiperefreshmarket = view.findViewById(R.id.swiperefreshmarket);
        swiperefreshmarket.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swiperefreshmarket.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryMarketItems();
                swiperefreshmarket.setRefreshing(false);
            }
        });
    }


    private void queryMarketItems() {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.include(Item.KEY_AUTHOR);
        query.addDescendingOrder(Item.KEY_CREATED);
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> items, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting Items", e);
                    return;
                }
                for (Item item : items) {
                    Log.i(TAG, "Item title: " + item.getItemName() + ", username: " + item.getAuthor().getUsername());
                }
                if (allItems.size() > 0) {
                    allItems.removeAll(allItems);
                }
                allItems.addAll(items);
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }
}