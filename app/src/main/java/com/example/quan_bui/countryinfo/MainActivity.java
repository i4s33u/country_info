package com.example.quan_bui.countryinfo;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.quan_bui.countryinfo.adapter.CountryAdapter;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity
    extends AppCompatActivity {

    public static final String BASE_URL = "https://restcountries.eu/rest/v1/";

    Retrofit retrofit;

    RxJavaCallAdapterFactory rxAdapter;

    RecyclerView rv;
    CountryAdapter adapter;

    List<Country> countries;

    NetworkService service;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxAdapter)
            .build();

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //countries = new ArrayList<>();
        //adapter = new CountryAdapter(countries);
        //rv.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            service = retrofit.create(NetworkService.class);
            service.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> rv.setAdapter(new CountryAdapter(list)));
        });
    }
}
