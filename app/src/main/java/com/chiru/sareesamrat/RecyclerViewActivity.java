package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private List<Item> itemlist = new ArrayList<>();
    private RecyclerView myrecycleview;
    private ItemsAdapter myitemadapter;
    String username;
    ProgressDialog pDialog;
    private static String TAG = RecyclerViewActivity.class.getSimpleName();
    Boolean detailstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        username = getIntent().getExtras().getString("Username");

        setUpsubviews();

        new GetItems().execute();


    }

    private void setUpsubviews(){

        myrecycleview = (RecyclerView) findViewById(R.id.recyclerview);
        myitemadapter = new ItemsAdapter(itemlist,getApplicationContext());


    }

    private class GetItems extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RecyclerViewActivity.this);
            pDialog.setMessage("Displaying Itmes. Please wait..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            HttpRequestHandler sh = new HttpRequestHandler();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100")
                    .appendPath("items")
                    .appendPath("detail")
                    .appendPath(username);

            String url = builder.build().toString();


            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    detailstatus = jsonObj.getBoolean("success");
                    


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
            if(detailstatus){
                return (jsonStr.toString());
            }
            else {
                return ("Unsuccessfull");
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            try{
                JSONObject jsonObj = new JSONObject(result);

                JSONArray items = jsonObj.getJSONArray("data");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject c = items.getJSONObject(i);
                    Item item = new Item();
                    item.title = c.getString("title");
                    item.description = c.getString("description");
                    item.quantity = c.getString("quantity");
                    item.price = c.getString("price");
                    item.imageurl = c.getString("image_uri");

                    itemlist.add(item);
                }

                myrecycleview.setLayoutManager(new LinearLayoutManager(RecyclerViewActivity.this));
                myrecycleview.addItemDecoration(new DividerforItems(getApplicationContext(), LinearLayoutManager.VERTICAL));
                myrecycleview.setItemAnimator(new DefaultItemAnimator());
                myrecycleview.setAdapter(myitemadapter);



            }catch (JSONException e ){
                Toast.makeText(RecyclerViewActivity.this, e.toString(), Toast.LENGTH_LONG).show();

            }






        }


    }
}



