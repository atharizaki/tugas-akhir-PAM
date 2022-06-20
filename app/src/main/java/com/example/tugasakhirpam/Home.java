package com.example.tugasakhirpam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    /* Deklarasi Variable yang ada di dalam layaout Home */
    private Button Menu, pesan, LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* Inisialisasi variable dengan Id nya masing" di layout Home*/
        Menu = findViewById(R.id.btnMenu);
        pesan = findViewById(R.id.btnDaftarPesanan);
        LogOut = findViewById(R.id.btnLogOut);

        /* Efek Klik pada Buttun Menu kemudian pindah ke halaman Menu Makanan */
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuMakanan.class);
                startActivity(intent);
            }
        });

        /* Efek Klik pada Button Daftar Pesanan dan pindah ke halaman Daftar Pesanan */
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(getApplicationContext(), DaftarPesanan.class);
                startActivity(data);
            }
        });

        /*Log Out*/
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(log);
            }
        });
    }
}