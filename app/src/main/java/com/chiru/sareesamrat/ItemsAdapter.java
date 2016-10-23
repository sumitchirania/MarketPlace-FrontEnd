package com.chiru.sareesamrat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chiru on 15/9/16.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private Context context;
    private String itemimageurl;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title,price,description,quantity,imageURL;
        public ImageView imageview;


        public ItemViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleofitem);
            price = (TextView) view.findViewById(R.id.priceofitem);
            description = (TextView) view.findViewById(R.id.descriptionofitem);
            quantity = (TextView) view.findViewById(R.id.quantityofitem);
            imageURL = (TextView) view.findViewById(R.id.imageurlofitem);

            imageview = (ImageView) view.findViewById(R.id.imageofitem1);

        }
    }


    public ItemsAdapter(List<Item> itemlist, Context context) {
        this.itemList = itemlist;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listinginrow, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.price.setText("Rs." + item.getPrice());
        holder.description.setText(item.getDescription());
        holder.quantity.setText(item.getQuantity());
        holder.imageURL.setText(item.getImageurl());
        itemimageurl = "http://54.214.190.100" + item.getImageurl();

        Picasso.with(context).load(itemimageurl).placeholder(R.drawable.addimage).into(holder.imageview);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}



