package com.bitmaster;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.bitmaster.EventBus.Events;
import com.bitmaster.EventBus.GlobalBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(Events.Msg msg) {
        Log.d("MainActivity", msg.getUrl() + " / " + msg.isNew());
        String url = msg.getUrl();
        if (msg.isNew()) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            pager.setCurrentItem(1, true);
            Events.ControlWebView controlWebView = new Events.ControlWebView(url);
            GlobalBus.getBus().post(controlWebView);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void backToMain(Events.BackToMain backToMain) {
        Log.d("MainActivity", backToMain.getMsg());
        if ("goback".equals(backToMain.getMsg()))
            pager.setCurrentItem(0, true);
    }
}
