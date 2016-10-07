package com.chiru.sareesamrat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private TextView sareecategory;
    private List<Saree> sareelist = new ArrayList<>();
    private RecyclerView myrecycleview;
    private SareesAdapter mysareeadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        sareecategory = (TextView) findViewById(R.id.simpletext);

        sareecategory.setText("You Selected for" +" " +getIntent().getExtras().getString("saree category"));



        myrecycleview = (RecyclerView) findViewById(R.id.recyclerview);

        mysareeadapter = new SareesAdapter(sareelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myrecycleview.setLayoutManager(mLayoutManager);
        myrecycleview.setItemAnimator(new DefaultItemAnimator());
        myrecycleview.addItemDecoration(new DividerforItems(this, LinearLayoutManager.VERTICAL));
        myrecycleview.setAdapter(mysareeadapter);

        myrecycleview.addOnItemTouchListener(new RecyclerviewListener(getApplicationContext(),myrecycleview,new RecyclerviewListener.ClickListener(){
        @Override
            public void onClick(View view, int position) {
                Saree saree = sareelist.get(position);
                Toast.makeText(RecyclerViewActivity.this,saree.getTitle() +"is Selected",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),FullPageActivity.class);
                intent.putExtra("title",saree.getTitle());
                intent.putExtra("description",saree.getDescription());
                intent.putExtra("price",saree.getPrice());
                intent.putExtra("image",saree.getImageurl());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareData();
    }

    private void prepareData() {
        Saree saree = new Saree("Cotton Blended Saree with design", "Rs.699", null, "Shyam Shubh Sarees");
        sareelist.add(saree);
        saree = new Saree("TerryCotton Blended Saree with Churidaar Design", "Rs.899", null, "Navratri Sarees");
        sareelist.add(saree);
        saree = new Saree("Printed SuperNet Saree with boutiQue work", "Rs.739", null, "Shyama Sarees");
        sareelist.add(saree);
        saree = new Saree("Banarasi Saree with Full Work", "Rs.1699", null, "Exclusive Sarees");
        sareelist.add(saree);
        saree = new Saree("Georgette saree with heavy aanchal work", "Rs.999", null, "ShubhLaxmi Sarees");
        sareelist.add(saree);
        saree = new Saree("Pure Silk saree,very lightweight with beautiful aanchal", "Rs.599", null, "Drishyam Sarees");
        sareelist.add(saree);
        saree = new Saree("Cotton Blended Saree with full churidaar boutique work", "Rs.1199", null, "Shyam Shubh Sarees");
        sareelist.add(saree);
        saree = new Saree("TerryCotton Saree with Fancy design", "Rs.899", null, "Shri Hari Sarees");
        sareelist.add(saree);
    }
}



