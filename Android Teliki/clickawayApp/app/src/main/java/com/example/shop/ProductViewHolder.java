package com.example.shop;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public  class ProductViewHolder extends RecyclerView.ViewHolder
{
    View mview;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        mview=itemView;
    }

    public void setTitle(String title)
    {
        TextView mtitle = mview.findViewById(R.id.title);
        mtitle.setText(title);
    }

    public void setAmount(String amount)
    {
        TextView am = mview.findViewById(R.id.ammount);
        am.setText(amount);
    }

    public void setPrice(String price)
    {
        TextView mtitle = mview.findViewById(R.id.price);
        mtitle.setText(price);
    }

    public void setImage(String image)
    {
        ImageView mImage = mview.findViewById(R.id.imageView);
        Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(mImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).into(mImage);

            }
        });
    }
}