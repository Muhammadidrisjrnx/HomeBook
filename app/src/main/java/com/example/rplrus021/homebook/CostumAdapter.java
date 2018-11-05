package com.example.rplrus021.homebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import static com.example.rplrus021.homebook.R.layout.abc_list_menu_item_icon;
import static com.example.rplrus021.homebook.R.layout.row_item;

public class CostumAdapter extends RecyclerView.Adapter<CostumAdapter.CardViewBukuHolder> {

    private ArrayList<buku> arrayListBuku;
    private Context context;
    public String url2;

    CostumAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<buku> getArrayListBuku() {
        return arrayListBuku;
    }

    void setArrayListBuku(ArrayList<buku> arrayListBuku) {
        this.arrayListBuku = arrayListBuku;
    }

    @Override
    public CardViewBukuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new CardViewBukuHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewBukuHolder holder, final int position) {
        final buku buku = getArrayListBuku().get(position);
        url2 = buku.getNama_gambar();
        Glide.with(context)
                .load(url2)
                .into(holder.imgBuku);
        holder.txtBuku.setText(buku.getJudul_buku());
        holder.btn_lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = position;
                final String id_buku = buku.getId_buku();
                final String judul_buku = buku.getJudul_buku();
                final String gambar = buku.getNama_gambar();
                final String description = buku.getDescription();
                Intent intent = new Intent(context.getApplicationContext(), book_description.class);
                intent.putExtra("id_buku", id_buku);
                intent.putExtra("judul_buku", judul_buku);
                intent.putExtra("gambar", gambar);
                intent.putExtra("deskripsi", description);
                intent.putExtra("posisi ", pos);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListBuku.size();
    }

    public class CardViewBukuHolder extends RecyclerView.ViewHolder {
        ImageView imgBuku;
        TextView txtBuku;
        Button btn_lihat;

        public CardViewBukuHolder(View itemView) {
            super(itemView);
            imgBuku = (ImageView) itemView.findViewById(R.id.imgBuku);
            txtBuku = (TextView) itemView.findViewById(R.id.txtBuku);
            btn_lihat = (Button) itemView.findViewById(R.id.btn_lihat);
        }
    }
}
