package co.za.myconcepts.hunger_ry.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import co.za.myconcepts.hunger_ry.Constants;
import co.za.myconcepts.hunger_ry.ImageContract;
import co.za.myconcepts.hunger_ry.ImageEntriesDBHelper;
import co.za.myconcepts.hunger_ry.MealAdapter;
import co.za.myconcepts.hunger_ry.R;
import co.za.myconcepts.hunger_ry.RequestPackage;
import co.za.myconcepts.hunger_ry.SharedPreferencesHelper;
import co.za.myconcepts.hunger_ry.connectivity.ConnectionCheckHelper;
import co.za.myconcepts.hunger_ry.connectivity.HttpManager;
import co.za.myconcepts.hunger_ry.model.Meal;
import co.za.myconcepts.hunger_ry.parsers.MealJSONParser;

public class MainActivity extends AppCompatActivity {

    private String location;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm = new LinearLayoutManager(this);
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvLocation = (TextView) findViewById(R.id.tvLocation);
        location = SharedPreferencesHelper.getStringPreference(Constants.KEY_LOCATION, getApplicationContext());

        //Check if values has been inserted to SQLite DB already
        boolean dbValuesEntered = SharedPreferencesHelper.getBooleanPreference(Constants.KEY_DB_VALUES_ENTERED, getApplicationContext());
        if(!dbValuesEntered) {
            insertValueToSQLiteDB("images/1.jpg");
            insertValueToSQLiteDB("images/2.jpg");
            insertValueToSQLiteDB("images/3.jpg");
            insertValueToSQLiteDB("images/4.jpg");
            insertValueToSQLiteDB("images/5.jpg");
            insertValueToSQLiteDB("images/6.jpg");
            insertValueToSQLiteDB("images/7.jpg");
        }

        //Setting up recyclerview objects
        recyclerView = (RecyclerView) findViewById(R.id.rvMeal);
        recyclerView.setHasFixedSize(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        //If location has been set then continue, if not open Set Location activity
        if (!location.equals("")) {
            tvLocation.setText(location);
            //Checking for network connection and request data if available
            if (ConnectionCheckHelper.isOnline(this)) {
                requestData(Constants.URL_PREFIX + "display_meals.php");
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.network_not_available), Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent = new Intent(MainActivity.this, SetLocation.class);
            startActivity(intent);
        }
    }

    private void insertValueToSQLiteDB(String imagePath) {
        ContentValues values = new ContentValues();
        values.put(ImageContract.ImageEntry.COLUMN_NAME_IMAGE_URL, Constants.URL_PREFIX + imagePath);

        ImageEntriesDBHelper entriesDBHelper = new ImageEntriesDBHelper(getApplicationContext());
        entriesDBHelper.insertEntries(values);
        SharedPreferencesHelper.setBooleanChoice(Constants.KEY_DB_VALUES_ENTERED, true);
    }

    private void requestData(String url) {
        RequestPackage p = new RequestPackage();
        p.setMethod(Constants.GET_METHOD);
        p.setUri(url);
        p.setParam(Constants.KEY_LOCATION, location.replaceAll(" ", "%20"));

        Downloader downloader = new Downloader();
        downloader.execute(p);
    }

    public void addMeal(View view) {
        Intent intent = new Intent(MainActivity.this, InsertMeal.class);
        startActivity(intent);
    }

    public void editLocation(View view) {
        Intent intent = new Intent(MainActivity.this, SetLocation.class);
        startActivity(intent);
    }

    private class Downloader extends AsyncTask<RequestPackage, String, String>{

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.connecting));
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            List <Meal> mealList = MealJSONParser.parseFeed(result);
            if(mealList != null) {
                ImageEntriesDBHelper entriesDBHelper = new ImageEntriesDBHelper(getApplicationContext());
                List<String> imageURLS = entriesDBHelper.readEntries();
                MealAdapter adapter = new MealAdapter(mealList, imageURLS);
                recyclerView.setAdapter(adapter);
            } else{
                Toast.makeText(MainActivity.this, getString(R.string.no_meals), Toast.LENGTH_LONG).show();
            }
        }
    }
}
