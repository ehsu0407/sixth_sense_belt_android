package me.eddiehsu.sixthsensebelt;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class NavigationVerifyAddressActivity extends ActionBarActivity {
    private static final String TAG = "verify-address";

    public static final String NAVI_LAT = "me.eddiehsu.sixthsensebelt.navi-lat";
    public static final String NAVI_LONG = "me.eddiehsu.sixthsensebelt.navi-long";

    protected List<Address> address_list;
    protected Button[] btn_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_verify_address);

        // Get UI elements
        ViewGroup layout = (ViewGroup) findViewById(R.id.verifyAddressId);

        // Get and verify the address. Show a list of possible correct addresses to navigate to.
        Intent intent = getIntent();
        address_list = getAddressList(intent);

        // For each address in address_list, create a button and put it into the activity
        if(address_list == null || address_list.size() == 0){
            TextView error_message = new TextView(this);
            error_message.setText("No matching addresses found, go back and check your address.");
            error_message.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            layout.addView(error_message);

        } else {
            btn_list = new Button[address_list.size()];
            for (int i = 0; i < address_list.size(); i++) {
                Address address = address_list.get(i);
                ArrayList<String> addressFragments = new ArrayList<>();

                for(int j = 0; j < address.getMaxAddressLineIndex(); j++) {
                    addressFragments.add(address.getAddressLine(j));
                }
                Log.i(TAG, getString(R.string.address_found));
                String verified_address = TextUtils.join(", ", addressFragments);

                btn_list[i] = new Button(this);
                btn_list[i].setText(verified_address);
                btn_list[i].setOnClickListener(handleOnClick(address));
                layout.addView(btn_list[i]);
            }
        }
    }

    private View.OnClickListener handleOnClick(final Address address) {

        return new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NavigationVerifyAddressActivity.this, NavigationCycleActivity.class);
                Log.i(TAG, String.valueOf(address.getLatitude()));
                Log.i(TAG, String.valueOf(address.getLongitude()));
                intent.putExtra(NAVI_LAT, address.getLatitude());
                intent.putExtra(NAVI_LONG, address.getLongitude());
                startActivity(intent);
            }
        };
    }

    private List<Address> getAddressList(Intent intent) {
        // Given a string address input, returns a list of possible addresses
        String errorMessage = "";

        String address_string = intent.getStringExtra(ModeNavigationActivity.ADDRESS_ENTRY);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocationName(address_string, 5);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Address = " + address_string, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
        }
        return addresses;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation_verify_address, menu);
        return true;
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

}
