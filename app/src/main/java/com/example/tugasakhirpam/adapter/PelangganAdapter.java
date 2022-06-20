package com.example.tugasakhirpam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasakhirpam.R;
import com.example.tugasakhirpam.model.PelangganModel;

import java.util.List;

public class PelangganAdapter extends RecyclerView.Adapter<PelangganAdapter.PelanggabViewHolder> {

    private Context context;
    private List<PelangganModel> list;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public PelangganAdapter(Context context, List<PelangganModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PelanggabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_makanan,parent,false);
        return new PelanggabViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PelanggabViewHolder holder, int position) {
        holder.nama.setTextSize(20);
        holder.total.setTextSize(15);

        holder.nama.setTextColor(Color.BLUE);
        holder.total.setTextColor(Color.BLACK);

        holder.nama.setText(list.get(position).getNamaPelanggan());
        holder.total.setText("Rp "+list.get(position).getTotal());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PelanggabViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView nama, total;

        public PelanggabViewHolder(@NonNull View itemView){
            super(itemView);

            cardView = itemView.findViewById(R.id.cardMakanan);
            nama = itemView.findViewById(R.id.textMakanan);
            total = itemView.findViewById(R.id.tHarga);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog != null){
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}

