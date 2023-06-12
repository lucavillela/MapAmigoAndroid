package com.example.mapamigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap map;
    private FloatingActionButton BtnPesquisar, BtnBuscarMarcadores;
    private SQLiteDatabase bancoDados;
    float cor = BitmapDescriptorFactory.HUE_GREEN;
    List<Marcador> Marcadorest = new ArrayList<>();
    Cor verde = new Cor();
    Cor rosa = new Cor();
    Cor azul = new Cor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rosa.setNome("rosa");
        rosa.setNumero(BitmapDescriptorFactory.HUE_MAGENTA);

        verde.setNome("verde");
        verde.setNumero(BitmapDescriptorFactory.HUE_GREEN);

        azul.setNome("azul");
        azul.setNumero(BitmapDescriptorFactory.HUE_BLUE);

        BtnPesquisar = (FloatingActionButton) findViewById(R.id.BtnPesquisar);
        BtnBuscarMarcadores = (FloatingActionButton) findViewById(R.id.BtnBuscarMarcadores);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BtnBuscarMarcadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListaMarcadores.class);
                startActivity(intent);
            }
        });

        BtnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });
    }
    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("mapamigo", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS MARCADOR(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", apelido VARCHAR(50)" +
                    ", endereco VARCHAR(200)" +
                    ", latitude VARCHAR(50)" +
                    ", longitude VARCHAR(50)" +
                    ", cor VARCHAR(50))");
            bancoDados.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        Marcadorest.clear();
        criarBancoDados();
        try {
            bancoDados = openOrCreateDatabase("mapamigo", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id, apelido, endereco, latitude, longitude, cor FROM MARCADOR", null);
            if (cursor.moveToFirst()) {
                do {
                    Marcador m = new Marcador();
                    String apelidot = cursor.getString(1);
                    m.setApelido(apelidot);
                    String enderecot = cursor.getString(2);
                    m.setEndereco(enderecot);
                    String latitudet = cursor.getString(3);
                    m.setLatitude(Double.parseDouble(latitudet));
                    String longitudet = cursor.getString(4);
                    m.setLongitude(Double.parseDouble(longitudet));
                    m.setLatLng();
                    String cort = cursor.getString(5);
                    m.setCor(cort);
                    int idt = cursor.getInt(0);
                    m.setId(idt);
                    Marcadorest.add(m);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if(Marcadorest.size() > 0){
            for(int i = 0; i < Marcadorest.size(); i++){
                BitmapDescriptor markerIcon = BitmapDescriptorFactory.defaultMarker(retornaCor(Marcadorest.get(i).getCor()));
                map.addMarker(new MarkerOptions()
                        .position(Marcadorest.get(i).getLatLng())
                        .title(Marcadorest.get(i).getApelido())
                        .snippet(Marcadorest.get(i).getEndereco())
                        .icon(markerIcon)).setTag(Marcadorest.get(i).getId());
            }
        }
        map.setOnInfoWindowClickListener(this);
    }
    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Intent intent = new Intent(MainActivity.this, Editar.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", Integer.parseInt(marker.getTag().toString()));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public float retornaCor(String cor){
        switch (cor) {
            case "verde":
                return verde.getNumero();

            case "azul":
                return azul.getNumero();

            case "rosa":
                return rosa.getNumero();

            default:
                System.out.println("ssssss");
                break;
        }
        return 0;
    }
}