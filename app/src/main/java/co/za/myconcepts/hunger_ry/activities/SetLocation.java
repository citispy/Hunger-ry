package co.za.myconcepts.hunger_ry.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import co.za.myconcepts.hunger_ry.Constants;
import co.za.myconcepts.hunger_ry.R;
import co.za.myconcepts.hunger_ry.SharedPreferencesHelper;

public class SetLocation extends AppCompatActivity {
    private EditText etLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        etLocation = (EditText) findViewById(R.id.etLocation);
    }

    public void saveLocation(View view) {
        String location = etLocation.getText().toString();
        //Checks if last character in location is a space, if so, removes it
        if(location.charAt(location.length()-1) == ' ') location = location.substring(0, location.length()-1);

        SharedPreferencesHelper.setStringChoice(Constants.KEY_LOCATION, location);
        Toast.makeText(this, getString(R.string.location_set), Toast.LENGTH_LONG).show();

        //Opens MainActivity after setting location
        Intent intent = new Intent(SetLocation.this, MainActivity.class);
        startActivity(intent);
    }
}
