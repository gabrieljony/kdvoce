package br.com.kdvoce.application.applicationkdvoce;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import br.com.kdvoce.application.applicationkdvoce.Model.Contato;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "MapsActivity";
    //Fornece o ponto de entrada aos serviços do Google Play.
   protected GoogleApiClient mGoogleApiClient;
    //Representa uma localização geográfica.
   protected Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Constrói uma GoogleApiClient. Usa o método ad Api () para solicitar a API Location Services.

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }*/

    /*@Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }*/

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("#=============>>>> ", "connected!");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.i(" =============>>>>> ", String.valueOf(mLastLocation.getLatitude()));
            myLocationaMap(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            Toast.makeText(this, "Erro ", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void sendLocation(Double myLatitude, Double myLongitude){

        Intent intent = new Intent(this, MapsActivity.class);
        Bundle extras = new Bundle();
        extras.putDouble("myLatitude", myLatitude );
        extras.putDouble("myLongitude", myLongitude );
        intent.putExtras(extras);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
            if (resultCode == RESULT_OK)
                Log.d("Faça alguma coisa", "faça!");
    }

    public void myLocationaMap(Double myLatitude, Double myLongitude){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PLAYGROUND-2", "Permission is not granted, requesting");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        } else {
            Log.d("PLAYGROUND-3", "Permission is granted");
            sendLocation(myLatitude, myLongitude);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // NAVEGACAO
        int id = item.getItemId();

        if (id == R.id.nav_contact) {
            // Lidar com a ação da câmera
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("PLAYGROUND-2", "Permission is not granted, requesting");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 123);
                //btnSendSms.setEnabled(false);
            } else {
                Log.d("PLAYGROUND-3", "Permission is granted");
                Intent intentContato = new Intent(this, ContatosActivity.class);
                startActivity(intentContato);
            }

        } else if (id == R.id.nav_local) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        } else if (id == R.id.nav_favorite) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class MyHttpRequest extends AsyncTask<String, Void, Contato> {

        @Override
        protected Contato doInBackground(String... params) {
            try {
                final String url = "http://192.168.15.6:3000/listaPermissoes";

                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                Contato contato = new Contato();
                contato = restTemplate.getForObject(url, Contato.class);

                System.out.println(contato);
                Log.d("Produto Nome:::::::::: ", contato.getId_telefone());

                return contato;
            } catch (Exception e) {
                Log.d(" Parametro ", e.getMessage());
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }
    }
}