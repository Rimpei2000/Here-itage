package com.bcit.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static Data.Root root;
    private FirebaseAuth mAuth;
    Button accountbutton;
    FirebaseFirestore db;
    public static LocationFragment  locationFragment;
    List<Marker> markers = null;
    HashMap<String, Object> currentMarkerDetails= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        EditText editText = findViewById(R.id.editText_main_searchbar);
        editText.clearFocus();
        FirebaseUser user = mAuth.getCurrentUser();
        accountbutton = (Button) findViewById(R.id.button_main_accountoptions);

        if (user != null) { //i.e. user is signed in, for clarity.
            String useremail = user.getEmail();
            String[] parts = useremail.split("@"); // String array, each element is text between @ signs
            accountbutton.setText(parts[0]);

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if(!hasFocus){
                        //this if condition is true when edittext lost focus...
                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(MainActivity.this, editText);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.searched_item, popup.getMenu());
                        List<String> hist = LocationFragment.getHistory();
                        String[] localhist = new String[hist.size()];

                        for (int i = 0; i < hist.size(); i++){
                            localhist[i] = hist.get(i);
                        }

                        for (int i = 0; i < localhist.length; i++){
                            if(localhist[i] != null){
                                popup.getMenu().getItem(i).setTitle(localhist[i]);
                            }
                        }

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                historySearch(item.getTitle().toString());
                                editText.clearFocus();
                                return true;
                            }
                        });
                        popup.show();//showing popup menu
                    }
                }
            });

            accountbutton.setOnClickListener(v -> {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, accountbutton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String selected = (String) item.getTitle();
                        switch(selected){
                            case "Favorites":
                                Intent favoriteintent = new Intent(getApplicationContext(), FavoritesList.class);
                                //This following line tries to read the locationsadded field, which is sometimes empty/null.
                                List<Data.Record> locadd = LocationFragment.getAddedLoc();
                                System.out.println(LocationFragment.getAddedLoc());
                                if(locadd.size()>0) {
                                    favoriteintent.putExtra("Records", (Serializable) locationFragment.locationsAdded);
                                    startActivity(favoriteintent);
                                } else {
                                    System.out.println("Locadd IS null D:!");
                                }
                                break;
                            case "Log Out":
                                mAuth.getInstance().signOut();
                                finish();
                                startActivity(getIntent());
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            });//closing the setOnClickListener method**/
        } else {
            accountbutton.setText(R.string.main_signindefaulttext);
            accountbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), LoginPage.class);
                    startActivity(intent);
                }
            });
        }
    }


    public void Search(View view){
        EditText editText = findViewById(R.id.editText_main_searchbar);
        editText.clearFocus();
        Editable text = editText.getText();
        DocumentReference docRef = db.collection("SearchMarkers").document(text.toString());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("Marker") != null){
                    currentMarkerDetails =  (HashMap<String, Object>) documentSnapshot.get("Marker");
                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    locationFragment = (LocationFragment) fragments.get(0);
                    markers = locationFragment.markers;
                    SupportMapFragment mapFragment = (SupportMapFragment) locationFragment.getChildFragmentManager().findFragmentById(R.id.map2);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(callback);
                    }
                }
            }
        });
    }

    public void historySearch(String searchtext){
        EditText editText = findViewById(R.id.editText_main_searchbar);
        editText.clearFocus();
        DocumentReference docRef = db.collection("SearchMarkers").document(searchtext.toString());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentMarkerDetails =  (HashMap<String, Object>) documentSnapshot.get("Marker");
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                locationFragment = (LocationFragment) fragments.get(0);
                markers = locationFragment.markers;
                SupportMapFragment mapFragment = (SupportMapFragment) locationFragment.getChildFragmentManager().findFragmentById(R.id.map2);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callback);
                }
            }
        });
    }

    public void faveButtonPress(View view){
        FirebaseUser user = mAuth.getCurrentUser();
        EditText editText = findViewById(R.id.editText_main_searchbar);
        editText.clearFocus();
        if (user != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            locationFragment = (LocationFragment) fragments.get(0);
            TextView textView = locationFragment.getView().findViewById(R.id.Title_card);
            boolean deleted = false;
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButton_fave_fragement);
            for (Data.Record record : locationFragment.locationsAdded) {
                if (record.fields.buildingnamespecifics == textView.getText()) {
                    imageButton.setImageDrawable(getBaseContext().getDrawable(android.R.drawable.btn_star_big_off));
                    locationFragment.locationsAdded.remove(record);
                    deleted = true;
                    break;
                }
            }
            if (!deleted) {
                imageButton.setImageDrawable(getBaseContext().getDrawable(android.R.drawable.btn_star_big_on));
                if (locationFragment.locationsAdded.size() == 1){
                    if(locationFragment.locationsAdded.get(0).fields.buildingnamespecifics == "No Favourites Found"){
                        locationFragment.locationsAdded.remove(0);
                    }
                }
                locationFragment.locationsAdded.add((Data.Record) locationFragment.lastPressedMarker.getTag());
            }
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            EditText editText = findViewById(R.id.editText_main_searchbar);
            editText.clearFocus();
            HashMap<String, Object> positions = (HashMap<String, Object>) currentMarkerDetails.get("position");
            Marker markerReference = null;
            for (Marker marker: markers) {
                if (marker.getPosition().latitude == (double) positions.get("latitude") && marker.getPosition().longitude == (double) positions.get("longitude")){
                    markerReference = marker;
                    break;
                }
            }
            if (markerReference != null){
                if (locationFragment.lastPressedMarker != null){
                    locationFragment.lastPressedMarker.setIcon(BitmapDescriptorFactory.defaultMarker());
                }
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerReference.getPosition(), 13f));
                locationFragment.lastPressedMarker = markerReference;
                markerReference.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                locationFragment.ChangeTheBottomSlider((Data.Record) markerReference.getTag());
            }
        }
    };


}