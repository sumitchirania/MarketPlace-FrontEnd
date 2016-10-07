package com.chiru.sareesamrat;

/**
 * Created by chiru on 15/9/16.
 */
public class Saree {


    private String description,price,imageurl,title;

    public Saree(){

    }

    public Saree(String description,String price,String imageurl,String title){

        this.description = description;
        this.price = price;
        this.imageurl = imageurl;
        this.title = title;
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
}
