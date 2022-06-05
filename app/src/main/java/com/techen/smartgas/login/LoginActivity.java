package com.techen.smartgas.login;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.*;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.ItheimaHttp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import com.itheima.retrofitutils.listener.HttpResponseListener;

import android.widget.EditText;
import android.widget.Button;
import com.techen.smartgas.R;
import com.techen.smartgas.model.TestBean;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.util.Tool;


import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import butterknife.ButterKnife;
import retrofit2.http.HTTP;

public class LoginActivity extends AppCompatActivity{
    private EditText login_username;
    private EditText login_password;
    private Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first);

        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(mListener);
        ButterKnife.bind(this);

        //设置监听
        login_username.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if(login_username.getText().length() > 0 && login_password.getText().length() > 2){
                    login_button.setBackgroundColor(Color.parseColor("#0099ff"));
                    login_button.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        login_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if(login_username.getText().length() > 0 && login_password.getText().length() > 2){
                    login_button.setBackgroundColor(Color.parseColor("#0099ff"));
                    login_button.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
//        setTitle("安检计划");
        // 调用列表初始化及接口
    }

    OnClickListener mListener = new OnClickListener() {
        //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_button:                          //登录界面的登录按钮
                    getData();
                    break;
            }
        }
    };
    public void getData(){
        String userName = login_username.getText().toString().trim();    //获取当前输入的用户名和密码信息
        String userPwd = login_password.getText().toString().trim();
        RequestUtils request = new RequestUtils();
        // get请求
//        Map<String,Object> getParams = new HashMap<>();
//        getParams.put("handle","test");
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("type","mobile");
//            paramObject.put("password","E10ADC3949BA59ABBE56E057F20F883E");
//            paramObject.put("username","admin");
            String newUsePwd = getMD5(userPwd);
            paramObject.put("username",userName);
            paramObject.put("password",newUsePwd);
//            paramObject.put("password","E10ADC3949BA59ABBE56E057F20F883E");
//            paramObject.put("username","mjwh");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.post("amiwatergas/auth/login",paramObject,false,LoginActivity.this, new HttpResponseListener<TestBean>() {
            @Override
            public void onResponse(TestBean testBean, Headers headers) {
                if(testBean.getCode() == 200){
                    if(testBean.getSuccess() == false){
                        Toast.makeText(LoginActivity.this, testBean.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String token =testBean.getResult().getToken();
                    if(TextUtils.isEmpty(token)){
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                Object result =testBean.getResult()
                    String role =testBean.getResult().getRoles();
                    commit(token,role);
                    if ("security".equals(role) ) {
                        Intent i = new Intent(LoginActivity.this, IndexActivity.class);
                        startActivity(i);
                    }else if("repairman".equals(role)){
                        Intent i = new Intent(LoginActivity.this, IndexRepairmanActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText(LoginActivity.this, "非安检员或维修工权限", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                System.out.println("print data -- " +  e);
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void commit(String token,String roles){
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        //获取editor对象
        SharedPreferences.Editor editor = preferences.edit();
        //存储数据时选用对应类型的方法
        editor.putString("token", token);
        editor.putString("roles", roles);
        //提交保存数据
        editor.commit();
        Tool.setToken(LoginActivity.this,token);
    }

    public String getMD5(String info)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++)
            {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1)
                {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                }
                else
                {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            return "";
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }

}
