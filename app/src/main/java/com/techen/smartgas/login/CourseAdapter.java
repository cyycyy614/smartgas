package com.techen.smartgas.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techen.smartgas.R;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.Viewholder> {

    private Context context;
    private ArrayList<CourseModel> courseModelArrayList;
    private final OnItemClickListener listener;

    // Constructor
    public CourseAdapter(Context context, ArrayList<CourseModel> courseModelArrayList, OnItemClickListener listener) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(CourseModel item);
    }

    @NonNull
    @Override
    public CourseAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout 3model方法获取值
        CourseModel model = courseModelArrayList.get(position);
        holder.order_name.setText(model.getOrder_name());
        holder.order_mobile.setText(model.getOrder_mobile());
        holder.order_code.setText(model.getOrder_code());
        holder.order_report_time.setText(model.getOrder_report_time());
        holder.order_account_address.setText(model.getOrder_account_address());
        holder.order_source.setText(model.getOrder_source());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(model);
            }
        });
//        holder.courseIV.setImageResource(model.getCourse_image());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return courseModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        //声明 变量 1

//        private ImageView courseIV;
        private TextView order_name, order_mobile,order_code,order_report_time,order_account_address,order_source;
        private Button order_disp_state;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            //卡片内部单元获取 2
            order_name = itemView.findViewById(R.id.order_name);
            order_mobile = itemView.findViewById(R.id.order_mobile);
            order_code = itemView.findViewById(R.id.order_code);
            order_report_time = itemView.findViewById(R.id.order_report_time);
            order_account_address = itemView.findViewById(R.id.order_account_address);
            order_source = itemView.findViewById(R.id.order_source);
            order_disp_state=itemView.findViewById(R.id.order_disp_state);

        }
    }
}