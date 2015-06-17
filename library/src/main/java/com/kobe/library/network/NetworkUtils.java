package com.kobe.library.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kobe.gong on 2015/6/17.
 */
public class NetworkUtils {
    public static boolean checkNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null && net.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String get(String url) {
        try {
            URL uri = new URL(url);
            URLConnection conn = uri.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            StringWriter sw = new StringWriter();
            while ((line = br.readLine()) != null) {
                sw.write(line);
            }
            sw.close();
            br.close();
            return sw.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void asyncHttpGet(String url, final HttpCallback callback){
        new AsyncTask<String, Integer, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String uri = strings[0];
                String result = NetworkUtils.get(uri);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result == null)
                    callback.failure();
                else
                    callback.success(result);
            }
        }.execute(url);
    }
}
