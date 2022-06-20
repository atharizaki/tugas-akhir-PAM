package com.example.tugasakhirpam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhirpam.model.MakananModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PesanMakanan extends AppCompatActivity {

    /* Deklarasi variable Yang ada di dalam Layaout Pesan Makanan*/
    private EditText nPelanggan, no_tlp, jmlPemesanan;
    private Spinner spinner;
    private RadioButton free1,free2,free3;
    private Button Selesai;

    /*FirebaseFireStore*/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private String id = "";

    /*Spinner*/
    // Pemanggil Database My SQL
    private static String URL_spinner = "https://20200140143.praktikumtiumy.com/TugasAkhir/bacaMakanan.php";
    private static final String TAG_nama_makanan = "nama_makanan";
    private ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    /* Variable Hitung */
    private String sMakanan,sJumlah;
    private int iHarga, iPromo, iJumlah;
    private double dTotal, dPromo;

    /* variable Convert data*/
    private String price,diskon,jumlahAkhir,jumlahPemesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_makanan);

        /* Inisialisasi Variable sesuai dengan Id di Layout Pesan Makanan */
        nPelanggan = findViewById(R.id.eTNama);
        no_tlp = findViewById(R.id.eTHP);
        spinner = findViewById(R.id.spinner);
        free1 = findViewById(R.id.rbWeekDay);
        free2 = findViewById(R.id.rbWeekEnd);
        free3 = findViewById(R.id.rbNoting);
        jmlPemesanan = findViewById(R.id.eTJmlhMkn);
        Selesai = findViewById(R.id.selesaiHitung);

        /* Penggunaan Popup Loading */
        progressDialog = new ProgressDialog(PesanMakanan.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Transaksi Berhasil...");

        /* Pemanggilan Data Spinner dari database MySql*/
        loadDataSpinner();


        /* Event Klik Button Selesai untuk proses Create dan menyimpan data ke dalam firebase firestore */
        Selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sJumlah = jmlPemesanan.getText().toString();

                /* Membuat Kondisi di mana data harus di isi semua */
                if(nPelanggan.getText().length() > 0 && no_tlp.getText().length() > 0 && jmlPemesanan.getText().length() > 0){

                    /* Pembuatan Promo radio Button*/
                    if(free1.isChecked()){
                        dPromo = 0.1;
                    }
                    else if(free2.isChecked()){
                        dPromo = 0.25;
                    }
                    else if(free3.isChecked()){
                        dPromo = 0;
                    }

                    /* Proses Menetapkan Harga Makanan melalai spinner*/
                    if(sMakanan.equals("Nasi Goreng")){
                        iHarga = 15000;
                    }
                    else if (sMakanan.equals("Ayam Penyet")){
                        iHarga = 18000;
                    }
                    else if (sMakanan.equals("Mie Goreng")){
                        iHarga = 12000;
                    }
                    else if (sMakanan.equals("Ayam Geprek")){
                        iHarga = 16000;
                    }
                    else if (sMakanan.equals("Ayam Bakar")){
                        iHarga = 17000;
                    }
                    else if (sMakanan.equals("Lele Goreng")){
                        iHarga = 20000;
                    }
                    else if (sMakanan.equals("Kwetiau")){
                        iHarga = 10000;
                    }
                    else if (sMakanan.equals("Tahu,Tempe")){
                        iHarga = 5000;
                    }



                    /*Proses Perhitungan*/
                    // mengambil data jumlah item makanan
                    iJumlah = Integer.parseInt(sJumlah);
                    // proses penentuan promo makanan
                    iPromo = (int) (dPromo * 100);
                    // proses perhitngan total makanan
                    dTotal = (iHarga * iJumlah) - (iHarga * iJumlah * dPromo);

                    /*Convert data int dan double menjadi String supaya bisa di tampilkan
                     * dan di simpan ke dalam firebase firestore
                     * */
                    price = String.valueOf(iHarga);
                    jumlahPemesanan = String.valueOf(iJumlah);
                    diskon = String.valueOf(iPromo);
                    jumlahAkhir = String.valueOf(dTotal);

                    /*Proses Create Data dan Di simpan ke dalam Firebase Firestore*/
                    createData(nPelanggan.getText().toString(), no_tlp.getText().toString(), sMakanan, diskon, price, jumlahPemesanan, jumlahAkhir);
                }
                else {
                    Toast.makeText(PesanMakanan.this, "Silahkan Isi Semua Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*Pengambil data dari Firebase Firestore untuk di Edit Data nya*/
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getStringExtra("id");
            nPelanggan.setText(intent.getStringExtra("nama"));
            no_tlp.setText(intent.getStringExtra("no_tlp"));
            jmlPemesanan.setText(intent.getStringExtra("item"));

        }
    }

    /* Proses Create Database */
    private void createData(String namaPelanggan, String tlp, String sMakanan, String diskon, String price, String jumlahPemesanan, String jumlahAkhir) {
        /*Penggunaan HashMap*/
        Map<String, Object> makanan = new HashMap<>();

        /*Proses Input database*/
        makanan.put("nama", namaPelanggan);
        makanan.put("no_tlp", tlp);
        makanan.put("makanan", sMakanan);
        makanan.put("harga makanan", price);
        makanan.put("promo", diskon);
        makanan.put("item", jumlahPemesanan);
        makanan.put("total", jumlahAkhir);

        progressDialog.show();

        if (id != null) {
            /**
             *  kode untuk edit data firestore dengan mengambil id
             */
            db.collection("Pelanggan").document(id)
                    .set(makanan)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PesanMakanan.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(PesanMakanan.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            /**
             * Code untuk menambahkan data dengan add
             */
            db.collection("Pelanggan")
                    .add(makanan)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(PesanMakanan.this, "Berhasil di simpan", Toast.LENGTH_SHORT).show();
                            Intent pindah = new Intent(getApplicationContext(), Home.class);
                            startActivity(pindah);
                            progressDialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PesanMakanan.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    /* Mengambil nilai data yang ada di MYSQL untuk di tampilkan ke dalam spinner */
    private void loadDataSpinner() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_spinner, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                /* Proses Menampilkan Nama Makanan */
                for (int i =0; i < response.length(); i++){

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        MakananModel item = new MakananModel();

                        item.setId(jsonObject.getString(TAG_nama_makanan));

                        String nama = jsonObject.optString(TAG_nama_makanan);

                        list.add(nama);
                        arrayAdapter = new ArrayAdapter<>(PesanMakanan.this, android.R.layout.simple_spinner_item, list);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                sMakanan = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}