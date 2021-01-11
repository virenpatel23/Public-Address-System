package com.example.viren.publicadresssystem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class StationDetail extends  AppCompatActivity implements TextToSpeech.OnInitListener{

    private Button back;
    ListView station_massage;
    FirebaseFirestore db;
    DocumentReference documentReference;
    private ArrayList<String> mStationMassage= new ArrayList<String>();

    private ArrayList<String> mCheptername = new ArrayList<String>();
    private ArrayList<String> mKeys = new ArrayList<String>();
    TextToSpeech textToSpeech;
    String id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);

        TextView sn = (TextView) findViewById(R.id.stationn_name);
        Intent intent = getIntent();
        sn.setText(intent.getStringExtra("Station_Key"));

        station_massage = (ListView) findViewById(R.id.station_massage);
        String station_key = intent.getStringExtra("Station_Key");

        db = FirebaseFirestore.getInstance();
        documentReference=db.collection("stations").document();
        back = (Button) findViewById(R.id.eback);

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(StationDetail.this, android.R.layout.simple_list_item_1, mStationMassage) {
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

         station_massage.setAdapter(arrayAdapter);

        db.collection("stations").document(station_key).collection("messages")


                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED) {

                        String Station_name = dc.getDocument().getString("msg");

                        mStationMassage.add(Station_name);

                        String Station_key = dc.getDocument().getId();

                        mKeys.add(Station_key);

                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        station_massage.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            FragmentManager fm = getFragmentManager();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = mKeys.get(position);

                id1 = mStationMassage.get(position);

                FragmentTransaction transection=getFragmentManager().beginTransaction();

                textToSpeech = new TextToSpeech(StationDetail.this, StationDetail.this);
                TextToSpeechFunction() ;

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(StationDetail.this,HomeActivity.class);
                startActivity(i);
            }
        });


    }
    @Override
    public void onInit(int Text2SpeechCurrentStatus) {

        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {

            textToSpeech.setLanguage(Locale.US);
            textToSpeech.setLanguage(new Locale("hi"));

            TextToSpeechFunction();
        }
    }


    public void TextToSpeechFunction()
    {

        String textholder = id1;

        textToSpeech.speak(textholder, TextToSpeech.QUEUE_FLUSH, null);

        Toast.makeText(StationDetail.this , textholder, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onDestroy() {

        textToSpeech.shutdown();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}