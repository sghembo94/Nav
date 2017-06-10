package com.example.andrea.nav;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrea on 23/03/17.
 */

public class Categoria {

    public String title;
    public String thumbnail;
    public String slug;

    public Categoria(String title, String thumbnail){
            this.title = title;
            this.thumbnail = thumbnail;
            }

    public Categoria(JSONObject jsonObject){
            /*
            try {
            this.title = jsonObject.getString("name");
            this.thumbnail = jsonObject.getString("image");
            this.slug = jsonObject.getString("slug");

            Log.e("pippo", thumbnail+"jb");
            } catch (JSONException e) {
            e.printStackTrace();
            }
            */

            try {
                this.title = jsonObject.getString("nome");
                this.thumbnail = jsonObject.getString("img");
                this.slug = jsonObject.getString("id");

                Log.e("pippo", thumbnail+"jb");
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

}
