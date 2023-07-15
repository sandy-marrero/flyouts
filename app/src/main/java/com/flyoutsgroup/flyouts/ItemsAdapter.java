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

import java.text.DecimalFormat;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private Context context;
    private List<Item> items;

    public ItemsAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName;
        private TextView tvItemDescription;
        private TextView tvItemContact;
        private TextView tvItemCondition;
        private TextView tvItemPrice;
        private ImageView ivItemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
            tvItemCondition = itemView.findViewById(R.id.tvItemCondition);
            tvItemContact = itemView.findViewById(R.id.tvItemContact);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            ivItemImage = itemView.findViewById(R.id.ivMarketItemImage);

        }

        public void bind(Item item) {
            tvItemName.setText(item.getItemName());
            tvItemDescription.setText(item.getItemDescription());
            tvItemContact.setText(item.getContact());
            tvItemCondition.setText(item.getCondition());
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String price = decimalFormat.format(item.getPrice());
            tvItemPrice.setText("$" + price);
            Glide.with(context).load(item.getImage().getUrl()).into(ivItemImage);
        }

    }
}