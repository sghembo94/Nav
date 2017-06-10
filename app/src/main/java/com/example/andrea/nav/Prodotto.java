package com.example.andrea.nav;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by andrea on 23/03/17.
 */

public class Prodotto implements Serializable {
    public String id;
    public String title;
    public Float price;
    public String thumbnail;
    public String shortDescripton;
    public String description;
    public Float averageRating;
    public Integer ratingCount;
    public Integer totaleSales;
    public String category;
    public String qta;


    public Prodotto(){


    }

    public Prodotto(JSONObject jsonObject){

        /*
        try {

            this.id = jsonObject.getString("id");
            this.title = jsonObject.getString("title");
            this.thumbnail = jsonObject.getString("featured_src");

            String desc = jsonObject.getString("description");
            if(desc.length()>0)
                description = desc.substring(3,desc.length()-5);

            price = Float.parseFloat(jsonObject.getString("price"));

            desc = jsonObject.getString("short_description");
            shortDescripton = desc.substring(3, desc.length()-5);

            averageRating = Float.parseFloat(jsonObject.getString("average_rating"));
            ratingCount = Integer.parseInt(jsonObject.getString("rating_count"));
            totaleSales = Integer.parseInt(jsonObject.getString("total_sales"));
            String temp = jsonObject.getString("categories");
            temp = temp.substring(2, temp.length()-2);
            category = temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        */

        try {

            this.id = jsonObject.getString("id");
            this.title = jsonObject.getString("nome");
            this.thumbnail = jsonObject.getString("img");
            this.description = jsonObject.getString("descrizione");
            this.price = Float.parseFloat(jsonObject.getString("prezzo"));
            this.shortDescripton = jsonObject.getString("descrizione_breve");
            this.category = jsonObject.getString("categoria");
            /*
            averageRating = Float.parseFloat(jsonObject.getString("average_rating"));
            ratingCount = Integer.parseInt(jsonObject.getString("rating_count"));
            totaleSales = Integer.parseInt(jsonObject.getString("total_sales"));
            */


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Prodotto(String title, String price, String thumbnail){
        this.title = title;
        this.thumbnail = thumbnail;
        this.price = Float.parseFloat(price);
    }

    public Prodotto(String json){

    }

    public static String getJsonEncode(Cursor CR){

        String result = "{";
        if( CR.getCount() !=0 ) {
            CR.moveToFirst();
            int i=0;
            do {
                if(i!=0)
                    result += ",";
                Prodotto p = new Prodotto();
                p.id = CR.getString(0);
                p.qta = CR.getString(3);
                //result += "{\"product_id\":"+p.id+",\"quantity\":"+p.qta+"}";
                result += "\""+ p.id +"\":"+p.qta;
                i++;

            } while (CR.moveToNext());

        }
        result +="}";
        return result;
    }

}
