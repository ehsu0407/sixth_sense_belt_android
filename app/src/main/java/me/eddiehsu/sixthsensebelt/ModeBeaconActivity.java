package me.eddiehsu.sixthsensebelt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ModeBeaconActivity extends ActionBarActivity {

    // Define keys
    protected final static String ACTION_KEY = "me.eddiehsu.sixthsensebelt.action_key";

    // Class variables
    protected static boolean mBroadcastingLocation;
    private PendingIntent pendingIntent;

    // UI Variables
    protected Button mStartButton;
    protected Button mStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_beacon);

        // Initialize class variables
        mBroadcastingLocation = false;

        // UI Variables
        mStartButton = (Button) findViewById(R.id.beacon_button_start);
        mStopButton = (Button) findViewById(R.id.beacon_button_stop);

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(ModeBeaconActivity.this, BroadcastLocationAlarm.class);
        pendingIntent = PendingIntent.getBroadcast(ModeBeaconActivity.this, 0, alarmIntent, 0);

    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startBroadcastButtonHandler(View view) {
        if (!mBroadcastingLocation) {
            mBroadcastingLocation = true;
            setButtonsEnabledState();
            startBroadcastLocation();
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopBroadcastButtonHandler(View view) {
        if (mBroadcastingLocation) {
            mBroadcastingLocation = false;
            setButtonsEnabledState();
            stopBroadcastLocation();
        }
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private void setButtonsEnabledState() {
        if (mBroadcastingLocation) {
            mStartButton.setEnabled(false);
            mStopButton.setEnabled(true);
        } else {
            mStartButton.setEnabled(true);
            mStopButton.setEnabled(false);
        }
    }

    /**
     * Starts broadcasting device location
     */
    protected void startBroadcastLocation() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 5000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    /**
     * Stops broadcasting device location.
     */
    protected void stopBroadcastLocation() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mode_beacon, menu);
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
