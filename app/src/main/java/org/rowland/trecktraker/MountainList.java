package org.rowland.trecktraker;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MountainList extends AppCompatActivity {
    public static final String robertIsStupid = "mountain";
    List<Button> mounatinButtons = new ArrayList<>(100);
    LinearLayout buttonList;
    static ArrayList<Mountain> mountainList = new ArrayList<Mountain>(100);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_list);
        buttonList = (LinearLayout) findViewById(R.id.buttonList);
        loadMountainsTest();
        buttonCreator();
        playServices();


    }
    protected void onDestroy() {
       super.onDestroy();
        mountainList.clear();
        mounatinButtons.clear();
    }

    public void buttonCreator () {

        for (int i = 0; i < mountainList.size(); i++) {
            Button b = new Button(this);
            mounatinButtons.add(b);
            buttonList.addView(b);
            mounatinButtons.get(i).setId(i);
            mounatinButtons.get(i).setText(mountainList.get(i).name);
            b.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            mountainClicked(v);
                        }
                    }
            );

        }
    }
    private void loadMountains() {
        try {
            InputStream is = getClassLoader().getResourceAsStream("broken.properties");
            BufferedReader r =  new BufferedReader(new InputStreamReader(is));
            String line = r.readLine();
            while (line != null) {
                mountainList.add( new Mountain(line));
                line = r.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMountainsTest() {
        mountainList.add(new Mountain("Algonquin,2,5114,44.143750,-73.986600"));
        mountainList.add(new Mountain("Armstrong,22,4400,44.134600,-73.849700"));
        mountainList.add(new Mountain("Basin,9,4827,44.121000,-73.887000"));
        mountainList.add(new Mountain("Big Slide,27,4240,44.182300,-73.870400"));
        mountainList.add(new Mountain("Blake Peak,43,3960,44.081300,-73.844500"));
        mountainList.add(new Mountain("Cascade,36,4098,44.218700,-73.860100"));
    }

    private void mountainClicked(View v) {

        Intent i = new Intent(this ,Details.class);
        //Mountain m = mountainList.get(v.getId());
        String s = Integer.toString(v.getId());
        i.putExtra(robertIsStupid, s);
        startActivity(i);
    }
    public void playServices() {
        GoogleApiAvailability a = GoogleApiAvailability.getInstance();
        int i = a.isGooglePlayServicesAvailable(this);
        if (i == ConnectionResult.SUCCESS) {
            Toast.makeText(this, "Yeah", Toast.LENGTH_LONG).show();
        }
        else if (a.isUserResolvableError(i)) {
            Dialog d = a.getErrorDialog(this, i, 0);
            d.show();
        }
        else {
            Toast.makeText( this, "Fuck Play Sercives", Toast.LENGTH_LONG).show();
        }
    }



}

