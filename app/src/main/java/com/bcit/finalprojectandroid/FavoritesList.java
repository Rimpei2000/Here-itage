package com.bcit.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FavoritesList extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static List<Data.Record> records = null;
    public static recyclerviewadapter recyclerviewadapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(LocationFragment.getHistory());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);
        TextView title = findViewById(R.id.textView_favoritelist_title);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) { //i.e. user is signed in, for clarity.
            String useremail = user.getEmail();
            String[] parts = useremail.split("@"); // String array, each element is text between @ signs

            String titletext = parts[0] + "'s favorites!";
            title.setText(titletext);
        }
        Intent intent = getIntent();
        records = (List<Data.Record>)intent.getSerializableExtra("Records");
        SetUpRecyclerView();
    }

    private void SetUpRecyclerView(){
        RecyclerView itemswitch = findViewById(R.id.recyclerview_favoritelist_display);

        for (Data.Record record: records) {
            System.out.println(record.fields.buildingnamespecifics);
        }
        Data.Record[] recordArray = new Data.Record[records.size()];
//        recyclerviewadapter switchadapter = new recyclerviewadapter(records.toArray(recordArray));
        recyclerviewadapter switchadapter = new recyclerviewadapter(records.toArray(recordArray));
        recyclerviewadapter = switchadapter;
        itemswitch.setAdapter(switchadapter);
        itemswitch.setLayoutManager((new LinearLayoutManager(this)));

    }


    public void toMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}