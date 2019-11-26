package com.example.luvyourleftovers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import com.google.gson.Gson;
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
        private String photos;
        private String place_id;
        private String name;
        private String vicinity;

        public Shop(String photos, String place_id, String name,String vicinity){
            this.photos =photos;
            this.place_id  = place_id;
            this.vicinity = vicinity;
            this.name = name;
        }

        public String getPhotos(){return photos;}
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


        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        for(String provider: lm.getAllProviders()){
            Log.d("Provider",provider);
            if(provider.equalsIgnoreCase("passive")){
                if(checkPermissions()) {
                    Log.d("Location", lm.getLastKnownLocation(provider).toString());
                    location = lm.getLastKnownLocation(provider);
                }
                else{
                    requestPermissions();
                    Log.d("Location",lm.getLastKnownLocation(provider).toString());
                    location = lm.getLastKnownLocation(provider);
                }
            }
        }
        try {
            test(this, location);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );

    }

    private boolean checkPermissions(){
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

    private void test(Context context, Location location) throws InterruptedException {


        String url = makeNearbySearch(location);
        String apiKey = "&key=***REMOVED***";

        ArrayList<String> shopListString = new ArrayList<String>();
        ArrayList<Shop> shopList = new ArrayList<Shop>();

        Ion.getDefault(this).getConscryptMiddleware().enable(false);
        Ion.with(this)
                .load(url+apiKey)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {

                            Log.d("MAPS RESULTS", result.toString());
//                            Log.d("Values: ",result.get("results").getAsString());

                            Log.d("Key Set", result.keySet().toString());
//                            Log.d("DeepCopy", );
                            for(JsonElement g: result.getAsJsonArray("results")){
                                Log.d("JsonElement",g.toString());

                                Log.d("Key Set2",g.getAsJsonObject().keySet().toString());
                                String place_id = g.getAsJsonObject().get("place_id").toString();
                                String name = g.getAsJsonObject().get("name").toString();
                                //String photos =g.getAsJsonObject().get("photo_reference").toString();
                                String vicinity = g.getAsJsonObject().get("vicinity").toString();

                                String photo_reference = "cannot";//"https://maps.googleapis.com/maps/api/place/photo?photoreference="+photos+"&sensor=false&maxheight=100&maxwidth=100"+apiKey;

                                shopList.add(new Shop(photo_reference, place_id, name, vicinity));
                                shopListString.add(name);

                                // TODO build the view Shop List via viewList here.

                                //for now remake the shopList with just Name

                            }
                            Log.d("Shop List Size:", Integer.toString(shopList.size()));
                            final ListView list = findViewById(R.id.shopList);
//                                ArrayList<String> arrayList = readFromFile(this);

                            ArrayAdapter<Shop> arrayAdapter = new ArrayAdapter<Shop>(context,
                                    android.R.layout.simple_list_item_1, shopList);
                            list.setAdapter(arrayAdapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    //send user     to DisplayRecipe.




                                    Shop clickedShop = (Shop) list.getItemAtPosition(position);

                                    Toast.makeText(FindShops.this,clickedShop.getName(),Toast.LENGTH_LONG).show();
                                    Log.d("Place_ID:",clickedShop.getPlace_id());
//                                    String url = "https://www.google.com/maps/place/?q=place_id:"+clickedShop.getPlace_id();
                                    String url = "https://www.google.com/maps/search/?api=1&query="+clickedShop.getVicinity().replace("\"", "")+"&query_place_id="+clickedShop.getPlace_id().replace("\"", "");
//                                    Intent intent = new Intent(context, DisplayRecipe.class);
//                                    startActivity(intent);
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

//        Thread.sleep(10000);
//        String foundShops = Integer.toString(shopList.size());
//        Log.d("Number of found Shops",foundShops);
    }
}
