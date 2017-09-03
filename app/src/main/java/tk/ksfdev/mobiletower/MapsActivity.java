package tk.ksfdev.mobiletower;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import tk.ksfdev.mobiletower.data.DataUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CircleProgressView mCircleView;

    private AsyncTask<String[], Void, String[]> mAsync;

    private LatLng initLocation;
    private LatLng location;
    private double radius;
    private String[] givenData;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get data from intent
        Intent intent = getIntent();
        if(intent != null){

            givenData = new String[]{
                    intent.getStringExtra(DataUtils.TOWER_MCC),
                    intent.getStringExtra(DataUtils.TOWER_MNC),
                    intent.getStringExtra(DataUtils.TOWER_LAC),
                    intent.getStringExtra(DataUtils.TOWER_CID)
            };

        } else {
            Toast.makeText(this, "Wrong data passed", Toast.LENGTH_SHORT).show();
            finish();
        }

        mCircleView = findViewById(R.id.circleView);
        mCircleView.setText("Working");
        mCircleView.setTextMode(TextMode.TEXT);
        mCircleView.setShowTextWhileSpinning(true);
        mCircleView.spin();

    }


    @Override
    protected void onStart() {
        super.onStart();

        //get data from API
        mAsync = new AsyncTask<String[], Void, String[]>() {
            @Override
            protected String[] doInBackground(String[]... strings) {
                if(MyUtils.isNetworkActive()){
                    try{

                        String request = "http://api.mylnikov.org/geolocation/cell?v=1.1&data=open" + "&mcc=" + strings[0][0] + "&mnc=" + strings[0][1] + "&lac=" + strings[0][2] + "&cellid=" + strings[0][3];
                        Log.d("TAG+++", "request: " + request);

                        URL url = new URL(request);

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        StringBuilder sb = new StringBuilder();

                        String ln;
                        while((ln = reader.readLine()) != null){
                            sb.append(ln);
                        }


                        JSONObject obj = new JSONObject(sb.toString());
                        Log.d("TAG+++", "result: " + obj.getInt("result"));

                        if(obj.getInt("result") == 200){
                            String[] result = {
                                    ""+ 200,
                                    obj.getJSONObject("data").getString("lat"),
                                    obj.getJSONObject("data").getString("lon"),
                                    obj.getJSONObject("data").getString("range")
                            };
                            return result;
                        }

                    } catch (Exception e){
                        Log.d("TAG+++", "Exception in mAsync");
                        e.printStackTrace();
                    }
                } else {
                    return new String[] {"404"};
                }
                return new String[] {"400"};
            }

            @Override
            protected void onPostExecute(String[] strings) {
                if(strings[0].equals("200")){
                    //ok, display stuff
                    mCircleView.stopSpinning();
                    mCircleView.setVisibility(View.GONE);

                    location = new LatLng(Double.valueOf(strings[1]), Double.valueOf(strings[2]));
                    radius = Double.valueOf(strings[3]);

                    mMap.addMarker(new MarkerOptions().position(location).title("Mobile Tower"));
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(location)
                            .radius(radius)
                            .strokeWidth(1.5f)
                            .strokeColor(Color.LTGRAY)
                            .fillColor(Color.parseColor("#24ff0000"))
                    );


                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));

                } else if(strings[0].equals("404")){

                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle("No internet connection")
                            .setMessage("We use online API to determine location of mobile tower because current database is 1.8GB large")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .show();

                } else {
                    //error
                    Log.d("TAG+++", "Error, strings[0] = " + strings[0]);
                }
            }
        };


        mAsync.execute(givenData);
    }

    /**
     * When camera is ready do stuff
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initLocation = new LatLng(55.774971, 37.6138063);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(initLocation));

    }




    public void onClickMap(View v){
        int id= v.getId();

        switch (id){
            case R.id.button_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.button_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.button_sattelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }

    }
}
