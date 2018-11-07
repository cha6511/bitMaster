package com.bitmaster;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bitmaster.EventBus.Events;
import com.bitmaster.EventBus.GlobalBus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import static android.graphics.Color.TRANSPARENT;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    public static ExchangeFragment exchangeFragment;

    public static ExchangeFragment getInstance() {
        if (exchangeFragment == null) {
            exchangeFragment = new ExchangeFragment();
        }
        return exchangeFragment;
    }

    public ExchangeFragment() {
        // Required empty public constructor
    }

    RecyclerView list;
    GridLayoutManager gridLayoutManager;
    ExchangeListAdapter adapter;

    StaticData staticData = StaticData.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exchange, container, false);
        list = v.findViewById(R.id.list);
        gridLayoutManager = new GridLayoutManager(getContext(), 4);
        adapter = new ExchangeListAdapter(getContext(), staticData.exchangeData, this, this);
        list.setLayoutManager(gridLayoutManager);
        list.setAdapter(adapter);

        refreshList();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalBus.getBus().unregister(this);
    }


    private void refreshList() {
        ArrayList<String> fav = new ArrayList<>();
        ArrayList<Integer> index = new ArrayList<>();
        TinyDB tinyDB = new TinyDB(getContext());
        fav = tinyDB.getListString("coinFav");

        LinkedList<ExchangeData> tmp = new LinkedList<ExchangeData>();
        for(int i = 0 ; i < staticData.listSize ; i++){
            tmp.add(new ExchangeData());
        }

        for (int i = 0 ; i < staticData.exchangeData.size(); i++) {
            for(int j = 0 ; j < fav.size() ; j++){
                if(staticData.exchangeData.get(i).getName().equals(fav.get(j))){
                    tmp.set(j, staticData.exchangeData.get(i));
                    index.add(i);
                }
            }
        }

        int p = 0;
        for (int i = 0; i < staticData.exchangeData.size(); i++) {
            if (!index.contains(i)) {
                tmp.set(fav.size() + p, staticData.exchangeData.get(i));
                p++;
            }
        }

        staticData.exchangeData.clear();
        for(int i = 0 ; i < tmp.size() ; i++){
            staticData.exchangeData.add(tmp.get(i));
        }


        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(Events.Refresh refresh) {
        refreshList();
    }

    @Override
    public void onClick(View view) {
        String name = ((ExchangeData) view.getTag()).getName();
        Log.d("exchangeFragment", name);
        boolean newBrowser = ((ExchangeData) view.getTag()).isNew_browser_yn();
        if ("contact".equals(name) || "affiliate".equals(name) || "share".equals(name)) {

        } else {
            String url = ((ExchangeData) view.getTag()).getUrl();
            Events.Msg msg = new Events.Msg(newBrowser, url);
            GlobalBus.getBus().post(msg);
            Log.d("exchangeFragment", url);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int pos = list.getChildLayoutPosition(view);
        if(pos >= 8){
            FavoriteDialog dialog = new FavoriteDialog(getContext(), ((ExchangeData) view.getTag()));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            return false;
        }
        return true;
    }
}
