package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class EditContent extends AppCompatActivity {

    EditText et1, et2, et3,et4;
    ImageButton ib1, ib2;
    String username,newdesc,newprice,newquan,title;
    Button finalupdate;
    ProgressDialog pDialog;
    private static String TAG = EditContent.class.getSimpleName();
    Boolean editstatus;
    private static int RESULT_LOAD_IMAGE =1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);

        setupsubviews();

        finalupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newdesc = et1.getText().toString();
                newprice = et2.getText().toString();
                newquan = et3.getText().toString();
                title = et4.getText().toString();

                new UpdateContent().execute();

            }
        });

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,RESULT_LOAD_IMAGE);
            }
        });

    }

    private void setupsubviews(){

        et1 = (EditText) findViewById(R.id.editcontenttext1);
        et2 = (EditText) findViewById(R.id.editcontenttext2);
        et3 = (EditText) findViewById(R.id.editcontenttext3);
        et4 = (EditText) findViewById(R.id.editcontenttext4);

        ib1 = (ImageButton) findViewById(R.id.updatedimage1);
        ib2 = (ImageButton) findViewById(R.id.updatedimage2);

        username = getIntent().getExtras().getString("Username");





        finalupdate = (Button) findViewById(R.id.finalupdatecontent);


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
            HttpRequestHandler pwordhandler = new HttpRequestHandler();

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100")
                    .appendPath("items")
                    .appendPath("edit")
                    .appendPath(username)
                    .appendPath(title)
                    .appendPath("")
                    .appendQueryParameter("description",newdesc)
                    .appendQueryParameter("price", newprice)
                    .appendQueryParameter("quantity", newquan);

             String url = builder.build().toString();


            String jsonStr = pwordhandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    editstatus= jsonObj.getBoolean("success");

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

            if (editstatus) {
                Toast.makeText(getApplicationContext(),"Item Updated Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"Not Authorized To edit Items", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ib1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }


    }
}

