package com.example.mapamigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Editar extends AppCompatActivity {

    SQLiteDatabase bancoDados;
    Spinner spinnerCor;
    EditText EditarApelido;
    Button BtnEditar, BtnDeletar;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        EditarApelido = (EditText) findViewById(R.id.EditarApelido);
        bancoDados = openOrCreateDatabase("mapamigo", MODE_PRIVATE, null);
        Cursor cursor = bancoDados.rawQuery("SELECT apelido, endereco FROM MARCADOR WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            int apelidoColumnIndex = cursor.getColumnIndexOrThrow("apelido");
            String apelido = cursor.getString(apelidoColumnIndex);
            EditarApelido.setText(apelido);
        }
        cursor.close();
        bancoDados.close();

        BtnEditar = (Button) findViewById(R.id.BtnEditar);
        BtnDeletar = (Button) findViewById(R.id.BtnDeletar);
        spinnerCor = (Spinner) findViewById(R.id.spinnerCor);

        String[] cores = {"verde", "azul", "rosa"};

        BtnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Editar.this, MainActivity.class);
                atualizaDados(id, EditarApelido.getText().toString(), spinnerCor.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        BtnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Editar.this, MainActivity.class);
                deletarMarcador(id);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCor.setAdapter(adapter);
    }

    private void atualizaDados(int id, String apelidof, String corf) {
        try {
            bancoDados = openOrCreateDatabase("mapamigo", MODE_PRIVATE, null);
            ContentValues valores = new ContentValues();
            valores.put("apelido", apelidof);
            valores.put("cor", corf);
            String[] whereArgs = {String.valueOf(id)};
            bancoDados.update("MARCADOR", valores, "id=?", whereArgs);
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deletarMarcador(int id) {
        try {
            bancoDados = openOrCreateDatabase("mapamigo", MODE_PRIVATE, null);
            String[] whereArgs = {String.valueOf(id)};
            bancoDados.delete("MARCADOR", "id=?", whereArgs);
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}