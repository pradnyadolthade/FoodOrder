package com.example.androideatit.Viewfolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androideatit.Interface.ItemClickListener;
import com.example.androideatit.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textMenuView;
    public ImageView imageView;
    private ItemClickListener itemClickListener;


    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

    textMenuView = (TextView)itemView.findViewById(R.id.menu_name);
    imageView = (ImageView)itemView.findViewById(R.id.imageView);

    itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
