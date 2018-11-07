package com.bitmaster;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitmaster.EventBus.Events;
import com.bitmaster.EventBus.GlobalBus;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;

public class FavoriteDialog extends Dialog {
    Context context;
    ExchangeData data;
    StaticData staticData = StaticData.getInstance();
    public FavoriteDialog(@NonNull Context context, ExchangeData data) {
        super(context);
        this.context = context;
        this.data = data;
    }

    ImageView img;
    TextView close, confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_dialog);
        img = findViewById(R.id.img);
        Glide.with(context).load(data.getImg_url()).into(img);

        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteDialog.this.dismiss();
            }
        });

        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TinyDB tinyDB = new TinyDB(context);
                ArrayList<String> fav = new ArrayList<>();
                if(tinyDB.getListString("coinFav") == null){
                    fav.add(data.getName());
                    tinyDB.putListString("coinFav", fav);
                } else{
                    fav.add(data.getName());
                    fav.addAll(tinyDB.getListString("coinFav"));
                    if(fav.size() == 9){
                        fav.remove(8);
                    }
                    tinyDB.putListString("coinFav", fav);
                }

                FavoriteDialog.this.dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Events.Refresh refresh = new Events.Refresh();
        GlobalBus.getBus().post(refresh);
    }
}
