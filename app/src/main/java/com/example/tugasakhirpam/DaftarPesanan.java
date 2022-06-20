package com.example.tugasakhirpam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tugasakhirpam.adapter.PelangganAdapter;
import com.example.tugasakhirpam.model.PelangganModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DaftarPesanan extends AppCompatActivity {

    /*Deklarasi variable*/
    private Button tambah;
    private RecyclerView recyclerView;

    // Penggunaan Firebase Firestore untuk meilihat,delete, dan edit
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<PelangganModel> list = new ArrayList<>();
    private PelangganAdapter pelangganAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pesanan);

        // Ini sialisasi variable dengan ID nya masing sesuai dengan layout Daftar Pesanan
        tambah = findViewById(R.id.btnTambah);
        recyclerView = findViewById(R.id.daftarPesanan);

        // Penggunan Diaglong Loading
        progressDialog = new ProgressDialog(DaftarPesanan.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil data...");
        pelangganAdapter = new PelangganAdapter(getApplicationContext(), list);

        // Button Pindah Halaman Pesan Makanan
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pesanan = new Intent(getApplicationContext(), PesanMakanan.class);
                startActivity(pesanan);
            }
        });

        // Penggunaan Firebase Firestore untuk Edit, Delete, Lihat Data
        // Event klik pada Firebase
        pelangganAdapter.setDialog(new PelangganAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit Data", "Delete", "Lihat Data"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(DaftarPesanan.this);

                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){

                            /* Pilihan untuk Edit Data */
                            case 0:
                                Intent edit = new Intent(getApplicationContext(),PesanMakanan.class);
                                edit.putExtra("id", list.get(pos).getId());
                                edit.putExtra("nama",list.get(pos).getNamaPelanggan());
                                edit.putExtra("no_tlp", list.get(pos).getNo_tlp());
                                edit.putExtra("item", list.get(pos).getJumlah());
                                startActivity(edit);
                                break;

                            /* Pilihan Untuk Hapus Data */
                            case 1:
                                deleteData(list.get(pos).getId());
                                break;
                            case 2:
                                Intent lihat = new Intent(getApplicationContext(), DetailPesanan.class);
                                lihat.putExtra("id",list.get(pos).getId());
                                lihat.putExtra("nama",list.get(pos).getNamaPelanggan());
                                lihat.putExtra("no_tlp", list.get(pos).getNo_tlp());
                                lihat.putExtra("makanan", list.get(pos).getNamaMakanan());
                                lihat.putExtra("harga makanan", list.get(pos).getHarga());
                                lihat.putExtra("item", list.get(pos).getJumlah());
                                lihat.putExtra("promo", list.get(pos).getPromo());
                                lihat.putExtra("total",list.get(pos).getTotal());
                                startActivity(lihat);
                        }
                    }
                });
                dialog.show();
            }
        });

        /* Menampilkan Data Pemesanan yang tersimpan di dalam firebase firestore
         *  dengan adapater
         * */
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(pelangganAdapter);
    }

    /* Bagian Delete Database */
    private void deleteData(String id) {
        progressDialog.show();
        // Nama kolom database adalaha Pelanggan
        db.collection("Pelanggan").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(DaftarPesanan.this, "Data Gagal Di hapus", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(DaftarPesanan.this, "Data Berhasil Di Hapus", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        getData();
                    }
                });
    }

    /*Membuat data tampil di halaman info Daftar Pemesanan*/
    @Override
    protected void onStart(){
        super.onStart();
        getData();
    }

    /* Method Menggambil Data */
    private void getData() {
        progressDialog.show();

        db.collection("Pelanggan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();

                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                PelangganModel pelangganModel = new PelangganModel(document.getString("nama"),
                                        document.getString("no_tlp"), document.getString("makanan"), document.getString("promo"),
                                        document.getString("item"), document.getString("harga makanan"), document.getString("total"));
                                pelangganModel.setId(document.getId());
                                list.add(pelangganModel);
                            }
                            pelangganAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(DaftarPesanan.this, "Data Gagal di Ambil", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

}