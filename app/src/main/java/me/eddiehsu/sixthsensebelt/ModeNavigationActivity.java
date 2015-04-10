package me.eddiehsu.sixthsensebelt;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class ModeNavigationActivity extends ActionBarActivity {
    public final static String ADDRESS_ENTRY = "me.eddiehsu.sixthsensebelt.ADDRESS_ENTRY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_navigation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mode_navigation, menu);
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

    /** Called when the user clicks the start tracking button */
    public void startNavigationCycleActivity(View view) {
        Intent intent = new Intent(this, NavigationCycleActivity.class);
        EditText raw_address = (EditText) findViewById(R.id.navigation_address_entry);
        String address = raw_address.getText().toString();
        intent.putExtra(ADDRESS_ENTRY, address);
        startActivity(intent);
    }

}
