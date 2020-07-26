package org.tensorflow.demo;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener
{

        Handler handler;
        private GoogleMap mMap;
        private GoogleApiClient mGoogleApiClient;
        private Location mLastLocation;
        private Marker mCurrLocationMarker;
        private LocationRequest mLocationRequest;
        public static final int REQUEST_LOCATION_CODE = 99;
        int PROXIMITY_RADIUS = 10000;
      static   double latitude, longitude;
        double end_latitude, end_longitude;
        private TextToSpeech tts;
        View id;
        Button search_btn;
        EditText search_edit;
        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
            handler = new Handler();

        id=(View)findViewById(R.id.view_id_touch);
        search_btn=(Button)findViewById(R.id.B_search);
        search_edit=(EditText)findViewById(R.id.TF_location);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = tts.setLanguage(Locale.US);
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "This Language is not supported");
                        }
                    } else {

                        Log.e("TTS", "Initialisation Failed!");
                    }
                }
            });

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listen();
                }
            }, 7000);




        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapsActivity.this, "hiiiii", Toast.LENGTH_SHORT).show();
            }
        });
        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
        private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }



        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady (GoogleMap googleMap){
            try{
                boolean success=googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyles));
                if(!success){
                    Log.e("MapsActivity","Style parsing method");
                }
            }
            catch (Resources.NotFoundException e)
            {
                Log.e("MapsActivity","Cant load",e);
            }
        mMap = googleMap;

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
    }
        protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

        public void onClick (View v)
        {
            Object dataTransfer[] = new Object[2];
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();


            switch (v.getId()) {
                case R.id.B_search: {
                    EditText tf_location = (EditText) findViewById(R.id.TF_location);
                    String location = tf_location.getText().toString();
                    List<Address> addressList = null;
                    MarkerOptions markerOptions = new MarkerOptions();
                    Log.d("location = ", location);

                    if (!location.equals("")) {
                        Geocoder geocoder = new Geocoder(this);
                        try {
                            addressList = geocoder.getFromLocationName(location, 5);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addressList != null) {
                            for (int i = 0; i < addressList.size(); i++) {
                                Address myAddress = addressList.get(i);
                                LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                                markerOptions.position(latLng);
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


                            }
                        }

                    }
                }
                break;

            }
        }

        private String getDirectionsUrl()
        {
            StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
            googleDirectionsUrl.append("origin=" + latitude + "," + longitude);
            googleDirectionsUrl.append("&destination=" + end_latitude + "," + end_longitude);
            googleDirectionsUrl.append("&key="+"AIzaSyDtvug8PT7lN7AelKlfkLSnxx6KTZK5C9c");

            return googleDirectionsUrl.toString();
        }


        private String getUrl (double latitude, double longitude, String nearbyPlace)
        {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=" + latitude + "," + longitude);
            googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
            googlePlacesUrl.append("&type=" + nearbyPlace);
            googlePlacesUrl.append("&sensor=true");
            googlePlacesUrl.append("&key=" + "AIzaSyDtvug8PT7lN7AelKlfkLSnxx6KTZK5C9c");
            Log.d("getUrl", googlePlacesUrl.toString());
            return (googlePlacesUrl.toString());
        }



        @Override
        public void onConnected (Bundle bundle){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, (LocationListener) this);
        }
    }



        @Override
        public void onConnectionSuspended ( int i){

    }


        @Override
        public void onLocationChanged (Location location){
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
Geocoder currentcode=new Geocoder(this);
            String result = null;
            try {
                Toast.makeText(this, ""+currentcode.getFromLocation(latitude,longitude,1), Toast.LENGTH_SHORT).show();
                List<Address> addressList = currentcode.getFromLocation(
                        latitude, longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
sb.append(address.getAddressLine(0));
                    result = sb.toString();
                    Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show();
                    String array1[]= result.split(" ");
                    speak("your location is ");
                   // speak("YOur current location is "+array1[0]+""+array1[1]+"\n"+array1[2]+""+array1[3]+""+array1[4]+""+array1[5]+"\n"+""+array1[8]+"\n"+array1[9]+"\n"+array1[10]+"\n"+array1[11]);
            }} catch (IOException e) {
                e.printStackTrace();
            }


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

    }

        @Override
        public void onConnectionFailed (ConnectionResult connectionResult){

    }

        public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
        public boolean checkLocationPermission () {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }



        @Override
        public void onRequestPermissionsResult (int requestCode,
                                                String permissions[], int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }



        @Override
        public boolean onMarkerClick (Marker marker){
        marker.setDraggable(true);
        Toast.makeText(this,"hello", Toast.LENGTH_SHORT).show();
        return false;
    }
    

        @Override
        public void onMarkerDragStart (Marker marker){

    }
    

        @Override
        public void onMarkerDrag (Marker marker){

    }
    

        @Override
        public void onMarkerDragEnd(Marker marker) {
        end_latitude = marker.getPosition().latitude;
        end_longitude =  marker.getPosition().longitude;

        Log.d("end_lat",""+end_latitude);
        Log.d("end_lng",""+end_longitude);
    }

    private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MapsActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);
            }
        }
    }
private void recognition(String text){
            search_edit.setText(text);
    EditText tf_location = (EditText) findViewById(R.id.TF_location);
    String location = tf_location.getText().toString();
    List<Address> addressList = null;
    MarkerOptions markerOptions = new MarkerOptions();
    Log.d("location = ", location);

    if (!location.equals("")) {
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(location, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList != null) {
            for (int i = 0; i < addressList.size(); i++) {
                Address myAddress = addressList.get(i);
                LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                markerOptions.position(latLng);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


            }
        }

    }
}
}





