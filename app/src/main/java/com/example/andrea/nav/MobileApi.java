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

public class MobileApi {

    public String publicKey;
    public String privateKey;
    public String url;
    public String result;
    public boolean apiInUse;
    public Endpoint requestType;
    public enum Endpoint { NONE, PRODOTTI, CATEGORIE, ORDINI, ORDINE };
    public enum Method {GET, POST};

    public MobileApi(String publicKey, String privateKey, String url){
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.url = url;
        this.apiInUse = false;
        this.requestType = Endpoint.NONE;
    }

    public String getUrlRequest(Endpoint request, List<UrlTag> urlTagListQuery) throws UnsupportedEncodingException {
        this.requestType = request;
        String endpoint = "";
        if(urlTagListQuery.size()!=0)
            endpoint = "?"  + UrlTag.getCompleteUrl(urlTagListQuery)+"&";
        Long tsLong = System.currentTimeMillis()/1000;
        SecureRandom random = new SecureRandom();



        List<UrlTag> urlTagList = new ArrayList<UrlTag>();
        UrlTag temp;
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
        String stringToEncrypt = UrlTag.getStringToEncrypt(urlTagList);
        Log.d("pippo", stringToEncrypt);
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
                String result = "?request=prodotti";
                if(id!=-1)
                    result += "&categoria=id";
                return result;
            case CATEGORIE:
                return "?request=categorie";
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

    public String makeRequest(Endpoint request, List<UrlTag> urlTagList) {
        if(!this.apiInUse) {
            this.apiInUse = true;
            String url = null;
            try {
                url = (getUrlRequest(request, urlTagList));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("pippo", url);
            return url;
        }
        return null;
    }



    public ArrayList<Categoria> getAllCategories(String backEndIpAddrress){
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
                    jsonArray = json.getJSONArray("categorie");

                } catch (JSONException e) {


                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Categoria temp = new Categoria(jsonArray.getJSONObject(i));
                        temp.thumbnail = backEndIpAddrress  +  temp.thumbnail;
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


    public ArrayList<Prodotto> parseResponseToProductsList(String backEndIpAddress){
        if(this.apiInUse && this.requestType == Endpoint.PRODOTTI){
            this.apiInUse = false;
            this.requestType = Endpoint.NONE;
            JSONObject json = null;
            try {
                json = new JSONObject(this.result);
            } catch (JSONException e) {
            }
            if(json != null) {

                ArrayList<Prodotto> categorie = new ArrayList<Prodotto>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.getJSONArray("prodotti");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Prodotto temp = new Prodotto(jsonArray.getJSONObject(i));
                        temp.thumbnail = backEndIpAddress  + temp.thumbnail;
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
                    jsonArray = json.getJSONArray("ordini");

                } catch (JSONException e) {

                    e.printStackTrace();

                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Ordine temp = new Ordine(jsonArray.getJSONObject(i));
                        //Log.d("pippo", temp.createdAt.getTime().toString());
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

    public Ordine parseResponseToOrder(String backEndIpAddress){
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
                    jsonObject = json.getJSONObject("ordine");
                } catch (JSONException e) {

                    e.printStackTrace();

                }
                Ordine temp = new Ordine();
                temp.parseToOrder(jsonObject, backEndIpAddress);
                return temp;
            }
        }
        return null;
    }


}




