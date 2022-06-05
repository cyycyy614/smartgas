package com.techen.smartgas.views.security;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techen.smartgas.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecurityResultActivity extends AppCompatActivity {

    @BindView(R.id.btn_goback)
    Button btnGoback;
    String plan_id;
    String result;
    @BindView(R.id.img_result)
    ImageView imgResult;
    @BindView(R.id.text_result)
    TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_result);
        ButterKnife.bind(this);
        setTitle("安检确认");
        //接受参数
        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");
        result = intent.getStringExtra("result");
        if (result.equals("success")) {
            imgResult.setImageResource(R.mipmap.icon_success);
            textResult.setText("提交成功");
        } else {
            imgResult.setImageResource(R.mipmap.icon_fail);
            textResult.setText("提交失败");
        }
        btnGoback.setOnClickListener(mListener);
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_goback:

                    // 打开列表页面
                    Intent intent = new Intent(SecurityResultActivity.this, UserListActivity.class);
                    intent.putExtra("id", plan_id);
                    SecurityResultActivity.this.finish();
                    startActivity(intent);
                    break;
            }
        }
    };
}