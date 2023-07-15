package com.flyoutsgroup.flyouts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FlyersAdapter extends RecyclerView.Adapter<FlyersAdapter.ViewHolder> {

    public OnLongClickListener longClickListener;
    public OnClickListener clickListener;
    private Context context;
    private List<Flyer> flyers;

    public FlyersAdapter(Context context, List<Flyer> flyers) {
        this.context = context;
        this.flyers = flyers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flyer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flyer flyer = flyers.get(position);
        holder.bind(flyer);
    }

    @Override
    public int getItemCount() {
        return flyers.size();
    }

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout flyerItem;
        private TextView tvFlyerTitle;
        private TextView tvFlyerDescription;
        private ImageView ivFlyerImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFlyerTitle = itemView.findViewById(R.id.tvFlyerTitle);
            tvFlyerDescription = itemView.findViewById(R.id.tvFlyerDescription);
            ivFlyerImage = itemView.findViewById(R.id.ivFlyerImage);
            flyerItem = itemView.findViewById(R.id.flyerItem);
        }

        public void bind(Flyer flyer) {
            tvFlyerTitle.setText(flyer.getTitle());
            tvFlyerDescription.setText(flyer.getDescription());
            Glide.with(context).load(flyer.getImage().getUrl()).into(ivFlyerImage);
            flyerItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailedBulletinActivity.class);
                    i.putExtra("title", flyer.getTitle());
                    i.putExtra("description", flyer.getDescription());
                    i.putExtra("image", flyer.getImage().getUrl());
                    context.startActivity(i);
                }
            });
            ivFlyerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, FullImageActivity.class);
                    i.putExtra("title", flyer.getTitle());
                    i.putExtra("description", flyer.getDescription());
                    i.putExtra("image", flyer.getImage().getUrl());
                    context.startActivity(i);

                }
            });
        }

    }
}
