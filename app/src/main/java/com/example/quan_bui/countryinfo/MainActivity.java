package com.example.quan_bui.countryinfo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;
import com.example.quan_bui.countryinfo.adapter.CountryAdapter;
import com.jakewharton.rxbinding.view.RxView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity
    extends AppCompatActivity {

    public static final String BASE_URL = "https://restcountries.eu/rest/v1/";

    Retrofit retrofit;

    RxJavaCallAdapterFactory rxAdapter;

    RecyclerView rv;

    List<Country> localCountryList;
    List<Country> asiaCountryList;

    CountryAdapter adapter;

    NetworkService service;

    FloatingActionButton fab;
    Button btnAsia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxAdapter)
            .build();

        localCountryList = new ArrayList<>();
        asiaCountryList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        btnAsia = (Button) findViewById(R.id.btnAsia);

        adapter = new CountryAdapter(localCountryList);
        rv.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            service = retrofit.create(NetworkService.class);
            service.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(list -> Observable.from(list))
                .map(country -> localCountryList.add(country))
                .subscribe(value -> adapter.notifyDataSetChanged());

            Toast.makeText(getApplicationContext(),
                           "Received: " + rv.getHeight(), Toast.LENGTH_LONG).show();
        });

        //Using RxBindings library to set Observable click listener
        RxView.clicks(btnAsia).subscribe(aVoid -> {
            Observable.from(localCountryList)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(country -> country.getRegion().equalsIgnoreCase("asia"))
                .map(asiaCountry -> asiaCountryList.add(asiaCountry))
                .subscribe(list -> rv.setAdapter(new CountryAdapter(asiaCountryList)));
        });
    }
}
