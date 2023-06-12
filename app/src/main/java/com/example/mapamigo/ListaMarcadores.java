package com.example.mapamigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaMarcadores extends AppCompatActivity {
    SQLiteDatabase bancoDados;
    ListView ListaMarcadores;
    ArrayList<Integer> ArrayIds;
    ArrayList<String> rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_marcadores);

        ListaMarcadores = (ListView) findViewById(R.id.ListaMarcadores);

        ListarMarcadores();

        ListaMarcadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int marcadorId = ArrayIds.get(position);
                Intent intent = new Intent(ListaMarcadores.this, PerfilMarcador.class);
                intent.putExtra("marcadorId", marcadorId);
                startActivity(intent);
            }
        });

        ListaMarcadores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int marcadorId = ArrayIds.get(position);
                Intent intent = new Intent(ListaMarcadores.this, Editar.class);
                intent.putExtra("id", marcadorId);
                startActivity(intent);
                return true;
            }
        });
    }

    public void ListarMarcadores(){
        try{
            ArrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("mapamigo", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id, apelido FROM MARCADOR", null);
            rows = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    rows
            );
            ListaMarcadores.setAdapter(adapter);
            cursor.moveToFirst();
            while(cursor != null){
                String row = cursor.getString(1);
                rows.add(row);
                ArrayIds.add(cursor.getInt(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
