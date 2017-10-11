package com.example.b10715.final_pj;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Serializable {
    Context context;
    List<Item> items;
    int item_layout;

    public RecyclerAdapter(Context context, List<Item> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);

        /* PetStagram Adapter값 */
        Glide.with(context).load(item.getUser_image()).into(holder.user_image);
        holder.user_email.setText(item.getUser_email());
        Glide.with(context).load(item.getImage()).into(holder.image);
        holder.text.setText(item.getText());
        holder.home_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, item.getId(), Toast.LENGTH_SHORT).show();
                Intent ContentIntent = new Intent(context, ContentActivity.class);
                ContentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // ContentIntent.putExtra("item", item);
                ContentIntent.putExtra("id", item.getId());
                ContentIntent.putExtra("user_image", item.getUser_image().toString());
                ContentIntent.putExtra("user_email", item.getUser_email());
                ContentIntent.putExtra("image", item.getImage().toString());
                ContentIntent.putExtra("content", item.getText());
                context.startActivity(ContentIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image, image;
        TextView user_email, text;
        CardView home_cardview;

        public ViewHolder(View itemView) {
            super(itemView);

            /* PetStagram Adapter값 */
            user_image = (ImageView) itemView.findViewById(R.id.user_image);
            user_email = (TextView) itemView.findViewById(R.id.user_email);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            home_cardview = (CardView) itemView.findViewById(R.id.home_cardview);

        }
    }

}

