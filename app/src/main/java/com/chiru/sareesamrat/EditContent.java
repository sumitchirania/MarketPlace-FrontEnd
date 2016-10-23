package com.chiru.sareesamrat;

import android.app.ProgressDialog;
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

public class EditContent extends AppCompatActivity {

    EditText et1, et2, et3, et4;
    ImageButton ib1;
    String username, newDesc, newPrice, newQuantity, title, message = "Network problem";
    String imageString, pic_uri = "alpha/beta";
    Button finalUpdate;
    ProgressDialog pDialog;
    private static String TAG = EditContent.class.getSimpleName();
    Boolean editStatus = false, success = false;
    private static int RESULT_LOAD_IMAGE = 1;
    Bitmap bitmap;
    public static String URL = "http://54.214.190.100/saveimage/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);

        setUpSubViews();

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });


        finalUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newDesc = et1.getText().toString();
                newPrice = et2.getText().toString();
                newQuantity = et3.getText().toString();
                title = et4.getText().toString();

                if (title.isEmpty()){

                    Toast.makeText(EditContent.this, "Title is required", Toast.LENGTH_SHORT).show();

                }else if (!newQuantity.isEmpty() && !isValidQuantity(newQuantity)) {

                    Toast.makeText(EditContent.this, "Enter a valid Quantity", Toast.LENGTH_SHORT).show();

                } else if (!newPrice.isEmpty() && !isValidPrice(newPrice)) {

                    Toast.makeText(EditContent.this, "Enter a valid Price", Toast.LENGTH_SHORT).show();

                } else {

                    new UpdateContent().execute();
                }

            }
        });
    }

    private void setUpSubViews() {

        et1 = (EditText) findViewById(R.id.editcontenttext1);
        et2 = (EditText) findViewById(R.id.editcontenttext2);
        et3 = (EditText) findViewById(R.id.editcontenttext3);
        et4 = (EditText) findViewById(R.id.editcontenttext4);

        ib1 = (ImageButton) findViewById(R.id.updatedimage1);
        username = getIntent().getExtras().getString("Username");
        finalUpdate = (Button) findViewById(R.id.finalupdatecontent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imagePath = data.getData();
            ib1.setImageURI(imagePath);

            uploadImage();


        }
    }

    public String getStringForImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] image = byteArrayOutputStream.toByteArray();
        imageString = Base64.encodeToString(image, Base64.DEFAULT);
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
                            success = jsonObject.getString("success").equalsIgnoreCase("true");
                            pic_uri = jsonObject.getString("url");
                            if (!success) {

                                Toast.makeText(EditContent.this, "Image Uploading failed, Image will not be changed.", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(EditContent.this, "Network Error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String image = getStringForImage(bitmap);
                String imageName = et4.getText().toString();
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


    private class UpdateContent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditContent.this);
            pDialog.setMessage("Updating Profile...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100")
                    .appendPath("items")
                    .appendPath("edit")
                    .appendPath(username)
                    .appendPath(title)
                    .appendPath("")
                    .appendQueryParameter("description", newDesc)
                    .appendQueryParameter("price", newPrice)
                    .appendQueryParameter("image_uri", pic_uri)
                    .appendQueryParameter("quantity", newQuantity);

            String url = builder.build().toString();


            String jsonStr = httpRequestHandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    editStatus = jsonObj.getBoolean("success");
                    message = jsonObj.getString("msg");

                } catch (final JSONException e) {
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

            if (editStatus) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        }
    }

    private boolean isValidQuantity(String quantity) {
        Pattern QUANTITY_PATTERN = Pattern.compile("^[1-9][0-9]{0,4}");
        return QUANTITY_PATTERN.matcher(quantity).matches();
    }

    private boolean isValidPrice(String price) {
        Pattern PRICE_PATTERN = Pattern.compile("(^[1-9][0-9]{1,6}[\\.]?[0-9]*)");
        return PRICE_PATTERN.matcher(price).matches();
    }
}


