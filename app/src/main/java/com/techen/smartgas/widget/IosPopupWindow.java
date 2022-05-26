package com.techen.smartgas.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.techen.smartgas.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IosPopupWindow extends PopupWindow {
    private String PaymentCodeTitle;//弹窗的标题
    //支付的类型
    private int PAY_TYPE_Alipay = 0;
    private int PAY_TYPE_WeChat = 1;

    private Activity mActivity;
    private List<Map<String, Object>> datas=new ArrayList<Map<String, Object>>();
    private View dialogView;
    private String dialogMessage = null;
    private OnClickListener mOnClickListener;
    private ListView listView;
    private SimpleAdapter simpleAdapter;

    public IosPopupWindow(Activity context, List<Map<String, Object>> data, OnClickListener itemsOnClick) {
        this.mActivity = context;
        mOnClickListener = itemsOnClick;
        datas = data;
        View view = LayoutInflater.from(context).inflate(R.layout.ios_popup_window_layout, null);

        setContentView(view);
        setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        setFocusable(true);
        setOutsideTouchable(true);
        //设置动画
        setAnimationStyle(R.style.BottomDialogWindowAnim);

        listView= (ListView) view.findViewById(R.id.listview);
        initDatas();//初始化数据集
        //实例化SimpleAdapter
        simpleAdapter=new SimpleAdapter(
                view.getContext(),
                datas,R.layout.item_popup,
                new String[]{"img","name"},
                new int[]{R.id.img,R.id.tv});
        listView.setAdapter(simpleAdapter);//设置配置器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> data = datas.get(position);
                if(mOnClickListener != null)mOnClickListener.onItemClick(view,position,data);
                dismissPopupWindow();
//                Toast.makeText(MainActivity.this, "您单击了" + datas.get(position).getAnimal(), Toast.LENGTH_SHORT).show();
            }
        });

        //设置消失监听
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissPopupWindow();
            }
        });
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void initDatas() {
        Map map1=new HashMap();
        map1.put("img","");
        map1.put("name","选择1");
        Map map2=new HashMap();
        map2.put("img",R.mipmap.icon_correct);
        map2.put("name","选择2");
        Map map3=new HashMap();
        map3.put("img","");
        map3.put("name","选择3");
//        datas.add(map1);
//        datas.add(map2);
//        datas.add(map3);
    }

    public void setPopupOnClickListener(OnClickListener mPopupOnClickListener){
        this.mOnClickListener  = mPopupOnClickListener;
    }
    public interface OnClickListener{
        void cancel();//取消
        void onItemClick(Object o,int position,Map<String, Object> data);
    }

    /**
     * ios弹窗点击事件
     *
     * @param view
     */
    private void setOnPopupViewClick(View view) {
//        dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_payment_qr_code_layout,null);
//        ImageView iv =dialogView.findViewById(R.id.iv_payment_code);


        TextView tv_cancel;

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> {
            //取消
            dismissPopupWindow();
            mOnClickListener.cancel();

        });
    }

    private void dismissPopupWindow() {
        dismiss();
        setBackgroundAlpha(1f);
    }

    public void show(View parentView){
        //设置位置
        showAtLocation(parentView, Gravity.BOTTOM, 0, 66);
    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = alpha;
        mActivity.getWindow().setAttributes(lp);
    }
}
