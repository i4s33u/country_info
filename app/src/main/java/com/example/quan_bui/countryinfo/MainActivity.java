package com.example.quan_bui.countryinfo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.example.quan_bui.countryinfo.adapter.CountryAdapter;
import com.jakewharton.rxbinding.view.RxView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity
    extends AppCompatActivity {

    public static final String BASE_URL = "https://restcountries.eu/rest/v1/";

    Retrofit retrofit;

    RxJavaCallAdapterFactory rxAdapter;

    RecyclerView rv;

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

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            fab.setVisibility(View.VISIBLE);
            service = retrofit.create(NetworkService.class);
            service.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> rv.setAdapter(new CountryAdapter(list)));
            Toast.makeText(getApplicationContext(),
                           "Received: " + rv.getHeight(),
                           Toast.LENGTH_LONG)
                .show();
        });

        //Using RxBindings library
        RxView.clicks(fab).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                fab.setVisibility(View.VISIBLE);
                service = retrofit.create(NetworkService.class);
                service.getCountries()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> rv.setAdapter(new CountryAdapter(list)));
                Toast.makeText(getApplicationContext(),
                               "Received: " + rv.getHeight(),
                               Toast.LENGTH_LONG).show();
            }
        });
    }
}
