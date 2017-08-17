package org.rowland.trecktraker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;

public class HikeTracker extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    int position = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    Location oldLocation;
    Location newLocation;
    Polyline path;
    ArrayList<LatLng> points = new ArrayList<>(10);
    PolylineOptions pLOptions;
    boolean drawPolyline = false;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    TextView distanceTravelled;
    TextView distanceLeft;
    TextView currentElevation;
    TextView averageSpeed;
    double averageTotal;
    int numberofValues;
    double totalDistance;
    LatLng summit;
    Location lastLocation;
    Button checkIn;
    boolean updateInfo = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_tracker);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                  onLocationChange(location);
                }
            };
        };
        Intent i = getIntent();
        position = i.getIntExtra("details", 0);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
               .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        summit = new LatLng(MountainList.mountainList.get(position).Long, MountainList.mountainList.get(position).Lat);
        distanceTravelled = (TextView) findViewById(R.id.distanceTraelled);
        distanceLeft = (TextView) findViewById(R.id.distanceToSummit);
        currentElevation = (TextView) findViewById(R.id.currentElevation);
        averageSpeed = (TextView) findViewById(R.id.averageSpeed);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            onLocationChange(location);
                        }
                    }
                });
        Button pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        onPause(false);
                    }
                }
        );
        checkIn = (Button) findViewById(R.id.checkIn);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
               createLocationRequest();

        } else {
            Toast.makeText(this, "Location is needed", Toast.LENGTH_LONG).show();
        }
        Color.argb(50,0,100,255);
        LatLng test = new LatLng(4.6,4.5);
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(summit)
                .title("Summit of " + MountainList.mountainList.get(position).name)
                .snippet(Integer.toString(MountainList.mountainList.get(position).height) + "ft"));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(summit)
                .radius(150)
                .strokeColor(Color.argb(50,0,0,255))
                .fillColor(Color.argb(50,0,100,255)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(summit, 13));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            //pLOptions.addAll(points);
           /* pLOptions.color(4)
                    .width(5); */
            path = mMap.addPolyline(new PolylineOptions().color(COLOR_PURPLE_ARGB).width(30));

    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }
    private void onLocationChange(Location l) {
        float[] result = new float[1];
        float altitude;
        double distanceSummit;
        if (lastLocation == null) {
            lastLocation = l;
        }
        if (l.distanceTo(lastLocation) > lastLocation.getAccuracy() && l.distanceTo(lastLocation) < 500 || updateInfo) {
            LatLng a = new LatLng(l.getLatitude(), l.getLongitude());
            points.add(a);
            path.setPoints(points);
            drawPolyline = true;
            averageTotal = averageTotal + (l.getSpeed() / .44704f);
            Location.distanceBetween(l.getLatitude(), l.getLongitude(), summit.latitude, summit.longitude, result);
            distanceSummit = result[0] / 1609.344f;
            distanceSummit = (double)Math.round(distanceSummit * 100d) / 100d;

            currentElevation.setText("Elevation " + (l.getAltitude() * 3.2808) + "ft");
                totalDistance = totalDistance + (lastLocation.distanceTo(l) / 1609.344f);

                distanceLeft.setText(Double.toString(distanceSummit));
                if (Double.isNaN(averageTotal / numberofValues)) {
                    averageSpeed.setText("Average 0.0 mph");
                }else {
                    averageSpeed.setText("Average " + (averageTotal / numberofValues) + " mph");
                }
                distanceTravelled.setText("You have  gone " + totalDistance + "miles");
                if (distanceSummit < 30 + l.getAccuracy()) {
                    onPause(true);
                }

            lastLocation = l;
            updateInfo = false;
        }
        numberofValues++;

    }

    private void onPause(boolean atSummit) {
        if (atSummit) {
            checkIn.setVisibility(View.VISIBLE);
            checkIn.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            activityChanger();
                        }
                    }
            );
        }
        if (false) {

        }
    }
    private void activityChanger() {
        Intent i = new Intent(this ,AtSummitData.class);

        i.putExtra("HikeTracker", position);
        startActivity(i);
    }

}
