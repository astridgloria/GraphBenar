package com.example.amiramaulina.retrievedataheartbeat;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RetrieveApp extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;


    int hrstateValue;
    String hrstateValueTimestamp;


    ArrayList<Integer> array2; //array untuk HR
    ArrayList<String> array7; //array untuk hrValueTimestamp



    LineGraphSeries<DataPoint> series ;
    int x=0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_app);
        DatabaseReference userdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        final GraphView graph = (GraphView)findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        graph.addSeries(series);
        Viewport vp = graph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(1);
        vp.setMaxX(5); //yg ditunjukin max berapa
        vp.setYAxisBoundsManual(true);
        vp.setMinY(1);
        vp.setMaxY(200); //yg ditunjukin max berapa
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        //graph.getViewport().setScrollableY(true); // enables vertical scrolling
        //graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        //graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


        DatabaseReference ref = userdatabase.child(user.getUid());


        array2 = new ArrayList<>(); //array untuk hr
        array7 = new ArrayList<>(); //array untuk hrValueTimestamp


        /*graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value, boolean isValueX)
            {
                if (isValueX)
                {
                    return (hrstateValueTimestamp);
                }
                return super.formatLabel(value,isValueX);
            }
        });*/




        //RETRIEVE DATA HR VALUE + DATE
        ref.child("hrstate").child("nilaihrstate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                hrstateValue = dataSnapshot.child("tmpHr").getValue(Integer.class);
                hrstateValueTimestamp = dataSnapshot.child("Time").getValue(String.class);

                Log.i("timestamp value", "timestamp value " + hrstateValueTimestamp);
                Log.i("hr value", "hr value " + hrstateValue);
                array2.add(hrstateValue);
                array7.add(hrstateValueTimestamp);
                    x = x+1;

                DataPoint point = new DataPoint(x, hrstateValue);
                series.appendData(point, false, 1000);

                graph.getGridLabelRenderer().setHorizontalAxisTitle(hrstateValueTimestamp);



            }







            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


}

