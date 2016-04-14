package com.example.quan_bui.countryinfo;

import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Quan Bui on 4/14/16.
 */
public interface NetworkService {

    @GET("all/")
    Observable<List<Country>> getCountries();
}
