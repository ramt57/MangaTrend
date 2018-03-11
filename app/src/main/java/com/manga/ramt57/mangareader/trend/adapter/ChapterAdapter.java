package com.manga.ramt57.mangareader.trend.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manga.ramt57.mangareader.trend.R;

import java.util.ArrayList;


/**
 * Created by user on 16-09-2017.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {
    static  ArrayList<String>  chapterlist;
    static viewchapter listener;
    Context ctx;
    static DownloadCHapter listener4;
    public  ChapterAdapter(ArrayList<String > var){
        chapterlist=var;
    }
    @Override
    public ChapterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_row,parent,false);
        ChapterAdapter.MyViewHolder viewholder=new ChapterAdapter.MyViewHolder(view);
        ctx=view.getContext();
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ChapterAdapter.MyViewHolder holder, int position) {
        holder.button.setText(chapterlist.get(position).toString()+"");
    }

    @Override
    public int getItemCount() {
        return chapterlist.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView button;
        LinearLayout linearLayout;
        ImageView download;
        public MyViewHolder(View itemView) {
            super(itemView);
            button=itemView.findViewById(R.id.normalButton);
            linearLayout=itemView.findViewById(R.id.clickChap);
            download=itemView.findViewById(R.id.download);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener4.DownloadChap(getAdapterPosition());
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.viewChap(getAdapterPosition());
                }
            });
        }
    }
    public static void setChapterlist(viewchapter listene){
        listener=listene;
    }
    public interface viewchapter{
        public void viewChap(int position);
    }
    public static void setDownloadListener(DownloadCHapter listener){
        listener4=listener;
    }
    public  interface DownloadCHapter{
        public void DownloadChap(int position);
    }
}
