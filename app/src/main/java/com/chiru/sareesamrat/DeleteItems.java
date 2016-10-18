package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteItems extends AppCompatActivity {

    EditText editText;
    Button button;
    ProgressDialog pDialog;
    private static String TAG = DeleteItems.class.getSimpleName();
    String username,itemtitle;
    Boolean deletestatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_items);

        setupsubViews();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemtitle = editText.getText().toString();
                new DeleteselectedItem().execute();

            }
        });

    }
    private void setupsubViews(){

        editText = (EditText) findViewById(R.id.edittexttodeleteitemfinal);
        button = (Button) findViewById(R.id.finaldeletebutton);

        username = getIntent().getExtras().getString("Username");


    }

    private class DeleteselectedItem extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DeleteItems.this);
            pDialog.setMessage("Deleting Item...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpRequestHandler pwordhandler = new HttpRequestHandler();

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100")
                    .appendPath("items")
                    .appendPath("delete")
                    .appendPath(username)
                    .appendPath(itemtitle);
            ;

            String url = builder.build().toString();


            String jsonStr = pwordhandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    deletestatus= jsonObj.getBoolean("success");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (deletestatus) {
                Toast.makeText(getApplicationContext(),"Item Deleted Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"Not Authorized to delete item", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        }


    }
}
