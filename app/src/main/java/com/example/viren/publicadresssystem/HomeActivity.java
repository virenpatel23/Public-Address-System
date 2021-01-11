package com.example.viren.publicadresssystem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viren.publicadresssystem.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TextToSpeech.OnInitListener  {


    ListView station_massage;
    FirebaseFirestore db;
    DocumentReference documentReference;
    private ArrayList<String> mStationMassage= new ArrayList<String>();

    private ArrayList<String> mKeys = new ArrayList<String>();
    TextToSpeech textToSpeech;

    String id1;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView sn = (TextView) findViewById(R.id.udaypur_data);
        sn.setText("UDZ");

        station_massage = (ListView) findViewById(R.id.udaypur_massage);
        String station_key = "UDZ";

        db = FirebaseFirestore.getInstance();
        documentReference=db.collection("stations").document();

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, mStationMassage) {
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

                textToSpeech = new TextToSpeech(HomeActivity.this, HomeActivity.this);
                TextToSpeechFunction() ;

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // create class object
        gps = new GPSTracker(HomeActivity.this);


        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       // mDatabase= FirebaseDatabase.getInstance().getReference("Id");
        String res = getIntent().getStringExtra("ID");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

         if (id == R.id.station) {
             Intent intent= new Intent(HomeActivity.this , StationActivity.class);
             startActivity(intent);

        } else if (id == R.id.Sche) {

        } else if (id == R.id.Help) {
            Intent intent= new Intent(HomeActivity.this , HelpActivity.class);
            startActivity(intent);

        } else if (id == R.id.set) {
            Intent intent= new Intent(HomeActivity.this , SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.Logout) {


             FirebaseAuth fAuth = FirebaseAuth.getInstance();
             fAuth.signOut();
             finish();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onInit(int Text2SpeechCurrentStatus) {

        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {

            textToSpeech.setLanguage(Locale.US);
            TextToSpeechFunction();
        }
    }


    public void TextToSpeechFunction()
    {

        String textholder = id1;

        textToSpeech.speak(textholder, TextToSpeech.QUEUE_FLUSH, null);

        Toast.makeText(HomeActivity.this , textholder, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onDestroy() {

        textToSpeech.shutdown();

        super.onDestroy();
    }

}
