package com.example.tugasakhirpam.model;

public class PelangganModel {

    /* Deklarasi data yang akan di simpan dan di ambil di dalam database */
    private  String id,namaPelanggan,no_tlp,namaMakanan,promo,jumlah,harga,total;

    public PelangganModel() {
    }

    /* Pembuatan constructor */
    public PelangganModel(String namaPelanggan, String no_tlp, String namaMakanan, String promo, String jumlah, String harga, String total) {
        this.namaPelanggan = namaPelanggan;
        this.no_tlp = no_tlp;
        this.namaMakanan = namaMakanan;
        this.promo = promo;
        this.jumlah = jumlah;
        this.harga = harga;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getNo_tlp() {
        return no_tlp;
    }

    public void setNo_tlp(String no_tlp) {
        this.no_tlp = no_tlp;
    }

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

