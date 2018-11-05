package com.example.rplrus021.homebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class CostumAdapter2 extends BaseAdapter {

    ArrayList<buku> arrayListBuku = new ArrayList<buku>();
    Context context;
    private buku data_buku;
    public String url2;

    public CostumAdapter2(ArrayList<buku> arrayListBuku, Context context) {
        this.arrayListBuku = arrayListBuku;
        this.context = context;
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
        url2 = data_buku.getNama_gambar().substring(45);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        convertView = inflater.inflate(R.layout.row_item2, null);
        ImageView icon = (ImageView) convertView.findViewById(R.id.imgBuku2);
        TextView text = (TextView) convertView.findViewById(R.id.txtBuku2);
        Glide.with(convertView.getContext())
                .load(config_url.url + url2)
                .into(icon);
        text.setText(data_buku.getJudul_buku());
        final View view = convertView;
        final String id_buku = data_buku.getId_buku();
        final String judul_buku = data_buku.getJudul_buku();
        final String gambar = config_url.url + url2;
        final String description = data_buku.getDescription();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(context.getApplicationContext(), book_description.class);
                pindah.putExtra("id_buku", id_buku);
                pindah.putExtra("judul_buku", judul_buku);
                pindah.putExtra("gambar", gambar);
                pindah.putExtra("deskripsi", description);
                pindah.putExtra("posisi ", pos);
                context.startActivity(pindah);

            }
        });

        return convertView;
    }
}
