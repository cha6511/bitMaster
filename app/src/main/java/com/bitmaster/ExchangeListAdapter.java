package com.bitmaster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedList;

public class ExchangeListAdapter extends RecyclerView.Adapter<ExchangeListAdapter.Holder> {

    LinkedList<ExchangeData> datas = new LinkedList<>();
    View.OnClickListener onClickListener;
    View.OnLongClickListener onLongClickListener;
    Context context;

    public ExchangeListAdapter(Context context, LinkedList<ExchangeData> datas, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        this.datas = datas;
        this.context = context;
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exchange_list_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ExchangeData data = datas.get(position);
        Glide.with(context).load(data.getImg_url()).into(holder.img);
        holder.body.setTag(data);
        holder.body.setOnClickListener(onClickListener);
        holder.body.setOnLongClickListener(onLongClickListener);
        holder.name.setText(data.getName());
//        holder.press_effect.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;

        ImageView press_effect;

        CustomImageView body;

        public Holder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            press_effect = itemView.findViewById(R.id.press_effect);
            body = itemView.findViewById(R.id.body);
        }
    }
}
