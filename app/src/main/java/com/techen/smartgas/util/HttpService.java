package com.techen.smartgas.util;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface HttpService {
    String BASE_URL = "http://10.10.5.209:8012/";

    @Headers({"Content-Type: application/json"})
    @GET
    Call<ResponseBody> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST
    Call<ResponseBody> post(@Url String url, @HeaderMap Map<String, String> headers,@Body RequestBody body);
}
