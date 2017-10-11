package com.example.b10715.final_pj;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

public class PetSitterAdapter extends RecyclerView.Adapter<PetSitterAdapter.ViewHolder> implements Serializable {
    Context context;
    List<Item> items;
    int item_layout;

    public PetSitterAdapter(Context context, List<Item> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.petsitter_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);

         /* PetSitter Adapter값 */
        Glide.with(context).load(item.getUser_image()).into(holder.user_image);
        holder.sitter_word.setText(item.getSitter_word());
        holder.sitter_career_year.setText(item.getSitter_career_year()+"년"+item.getSitter_career_month()+"개월");
//        holder.sitter_career_month.setText(item.getSitter_career_month());
        holder.sitter_price.setText(toString().valueOf(item.getSitter_price())+" 원 / 시간");
        holder.petsitter_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, item.getId(), Toast.LENGTH_SHORT).show();
                Intent sitterIntent = new Intent(context, PetSitterInfoActivity.class);
                sitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sitterIntent.putExtra("id", item.getId());
                sitterIntent.putExtra("sitter_image", item.getUser_image());
                sitterIntent.putExtra("user_email", item.getUser_email());
                sitterIntent.putExtra("sitter_name", item.getSitter_name());
                sitterIntent.putExtra("sitter_age", item.getSitter_age());
                sitterIntent.putExtra("sitter_sex", item.getSitter_sex());
                sitterIntent.putExtra("sitter_phone_1", item.getSitter_phone_1());
                sitterIntent.putExtra("sitter_phone_2", item.getSitter_phone_2());
                sitterIntent.putExtra("sitter_phone_3", item.getSitter_phone_3());
                sitterIntent.putExtra("sitter_adr", item.getSitter_adr());
                sitterIntent.putExtra("sitter_career_year", item.getSitter_career_year());
                sitterIntent.putExtra("sitter_career_month", item.getSitter_career_month());
                sitterIntent.putExtra("sitter_pet", item.getSitter_pet());
                sitterIntent.putExtra("sitter_price", item.getSitter_price());
                sitterIntent.putExtra("sitter_word", item.getSitter_word());
                context.startActivity(sitterIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView user_email, sitter_name, sitter_age, sitter_sex, sitter_phone_1, sitter_phone_2, sitter_phone_3, sitter_adr, sitter_career_year, sitter_career_month, sitter_pet, sitter_price, sitter_word;
        CardView petsitter_cardview;

        public ViewHolder(View itemView) {
            super(itemView);

            /* PetSitter Adapter값 */
            user_image = (ImageView) itemView.findViewById(R.id.img_pet_sitter);
            user_email = (TextView) itemView.findViewById(R.id.user_email);
            sitter_name = (TextView) itemView.findViewById(R.id.sitter_name);
            sitter_age = (TextView) itemView.findViewById(R.id.sitter_age);
            sitter_sex = (TextView) itemView.findViewById(R.id.sitter_sex);
           /* sitter_phone_1 = (TextView) itemView.findViewById(R.id.sitter_phone_1);
            sitter_phone_2 = (TextView) itemView.findViewById(R.id.sitter_phone_2);
            sitter_phone_3 = (TextView) itemView.findViewById(R.id.sitter_phone_3);*/
            sitter_adr = (TextView) itemView.findViewById(R.id.sitter_adr);
            sitter_career_year = (TextView) itemView.findViewById(R.id.career);
            /*sitter_career_month = (TextView) itemView.findViewById(R.id.career_month);*/
            sitter_pet = (TextView) itemView.findViewById(R.id.sitter_pet);
            sitter_price = (TextView) itemView.findViewById(R.id.price);
            sitter_word = (TextView) itemView.findViewById(R.id.word);
            petsitter_cardview = (CardView) itemView.findViewById(R.id.petsitter_cardview);

        }
    }

}

