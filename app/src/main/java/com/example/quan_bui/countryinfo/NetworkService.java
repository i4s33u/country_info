package com.example.quan_bui.countryinfo;

import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Quan Bui on 4/14/16.
 */
public interface NetworkService {

    @GET("all/")
    Observable<List<Country>> getCountries();

    @PUT("{code}")
    Observable<List<Country>> searchByCode(@Path("code") String code);
}
