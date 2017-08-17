package org.rowland.trecktraker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Details extends AppCompatActivity {
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent i = getIntent();
        position = i.getIntExtra("mountainList", 0);
        getSupportActionBar().setTitle(MountainList.mountainList.get(position).name);
        widgetInit();
    }

    private void widgetInit() {
        TextView elevation = (TextView) findViewById(R.id.Elavation);
        elevation.setText("Elevation of " + Integer.toString(MountainList.mountainList.get(position).height) + "ft");
        TextView rank = (TextView) findViewById(R.id.Rank);
        int i = MountainList.mountainList.get(position).rank % 10;
        if ((MountainList.mountainList.get(position).rank % 10) == 2 || (MountainList.mountainList.get(position).rank % 10) == 3) {
            rank.setText(Integer.toString(MountainList.mountainList.get(position).rank) + "nd highest mountain");
        }
        else if ((MountainList.mountainList.get(position).rank % 10) == 1) {
            rank.setText("Tallest Mountain");
        }
        else {
            rank.setText(Integer.toString(MountainList.mountainList.get(position).rank) + "th highest mountain");
        }
        Button b = (Button) findViewById(R.id.startClimb);
        b.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        activityChange(v);
                    }
                }
        );
    }

    private void activityChange(View v) {
        Intent i = new Intent(this ,HikeTracker.class);
        i.putExtra("details", position);
        startActivity(i);
    }
}
