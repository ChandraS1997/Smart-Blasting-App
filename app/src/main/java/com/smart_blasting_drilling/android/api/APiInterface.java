package com.smart_blasting_drilling.android.api;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface APiInterface {

    @Multipart
    @POST("v2/index.php")
    Call<JsonObject> loginApiCaller(@QueryMap Map<String, Object> queryMap, @PartMap Map<String, RequestBody> map);

    @GET("Retrieve3DDesignByDate/{start_time}/{end_time}/c3VzaGls/1/c3VzaGls/centralmineinfo")
    Call<JsonObject> retrieve3DDegignByDateApiCaller(@Path(value = "start_time", encoded = true) String startDate, @Path(value = "end_time", encoded = true) String endDate);

    @GET("RetrieveByDate/{start_time}/{end_time}/c3VzaGls/1/c3VzaGls/centralmineinfo")
    Call<JsonObject> retrieveByDateApiCaller(@Path(value = "start_time", encoded = true) String startDate, @Path(value = "end_time", encoded = true) String endDate);

}
