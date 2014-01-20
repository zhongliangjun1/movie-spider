package com.dianping.spider.util.support;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liming_liu
 * Date: 14-1-20
 * Time: 下午1:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class PoiUtils {

    private static JSONArray callPoi(JSONObject query) {
        String param = "";
        JSONArray result = null;
        try {
            param = URLEncoder.encode(query.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return result;
        }
        String url = "http://10.1.1.75/?m=getdupshopsbyshopinfo&shopinfo=" + param;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpget);
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        HttpEntity entity = response.getEntity();
        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        try {
            result = new JSONArray(body);
        } catch (Exception je) {
            return result;
        }

        return result;
    }

    public static int getDpShopId(int cityId, String shopName, String address, String phone) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cityid", cityId);
            jsonObject.put("shopname", shopName);
            jsonObject.put("address", address);
            jsonObject.put("phoneno", phone);
            jsonObject.put("shoptype", 30);

            JSONArray ress = callPoi(jsonObject);
            if (ress == null || ress.length() != 1) {
                return 0;
            }
            JSONObject obj = ress.getJSONObject(0);
            if (obj.getInt("score") < 70) {
                return 0;
            }
            return obj.getInt("shopid");
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return 0;
        }
    }
}
