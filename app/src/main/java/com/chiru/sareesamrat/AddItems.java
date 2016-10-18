package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class AddItems extends AppCompatActivity implements View.OnClickListener {

    EditText et1, et2, et3, et4;
    ImageButton ib1;
    Button finaladd;
    private static int RESULT_LOAD_IMAGE = 1;
    ProgressDialog pDialog;
    String username, title, description, price, quantity,imagestring,pic_uri;
    private static String TAG = AddItems.class.getSimpleName();
    Boolean addstatus,success = false;
    public static String URL = "http://54.214.190.100/saveimage/";
    private final Context context = this;
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private byte[] multipartBody;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        SetupSubViews();

        ib1.setOnClickListener(this);

        finaladd.setOnClickListener(this);


    }

    private void SetupSubViews() {

        et1 = (EditText) findViewById(R.id.additem1);
        et2 = (EditText) findViewById(R.id.additem2);
        et3 = (EditText) findViewById(R.id.additem3);
        et4 = (EditText) findViewById(R.id.additem4);

        ib1 = (ImageButton) findViewById(R.id.imagebutton1);

        finaladd = (Button) findViewById(R.id.finaladdbutton);

        username = getIntent().getExtras().getString("Username");




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagebutton1: {
                imageBrowse();

                if(success){
                    finaladd.setEnabled(true);
                }
                break;
            }
            case R.id.finaladdbutton: {

                new AddNewItems().execute();
                title = et1.getText().toString();
                description = et2.getText().toString();
                quantity = et3.getText().toString();
                price = et4.getText().toString();
                break;
            }
        }
    }

    private void imageBrowse(){

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);

    }
    @Override
    public void onActivityResult(int requestcode, int responsecode, Intent data) {

        super.onActivityResult(requestcode, responsecode, data);
        if (requestcode == RESULT_LOAD_IMAGE && responsecode == RESULT_OK && null != data) {

            Uri imagePath = data.getData();
            ib1.setImageURI(imagePath);

            uploadImage();


        }


    }

    public String getStringForImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] image = byteArrayOutputStream.toByteArray();
        imagestring = Base64.encodeToString(image,Base64.DEFAULT);
        return imagestring;

    }

   


   private void uploadImage(){

        bitmap= ((BitmapDrawable) ib1.getDrawable()).getBitmap();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try{
                        JSONObject jsonObject = new JSONObject(s);
                        success = jsonObject.getString("success").equalsIgnoreCase("true")? true:false;
                        pic_uri = jsonObject.getString("url");
                    }catch (JSONException e){

                    }


                    Toast.makeText(AddItems.this, s , Toast.LENGTH_LONG).show();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    Toast.makeText(AddItems.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            String image = getStringForImage(bitmap);
            String imagename = et1.getText().toString();
            String user = getIntent().getExtras().getString("Username");
            Map<String,String> params = new Hashtable<>();

            params.put("image", image);
            params.put("title" ,imagename);
            params.put("username", user);

            return params;
        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(this);
        
    requestQueue.add(stringRequest);
}


    private class AddNewItems extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(AddItems.this);
            pDialog.setMessage("Adding Items...");
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
                    .appendPath("add")
                    .appendPath(username)
                    .appendPath("")
                    .appendQueryParameter("title", title)
                    .appendQueryParameter("description", description)
                    .appendQueryParameter("price", price)
                    .appendQueryParameter("quantity", quantity)
                    .appendQueryParameter("image_uri", pic_uri);

            String url = builder.build().toString();


            String jsonStr = pwordhandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    addstatus = jsonObj.getBoolean("success");

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

            if (addstatus) {
                Toast.makeText(getApplicationContext(), "Item added Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            } else if (!addstatus) {
                Toast.makeText(getApplicationContext(), "You are not authorized to add Items", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        }


    }
}







