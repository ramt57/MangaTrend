package com.manga.ramt57.mangareader.trend;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.manga.ramt57.mangareader.trend.adapter.ChapterAdapter;
import com.manga.ramt57.mangareader.trend.pojomodels.ChapterDetail;
import com.manga.ramt57.mangareader.trend.pojomodels.ImageModel;
import com.tonyodev.fetch.Fetch;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class Chapterlist extends AppCompatActivity implements ChapterAdapter.viewchapter, ChapterAdapter.DownloadCHapter {
    private String url = "https://www.mangaeden.com/api/manga/";
    ProgressDialog gress;
    RecyclerView recyclerView;
    ImageView img;
    ChapterAdapter adapter;
    ProgressDialog bar;
    ArrayList<String> chapNo = new ArrayList<>();
    ArrayList<String> chapId = new ArrayList<>();
    static final int WRITE_PERMISSION = 1001;
    ArrayAdapter<String> adapter1;
    static ArrayList<String> imagelist = new ArrayList<>();
    TextView genre;
    //    GridView listView;
    String msgdescription = "";
    CoordinatorLayout coordinatorLayout;
    Fetch fetch;
    TextView title, total_chapter, author, artist, released;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_chapterlist);
        bar = new ProgressDialog(this);
        bar.setIndeterminate(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        img = (ImageView) findViewById(R.id.imageView);
        title = (TextView) findViewById(R.id.textView);
        genre = (TextView) findViewById(R.id.category2);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        adapter = new ChapterAdapter(chapNo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.parseColor("#BBBBBB"))
                        .sizeResId(R.dimen.divider)
                        .build());
        recyclerView.setAdapter(adapter);
//        listView=(GridView)findViewById(R.id.list_item);
//        listView.setNumColumns(1);
        total_chapter = (TextView) findViewById(R.id.textView6);
        author = (TextView) findViewById(R.id.textView2);
        artist = (TextView) findViewById(R.id.textView7);
        released = (TextView) findViewById(R.id.textView4);
        url = url + getIntent().getStringExtra("ID");
        volleyCall();
        gress = new ProgressDialog(this);
        gress.setMessage("Loding Chapter List...");
        gress.setIndeterminate(true);
        fetch = Fetch.newInstance(getApplicationContext());
        new Fetch.Settings(getApplicationContext()).setConcurrentDownloadsLimit(2).apply();
//        Button button=(Button)findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bulidDialog(msgdescription);
//            }
//        });

        ChapterAdapter.setChapterlist(this);
        ChapterAdapter.setDownloadListener(this);
        gress.show();
    }

    private void volleyCall() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        ChapterDetail chapter = new ChapterDetail();
                        chapter = gson.fromJson(response.toString(), ChapterDetail.class);
                        Glide.with(getApplicationContext()).load("https://cdn.mangaeden.com/mangasimg/" + chapter.getImage())
                                .apply(new RequestOptions()
                                        .override(154,250).centerCrop().placeholder(R.drawable.placeholder).error(R.drawable.error))
                                .into(img);
//                        Picasso.with(getApplicationContext())
//                                .load("https://cdn.mangaeden.com/mangasimg/" + chapter.getImage()).resize(154, 230).centerCrop()
//                                .placeholder(R.drawable.placeholder)
//                                .error(R.drawable.error)
//                                .into(img);
                        title.setText(chapter.getTitle());
                        total_chapter.setText(chapter.getChapters_len() + " chapters");
                        author.setText("Author: " + chapter.getAuthor());
                        artist.setText("Artist: " + chapter.getArtist());
                        released.setText("Released: " + chapter.getReleased());
                        msgdescription = chapter.getDescription();
                        TextView textView = (TextView) findViewById(R.id.description);
                        textView.setText(msgdescription);
                        StringBuffer stringBuffer = new StringBuffer();
                        for (String catgry : chapter.getCategories()) {
                            stringBuffer.append(catgry + " | ");
                        }
                        genre.setText(stringBuffer.toString());
//                            adapter1=new ArrayAdapter<String>(getApplicationContext(),R.layout.category_row,R.id.textView3,chapter.getCategories());
//                            listView.setAdapter(adapter1);
                        Object ov[] = chapter.getChapters().toArray();
                        for (Object d : ov) {
                            Log.d("URL", d.toString() + 'd');
                            String red = d.toString().replace("[", "");
                            red = red.replace("]", "");
                            String newarray[] = red.split(",");
                            chapNo.add(newarray[0]);
                            chapId.add(newarray[newarray.length - 1]);
                        }
                        adapter.notifyDataSetChanged();
                        gress.dismiss();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        gress.dismiss();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void bulidDialog(String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(description)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void viewChap(int position) {
        chapNo.get(position);
        Intent i = new Intent(getApplicationContext(), MangaViewer.class);
        i.putExtra("ID", chapId.get(position));
        startActivity(i);
    }

    @Override
    public void DownloadChap(int position) {
        if (checkPermission()) {
            callvolley(position);
        }
    }

    private void DownloadImages(final ArrayList<String> imagelist1, final int position) {
        Snackbar.make(coordinatorLayout, "Saved to sdcard/MangaTrend .", Snackbar.LENGTH_LONG).show();
        for (final String imgurl : imagelist1) {
            String PATH = Environment.getExternalStorageDirectory() + "/" + "MangaTrend" + "/";
            final File folder = new File(PATH);
            if (!folder.exists()) {
                folder.mkdir();//If there is no folder it will be created.
            }
            com.tonyodev.fetch.request.Request request = new com.tonyodev.fetch.request.Request("https://cdn.mangaeden.com/mangasimg/" + imgurl.trim(), folder.toString() + "/" + title.getText().toString() + "/" + chapNo.get(position) + "/", imgurl.replace("/", ""));
            final long downloadId = fetch.enqueue(request);
            if (downloadId != Fetch.ENQUEUE_ERROR_ID) {
                //Download was successfully queued for download.
            }
        }

    }

    private boolean checkPermission() {
        int writestoragepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writestoragepermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_PERMISSION);
        } else {
            //permission granted download
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // download task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please Accept The Sd card permission.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void callvolley(final int pos) {
        if (bar.isShowing()) {
            bar.dismiss();
        }
        bar.setMessage("Downloading..");
        bar.show();
        final ArrayList<String> listimg = new ArrayList<>();
        String urlsd = "https://www.mangaeden.com/api/chapter/";
        listimg.clear();
        urlsd = urlsd + chapId.get(pos).trim() + "/";
        StringRequest req = new StringRequest(Request.Method.GET, urlsd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ImageModel model = gson.fromJson(response.toString(), ImageModel.class);
                for (Object obj : model.getImages()) {
                    String i = obj.toString().replace("[", "");
                    i = i.replace("]", "");
                    String url[] = i.split(",");
                    listimg.add(url[1]);
                }
                Collections.reverse(listimg);
                DownloadImages(listimg, pos);
                bar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                bar.dismiss();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(req);
    }
}
