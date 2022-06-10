package com.techen.smartgas.util;

import static com.alibaba.fastjson.JSON.parseObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techen.smartgas.login.LoginActivity;
import com.techen.smartgas.model.SecurityTempBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tool {
    /**
     * 对象转换成json字符串
     * @param obj
     * @return
     */
    public static String toJson(Object obj){

        Gson gson = new Gson();

        return gson.toJson(obj);
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * json字符串转成数组
     * @param str
     * @param clazz
     * @return
     */
    public static <T> List<T>  toArray(String str, Class clazz) {
        Gson gson = new Gson();
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = gson.fromJson(str, type);
        return list;
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    /**
     * 得到json文件中的内容
     * @param context
     * @param fileName
     * @return
     */
    public static String getAsstesJson(Context context, String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName),"utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 对象转化为json fastjson 使用方式
     *
     * @return
     */
    public static String objectToJson(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return JSON.toJSONString(object);
        } catch (JSONException e) {
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * json转化为对象  fastjson 使用方式
     *
     * @return
     */
    public static <T> T jsonToObject(String jsonData, Class<T> clazz) {
        T t = null;
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }
        try {
            t = parseObject(jsonData, clazz);
        } catch (Exception e) {
            Log.i("to", String.valueOf(e));
        }
        return t;
    }

    public static <T> T jsonToObject1(final String json, final Type typeOfT) {
        if (json != null) {
            try {
                return JSON.parseObject(json, typeOfT);
            } catch (Exception e) {
               Log.i("to", String.valueOf(e));
            }
        }
        return null;
    }

    /**
     * json转化为List<Person>或List<String>数据  fastjson 使用方式
     *
     * @param jsonData
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToArray(String jsonData, Class<T> clazz) {
        List<T> list = null;
        try {
            list = JSON.parseArray(jsonData, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * json转化为List  fastjson 使用方式
     */
    public static List jsonToList(String jsonData) {
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }
        List arrayList = null;
        try {
            arrayList = parseObject(jsonData, new TypeReference<ArrayList>() {
            });
        } catch (Exception e) {
        }
        return arrayList;
    }


    /**
     * json转化为Map  fastjson 使用方式
     */
    public static Map jsonToMap(String jsonData) {
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }
        Map map = null;
        try {
            map = parseObject(jsonData, new TypeReference<Map>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        return width;
    }

    public static void Logout(Context context){
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

    public static String getToken(Context context){
        String token = (String) SharedPreferencesUtil.get(context,"token","");
//        token = "8c66780d-ee90-4909-bcf6-c71bf1d5822e";
        return token;
    }
    public static void setToken(Context context,String token){
        SharedPreferencesUtil.put(context,"token",token);
    }

    public static String getUserId(Context context){
        String userid = (String) SharedPreferencesUtil.get(context,"userid","");
        userid = "";
        return userid;
    }
}
