package com.chiru.sareesamrat;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiru on 15/9/16.
 */
public class SareesAdapter extends RecyclerView.Adapter<SareesAdapter.SareeViewHolder> {

    private List<Saree> sareelist;

    public class SareeViewHolder extends RecyclerView.ViewHolder {
        public TextView title,price,description;
        public ImageView imageurl;

        public SareeViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleofsaree);
            price = (TextView) view.findViewById(R.id.priceofsaree);
            description = (TextView) view.findViewById(R.id.descriptionofsaree);
            imageurl = (ImageView) view.findViewById(R.id.imageofsaree);
        }
    }


    public SareesAdapter(List<Saree> sareelist) {
        this.sareelist = sareelist;
    }

    @Override
    public SareeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saree_listinginrow, parent, false);

        return new SareeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SareeViewHolder holder, int position) {
        Saree saree = sareelist.get(position);
        holder.title.setText(saree.getTitle());
        holder.price.setText(saree.getPrice());
        holder.description.setText(saree.getDescription());
        holder.imageurl.setImageURI(Uri.EMPTY);

       /* holder.imageurl.setImageURI(new Uri() {
        });*/
    }

    @Override
    public int getItemCount() {
        return sareelist.size();
    }
}



