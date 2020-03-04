package com.example.lokasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.example.lokasi.MapsActivity.EXTRA_LATITUDE;
import static com.example.lokasi.MapsActivity.EXTRA_LONGITUDE;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnSuccessListener<Location>{

    Button btGetLoc;
    TextView _txUpda;
    private FusedLocationProviderClient fusedLocationClient;
    Task<Location> lastLocation;
    Button btnMaps;
    private double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btGetLoc = findViewById(R.id.btGetLock);
        btGetLoc.setOnClickListener(this);
        _txUpda = findViewById(R.id.txUpdateLok);
        btnMaps = findViewById(R.id.btn_nextact);

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra(EXTRA_LATITUDE, latitude);
                intent.putExtra(EXTRA_LONGITUDE, longitude);
                startActivity(intent);

            }
        });
// if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 123);

        fusedLocationClient = getFusedLocationProviderClient(this);
        createLocationRequest();


    }

    @Override
    public void onClick(View v) {


        lastLocation = fusedLocationClient.getLastLocation();
        lastLocation.addOnSuccessListener(this,this);
        //Toast.makeText(this,"halo",Toast.LENGTH_LONG).show();

    }


    @Override
    public void onSuccess(Location location) {
        if (location!=null)
        {
            String lat = String.valueOf(location.getLatitude());
            String longi = String.valueOf(location.getLongitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Toast.makeText(this,lat+" "+longi,Toast.LENGTH_SHORT).show();

        }
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                       // onLocationChanged(locationResult.getLastLocation());
                        String msg = "Updated Location: " +
                                Double.toString(locationResult.getLastLocation().getLatitude()) + "," +
                                Double.toString(locationResult.getLastLocation().getLongitude());
                        _txUpda.setText(msg);
                    }
                },
                Looper.myLooper());

      /*  task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                123);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });*/

   }

}
