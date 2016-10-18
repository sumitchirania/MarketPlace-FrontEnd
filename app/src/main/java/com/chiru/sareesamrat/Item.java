package com.chiru.sareesamrat;

/**
 * Created by chiru on 15/9/16.
 */
public class Item {


    public String description,price,imageurl,title,quantity;

    public Item(){

    }

    public Item(String description, String price, String imageurl, String title, String quantity){

        this.description = description;
        this.price = price;
        this.imageurl = imageurl;
        this.title = title;
        this.quantity = quantity;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price = price;
    }

    public String getImageurl(){
        return imageurl;
    }
    public void setImageurl(String imageurl){
        this.imageurl = imageurl;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
}
