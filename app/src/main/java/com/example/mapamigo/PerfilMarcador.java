package com.example.mapamigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PerfilMarcador extends AppCompatActivity {

    TextView TituloApelido, DescricaoEndereco;
    Button BtnCopiarClip;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_marcador);

        TituloApelido = findViewById(R.id.TituloApelido);
        DescricaoEndereco = findViewById(R.id.DescricaoEndereco);
        BtnCopiarClip = findViewById(R.id.BtnCopiarClip);

        int marcadorId = getIntent().getIntExtra("marcadorId", 0);

        bancoDados = openOrCreateDatabase("mapamigo", MODE_PRIVATE, null);
        Cursor cursor = bancoDados.rawQuery("SELECT apelido, endereco FROM MARCADOR WHERE id = ?", new String[]{String.valueOf(marcadorId)});
        if (cursor.moveToFirst()) {
            int apelidoColumnIndex = cursor.getColumnIndexOrThrow("apelido");
            int enderecoColumnIndex = cursor.getColumnIndexOrThrow("endereco");

            String apelido = cursor.getString(apelidoColumnIndex);
            String endereco = cursor.getString(enderecoColumnIndex);

            TituloApelido.setText(apelido);
            DescricaoEndereco.setText(endereco);

            BtnCopiarClip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("descricao_endereco", endereco);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(PerfilMarcador.this, "Conteúdo copiado para o clipboard", Toast.LENGTH_SHORT).show();
                }
            });
        }
        cursor.close();
        bancoDados.close();
    }
}





