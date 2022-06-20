package com.example.tugasakhirpam.model;

public class MakananModel {

    private String id,nama_makanan,harga;

    public MakananModel() {
    }

    public MakananModel(String id, String nama_makanan, String harga) {
        this.id = id;
        this.nama_makanan = nama_makanan;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_makanan() {
        return nama_makanan;
    }

    public void setNama_makanan(String nama_makanan) {
        this.nama_makanan = nama_makanan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}

