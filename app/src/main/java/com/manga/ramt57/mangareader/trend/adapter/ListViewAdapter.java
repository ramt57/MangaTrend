package com.manga.ramt57.mangareader.trend.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manga.ramt57.mangareader.trend.R;

import java.util.ArrayList;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
   Context context;
    ArrayList<String> url = new ArrayList<>();
    static  ItemSelector listener;
    public ListViewAdapter(ArrayList<String> image,Context ctx){
        context=ctx;
        url=image;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_bottom_row,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       holder.imageView.setText(position+1+"");
    }

    @Override
    public int getItemCount() {
        return url.size();
    }
    public static  class ViewHolder extends RecyclerView.ViewHolder{
       TextView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(TextView)itemView.findViewById(R.id.preview);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnSelect(getAdapterPosition());
                    Log.d("Tag",getAdapterPosition()+"s");
                }
            });
        }
    }
    public void setItemSelectListener(ItemSelector selectListener){
        listener=selectListener;
    }
    public interface ItemSelector{
        public void OnSelect(int position);
    }
}
