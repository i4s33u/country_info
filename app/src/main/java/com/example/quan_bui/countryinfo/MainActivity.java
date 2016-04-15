package com.example.quan_bui.countryinfo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;
import com.example.quan_bui.countryinfo.adapter.CountryAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
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

    ExecutorService executor;
    ExecutorService anotherExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executor = Executors.newSingleThreadExecutor();
        anotherExecutor = Executors.newSingleThreadExecutor();

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

        adapter = new CountryAdapter(new ArrayList<>());
        rv.setAdapter(adapter);

        service = retrofit.create(NetworkService.class);

        fab.setOnClickListener(v -> {

            service.getCountries()
                .subscribeOn(Schedulers.from(executor))
                .flatMap(list -> Observable.from(list))
                .subscribe(country -> localCountryList.add(country));
        });

        btnAsia.setOnClickListener(v -> {

            Observable.from(localCountryList)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.from(anotherExecutor))
                .filter(country -> country.getRegion().equalsIgnoreCase("asia"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Country>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Country country) {
                        adapter.add(country);
                    }
                });
        });
    }
}
