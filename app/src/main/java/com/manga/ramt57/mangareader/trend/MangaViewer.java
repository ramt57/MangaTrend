package com.manga.ramt57.mangareader.trend;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.manga.ramt57.mangareader.trend.Transformer.ImageZoomViewPager;
import com.manga.ramt57.mangareader.trend.Transformer.ZoomOutPageTransformer;
import com.manga.ramt57.mangareader.trend.adapter.ListViewAdapter;
import com.manga.ramt57.mangareader.trend.adapter.PagerAdapter;
import com.manga.ramt57.mangareader.trend.pojomodels.ImageModel;

import java.util.ArrayList;
import java.util.Collections;


public class MangaViewer extends FragmentActivity  implements PagerAdapter.fragmentClickListener{
//   ViewPager viewPager;
    ImageZoomViewPager zoomViewPager;
    PagerAdapter adapter;
    ArrayList<String> imagelist=new ArrayList<>();
    RecyclerView listView;
    String url = "https://www.mangaeden.com/api/chapter/";
    ListViewAdapter listViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_viewer);
        listView=(RecyclerView)findViewById(R.id.listView);
        zoomViewPager= (ImageZoomViewPager) findViewById(R.id.viewpager);
        zoomViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        adapter=new PagerAdapter(getSupportFragmentManager(),imagelist);
        zoomViewPager.setAdapter(adapter);
        String i=getIntent().getStringExtra("ID");
        url=url+i.trim()+"/";
        adapter.setFragmentListe(this);
        callvolley();
    }

    private void callvolley() {
        StringRequest req=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
                ImageModel model=gson.fromJson(response.toString(),ImageModel.class);
                for (Object obj:model.getImages()){
                    String i=obj.toString().replace("[","");
                    i=i.replace("]","");
                    String url[]=i.split(",");
                    imagelist.add(url[1]);
                }
                Collections.reverse(imagelist);
                listViewAdapter=new ListViewAdapter(imagelist,getApplicationContext());
                listView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                listView.setAdapter(listViewAdapter);
                listViewAdapter.setItemSelectListener(new ListViewAdapter.ItemSelector() {
                    @Override
                    public void OnSelect(int position) {
                        zoomViewPager.setCurrentItem(position);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MangaViewer.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(req);
    }
    @Override
    public void onBackPressed() {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
    }

    @Override
    public void OnClick(boolean var) {
        BottomNavigationView view_bottom=(BottomNavigationView)findViewById(R.id.bottom_bar);
        Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_up);
        Animation bottomDown= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_down);
        if(view_bottom.getVisibility()== View.VISIBLE){
            //hide it
            view_bottom.startAnimation(bottomDown);
            view_bottom.setVisibility(View.INVISIBLE);
        }else{
            //show it
            view_bottom.startAnimation(bottomUp);
                view_bottom.setVisibility(View.VISIBLE);
        }
    }
}
