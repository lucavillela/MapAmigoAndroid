package com.example.mapamigo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    SQLiteDatabase bancoDados;
    private SearchView InputEndereco;
    private ListView ListaResultados;
    private Geocoder geocoder;
    private List<Address> addressList = new ArrayList<>(3);
    private List<String> stringedAddressList;
    private ArrayAdapter<String> adapter;
    private String apelido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        InputEndereco = findViewById(R.id.InputEndereco);
        ListaResultados = findViewById(R.id.ListaResultados);
        geocoder = new Geocoder(this);
        stringedAddressList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringedAddressList);
        ListaResultados.setAdapter(adapter);

        ListaResultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
                builder.setTitle("Apelido");
                builder.setMessage("Digite o apelido do endereço que vai apareçer no mapa.");

                final EditText input = new EditText(Search.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apelido = input.getText().toString();
                        criarMarcador(apelido, i);
                        Intent intent = new Intent(Search.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        InputEndereco.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s)) {
                    new GeocodeTask().execute(s);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
    }

    private void criarMarcador(String apelidof, int indexLista) {
        try {
            bancoDados = openOrCreateDatabase("mapamigo", MODE_PRIVATE, null);
            ContentValues valores = new ContentValues();
            valores.put("apelido", apelidof);
            valores.put("endereco", addressList.get(indexLista).getAddressLine(indexLista).toString());
            valores.put("latitude", String.valueOf(addressList.get(indexLista).getLatitude()));
            valores.put("longitude", String.valueOf(addressList.get(indexLista).getLongitude()));
            valores.put("cor", "verde");
            bancoDados.insert("MARCADOR", null, valores);
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GeocodeTask extends AsyncTask<String, Void, List<Address>> {
        @Override
        protected List<Address> doInBackground(String... params) {
            String addressQuery = params[0];
            try {
                return geocoder.getFromLocationName(addressQuery, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if (addresses != null) {
                stringedAddressList.clear();
                for (int i = 0; i < addresses.size(); i++) {
                    Address address = addresses.get(i);
                    String legal = address.getAddressLine(i);
                    stringedAddressList.add(legal);
                }
                addressList = addresses;
                adapter.notifyDataSetChanged();
            }
        }
    }
}