package com.example.quan_bui.countryinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        adapter = new PersonAdapter(new ArrayList<>());
        rv.setAdapter(adapter);

        service = retrofit.create(NetworkService.class);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Observable.from(personList)
                    .zipWith(service.getCountries(),
                             (person, countries) -> matchData(person, countries))
                    .subscribe(new Subscriber<Person>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Person person) {
                            adapter.add(person);
                        }
                    });

                //service.getCountries()
                //    .subscribeOn(Schedulers.newThread())
                //    .observeOn(AndroidSchedulers.mainThread())
                //    .flatMap(Observable::from)
                //    .zipWith(Observable.from(personList),
                //             (country, person) -> matchData(country, person))
                //    .subscribe(person -> adapter.add(person));
            }
        });
    }

    //public Person combine(Country country, Person person) {
    //    person.setCountryName(country.getName());
    //    return person;
    //}

    public Person matchData(Person person, List<Country> countries) {

        for (Country country : countries) {
            if (country.getCountryCode().equalsIgnoreCase(person.getCountryCode())) {
                person.setCountryName(country.getName());
            }
            Log.d("Match or not ? ",
                  "" + country.getCountryCode().equalsIgnoreCase(person.getCountryCode()));
        }
        return person;
    }
}
