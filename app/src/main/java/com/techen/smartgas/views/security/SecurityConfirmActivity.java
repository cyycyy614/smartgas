package com.techen.smartgas.views.security;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;
import com.techen.smartgas.login.LoginActivity;
import com.techen.smartgas.model.NormalBean;
import com.techen.smartgas.model.SecurityResultBean;
import com.techen.smartgas.util.LoadingDialog;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.util.SharedPreferencesUtil;
import com.techen.smartgas.util.Tool;


import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SecurityConfirmActivity extends AppCompatActivity {

    @BindView(R.id.label_checkbox)
    TextView labelCheckbox;
    @BindView(R.id.checkbox_isrecheck)
    CheckBox checkboxIsrecheck;
    @BindView(R.id.layout_checkbox)
    LinearLayout layoutCheckbox;
    @BindView(R.id.radio_user)
    RadioButton radioUser;
    @BindView(R.id.radio_edit)
    RadioButton radioEdit;
    @BindView(R.id.btn_check)
    Button btnCheck;

    String plan_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_confirm);
        ButterKnife.bind(this);
        setTitle("安检确认");

        //接受参数
        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");

        btnCheck.setOnClickListener(mListener);
        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_check:

                    if(radioUser.isChecked() == false){
                        Toast.makeText(SecurityConfirmActivity.this, "请勾选用户告知", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(radioEdit.isChecked() == false){
                        Toast.makeText(SecurityConfirmActivity.this, "请勾选整改告知", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String securityStr = (String) SharedPreferencesUtil.get(SecurityConfirmActivity.this,"securitydata","{}");
                    SecurityResultBean resultBean = Tool.jsonToObject1(securityStr,SecurityResultBean.class);
                    if(resultBean != null){
                        resultBean.setRepetitionFlag(checkboxIsrecheck.isChecked() ? 1 : 0);
                        submit(resultBean);
                    }
                    break;
            }
        }
    };


    private void submit(SecurityResultBean resultBean){
        RequestUtils request = new RequestUtils();
        String securityStr = JSONObject.toJSONString(resultBean);
        JSONObject paramObject = new JSONObject();
        try {
            paramObject = JSONObject.parseObject(securityStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.post("amiwatergas/mobile/securityRecord/save",paramObject,true,SecurityConfirmActivity.this, new HttpResponseListener<NormalBean>() {
            @Override
            public void onResponse(NormalBean securityBean, Headers headers) {

                if(securityBean != null && securityBean.getCode() == 200){
                    if(securityBean.getSuccess() == false){
                        Toast.makeText(SecurityConfirmActivity.this, securityBean.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 打开结果页面
                    Intent i = new Intent(SecurityConfirmActivity.this,SecurityResultActivity.class);
                    i.putExtra("plan_id", plan_id + "");
                    i.putExtra("result", "success");
                    SecurityConfirmActivity.this.finish();
                    startActivity(i);
                }else{
                    Intent i = new Intent(SecurityConfirmActivity.this,SecurityResultActivity.class);
                    i.putExtra("plan_id", plan_id + "");
                    i.putExtra("result", "fail");
                    SecurityConfirmActivity.this.finish();
                    startActivity(i);
                }
//                Toast.makeText(SecurityConfirmActivity.this, "提交成功了！！", Toast.LENGTH_SHORT).show();
            }
            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                System.out.println("print data -- " +  e);
                // 打开结果页面
                Intent i = new Intent(SecurityConfirmActivity.this,SecurityResultActivity.class);
                i.putExtra("plan_id", plan_id + "");
                i.putExtra("result", "fail");
                SecurityConfirmActivity.this.finish();
                startActivity(i);
//                Toast.makeText(SecurityConfirmActivity.this, "提交失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}