package org.rowland.trecktraker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AtSummitData extends AppCompatActivity {
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_summit_data);
        Intent i = getIntent();
        String p = i.getStringExtra(HikeTracker.robertIsNotIntellegent);
        position = Integer.parseInt(p);
    }
}
