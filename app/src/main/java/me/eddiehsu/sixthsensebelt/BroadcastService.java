package me.eddiehsu.sixthsensebelt;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class BroadcastService extends IntentService implements
        ConnectionCallbacks, OnConnectionFailedListener {

    // Define constants
    protected static final String TAG = "BroadcastService";
    public final static String ACTION_GET_LOCATION = "me.eddiehsu.sixthsensebelt.action-get-location";

    // Class variables
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    public BroadcastService() {
        super("BroadcastService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_LOCATION.equals(action)) {
                // TODO: Instead of connecting and disconnecting every time, maintain the connection.
                // Start the google api connection to update mLastLocation
                buildGoogleApiClient();
                mGoogleApiClient.connect();

                // Done, end the connection
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            // Post the location to the web.

            // Instantiate the RequestQueue.
            RequestQueue request_queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            final String URL = "http://50.148.113.36:8001/api/v1/locationlog/?format=json";
            // Post params to be sent to the server
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("latitude", String.valueOf(mLastLocation.getLatitude()));
            params.put("longitude", String.valueOf(mLastLocation.getLongitude()));
            params.put("device_id", "/api/v1/device/1/");

            JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            request_queue.add(req);

            Log.i(TAG, "Connected, last location is: " + String.valueOf(mLastLocation.getLatitude()) + ", " +
                    String.valueOf(mLastLocation.getLongitude()));
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

}
