package com.manga.ramt57.mangareader.trend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
