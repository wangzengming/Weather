package com.example.lrp.auto.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.lrp.auto.gson.Weather;
import com.example.lrp.auto.util.HttpUtil;
import com.example.lrp.auto.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/20.
 */

public class AutoUpdateVice extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        updateWeather();
        updateBingpic();


        AlarmManager manger = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anhour = 8 * 60 * 60 * 1000;
        long triggerAttime = SystemClock.elapsedRealtime() + anhour;
        Intent i = new Intent(this, AutoUpdateVice.class);

        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manger.cancel(pi);
        manger.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAttime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingpic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {


            Weather weather = Utility.handlWeatherResponse(weatherString);

            String weatherId = weather.basic.weatherid;
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String responseText = response.body().string();
                    final Weather weather = Utility.handlWeatherResponse(responseText);


                    if (weather != null && "ok".equals(weather.status)) {

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateVice.this).edit();
                        editor.putString("weather", responseText);

                        editor.apply();

                    }


                }

                @Override
                public void onFailure(Call call, IOException e) {


                    e.printStackTrace();
                }
            });

        }


    }

    private void updateWeather() {


        String requestBingPic = "http://guolin.tech/api/bing_pic";

        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String bingpic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateVice.this).edit();


                editor.putString("bing_pic", bingpic);
                editor.apply();


            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });


    }

}
