package com.novatta.bmb.utils;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.Nullable;

import com.novatta.bmb.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * created by Conelius on 1/17/2020 at 1:21 PM
 */
public class SampleSchedulingService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public SampleSchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;

        try {
            // constants
            URL url = new URL("https://raw.githubusercontent.com/TralahM/bmbapk/master/version.php");
            JSONObject jsonObject = new JSONObject();
            int currentversionCode = BuildConfig.VERSION_CODE;
            jsonObject.put("version",currentversionCode);

            String message = jsonObject.toString();
            StringBuffer response = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent","Mozilla");
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(35000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);

            //make some HTTP reder nicety

            conn.setRequestProperty("Content-Type","application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-wit