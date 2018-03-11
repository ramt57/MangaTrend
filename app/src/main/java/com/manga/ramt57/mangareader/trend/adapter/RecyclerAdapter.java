package com.manga.ramt57.mangareader.trend.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.manga.ramt57.mangareader.trend.Chapterlist;
import com.manga.ramt57.mangareader.trend.R;
import com.manga.ramt57.mangareader.trend.pojomodels.Mangalist;

import java.util.ArrayList;
/**
 * Created by user on 15-09-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    static ArrayList<Mangalist> mnga=new ArrayList<>();
    Context ctx;
    public void setData(ArrayList<Mangalist> mnga){
        this.mnga=mnga;
    }
    public RecyclerAdapter(){

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mangacard,parent,false);
        MyViewHolder viewholder=new MyViewHolder(view);
        ctx=view.getContext();
        return viewholder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mnga.get(position).getT());
        Glide.with(ctx).load("https://cdn.mangaeden.com/mangasimg/"+mnga.get(position).getIm())
                .apply(new RequestOptions()
                        .override(200,150).centerCrop().placeholder(R.drawable.placeholder).error(R.drawable.error))
                .into(holder.mangathumb);
//        Picasso.with(ctx)
//                .load("https://cdn.mangaeden.com/mangasimg/"+mnga.get(position).getIm()).resize(200,150).centerCrop()
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.error)
//                .into(holder.mangathumb);
    }

    @Override
    public int getItemCount() {
        return mnga.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mangathumb;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mangathumb=itemView.findViewById(R.id.mangapic);
            textView=itemView.findViewById(R.id.manganame);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(view.getContext(), Chapterlist.class);
                    i.putExtra("ID",mnga.get(getAdapterPosition()).getI());
                    view.getContext().startActivity(i);
                }
            });
        }
    }
}
