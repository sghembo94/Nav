package com.example.andrea.nav;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by andrea on 22/03/17.
 */

public class UrlTag implements Comparable{
    public String tagName;
    public String tagValue;

    public UrlTag(String tagName, String tagValue){
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public String toString(){
        return this.tagName + "=" + this.tagValue;
    }

    @Override
    public int compareTo(Object o) {
        UrlTag me = (UrlTag) this;
        UrlTag other = (UrlTag) o;
        for(int i=0; i< me.tagName.length(); i++){
            if(i < other.tagName.length()){
                char m = me.tagName.charAt(i);
                char h = other.tagName.charAt(i);
                if(m > h){
                    return 1;
                }
                if(m < h){
                    return -1;
                }
            }else {
                return 1;
            }
        }
        return 1;
    }

    public static String getCompleteUrl(List<UrlTag> urlTagList){
        String result = "";
        for(int i=0; i < urlTagList.size(); i++){
            if (i==0)
                result = urlTagList.get(i).toString();
            else
                result+= "&" + urlTagList.get(i).toString();
        }
        return result;
    }

    public static String getStringToEncrypt(List<UrlTag> urlTagList,String method, String baseUrl, String endpoint){
        String result = "";
        //Collections.sort(urlTagList);
        result += method + "&";
        try {
            result += URLEncoder.encode(baseUrl + endpoint,"UTF-8");
            result += "&" + URLEncoder.encode(getCompleteUrl(urlTagList));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getStringToEncrypt(List<UrlTag> urlTagList){
        String result = "";
        try {
            result += URLEncoder.encode(getCompleteUrl(urlTagList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




}