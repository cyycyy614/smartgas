package com.techen.smartgas;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.retrofitutils.ItheimaHttp;
import com.techen.smartgas.model.SecurityBean;
import com.techen.smartgas.util.HttpService;

import org.itheima.recycler.L;
import org.json.JSONException;
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

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ItheimaHttp.init(this, "https://www.oschina.net/");
    }
}
