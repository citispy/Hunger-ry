package co.za.myconcepts.hunger_ry.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.za.myconcepts.hunger_ry.Constants;
import co.za.myconcepts.hunger_ry.R;
import co.za.myconcepts.hunger_ry.RequestPackage;
import co.za.myconcepts.hunger_ry.connectivity.ConnectionCheckHelper;
import co.za.myconcepts.hunger_ry.connectivity.HttpManager;

public class InsertMeal extends AppCompatActivity{

    private EditText etTitle, etDescription;
    private Button btnSubmit;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_meal);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }

    public void submit(View view) {
        if (ConnectionCheckHelper.isOnline(this)) {
            sendData(Constants.URL_PREFIX + "insert_meal.php");
        } else {
            Toast.makeText(InsertMeal.this, getString(R.string.network_not_available), Toast.LENGTH_LONG).show();
        }
    }

    private void sendData(String url) {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String location = "Kensington";

        RequestPackage p = new RequestPackage();
        p.setMethod(Constants.POST_METHOD);
        p.setUri(url);
        p.setParam(Constants.KEY_TITLE, title);
        p.setParam(Constants.KEY_DESCRIPTION, description);
        p.setParam(Constants.KEY_LOCATION, location);

        SubmitMeal sm = new SubmitMeal();
        sm.execute(p);
    }

    private class SubmitMeal extends AsyncTask<RequestPackage, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(InsertMeal.this);
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

        protected void onPostExecute(String result) {

            pDialog.dismiss();

            if (result != null) {
                Toast.makeText(InsertMeal.this, getString(R.string.meal_successful), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(InsertMeal.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(InsertMeal.this, getString(R.string.meal_unsuccessful), Toast.LENGTH_LONG).show();
            }
        }
    }

}
