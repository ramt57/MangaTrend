package com.manga.ramt57.mangareader.trend;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class PageFragment extends Fragment {
    ImageViewTouch img;
    static  ItemClick listner;
    GestureDetector gestureDetector;
    ProgressBar bar;
    public PageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_page, container, false);
        img=rootView.findViewById(R.id.image);
//        Target target=new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                img.setImageBitmap(bitmap);
//                bar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                bar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                bar.setVisibility(View.VISIBLE);
//            }
//        };
//
//        img.setTag(target);
        bar=(ProgressBar)rootView.findViewById(R.id.progress);
        bar.setVisibility(View.VISIBLE);
        gestureDetector = new GestureDetector(getContext(), new GestureListener());
        String url=getArguments().getString("URL");
//        Picasso.with(rootView.getContext()).load("https://cdn.mangaeden.com/mangasimg/"+url.trim())
//               .into(target);
        Glide.with(getActivity())
                .load("https://cdn.mangaeden.com/mangasimg/"+url.trim())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                        bar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        bar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img);
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean da=gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        return rootView;
    }
    public void setItemListener(ItemClick listener){
        listner=listener;
    }
    public  interface  ItemClick{
        public void OnClick(boolean position);
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            listner.OnClick(true);
            return true;
        }
    }
}
