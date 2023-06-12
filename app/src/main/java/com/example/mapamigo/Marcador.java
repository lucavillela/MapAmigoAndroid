package com.example.mapamigo;

import com.google.android.gms.maps.model.LatLng;

public class Marcador {
    int id;
    String endereco;
    String apelido;
    double latitude;
    double longitude;
    LatLng latLng;
    String cor;

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng() {
        this.latLng = new LatLng(this.getLatitude(), this.getLongitude());
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
