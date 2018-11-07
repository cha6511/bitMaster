package com.bitmaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    StaticData staticData = StaticData.getInstance();
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        img = findViewById(R.id.img);
        Glide.with(SplashActivity.this).load(getResources().getDrawable(R.drawable.splash_img)).into(img);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDatas gd = new getDatas();
                gd.execute();
            }
        }, 1000);

    }

    private class getDatas extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url("http://13.125.241.78:3000/site/l_site");
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                String myResponse = response.body().string();
                return myResponse;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.isEmpty(s)) {
                Toast.makeText(SplashActivity.this, "사이트 리스트를 받아오지 못했습니다.\n앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                try {
                    StaticData.exchangeData.clear();
                    JSONArray res = new JSONArray(s);
                    JSONArray resultArray;
                    Log.d("site list", s);

                    TinyDB tinyDB = new TinyDB(SplashActivity.this);
                    if(TextUtils.isEmpty(tinyDB.getString("originList"))){
                        tinyDB.putString("originList", s);
                        resultArray = res.getJSONArray(0);
                    } else{
                        if(s.equals(tinyDB.getString("originList"))) {
                            resultArray = new JSONArray(tinyDB.getString("originList")).getJSONArray(0);
                        } else{
                            resultArray = res.getJSONArray(0);
                            tinyDB.putString("originList", s);
                        }
                    }

                    staticData.listSize = resultArray.length();

                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject obj = resultArray.getJSONObject(i);
                        if(tinyDB.getListString("coinFav") == null){
                            staticData.exchangeData.add(new ExchangeData(
                                    obj.getString("site_name"),
                                    obj.getString("icon_url"),
                                    obj.getString("site_url"),
                                    obj.getString("new_browser_yn").equals("Y")
                            ));
                        } else {
                            if (tinyDB.getListString("coinFav").contains(obj.getString("site_name"))) {
                                staticData.exchangeData.addFirst(new ExchangeData(
                                        obj.getString("site_name"),
                                        obj.getString("icon_url"),
                                        obj.getString("site_url"),
                                        obj.getString("new_browser_yn").equals("Y")
                                ));
                            } else {
                                staticData.exchangeData.add(new ExchangeData(
                                        obj.getString("site_name"),
                                        obj.getString("icon_url"),
                                        obj.getString("site_url"),
                                        obj.getString("new_browser_yn").equals("Y")
                                ));
                            }
                        }
                    }

//                    editor = shref.edit();
//                    editor.remove("fab").commit();
//                    String json = gson.toJson(staticData.exchangeData);
//                    editor.putString("fab", json).commit();


                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SplashActivity.this, "사이트 리스트를 받아오지 못했습니다.\n앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
