package br.com.kdvoce.application.applicationkdvoce;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import br.com.kdvoce.application.applicationkdvoce.Model.Contato;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    protected Double myLatitude;
    protected Double myLongitude;
    protected Boolean syncroning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        syncroning = new Boolean(true);
        Intent intent = getIntent();
        myLatitude = intent.getExtras().getDouble("myLatitude");
        myLongitude = intent.getExtras().getDouble("myLongitude");

        //System.out.println(myLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    //Este é o lugar onde podemos adicionar marcadores ou linhas, adicione ouvintes ou mover a câmera. Neste caso, basta adicionar um marcador perto de África.
    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        Log.i(" =============>>>>> ", "myLasLocation!!");
        map.addMarker(new MarkerOptions().position(new LatLng(myLatitude, myLongitude)).title("Você está aqui!"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude), 14));

       // HttpRequestTask httpRequestTask = new HttpRequestTask(myLatitude, myLongitude);
        //formData = new MultiValueMap<Double, Object>() {
            
        //} //LinkedMultiValueMap<Double, Object>();
        //formData.add(myLatitude, "myLatitude");
        //formData.add(myLongitude, "myLongitude");
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.execute(myLatitude, myLongitude);
        Log.i(" =============>>>>> ", "return HttpResquest");

    }

    private void showResult() {
        if (syncroning == true) {
            // display a notification to the user with the response message
            Toast.makeText(this, "Sincronizado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Erro ao sincronizar", Toast.LENGTH_LONG).show();
        }
    }

    private class HttpRequestTask extends AsyncTask<Double, Void, HttpStatus> {

        private MultiValueMap<String, String> formData;
        private Contato contato;

        @Override
        protected HttpStatus doInBackground(Double... params) {
            Log.i(" =============>>>>> ", "doInBackground");
            try{
                // The URL for making the POST request
                final String url = "http://192.168.15.6:3000/addLocation";

                Log.i("28188*!@*@!&(!*&@", String.valueOf(params[1]));
                //Log.i("amsasam", params[0]);
                formData = new LinkedMultiValueMap<>();
                formData.add("myLatitude", String.valueOf(params[0]));
                formData.add("myLongitude", String.valueOf(params[1]));
                contato = new Contato();
                contato.setLatitude(params[0]);
                contato.setLongitude(params[1]);

                HttpHeaders requestHeaders = new HttpHeaders();

                // Sending multipart/form-data
                //requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                //requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);

                // Create a new RestTemplate instance
                //RestTemplate restTemplate = new RestTemplate(true);

                // Populate the MultiValueMap being serialized and headers in an HttpEntity object to use for the request
                HttpEntity<Contato> requestEntity = new HttpEntity<Contato>(contato, requestHeaders);

                // Create a new RestTemplate instance
                RestTemplate restTemplate = new RestTemplate(true);
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // Make the network request, posting the message, expecting a String in response from the server
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                        String.class);

                // Return the response body to display to the user
                System.out.println(response.getStatusCode());

                return response.getStatusCode();

            } catch (Exception e){
                Log.e("MapsActivity", e.getMessage(), e); //+ " " + response.get);
                syncroning = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(HttpStatus d) {
            showResult();
        }

    }
}
