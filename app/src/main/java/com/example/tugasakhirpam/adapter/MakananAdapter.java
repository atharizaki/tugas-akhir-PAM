package com.example.tugasakhirpam.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasakhirpam.R;
import com.example.tugasakhirpam.model.MakananModel;

import java.util.ArrayList;

public class MakananAdapter extends RecyclerView.Adapter<MakananAdapter.MakananViewHolder> {

    private ArrayList<MakananModel> listData;
    private ArrayAdapter<MakananModel> list;


    public MakananAdapter(int simple_spinner_item, ArrayList<MakananModel> listData){
        this.listData = listData;
    }

    @NonNull
    @Override
    public MakananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_makanan,parent,false);
        return new MakananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakananViewHolder holder, int position) {
        String nama_makanan,hargaMakanan;

        nama_makanan = listData.get(position).getNama_makanan();
        hargaMakanan = listData.get(position).getHarga();

        holder.txtNama.setTextColor(Color.BLUE);
        holder.txtHarga.setTextColor(Color.BLACK);
        holder.txtNama.setTextSize(20);
        holder.txtHarga.setTextSize(16);

        holder.txtNama.setText(nama_makanan);
        holder.txtHarga.setText("Rp. " + hargaMakanan);


    }

    @Override
    public int getItemCount() {
        return (listData != null) ? listData.size() : 0;
    }


    public class MakananViewHolder extends RecyclerView.ViewHolder {
        private CardView cardMakanan;
        private TextView txtNama, txtHarga;

        public MakananViewHolder(View view){
            super(view);

            cardMakanan = (CardView) view.findViewById(R.id.cardMakanan);
            txtNama = (TextView) view.findViewById(R.id.textMakanan);
            txtHarga = (TextView) view.findViewById(R.id.tHarga);
        }
    }
}
