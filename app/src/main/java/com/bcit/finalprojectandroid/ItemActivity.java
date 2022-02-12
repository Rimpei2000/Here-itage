package com.bcit.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class ItemActivity extends AppCompatActivity {
    private TextView buildingName;
    private TextView address;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        buildingName = findViewById(R.id.textView_furtherinfo_buildingname);
        address = findViewById(R.id.textView_furtherinfo_address);
        description = findViewById(R.id.textView_furtherinfo_description);

        Data.Record data = (Data.Record) getIntent().getSerializableExtra("Record");
        System.out.println(data.fields.buildingnamespecifics);
        buildingName.setText(data.fields.buildingnamespecifics);
        String addressStr = data.fields.streetnumber + " " + data.fields.streetname + " " +
                data.fields.localarea;
        address.setText(addressStr);
        String desc = "This is a ";
        desc += data.fields.category.toLowerCase();
        desc += ". It is currently ";
        desc += data.fields.status.toLowerCase();
        desc += ". This site is ";
        if (data.fields.municipaldesignationm.equals("No")) {
            desc += "not ";
        }
        desc += "protected by a legal heritage designation by the City of Vancouver. ";
        desc += "This site is ";
        if (data.fields.heritagerevitalizationagreementh.equals("No")) {
            desc += "not ";
        }
        desc += "the subject of a Heritage Revitalization Agreement.";
        description.setText(desc);
    }
}