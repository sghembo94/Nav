package com.example.andrea.nav;

/**
 * Created by andrea on 20/03/17.
 */


import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Hash_MAC {

        public String publicKey;
        public String privateKey;
        public String url;
        public String endpoint;

        public Hash_MAC(String publicKey, String privateKey, String url, String endpoint){
            this.publicKey = publicKey;
            this.privateKey = privateKey;
            this.url = url;
            this.endpoint = endpoint;
        }


        public String getProducts() throws UnsupportedEncodingException {
            String method = "GET";
            String endpoint = this.endpoint + "?";

            String baseRequestUrl = "";

            String oauth_consumer_key = "oauth_consumer_key="+this.publicKey;
            Long tsLong = System.currentTimeMillis()/1000;
            String oauth_timestamp = "oauth_timestamp="+tsLong.toString();
            SecureRandom random = new SecureRandom();
            String oauth_nonce = "oauth_nonce="+new BigInteger(130, random).toString(32);
            String oauth_signature_method = "oauth_signature_method=HMAC-SHA256";
            String oauth_signature = "oauth_signature=";

            try {
                baseRequestUrl = URLEncoder.encode(this.url+ this.endpoint,"UTF-8") ;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //url = "http://www.littlebonfi.altervista.org/wc-api/v2/";
            String url = this.url + endpoint + oauth_consumer_key + "&" + oauth_timestamp + "&" + oauth_nonce + "&" + oauth_signature_method + "&" +oauth_signature;
            oauth_consumer_key = URLEncoder.encode(oauth_consumer_key +"&","UTF-8");
            oauth_timestamp= URLEncoder.encode(oauth_timestamp,"UTF-8");
            oauth_nonce = URLEncoder.encode(oauth_nonce + "&","UTF-8");
            oauth_signature_method = URLEncoder.encode(oauth_signature_method + "&","UTF-8");


            String stringToEncrypt = method + "&" + baseRequestUrl + "&" + oauth_consumer_key + oauth_nonce  + oauth_signature_method  + oauth_timestamp;
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

    //Long tsLong = System.currentTimeMillis()/1000;
    //hash = tsLong.toString();


}
