/**
* <h1>Find Shops Activity</h1>
* This java file is the activity of FindShops. The main functionality of thhis
* file is to use the Google API and return a list of nearby shops. The shops are 
* ranked by   
*/

package com.example.luvyourleftovers;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luvyourleftovers.basic_classes.DBHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.util.ArrayList;

public class FindShops extends AppCompatActivity implements
        RecyclerViewAdapter.ItemClickListener{

    CustomAdapter rvaAdapter;
    ArrayList<Shop> shopList;
    @Override
    public void onItemClick(View view, int position) {

    }





    private static final int PERMISSION_ID = 44;
    private Location location;

    private LocationManager lm;
    private LocationListener ll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_shops);
        if(!checkPermissions())
            requestPermissions();
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
        String query ="&rankby=distance&type=supermarket";

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

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //function returns a url containing users location and a set of parameters indicating type of 
        //place to look for  (for our app we are only looking for 'stores' for ingredients not available.) 
        String url = makeNearbySearch(location);
        //TODO change this, very bad lmao
        String apiKey = "&key=***REMOVED***";
       ArrayList<String> shopListNames = new ArrayList<>();
//       String apiKey = "&key=***REMOVED***";
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

                            shopList = new ArrayList<Shop>();
                            for(JsonElement g: result.getAsJsonArray("results")){
                                String place_id = g.getAsJsonObject().get("place_id").toString();
                                String name = g.getAsJsonObject().get("name").toString();
                                String vicinity = g.getAsJsonObject().get("vicinity").toString();
//                                 String photo_reference = "cannot";//"https://maps.googleapis.com/maps/api/place/photo?photoreference="+photos+"&sensor=false&maxheight=100&maxwidth=100"+apiKey;

                                Log.d("KeySet2: ",g.getAsJsonObject().toString());
//                                for(JsonElement mElement: g.getAsJsonArray()){
//                                    Log.d("Vals", mElement.getAsJsonObject().keySet().toString());
//                                }

                                shopList.add(new Shop(place_id, name.replace("\"", ""), vicinity.replace("\"", "")));
                                shopListNames.add(name);
//                                shopListString.add(name);

                            }
                            Log.d("Shop List Size:", Integer.toString(shopList.size()));

                            rvaAdapter = new CustomAdapter(context, shopList);

                            recyclerView.setAdapter(rvaAdapter);

//                            rvaAdapter.setClickListener(FindShops.this::onItemClick);
                        } catch (Exception ex) {
                            Toast.makeText(FindShops.this, "Error Loading from API, please try again.", Toast.LENGTH_SHORT).show();
                        }


                    }

                });
    }
}

class Shop{
    /* Private inner class that creates the data structure that
        acts as an object type store (which we abstract from the google
        maps api into our simplified version).

        classic getter method that is initialised with all its necessary
        data at the start. at the start.

    */
     private String photos;
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

    public void setPhoto(String photos){this.photos = photos;}

    public String getPhotos(){return this.photos;}


    public String toString(){
        return "Name: "+this.name+"\nAddress: "+this.vicinity;
    }
}

class CustomAdapter extends
    RecyclerView.Adapter<com.example.luvyourleftovers.CustomAdapter.MyViewHolder> {

    private ArrayList<Shop> dataSet;
    DBHelper db;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shopName;
        TextView address;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.shopName = (TextView) itemView.findViewById(R.id.shop_name_single);

            this.address  = (TextView) itemView.findViewById(R.id.address_field);
        }
    }

    public CustomAdapter(Context context, ArrayList<Shop> data) {
        db = new DBHelper(context);
        this.dataSet = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.shop_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        //Set a onClick option to the ViewHolder. When user clicks on shop it gets transported
        //to the google maps activity of that location.
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shop clickedShop = (Shop) dataSet.get(myViewHolder.getAdapterPosition());
                String url = "https://www.google.com/maps/search/?api=1&query="+clickedShop.getVicinity().replace("\"", "")+"&query_place_id="+clickedShop.getPlace_id().replace("\"", "");

                String uri = url;//"http://maps.google.com/maps?saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + destinationLatitude + "," + destinationLongitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                parent.getContext().startActivity(intent);

            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(
        final com.example.luvyourleftovers.CustomAdapter.MyViewHolder holder,
        final int listPosition) {

        TextView textViewName = holder.shopName;
        textViewName.setText(dataSet.get(listPosition).getName());

        TextView address = holder.address;
        address.setText(dataSet.get(listPosition).getVicinity());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
