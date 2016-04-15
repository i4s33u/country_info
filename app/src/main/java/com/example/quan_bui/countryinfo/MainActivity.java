package com.example.quan_bui.countryinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.example.quan_bui.countryinfo.adapter.PersonAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    List<Person> personList;

    List<Person> listToShow;

    PersonAdapter adapter;

    NetworkService service;

    ExecutorService executor;

    Button btnGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personList = new ArrayList<>();
        personList.add(new Person("Quan Bui", 25, "vn"));
        personList.add(new Person("Mohamed Abu", 29, "ae"));
        personList.add(new Person("Man from Venezuela", 34, "ve"));
        personList.add(new Person("Zhao Lee", 21, "tw"));

        executor = Executors.newSingleThreadExecutor();

        rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxAdapter)
            .build();

        btnGet = (Button) findViewById(R.id.btnGet);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        listToShow = new ArrayList<>();

        adapter = new PersonAdapter(listToShow);
        rv.setAdapter(adapter);

        service = retrofit.create(NetworkService.class);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Observable.from(personList)
                //    .subscribeOn(Schedulers.from(executor))
                //    .observeOn(AndroidSchedulers.mainThread())
                //    .zipWith(service.getCountries()
                //    .flatMap(Observable::from), (person, country) -> combine(country, person))
                //    .subscribe(newPerson -> listToShow.add(newPerson));

                service.getCountries()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(Observable::from)
                    .zipWith(Observable.from(personList),
                             (country, person) -> combine(country, person))
                    .subscribe(new Subscriber<Person>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Person newPerson) {
                            listToShow.add(newPerson);
                            adapter.notifyItemInserted(adapter.getItemCount() + 1);
                        }
                    });
            }
        });
    }

    public Person combine(Country country, Person person) {
        Person combinedPerson = new Person();
        //if (person.getCountryCode().equalsIgnoreCase(country.getCountryCode())) {
        combinedPerson.setName(person.getName());
        combinedPerson.setAge(person.getAge());
        combinedPerson.setCountryCode(person.getCountryCode());
        combinedPerson.setCountryName(country.getName());
        //}
        return combinedPerson;
    }
}
