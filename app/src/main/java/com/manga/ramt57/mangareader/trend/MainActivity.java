package com.manga.ramt57.mangareader.trend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.manga.ramt57.mangareader.trend.adapter.RecyclerAdapter;
import com.manga.ramt57.mangareader.trend.pojomodels.Mangabasemodel;
import com.manga.ramt57.mangareader.trend.pojomodels.Mangalist;
import com.manga.ramt57.mangareader.trend.pojomodels.SearchPojo;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recyclerView;
    String url = "https://www.mangaeden.com/api/list/0/";
    ArrayList<Mangalist> mangalist = new ArrayList<>();
    static ArrayList<Mangalist> listmanga = new ArrayList<>();
    ProgressDialog gress;
    RecyclerAdapter adapter;
    MaterialSearchView searchView;
    RelativeLayout previous,next;
    ArrayList<Mangalist> category=new ArrayList<>();
    SharedPreferences categoryPrefrences;
    SharedPreferences.Editor editor;
   static ArrayList<SearchPojo> searchList=new ArrayList<>();
    int page=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchvolleyrequest();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gress = new ProgressDialog(this);
        gress.setIndeterminate(true);
        volleyrequest(0);
        gress.setMessage("Fetching Manga..");
        gress.show();
        categoryPrefrences=getSharedPreferences("CATEGORY",MODE_PRIVATE);
        editor=categoryPrefrences.edit();
        final List<String> g = new ArrayList<String>();
        adapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                for (SearchPojo pojo:searchList){
                    if(pojo.getTitle()!=null){
                        if(pojo.getTitle().equals(query)){
                            Intent i=new Intent(getApplicationContext(), Chapterlist.class);
                            i.putExtra("ID",pojo.getId());
                            startActivity(i);
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                //search manga here

                if (newText.length() > 2) {
                    g.clear();
                    for (SearchPojo l : searchList) {
                        String alias=l.getTitle().toLowerCase().trim();
                        alias=alias.replace("*","");
                        alias=alias.replace("+","");
                        alias=alias.replace("/","");
                        alias=alias.replace("!","");
                        alias=alias.replace(":","");
                        alias=alias.replace("?","");
                        alias=alias.replace("-","");
                        alias=alias.replace("[","");
                        alias=alias.replace("]","");
                        alias=alias.replace("@","");
                        alias=alias.replace("(","");
                        alias=alias.replace(")","");
                        alias=alias.replace("_","");
                        String cmpStr=newText.toLowerCase().trim();
                        if(alias!=null&&!alias.isEmpty()){
                            if (alias.contains(cmpStr)){
                                g.add(l.getTitle());
                                Log.d("RAG",l.getTitle());
                            }
                        }
                    }
                    sortlist(g);
                }
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }
            @Override
            public void onSearchViewClosed() {
                //Do some magic
                Log.d("TAG", "search closed");
            }
        });
        previous=(RelativeLayout)findViewById(R.id.previous);
        next=(RelativeLayout)findViewById(R.id.next);
        previous.setVisibility(View.GONE);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gress.show();
                if(page<=34&&page>0){
                    volleyrequest(--page);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gress.show();
                if(page<34&&page>=0){
                    volleyrequest(++page);
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        try{
            navigationView.getMenu().getItem(categoryPrefrences.getInt("ID",0)).setChecked(true);
        }catch (Exception e){
            try {
                navigationView.getMenu().getItem(2).setChecked(true);
            }catch (Exception e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void sortlist(List<String> g) {
        String arrays[] = new String[g.size()];
                    arrays = g.toArray(arrays);
                    searchView.setSuggestions(arrays);
//        for (SearchPojo pojo:searchList){
//            if(pojo.getTitle()!=null){
//                if(g.contains(pojo.getTitle())){
//                    Log.d("sap",pojo.getTitle());
//                }
//            }
//        }
    }

    private void volleyrequest(final int  i) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url+"?p="+i, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if(i==0){

                            if(previous.getVisibility()==View.VISIBLE){
                                previous.setVisibility(View.GONE);
                            }
                        }
                        if(i==34){
                            if(next.getVisibility()==View.VISIBLE){
                                next.setVisibility(View.GONE);
                            }
                        }else{
                            if(next.getVisibility()==View.GONE){
                                next.setVisibility(View.VISIBLE);
                            }

                        }
                        if(i==0){
                            if(previous.getVisibility()==View.VISIBLE){
                                previous.setVisibility(View.GONE);
                            }

                        }else{
                            if(previous.getVisibility()==View.GONE){
                                previous.setVisibility(View.VISIBLE);
                            }

                        }
                        Gson gson = new Gson();
                        Mangabasemodel people;
                        people = gson.fromJson(response.toString(), Mangabasemodel.class);
                        mangalist = people.getManga();
                        listmanga=people.getManga();
                        showcategoryData();
                        gress.dismiss();
                        
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        View view=findViewById(R.id.cordinate);
                        Log.d("TAG",error.toString());
                        Snackbar.make(view,"Check internet connection or refresh",Snackbar.LENGTH_LONG).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyrequest(0);
                            }
                        }).show();
                        gress.dismiss();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.all:
                editor.putString("CAT","All");
                editor.putInt("ID",0);
                editor.apply();
                showcategoryData();
                break;
            case R.id.action:
                editor.putString("CAT","Action");
                editor.putInt("ID",1);
                editor.apply();
                showcategoryData();
                break;
            case R.id.adult:
                editor.putString("CAT","Adult");
                editor.putInt("ID",2);
                editor.apply();
                showcategoryData();
                break;
            case R.id.adventure:
                editor.putString("CAT","Adventure");
                editor.putInt("ID",3);
                editor.apply();
                showcategoryData();
                break;
            case R.id.comedy:
                editor.putString("CAT","Comedy");
                editor.putInt("ID",4);
                editor.apply();
                showcategoryData();
                break;
            case R.id.drama:
                editor.putString("CAT","Drama");
                editor.putInt("ID",5);
                editor.apply();
                showcategoryData();
                break;
            case R.id.fantasy:
                editor.putString("CAT","Fantasy");
                editor.putInt("ID",6);
                editor.apply();
                showcategoryData();
                break;
            case R.id.mature:
                editor.putString("CAT","Mature");
                editor.putInt("ID",7);
                editor.apply();
                showcategoryData();
                break;
            case R.id.mystery:
                editor.putString("CAT","Mystery");
                editor.putInt("ID",8);
                editor.apply();
                showcategoryData();
                break;
            case R.id.school:
                editor.putString("CAT","School Life");
                editor.putInt("ID",9);
                editor.apply();
                showcategoryData();
                break;
            case R.id.shounen:
                editor.putString("CAT","Shounen");
                editor.putInt("ID",10);
                editor.apply();
                showcategoryData();
                break;
            case R.id.seinen:
                editor.putString("CAT","Seinen");
                editor.putInt("ID",11);
                editor.apply();
                showcategoryData();
                break;
            case R.id.smut:
                editor.putString("CAT","Smut");
                editor.putInt("ID",12);
                editor.apply();
                showcategoryData();
                break;
            case R.id.supernatural:
                editor.putString("CAT","Supernatural");
                editor.putInt("ID",13);
                editor.apply();
                showcategoryData();
                break;
            case R.id.yuri:
                editor.putString("CAT","Yuri");
                editor.putInt("ID",15);
                editor.apply();
                showcategoryData();
                break;
            case R.id.josei:
                editor.putString("CAT","Josei");
                editor.putInt("ID",16);
                editor.apply();
                showcategoryData();
                break;
            case R.id.historical:
                editor.putString("CAT","Historical");
                editor.putInt("ID",17);
                editor.apply();
                showcategoryData();
                break;
            case R.id.martial_arts:
                editor.putString("CAT","Martial Arts");
                editor.putInt("ID",18);
                editor.apply();
                showcategoryData();
                break;
            case R.id.romance:
                editor.putString("CAT","Romance");
                editor.putInt("ID",20);
                editor.apply();
                showcategoryData();
                break;
            case R.id.sci:
                editor.putString("CAT","Sci-fi");
                editor.putInt("ID",21);
                editor.apply();
                showcategoryData();
                break;
            case R.id.sports:
                editor.putString("CAT","Sports");
                editor.putInt("ID",22);
                editor.apply();
                showcategoryData();
                break;
            case R.id.tragedy:
                editor.putString("CAT","Tragedy");
                editor.putInt("ID",23);
                editor.apply();
                showcategoryData();
                break;
            case R.id.yaoi:
                editor.putString("CAT","Yaoi");
                editor.putInt("ID",24);
                editor.apply();
                showcategoryData();
                break;
            default:
                editor.putString("CAT","All");
                editor.putInt("ID",25);
                editor.apply();
                showcategoryData();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=mangatrends.newmangareader.infotrench.ramt57.best.com")));
                return true;
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Best Manga reader app download now.https://play.google.com/store/apps/details?id=mangatrends.newmangareader.infotrench.ramt57.best.com";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            case R.id.refresh:
                gress.show();
                Toast.makeText(this, "refreshing..", Toast.LENGTH_SHORT).show();
                volleyrequest(0);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void searchvolleyrequest() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        InputStream inputStream=new ByteArrayInputStream(response.toString().getBytes());
                        try {
                            Reader streamReader=new InputStreamReader(inputStream,"UTF-8");
                            JsonReader reader = new JsonReader(streamReader);
                            reader.beginObject();
                            while (reader.hasNext()) {
                                String name = reader.nextName();
                                if (name.equals("manga")) {
                                    reader.beginArray();
                                    while (reader.hasNext()) {
                                        SearchPojo pojo=new SearchPojo();
                                        reader.beginObject();
                                        while (reader.hasNext()) {
                                            String title = reader.nextName();
                                            if (title.equals("t")) {
                                                String arc=reader.nextString();
                                                pojo.setTitle(arc);
                                            }else if(title.equals("i")){
                                                String id=reader.nextString();
                                                pojo.setId(id);
                                            }else if(title.equals("im")&&reader.peek()!= JsonToken.NULL) {
                                                String img=reader.nextString();
                                                pojo.setImg(img);
                                            } else {
                                                reader.skipValue();
                                            }
                                        }
                                        reader.endObject();
                                        if(pojo.getTitle()!=null){
                                            searchList.add(pojo);
                                        }
                                    }
                                    reader.endArray();
                                }else {
                                    reader.skipValue(); // avoid some unhandle events
                                }
                            }
                            reader.endObject();
                            reader.close();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("TAG1",error.toString());
                        Toast.makeText(MainActivity.this, "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        gress.dismiss();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
    public void showcategoryData(){
        String cat=categoryPrefrences.getString("CAT","All");
        if(cat.equals("All")){
            adapter.setData(listmanga);
            adapter.notifyDataSetChanged();
        }else{
            ArrayList<Mangalist> news=new ArrayList<>();
            for (Mangalist list:mangalist){
                for(String s:list.getC()){
                    if(s.equals(cat)){
                        news.add(list);
                    }
                }
            }
            adapter.setData(news);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
