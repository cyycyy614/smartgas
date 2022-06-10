package com.techen.smartgas.views.workorder;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.RequestMethod;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.itheima.retrofitutils.listener.UploadListener;
import com.techen.smartgas.R;
import com.techen.smartgas.model.FileUploadBean;
import com.techen.smartgas.model.NormalBean;
import com.techen.smartgas.model.SecurityResultBean;
import com.techen.smartgas.model.WorkOrderDetailBean;
import com.techen.smartgas.util.FileUtils;
import com.techen.smartgas.util.HttpService;
import com.techen.smartgas.util.ImageCompressUtils;
import com.techen.smartgas.util.LoadingDialog;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.util.SharedPreferencesUtil;
import com.techen.smartgas.util.ThreadPoolUtil;
import com.techen.smartgas.util.Tool;
import com.techen.smartgas.views.security.SecurityAddActivity;
import com.techen.smartgas.views.security.SecurityConfirmActivity;
import com.techen.smartgas.views.security.SecurityDetailActivity;

import org.apmem.tools.layouts.FlowLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class WorkOrderAddActivity extends AppCompatActivity {
    String workId = "";
    @BindView(R.id.w_username)
    TextView wUsername;
    @BindView(R.id.tag_handled)
    Button tagHandled;
    @BindView(R.id.tag_unhandle)
    Button tagUnhandle;
    @BindView(R.id.tag_closed)
    Button tagClosed;
    @BindView(R.id.w_code)
    TextView wCode;
    @BindView(R.id.w_source)
    TextView wSource;
    @BindView(R.id.w_worker)
    TextView wWorker;
    @BindView(R.id.w_report_time)
    TextView wReportTime;
    @BindView(R.id.w_reporter_mobile)
    TextView wReporterMobile;
    @BindView(R.id.w_order_time)
    TextView wOrderTime;
    @BindView(R.id.w_addr)
    TextView wAddr;
    @BindView(R.id.w_const)
    TextView wConst;
    @BindView(R.id.layout_work_cont)
    LinearLayout layoutWorkCont;
    @BindView(R.id.textarea)
    EditText textarea;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.activity_rootview)
    ScrollView activity_rootview;
    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;
    @BindView(R.id.upload)
    LinearLayout upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_add);
        ButterKnife.bind(this);
        setTitle("工单维修提交");

        btnSubmit.setOnClickListener(mListener);

        //接受参数
        Intent intent = getIntent();
        workId = intent.getStringExtra("id");

        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        LoadingDialog.getInstance(this).show();
        this.getDetail();
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submit:
                    submit();
                    break;
            }
        }
    };

    private void getDetail() {
        RequestUtils request = new RequestUtils();
        // get请求
        Map<String, Object> getParams = new HashMap<>();
        String url = "";
        getParams.put("id", workId);
        url = "amiwatergas/mobile/workOrder/order_details";
        request.get(url, getParams, true, WorkOrderAddActivity.this, new HttpResponseListener<WorkOrderDetailBean>() {
            @Override
            public void onResponse(WorkOrderDetailBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if (bean != null && bean.getCode() == 200) {
                    WorkOrderDetailBean.ResultBean resultBean = bean.getResult();
                    show(resultBean);
                    LoadingDialog.getInstance(WorkOrderAddActivity.this).hide();
                }else{
                    LoadingDialog.getInstance(WorkOrderAddActivity.this).hide();
                }
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                System.out.println("print data -- " + e);
                LoadingDialog.getInstance(WorkOrderAddActivity.this).hide();
            }
        });
    }


    private Boolean valid(){
        if(TextUtils.isEmpty(textarea.getText())){
            Toast.makeText(WorkOrderAddActivity.this, "请输入备注", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(imgList != null && imgList.size() == 0){
            Toast.makeText(WorkOrderAddActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }


    private void submit() {
        if(!valid()){
            return;
        }
        RequestUtils request = new RequestUtils();
        // get请求
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("id",workId);
            paramObject.put("repair_content",textarea.getText());
            paramObject.put("repairUrl",TextUtils.join(",", imgList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String  url = "amiwatergas/mobile/workOrder/updateWorkOrder";
        request.post(url, paramObject, true, WorkOrderAddActivity.this, new HttpResponseListener<NormalBean>() {
            @Override
            public void onResponse(NormalBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if (bean != null && bean.getCode() == 200) {
                    if(bean.getSuccess() == false){
                        Toast.makeText(WorkOrderAddActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(WorkOrderAddActivity.this, "提交成功了！！", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    WorkOrderAddActivity.this.finish();
                }else{
                    Toast.makeText(WorkOrderAddActivity.this, "提交失败了！！", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                LoadingDialog.getInstance(WorkOrderAddActivity.this).hide();
                Toast.makeText(WorkOrderAddActivity.this, "提交失败了！！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void show(WorkOrderDetailBean.ResultBean resultBean) {
        if (resultBean != null) {
            if (resultBean.getState().equals("handling")) {
                tagHandled.setVisibility(View.GONE);
                tagUnhandle.setVisibility(View.VISIBLE);
                tagClosed.setVisibility(View.GONE);
            } else if (resultBean.getState().equals("handled")){
                tagHandled.setVisibility(View.VISIBLE);
                tagUnhandle.setVisibility(View.GONE);
                tagClosed.setVisibility(View.GONE);
            } else{
                tagHandled.setVisibility(View.GONE);
                tagUnhandle.setVisibility(View.GONE);
                tagClosed.setVisibility(View.VISIBLE);
            }
        }
        wCode.setText(resultBean.getOrder_code());
        wAddr.setText(resultBean.getAccount_address());
        String orderTime = resultBean.getRepair_time();
        orderTime = TextUtils.isEmpty(orderTime) ? "" : orderTime;
        wOrderTime.setText(orderTime);
        wWorker.setText(TextUtils.isEmpty(resultBean.getWorker_name())?"":resultBean.getWorker_name());
        wUsername.setText(resultBean.getReporter_name() + "");
        wReporterMobile.setText(resultBean.getAccount_phone() + "");
        wReportTime.setText(resultBean.getReport_time() + "");
        wConst.setText(resultBean.getWork_content() + "");
        wSource.setText(resultBean.getOrder_source() + "");

        List<WorkOrderDetailBean.ResultBean.WorkInfoBean> workInfoBeanList = resultBean.getWorkInfo();
        if(workInfoBeanList != null && workInfoBeanList.size() > 0){
            generate_camera(resultBean.getWorkInfo());
        }

    }

    ImageView curUploadImageView;
    ArrayList imgList = new ArrayList();
    // 生成拍照组件，并处理上传方法
    private void generate_camera(List<WorkOrderDetailBean.ResultBean.WorkInfoBean> workInfoBeanList){
        // layout_camaro
        // 拍照
        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(imgList != null && imgList.size() > 5) {
                    Toast.makeText(WorkOrderAddActivity.this,"最多上传6张",Toast.LENGTH_SHORT);
                    return;
                }
                startUpload();
            }
        });
    }

    private void generate_img(){
        // 上传后显示的图片
        RelativeLayout relative_img = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.comp_camera_img, flowlayout,false);
        curUploadImageView = (ImageView) relative_img.findViewById(R.id.upload_img);
        ImageView upload_del = (ImageView) relative_img.findViewById(R.id.upload_del);
        flowlayout.addView(relative_img);

        upload_del.setTag(imgList.size());
        upload_del.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                flowlayout.removeView(relative_img);
                int index = (int) upload_del.getTag();
                if(imgList != null && imgList.size() > 0){
                    imgList.remove(index);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                LoadingDialog.setInstance(null);
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String imgPath = "";
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startUpload() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        choosePictureDialog(10);
    }

    /**
     * 选择拍照/相册图片
     *
     * @param type 打开类型区分码
     */
    private void choosePictureDialog(int type) {
        View chooseTypeView = LayoutInflater.from(this).inflate(R.layout.dialog_choose_pic_type, null);
        AlertDialog selectDialog = new AlertDialog.Builder(this).setView(chooseTypeView).setCancelable(false).create();
        selectDialog.show();
        Objects.requireNonNull(selectDialog.getWindow()).setBackgroundDrawableResource(R.color.transparent);

        TextView tvChoosePicCamera = chooseTypeView.findViewById(R.id.tv_choose_pic_camera);
        TextView tvChoosePicGallery = chooseTypeView.findViewById(R.id.tv_choose_pic_gallery);
        TextView tvChoosePicCancel = chooseTypeView.findViewById(R.id.tv_choose_pic_cancel);

        tvChoosePicCamera.setOnClickListener(v -> {
            selectDialog.dismiss();
            // 拉起相机
            openCamera(10);
        });
        tvChoosePicGallery.setOnClickListener(v -> {
            selectDialog.dismiss();
            // 打开相册
            openGallery(11);
        });
        tvChoosePicCancel.setOnClickListener(v -> selectDialog.dismiss());
    }

    /**
     * 打开相机
     *
     * @param type 打开类型区分码
     */
    private void openCamera(int type) {
        // 创建照片存储目录
        File imgDir = new File(getFilePath(null));
        // 创建照片
        String photoName = System.currentTimeMillis() + ".png";
        File picture = new File(imgDir, photoName);
        if (!picture.exists()) {
            try {
                picture.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "choosePictureTypeDialog: 创建图片失败", e);
            }
        }
        imgPath = picture.getAbsolutePath();
        // 调用相机拍照
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.techen.smartgas.fileprovider", picture));
        startActivityForResult(camera, type);
    }

    /**
     * 打开相册
     *
     * @param type 打开类型区分码
     */
    private void openGallery(int type) {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(gallery, type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拉起相机回调data为null，打开相册回调不为null
        if(requestCode == 0) {
            Toast.makeText(this, "权限获得", Toast.LENGTH_SHORT).show();
        } else if(requestCode > 0){
            LoadingDialog.getInstance(WorkOrderAddActivity.this).hide();

            File file = null;
            if(requestCode == 10){//相机
                file = new File(imgPath);
                if(file != null && file.length() == 0){
                    return;
                }else{
                    generate_img();
                }
                Glide.with(this).load(imgPath).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(curUploadImageView);
                compressImage(imgPath,curUploadImageView);
            }else if(requestCode == 11 && data != null){
                generate_img();
                file = new File(UriToFile(data.getData()));
                compressImage(file.getPath(),curUploadImageView);
                Glide.with(this).load(data.getData()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(curUploadImageView);
            }
        }
        imgPath = "";
    }


    /**
     * 压缩
     *
     * @param filePath 文件路径
     */
    private void compressImage(final String filePath,ImageView imageView) {
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                File file = null;
                Bitmap bitmap = ImageCompressUtils.compressImageFromFile(filePath, 1024f);// 按尺寸压缩图片
                if(bitmap == null){
                    file = new File(imgPath);
                }else{
                    int size = bitmap.getByteCount();
                    file = ImageCompressUtils.compressImage(bitmap);  //按质量压缩图片
//                File file = ImageUtils.bitmap2File(compressBitmap);
                    Long fileSize = FileUtils.getFileSize(file);
                }
                imgPath = "";
                if(file != null){
                    uploadImg(file);
                }
            }
        });
    }

    protected void uploadImg(File uploadFile){
        ArrayList<Map<String, Object>> curList = null;
        String url = HttpService.BASE_URL + "amiwatergas/mobile/fileUpload";
        Request request = ItheimaHttp.newUploadRequest(url, RequestMethod.POST);
        request.putHeader("Content-Type","multipart/form-data");
        request.putHeader("Authorization","Bearer " + Tool.getToken(WorkOrderAddActivity.this));
        request.putUploadFile("file",uploadFile).putMediaType(MediaType.parse("multipart/form-data"));
        ItheimaHttp.upload(request, new UploadListener() {
            @Override
            public void onResponse(okhttp3.Call call, Response response) {
                if(response.body() != null){
                    FileUploadBean resultBean = null;
                    try {
                        resultBean = new Gson().fromJson(response.body().string(), FileUploadBean.class);
                        if(resultBean != null && resultBean.getCode() == 200){
                            imgList.add(resultBean.getResult());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LoadingDialog.getInstance(WorkOrderAddActivity.this).hide();
            }

            @Override
            public void onProgress(long l, long l1, boolean b) {

            }
        });
    }


    /**
     * todo  uri 转 file
     * @param uri
     * @return
     */
    public String UriToFile(Uri uri) {
        String[] filePc = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePc, null, null, null);
        cursor.moveToFirst();
        Log.i(TAG, "UriToFile: 22"+cursor);
        int col = cursor.getColumnIndex(filePc[0]);
        String pic = cursor.getString(col);
        cursor.close();
        return pic;
    }

    /**
     * 获取存储文件路径
     *
     * @param dir     选择目录
     * @return 路径
     */
    public String getFilePath(String dir) {
        String path;
        // 判断是否有外部存储，是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = getExternalFilesDir(dir).getAbsolutePath();
        } else {
            // 使用内部储存
            path = getFilesDir() + File.separator + dir;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
}