package com.example.tugasakhirpam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class DetailPesanan extends AppCompatActivity {

    /*Deklarasi Variable*/
    private TextView nama,tlp,nama_makanan,harga,promo,item,total;

    /*Firebase Firestore*/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        nama = findViewById(R.id.HNama2);
        tlp = findViewById(R.id.HTelp2);
        nama_makanan = findViewById(R.id.HMerk2);
        harga = findViewById(R.id.HHarga2);
        promo = findViewById(R.id.HPromo2);
        item = findViewById(R.id.HLamaSewa2);
        total = findViewById(R.id.HTotal2);

        progressDialog = new ProgressDialog(DetailPesanan.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengambil Data...");

        Intent intent = getIntent();

        if(intent != null){
            nama.setText(intent.getStringExtra("nama"));
            tlp.setText(intent.getStringExtra("no_tlp"));
            nama_makanan.setText(intent.getStringExtra("makanan"));
            harga.setText("Rp " +intent.getStringExtra("harga makanan"));
            item.setText(intent.getStringExtra("item") + " Item");
            promo.setText(intent.getStringExtra("promo") + " %");
            total.setText("Rp " +intent.getStringExtra("total"));
        }
    }
}