package com.example.aplikasikelontong;

public class Produk {
    public String userId;
    public String nama;
    public String harga;
    public String gambar;

    public Produk() {}

    public Produk(String userId, String nama, String harga, String gambar) {
        this.userId = userId;
        this.nama = nama;
        this.harga = harga;
        this.gambar = gambar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
