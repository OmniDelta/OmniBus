package com.example.ethanmann.omnibus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

public class StudentHome extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String userLocation = "";
    private String userStop_string = "";
    private String busLocation = "";
    private LocationManager locationManager;
    private LocationListener listener;
    // Connect to the Firebase database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // Get a reference to the todoItems child items it the database
    final DatabaseReference notRiding = database.getReference("busses/A/not_riding");
    final DatabaseReference busLocationDB = database.getReference("busses/A/location");
    DatabaseReference userStop = database.getReference("users/students/"+Settings.UID+"/stop");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);
        Intent intent = getIntent();
        DatabaseReference userType = database.getReference("users/students/"+Settings.UID+"/accountType");
        userType.setValue("Student");
//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //set up the nav drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.userName)).setText(Settings.user_name);
        ((TextView) headerView.findViewById(R.id.userEmail)).setText(Settings.user_email);
        ImageView userImg = headerView.findViewById(R.id.userImg);
        //Glide.with(this).load(Settings.user_img_url).into(userImg);

        userStop.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String stop = dataSnapshot.getValue(String.class);
                userStop_string = stop;
                System.out.println("USERS STOP IS " + stop);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        busLocationDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String location = dataSnapshot.getValue(String.class);
                busLocation=location;
                System.out.println("BUS LOCATION IS: " + busLocation);
                sendRequest();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        busLocationDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String location = dataSnapshot.getValue(String.class);
                busLocation=location;
                System.out.println("BUS LOCATION IS: " + busLocation);
                sendRequest();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = location.getLatitude() + "," + location.getLongitude();
                System.out.println("USER LOCATION IS: " + userLocation);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        locationManager.requestLocationUpdates("gps", 100, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, listener);

        final Button button = (Button) findViewById(R.id.sendAddress);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Create a new child with a auto-generated ID.
                DatabaseReference childRef = notRiding.push();

                //get the user's stop
                userStop.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String stop = dataSnapshot.getValue(String.class);
                        userStop_string = stop;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

                // Set the child's data to the user's stop
                childRef.setValue(userStop_string);

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sendRequest();
        // Create a new Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);
        sendRequest();

    }


    private void sendRequest() {
        if (((userStop_string != null) && (busLocation != null))&& !(userStop_string.isEmpty()) && !(busLocation.isEmpty())) {
            System.out.println("STARTING REQUEST WITH USER STOP [" + userStop_string + "] AND BUS LOCATION [" + busLocation + "]");
            try {
                new DirectionFinder(this, userLocation, busLocation).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return;
        }
        if ((userStop_string != null) && userStop_string.isEmpty()) {
            Toast.makeText(this, "Stop not yet found. Using location.", Toast.LENGTH_SHORT).show();
        }
        if ((userLocation != null) && userLocation.isEmpty()) {
            Toast.makeText(this, "Can't find your location!", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((busLocation != null) && busLocation.isEmpty()) {
            Toast.makeText(this, "Can't find your bus!", Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println("STARTING REQUEST WITH USER LOCATION [" + userLocation + "] AND BUS LOCATION [" + busLocation + "]");
        try {
            new DirectionFinder(this, userLocation, busLocation).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng school = new LatLng(41.224962, -73.185090);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(school, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Fairchild Wheeler")
                .position(school)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bus_location))));
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.endLocation, 16));
            if(false){//((Switch) findViewById(R.id.eta)).isChecked()) {
                ((TextView) findViewById(R.id.tvEta)).setText(route.duration.getArrival());
            }
            else {
            ((TextView) findViewById(R.id.tvEta)).setText(route.duration.text);
            }
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bus_location))
                    .title("Bus")
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                // first check for permissions
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                                ,10);
                    }
                    return;
                }
                locationManager.requestLocationUpdates("gps", 100, 0, listener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, listener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, listener);
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent home = new Intent(this, StudentHome.class);
        Intent analytics = new Intent(this, StudentHome.class);
        Intent businfo = new Intent(this, BusInfo.class);
        Intent other = new Intent(this, StudentHome.class);
        Intent settings = new Intent(this, Settings.class);
        Intent help = new Intent(this, StudentHome.class);
        Intent feedback = new Intent(this, Feedback.class);
        Intent about = new Intent(this, About.class);
        Intent wip = new Intent(this, WIP.class);

        if (id == R.id.nav_home) {
            startActivity(home);
        } else if (id == R.id.nav_analytics) {
            startActivity(wip);
        } else if (id == R.id.nav_businfo) {
            startActivity(businfo);
        } else if (id == R.id.nav_other) {
            startActivity(wip);
        } else if (id == R.id.nav_settings) {
            startActivity(settings);
        } else if (id == R.id.nav_help) {
            startActivity(wip);
        } else if (id == R.id.nav_feedback) {
            startActivity(feedback);
        } else if (id == R.id.nav_about) {
            startActivity(about);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
