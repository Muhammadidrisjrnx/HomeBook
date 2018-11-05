package com.example.rplrus021.homebook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CostumerAdapter3 extends BaseAdapter {
    ArrayList<user_booking> arrayListBuku = new ArrayList<user_booking>();
    Context ctx;
    private user_booking data_buku;
    private buku data_buku2;
    public String url2;

    public CostumerAdapter3(ArrayList<user_booking> arrayListBuku, Context ctx) {
        this.arrayListBuku = arrayListBuku;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return this.arrayListBuku.size();
    }

    @Override
    public Object getItem(int position) {
        return this.arrayListBuku.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        data_buku = arrayListBuku.get(pos);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        convertView = inflater.inflate(R.layout.row_item3, null);
        ImageView icon = (ImageView) convertView.findViewById(R.id.img_buku_my_book);
        TextView text = (TextView) convertView.findViewById(R.id.tv_judul_my_book);
        TextView text2 = (TextView) convertView.findViewById(R.id.tv_deskripsi_my_book);
        text.setText(data_buku.getJudul_buku());
        text2.setText("" + data_buku.getTanggal_kembali());
        Glide.with(ctx).load(data_buku.getGambar()).into(icon);
        return convertView;
    }
}
