package com.example.mapamigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private SearchView InputEndereco;
    private ListView ListaResultados;
    private Geocoder geocoder;
    private List<Address> addressList = new ArrayList<>(3);
    private List<String> stringedAddressList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        InputEndereco = findViewById(R.id.InputEndereco);
        ListaResultados = findViewById(R.id.ListaResultados);
        geocoder = new Geocoder(this);
        stringedAddressList = new ArrayList<>(); // Inicializa a lista
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringedAddressList);
        ListaResultados.setAdapter(adapter);

//        ListaResultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                Bundle bundle = new Bundle();
//
//                double lat = addressList.get(i-1).getLatitude();
//                double lon = addressList.get(i-1).getLongitude();
//
//                bundle.putDouble("chave1", lat);
//                bundle.putDouble("chave2", lon);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

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
            } else {
                // Tratar o caso em que a lista de endereços é nula ou ocorreu um erro
            }
        }
    }
}