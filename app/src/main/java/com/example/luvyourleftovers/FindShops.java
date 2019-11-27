/**
* <h1>Find Shops Activity</h1>
* This java file is the activity of FindShops. The main functionality of thhis
* file is to use the Google API and return a list of nearby shops. The shops are 
* ranked by   
*/




package com.example.luvyourleftovers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FindShops extends AppCompatActivity {


    private class Shop{
        /* Private inner class that creates the data structure that 
            acts as an object type store (which we abstract from the google
            maps api into our simplified version).

            classic getter method that is initialised with all its necessary
            data at the start. at the start.

        */
        // private String photos;
        private String place_id;
        private String name;
        private String vicinity;    //represents the address. (name kept for convention sake).

        public Shop(String place_id, String name,String vicinity){
            // this.photos =photos;
            this.place_id  = place_id;
            this.vicinity = vicinity;
            this.name = name;
        }

        //getter methods for all our objects.
        // public String getPhotos(){return photos;}
        public String getPlace_id(){return place_id;}
        public String getName(){return name;}
        public String getVicinity(){return vicinity;}


        public String toString(){
            return "Name: "+this.name+"\nAddress: "+this.vicinity;
        }
    }



    private static final int PERMISSION_ID = 44;
    private Location location;

    private LocationManager lm;
    private LocationListener ll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_shops);

        //LocationManager is initiailised (with TODO Explain more).
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions();
        }


        for(String provider: lm.getAllProviders()){
            Log.d("Provider",provider);
            if(provider.equalsIgnoreCase("passive")){
                //below case checks if permissions are granted for Coarse and Fine Location
                //Permissions. If not then these permissions are requested.
                if(!checkPermissions()) 
                    requestPermissions();
                //Location of user is achieved here. Use this for finding nearby shops.
                location = lm.getLastKnownLocation(provider);
            }
        }
        try {
            CreateListOfShops(this, location);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private void requestPermissions(){
        //Requests permission for Coarse and Fine Location Access of the android.
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }


    private boolean checkPermissions(){
        /**
         * checks that coarseand fine location permissions are granted. returns a boolean
         * variable wether they are or not.
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }


    private String makeNearbySearch(Location location){

        String longitude = Double.toString(location.getLongitude());
        String latitude = Double.toString(location.getLatitude());

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

        String full_query_url = "";
        String location_parameter = "location="+latitude+","+longitude;
        String query ="&rankby=distance&type=store&type=food";

        Log.d("Current Location", longitude+","+latitude);
        return url+location_parameter+query;
    }

    private void CreateListOfShops(Context context, Location location) throws InterruptedException {
        /*
            CreateListOfShops uses the Google Maps API in order to call for a given maps query.
            The list of returned places is then displayed in this Activity (FindShops).
            A user clicks on the shop they prefer and the application sends them off to Maps App
            with the given place open as its current place.
        */


        //function returns a url containing users location and a set of parameters indicating type of 
        //place to look for  (for our app we are only looking for 'stores' for ingredients not available.) 
        String url = makeNearbySearch(location);
        //TODO change this, very bad lmao
        String apiKey = "&key=***REMOVED***";

        //Use Ion to make a api call via http. This will return a JSON Object 
        //which we use to get the nearest places (shops) of a user.
        Ion.getDefault(this).getConscryptMiddleware().enable(false);
        Ion.with(this)
                .load(url+apiKey)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {

                            ArrayList<Shop> shopList = new ArrayList<Shop>();
                            for(JsonElement g: result.getAsJsonArray("results")){
                                String place_id = g.getAsJsonObject().get("place_id").toString();
                                String name = g.getAsJsonObject().get("name").toString();
                                String vicinity = g.getAsJsonObject().get("vicinity").toString();
                                // String photo_reference = "cannot";//"https://maps.googleapis.com/maps/api/place/photo?photoreference="+photos+"&sensor=false&maxheight=100&maxwidth=100"+apiKey;

                                shopList.add(new Shop(place_id, name, vicinity));
                                shopListString.add(name);

                            }
                            Log.d("Shop List Size:", Integer.toString(shopList.size()));
                            final ListView list = findViewById(R.id.shopList);

                            ArrayAdapter<Shop> arrayAdapter = new ArrayAdapter<Shop>(context,
                                    android.R.layout.simple_list_item_1, shopList);
                            list.setAdapter(arrayAdapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Shop clickedShop = (Shop) list.getItemAtPosition(position);

                                    Toast.makeText(FindShops.this,clickedShop.getName(),Toast.LENGTH_LONG).show();
                                    Log.d("Place_ID:",clickedShop.getPlace_id());

                                    String url = "https://www.google.com/maps/search/?api=1&query="+clickedShop.getVicinity().replace("\"", "")+"&query_place_id="+clickedShop.getPlace_id().replace("\"", "");

                                    String uri = url;//"http://maps.google.com/maps?saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + destinationLatitude + "," + destinationLongitude;
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                    startActivity(intent);
                                }
                            });


                        } catch (Exception ex) {
                            Toast.makeText(FindShops.this, "Error Loading from API, please try again.", Toast.LENGTH_SHORT).show();
                        }


                    }

                });
    }
}
