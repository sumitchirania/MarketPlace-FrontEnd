package com.chiru.sareesamrat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContentActivity extends AppCompatActivity {

    TextView textview;
    ListView listview;
    String[] sareeTypes = {"Silk Sarees", "Chiffon Sarees", "Georgette Sarees", "Terrycotton Sarees", "Cotton Sarees",
            "Banarasi Sarees", "Polyster Sarees", "Net Sarees", "SuperNet Sarees"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        textview = (TextView) findViewById(R.id.textviewcontent);
        textview.setText("Hello" + " " + getIntent().getExtras().getString("Username"));

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.listview, sareeTypes);

        listview = (ListView) findViewById(R.id.listviewsaree);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                String item = ((TextView) view).getText().toString();
                Toast.makeText(ContentActivity.this,"You Selected" +" " +item,Toast.LENGTH_SHORT ).show();
                Intent intent = new Intent(getApplicationContext(), RecyclerViewActivity.class);
                intent.putExtra("saree category", item);
                startActivity(intent);

            }
        });
    }
}


