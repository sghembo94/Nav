package com.example.andrea.nav;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by andrea on 28/03/17.
 */

public class Ordine implements Serializable {
    public String id;
    public Float total;
    public String status;
    public String note;
    public List<Prodotto> prodotti;
    public Date createdAt;

    public Ordine(){}

    public Ordine(JSONObject jsonObject){
        String data = "";
        try {
            this.id = jsonObject.getString("id");
            this.total = Float.parseFloat(jsonObject.getString("totale"));
            Log.d("pippo", this.total.toString());
            //this.status = jsonObject.getString("status");
            //this.note = jsonObject.getString("note");
            data = jsonObject.getString("data");
            //JSONArray items = jsonObject.getJSONArray("line_items");
            /*
            prodotti = new ArrayList<Prodotto>();
            for(int i=0; i< items.length(); i++){
                Prodotto temp = new Prodotto();
                temp.title = items.getJSONObject(i).getString("name");
                temp.qta = items.getJSONObject(i).getString("quantity");
                temp.price = Float.parseFloat(items.getJSONObject(i).getString("price"));
                prodotti.add(temp);
                //Log.d("pippo", );

            }
            */
        }catch (JSONException e) {
            e.printStackTrace();
            Log.d("pippo", e.toString());
        }

        if(data.length()>0) {
            try {
                DateFormat formatter;
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = (Date) formatter.parse(data);
                java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                this.createdAt = new Date();
                this.createdAt.setTime(timeStampDate.getTime());



            } catch (ParseException e) {
                System.out.println("Exception :" + e);

            }
            /*
            int year = Integer.parseInt(data.substring(0, data.indexOf("-")));
            data = data.substring(data.indexOf("-") + 1);
            //Log.d("pippo", "Year: " + year);
            //Log.d("pippo", data);

            int month = Integer.parseInt(data.substring(0, data.indexOf("-")));
            data = data.substring(data.indexOf("-") + 1);

            int day =  Integer.parseInt(data.substring(0, data.indexOf("T")));
            data = data.substring(data.indexOf("T") + 1);

            int hour =  Integer.parseInt(data.substring(0, data.indexOf(":")));
            data = data.substring(data.indexOf(":") + 1);

            int minute =  Integer.parseInt(data.substring(0, data.indexOf(":")));
            data = data.substring(data.indexOf(":") + 1);

            int second =  Integer.parseInt(data.substring(0, data.indexOf("Z")));
            // Date date = new Date(year,month,day,hour,minute,second);
            this.createdAt = Calendar.getInstance();
            this.createdAt.set(year,month,day,hour,minute,second);
            Log.d("pippo", this.createdAt.getTime().toString());

    */
        }
    }
    public void parseToOrder(JSONObject jsonObject, String backEndIpAddress){
        JSONArray items =null;
        try {
            this.id = jsonObject.getString("id");
            items = jsonObject.getJSONArray("prodotti_ordine");
            try {
                DateFormat formatter;
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = (Date) formatter.parse(jsonObject.getString("data"));
                java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                this.createdAt = new Date();
                this.createdAt.setTime(timeStampDate.getTime());
            }catch (Exception e){

            }
            this.status = jsonObject.getString("stato");
            this.total = Float.parseFloat(jsonObject.getString("totale"));

            prodotti = new ArrayList<Prodotto>();
            for(int i=0; i< items.length(); i++) {
                Prodotto temp = new Prodotto();
                temp.title = items.getJSONObject(i).getString("nome");
                temp.qta = items.getJSONObject(i).getString("quantita");
                temp.thumbnail =  backEndIpAddress + items.getJSONObject(i).getString("img");
                temp.price = Float.parseFloat(items.getJSONObject(i).getString("prezzo"));
                prodotti.add(temp);
            }

        }catch (Exception e){

        }
    }
}
