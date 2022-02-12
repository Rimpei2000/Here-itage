package com.bcit.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LocationFragment extends Fragment {

    public Marker lastPressedMarker = null;
    FirebaseFirestore db;
    public GoogleMap googleMapReference = null;
    public List<Marker> markers = new ArrayList<>();
    public static List<Data.Record> locationsAdded = new ArrayList<>();
    public static ArrayList<String> history;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker in Vancouver, Canada.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            history = new ArrayList<String>(7);
            googleMapReference = googleMap;
            LatLng vancouver = new LatLng( 49.246292,-123.116226);
//            googleMap.addMarker(new MarkerOptions().position(vancouver).title("Marker in Vancouver"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(vancouver));
            googleMap.setMinZoomPreference(10.75f);
            createHeritageSites(googleMap);
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    if (lastPressedMarker != null){
                        lastPressedMarker.setIcon(BitmapDescriptorFactory.defaultMarker());
                    }
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13f));
                    lastPressedMarker = marker;
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    ChangeTheBottomSlider((Data.Record) marker.getTag());
                    //System.out.println(((Data.Record) marker.getTag()).fields.buildingnamespecifics);
                    String input = (((Data.Record) marker.getTag()).fields.buildingnamespecifics);
                    //System.out.println(input);
                    addHistory(input);
                    return true;
                }
            });
        }
    };

    public void addHistory(String input){
        if (!(history.contains(input))) {
            history.add(input);
        }
        int diff = history.size() - 6;
        if (diff > 0){
            for(int i = 0; i < diff; i++){
                history.remove(0);
            }
        }
    }

    public static List<String> getHistory(){
        return history;
    }

    public static List<Data.Record> getAddedLoc() { return locationsAdded;}

    public void ChangeTheBottomSlider(Data.Record record){
        CardView cardView = getView().findViewById(R.id.card_fragment_location);
        cardView.setVisibility(View.VISIBLE);
        TextView textView_title = getView().findViewById(R.id.Title_card);
        textView_title.setText(record.fields.buildingnamespecifics);
        boolean picked = false;
        ImageButton imageButton = (ImageButton) getView().findViewById(R.id.imageButton_fave_fragement);
        for (Data.Record currentFave: locationsAdded) {
            if (currentFave.fields.buildingnamespecifics.equals(record.fields.buildingnamespecifics)){
                imageButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                picked= true;
            }
        }
        if (!picked){
            imageButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
        }
        TextView textView_desc = getView().findViewById(R.id.Description_Card);

        String text = "" + record.fields.streetnumber + "  " + record.fields.streetname + "\n"
                + "Current Additional Information: " + record.fields.additionallocationinformation;

        textView_desc.setText(text);


        System.out.println(record.fields.buildingnamespecifics);
        System.out.println(record);
        Button button = getView().findViewById(R.id.more);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ItemActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("Record", record);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location2, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        // get the arraylist from the database right here...
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onStop() {
        // call database to update arraylist here...
        super.onStop();
    }

    public void createHeritageSites(GoogleMap googleMap){
        HttpHandler<String> mainModel= new HttpHandler<>("https://opendata.vancouver.ca/api/records/1.0/search/?dataset=heritage-sites&q=&rows=100&facet=category&facet=evaluationgroup&facet=municipaldesignationm&facet=provincialdesignationp&facet=heritagerevitalizationagreementh&facet=interiordesignationi&facet=landscapedesignationl&facet=heritageconservationareaca&facet=heritageconservationcovenanthc&facet=federaldesignationf&facet=localarea");
        Future<String> f = mainModel.CreateHttpRequestForJson("https://opendata.vancouver.ca/api/records/1.0/search/?dataset=heritage-sites&q=&rows=100&facet=category&facet=evaluationgroup&facet=municipaldesignationm&facet=provincialdesignationp&facet=heritagerevitalizationagreementh&facet=interiordesignationi&facet=landscapedesignationl&facet=heritageconservationareaca&facet=heritageconservationcovenanthc&facet=federaldesignationf&facet=localarea");

        String str = null;
        try {
            str = f.get();
        } catch(ExecutionException | InterruptedException e){
            e.printStackTrace();
        }

        Data.Root roots = mainModel.ParseJson(str);
        MainActivity.root = roots;
        for (Data.Record record: roots.records) {
            if ( record.fields.buildingnamespecifics == null|| record.fields.buildingnamespecifics.equals("N/A")){
                continue;
            }
            LatLng recordLatLng = new LatLng(record.geometry.coordinates.get(1), record.geometry.coordinates.get(0));
            Marker recordMarker = googleMap.addMarker(new MarkerOptions().position(recordLatLng));
            markers.add(recordMarker);
            recordMarker.setTag(record);
            Map<String, Object> data = new HashMap<>();
            data.put("Marker", recordMarker);
            db.collection("SearchMarkers")
                    .document(record.fields.buildingnamespecifics)
                    .set(data);
        }

//
    }


}