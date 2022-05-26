package com.techen.smartgas.util;

import com.google.gson.Gson;
import com.itheima.retrofitutils.listener.HttpResponseListener;

import org.itheima.recycler.L;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RequestUtils {
    HttpService service;
    Map<String,String> headers = new HashMap<>();;
    RequestBody requestBody;
    public HttpService newstance(){
        if(service == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HttpService.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(HttpService.class);
        }
        return service;
    }

    public <T> void get(String url, Map<String,Object> getParams,Boolean isNeedToken, final HttpResponseListener<T> httpResponseListener){
        // get请求  "amiwatergas/mobile/securityTemplate/dataCodeList"
        if(isNeedToken){
            addHeaders();
        }
        if(service == null){
            newstance();
        }
        service.get("amiwatergas/mobile/securityTemplate/dataCodeList",headers,getParams).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody body;
                    if(response.body() == null) {
                        if(response.errorBody() == null){
                            L.i("response data: null" );
                            return;
                        }else {
                            body = response.errorBody();
                        }
                    } else {
                        body = response.body();
                    }
                    String json = body.string();
                    if (!String.class.equals(httpResponseListener.getType())) {
                        Gson gson = new Gson();
                        T t = gson.fromJson(json, httpResponseListener.getType());
//                        try {
//                            Class clz = Class.forName(t.getClass().getName());
//                            Method getVal = clz.getMethod("getCode");
//                            String code =  getVal.invoke(t).toString();
//                            if(code.toString().equals("400")) {
//                                L.i("test");
//                                // 调用退出登录公用方法
//                            }
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
                        httpResponseListener.onResponse(t, response.headers());
                    } else {
                        httpResponseListener.onResponse((T) json, response.headers());
                    }
                } catch (Exception e) {
                    L.e("Http Exception:", e);
                    httpResponseListener.onFailure(call, e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                httpResponseListener.onFailure(call, t);
//                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                    return;
            }
        });
    }


    public <T> void post(String url,JSONObject paramObject,Boolean isNeedToken, final HttpResponseListener<T> httpResponseListener){
        // post请求  "amiwatergas/auth/login"
        if(isNeedToken){
            addHeaders();
        }
        if(service == null){
            newstance();
        }
        requestBody = RequestBody.create(MediaType.parse("application/json"), paramObject.toString());
        service.post("amiwatergas/auth/login",headers,requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body() == null) {
                        L.i("response data: null" );
                        return;
                    }
                    String json = response.body().string();
                    if (!String.class.equals(httpResponseListener.getType())) {
                        Gson gson = new Gson();
                        T t = gson.fromJson(json, httpResponseListener.getType());
                        httpResponseListener.onResponse(t, response.headers());
                    } else {
                        httpResponseListener.onResponse((T) json, response.headers());
                    }
                } catch (Exception e) {
                    L.e("Http Exception:", e);
                    httpResponseListener.onFailure(call, e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                httpResponseListener.onFailure(call, t);
//                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                    return;
            }
        });
    }

    private void addHeaders(){
        // 获取token，登录后从存储里面获取，增加一个公用的存储方法
        String token = "9ceefe52-e4c6-4a50-9d6e-c44c9e011b0a";
        headers.put("Authorization","Bearer " + token);
        headers.put("lang","en");
    }

}
