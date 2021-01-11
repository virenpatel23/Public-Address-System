package com.example.viren.publicadresssystem;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;



public class StationActivity extends AppCompatActivity {

    private ListView list;
    ArrayAdapter<String> adapter;
    EditText datasearch;



    private static String  Key = "com.example.viren.publicadresssystem.station_key";

    FirebaseFirestore db;
    DocumentReference documentReference;

    private ArrayList<String> mStationName= new ArrayList<String>();
    private ArrayList<String> mKeys = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

.


        db = FirebaseFirestore.getInstance();
        documentReference=db.collection("stations").document();

        final ListView StationName = (ListView)findViewById(R.id.searchlist);




        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(StationActivity.this, android.R.layout.simple_list_item_1, mStationName) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView

                View view=super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv=(TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(24);
                // Generate ListView Item using TextView
                return view;
            }

        };

       // StationName.setAdapter(arrayAdapter);

        Log.d("Station name", String.valueOf(mStationName));


        db.collection("stations").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED) {

                        String Station_name = dc.getDocument().getString("name");

                        String Station_key = dc.getDocument().getId();
    //                    Log.d("IDDDD",Station_key);


                        mKeys.add(Station_key);
//                        StationData getData = dc.getDocument().toObject(StationData.class);
                        mStationName.add(Station_name);

                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

       // String
           //     month[]={"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
        list=(ListView)findViewById(R.id.searchlist);
        datasearch=(EditText)findViewById(R.id.searched);
        adapter = new ArrayAdapter<String>(StationActivity.this, R.layout.search_layout, R.id.station_name, mStationName);
        list.setAdapter(adapter);

        datasearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                StationActivity.this.adapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {

            }
            public String getItem(int position){
                return adapter.getItem(position);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   String key = mKeys.get(position);

                Intent i = new Intent(StationActivity.this,StationDetail.class);

             //   Log.d("Station NAme",);

                i.putExtra("Station_Key",key);

                startActivity(i);

                 Log.d("Item CLickd", key);



            }
        });



    }
}
