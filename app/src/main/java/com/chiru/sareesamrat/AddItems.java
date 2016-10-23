package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;

public class AddItems extends AppCompatActivity implements View.OnClickListener {

    EditText et1, et2, et3, et4;
    ImageButton ib1;
    Button finalAdd;
    private static int RESULT_LOAD_IMAGE = 1;
    ProgressDialog pDialog;
    String username, title, description, price, quantity, imageString,pic_uri = "/static/images/default.jpg";
    private static String TAG = AddItems.class.getSimpleName();
    Boolean addStatus = false,success = false;
    public static String URL = "http://54.214.190.100/saveimage/";
    private Bitmap bitmap;
    String message = "Network Problem. Try again later.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        SetupSubViews();

        ib1.setOnClickListener(this);

        finalAdd.setOnClickListener(this);


    }

    private void SetupSubViews() {

        et1 = (EditText) findViewById(R.id.additem1);
        et2 = (EditText) findViewById(R.id.additem2);
        et3 = (EditText) findViewById(R.id.additem3);
        et4 = (EditText) findViewById(R.id.additem4);

        ib1 = (ImageButton) findViewById(R.id.imagebutton1);

        finalAdd = (Button) findViewById(R.id.finaladdbutton);

        username = getIntent().getExtras().getString("Username");




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagebutton1: {
                imageBrowse();
                break;
            }
            case R.id.finaladdbutton: {


                title = et1.getText().toString();
                description = et2.getText().toString();
                quantity = et3.getText().toString();
                price = et4.getText().toString();

                if(title.isEmpty()||description.isEmpty()||quantity.isEmpty()||price.isEmpty()){

                    Toast.makeText(AddItems.this, "All Fields are required.Don't leave it blank", Toast.LENGTH_SHORT).show();

                }else if(!isValidTitle(title)){

                    Toast.makeText(AddItems.this, "Enter a valid Title", Toast.LENGTH_SHORT).show();

                }else if(!isValidQuantity(quantity)){

                    Toast.makeText(AddItems.this, "Enter a valid Quantity", Toast.LENGTH_SHORT).show();

                }else if(!isValidPrice(price)){

                    Toast.makeText(AddItems.this, "Enter a valid Price", Toast.LENGTH_SHORT).show();

                }else {

                    new AddNewItems().execute();
                }
                break;
            }
        }
    }

    private void imageBrowse(){

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri imagePath = data.getData();
            ib1.setImageURI(imagePath);

            uploadImage();

        }


    }

    public String getStringForImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] image = byteArrayOutputStream.toByteArray();
        imageString = Base64.encodeToString(image,Base64.DEFAULT);
        return imageString;

    }




   private void uploadImage() {

       bitmap = ((BitmapDrawable) ib1.getDrawable()).getBitmap();
       StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String s) {
                       try {
                           JSONObject jsonObject = new JSONObject(s);
                           success = jsonObject.getBoolean("success");
                           pic_uri = jsonObject.getString("url");
                           if (!success) {

                               Toast.makeText(AddItems.this, "Image Uploading failed, Image will not be changed.", Toast.LENGTH_LONG).show();
                           }

                       } catch (JSONException e) {

                       }
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError volleyError) {

                       Toast.makeText(AddItems.this, "Network Error", Toast.LENGTH_LONG).show();
                   }
               }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {

               String image = getStringForImage(bitmap);
               String imageName = et1.getText().toString();
               String user = username;
               Map<String, String> params = new Hashtable<>();

               params.put("image", image);
               params.put("title", imageName);
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
                    addStatus = jsonObj.getBoolean("success");

                }catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (addStatus) {
                Toast.makeText(getApplicationContext(), "Item added Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);

            } else if (!addStatus) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        }


    }

    private boolean isValidQuantity(String quantity){
        Pattern QUANTITY_PATTERN = Pattern.compile("^[1-9][0-9]{0,4}");
        return QUANTITY_PATTERN.matcher(quantity).matches();
    }

    private boolean isValidPrice(String price){
        Pattern PRICE_PATTERN = Pattern.compile("(^[1-9][0-9]{1,6}[\\.]?[0-9]*)");
        return PRICE_PATTERN.matcher(price).matches();
    }
    private boolean isValidTitle(String title){
        Pattern TITLE_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9\\-\\.@&]+");
        return TITLE_PATTERN.matcher(title).matches();
    }
}







