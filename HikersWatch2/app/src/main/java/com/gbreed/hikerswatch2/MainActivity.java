package com.gbreed.hikerswatch2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    LocationManager locationManager;
    LocationListener locationListener;

    TextView textLat;
    TextView textLong;
    TextView textAcc;
    TextView textAlt;
    TextView textAdd;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textLat = findViewById(R.id.textViewLat);
        textLong = findViewById(R.id.textViewLong);
        textAcc = findViewById(R.id.textViewAcc);
        textAlt = findViewById(R.id.textViewAlt);
        textAdd = findViewById(R.id.textViewAdd);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                textLat.setText("Latitude: " + location.getLatitude());
                textLong.setText("Longitude: " + location.getLongitude());
                textAcc.setText("Accuracy: " + location.getAccuracy());
                textAlt.setText("Altitude: " + location.getAltitude());

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                    if(addresses != null && addresses.size() > 0)
                    {
                        String place = "Address:\n";

                        if(addresses.get(0).getThoroughfare() != null)
                            place = place + addresses.get(0).getThoroughfare() + "\n";

                        if(addresses.get(0).getPostalCode() != null)
                            place = place + addresses.get(0).getPostalCode() + " ";

                        if(addresses.get(0).getAdminArea() != null)
                            place = place + addresses.get(0).getAdminArea();

                        textAdd.setText(place);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT < 23)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {   //Requesting Permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
            }
        }
    }
}
