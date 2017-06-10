package com.example.andrea.nav;

import android.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by andrea on 21/03/17.
 */

public class RestApi {

    public String publicKey;
    public String privateKey;
    public String url;
    public String result;
    public boolean apiInUse;
    public Endpoint requestType;
    public enum Endpoint { NONE, PRODOTTI, CATEGORIE, ORDINI, ORDINE };
    public enum Method {GET, POST};

    public RestApi(String publicKey, String privateKey, String url){
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.url = url;
        this.apiInUse = false;
        this.requestType = Endpoint.NONE;
    }

    public String getUrlRequest(String request, Method m, List<UrlTag> urlTagList) throws UnsupportedEncodingException {
        String method = getMethod(m);
        String endpoint = request + "?";

        Long tsLong = System.currentTimeMillis()/1000;
        SecureRandom random = new SecureRandom();

        UrlTag temp;

        for(int i = 0; i < urlTagList.size(); i++){
            temp = urlTagList.get(i);
            temp.tagName = URLEncoder.encode(temp.tagName,"UTF-8");
        }

        temp = new UrlTag("oauth_consumer_key", this.publicKey);
        urlTagList.add(temp);
        temp = new UrlTag("oauth_timestamp", tsLong.toString());
        urlTagList.add(temp);
        temp = new UrlTag("oauth_nonce", new BigInteger(130, random).toString(32));
        urlTagList.add(temp);
        temp = new UrlTag("oauth_signature_method", "HMAC-SHA256");
        urlTagList.add(temp);

        Collections.sort(urlTagList);

        String url = this.url + endpoint + UrlTag.getCompleteUrl(urlTagList) + "&oauth_signature=";
        String stringToEncrypt = UrlTag.getStringToEncrypt(urlTagList,method,this.url ,request );

        url += URLEncoder.encode(encrypt(stringToEncrypt),"UTF-8");

        return url;
    }

    public String encrypt(String message){
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(privateKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            String hash = new String(Base64.encodeBase64(sha256_HMAC.doFinal(message.getBytes())));
            return hash;
        }
        catch (Exception e){
            System.out.println("Error");
        }
        return null;

    }

    public String getEndpoint(Endpoint endpoint, int id){
        switch (endpoint){
            case PRODOTTI:
                return "products";
            case CATEGORIE:
                return "products/categories";
            case ORDINI:
                return "orders";
            case ORDINE:
                return "orders/"+id;
        }
        return "";
    }
    public String getMethod(Method method){
        switch (method){
            case GET:
                return "GET";
            case POST:
                return "POST";
        }
        return "";
    }

    public String makeRequest(Method method, Endpoint type, List<UrlTag> urlTagList, int id) {
        if(!this.apiInUse) {
            this.apiInUse = true;
            this.requestType = type;
            String endpoint = getEndpoint(type, id);
            String url = null;
            try {
                url = (getUrlRequest(endpoint, method, urlTagList));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("pippo", url);
            return url;
        }
        return null;
    }



    public ArrayList<Categoria> getAllCategories(){
        if(this.apiInUse && this.requestType == Endpoint.CATEGORIE){
            this.apiInUse = false;
            this.requestType = Endpoint.NONE;
            JSONObject json = null;
            try {
                json = new JSONObject(this.result);
            } catch (JSONException e) {
            }
            if(json != null) {
                ArrayList<Categoria> categorie = new ArrayList<Categoria>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.getJSONArray("product_categories");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Categoria temp = new Categoria(jsonArray.getJSONObject(i));
                        categorie.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return categorie;
            }
        }
        return null;
    }


    public ArrayList<Prodotto> parseResponseToProductsList(){
        if(this.apiInUse && this.requestType == Endpoint.PRODOTTI){
            this.apiInUse = false;
            this.requestType = Endpoint.NONE;
            JSONObject json = null;
            try {
                json = new JSONObject(this.result);
                Log.d("pippo", this.result);
            } catch (JSONException e) {
            }
            if(json != null) {

                ArrayList<Prodotto> categorie = new ArrayList<Prodotto>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.getJSONArray("products");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Prodotto temp = new Prodotto(jsonArray.getJSONObject(i));
                        Log.d("pippo", temp.title);
                        categorie.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return categorie;
            }

        }
        return null;
    }


    public ArrayList<Ordine> parseResponseToOrdersList(){
        if(this.apiInUse && this.requestType == Endpoint.ORDINI){
            this.apiInUse = false;
            this.requestType = Endpoint.NONE;
            JSONObject json = null;
            try {
                json = new JSONObject(this.result);
            } catch (JSONException e) {
                Log.d("pippo2", "errore parsing json");
            }
            if(json != null) {

                ArrayList<Ordine> ordini = new ArrayList<Ordine>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.getJSONArray("orders");
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Ordine temp = new Ordine(jsonArray.getJSONObject(i));

                        ordini.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return ordini;
            }

        }
        return null;
    }

    public Ordine parseResponseToOrder(){
        if(this.apiInUse && this.requestType == Endpoint.ORDINE){
            this.apiInUse = false;
            this.requestType = Endpoint.NONE;
            JSONObject json = null;
            try {
                json = new JSONObject(this.result);
            } catch (JSONException e) {
                Log.d("pippo2", "errore parsing json");
            }
            if(json != null) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = json.getJSONObject("order");
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                return new Ordine(jsonObject);
            }
        }
        return null;
    }


}




