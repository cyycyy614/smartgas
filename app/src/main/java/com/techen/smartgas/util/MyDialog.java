package com.techen.smartgas.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techen.smartgas.R;

public class MyDialog {
    //点击确认按钮回调接口
    public interface OnConfirmListener {
        public void onConfirmClick();
    }

    /**
     * @Title: show
     * @Description: 显示Dialog
     * @param activity
     * @param content
     *            提示内容
     * @param confirmListener
     *            void
     * @throws
     */
    public static void show(Activity activity, String content,
                            final OnConfirmListener confirmListener) {
        // 加载布局文件
        View view = View.inflate(activity, R.layout.layout_my_dialog, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        if (!TextUtils.isEmpty(content)) {
            text.setText(content);
        }

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCancelable(false);// 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(Tool.getWidth(activity) / 3 * 2,
                ViewGroup.LayoutParams.WRAP_CONTENT);//设置弹出框宽度为屏幕宽度的三分之二

        // 确定
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmListener.onConfirmClick();
            }
        });
        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
