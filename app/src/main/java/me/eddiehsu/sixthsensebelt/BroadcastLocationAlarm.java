package me.eddiehsu.sixthsensebelt;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastLocationAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        Toast.makeText(context, "Posting", Toast.LENGTH_SHORT).show();

        // Send an intent to BroadcastService to post location to web.
        Intent mServiceIntent = new Intent(context, BroadcastService.class);
        mServiceIntent.setAction(BroadcastService.ACTION_GET_LOCATION);
        context.startService(mServiceIntent);

    }
}
