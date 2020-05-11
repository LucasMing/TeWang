package com.qing.tewang.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.Location;
import com.qing.tewang.model.User;
import com.qing.tewang.ui.location.SelectLocation01;
import com.qing.tewang.util.AMapUtil;
import com.qing.tewang.util.DisplayUtils;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.widget.SmartTitleBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateShopActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private String mDrawableAdd = "drawable://add_image";

    private EditText shopName, shopMobile, description, jianjie;
    private TextView zone;
    private EditText address;
    private RecyclerView mRecycler;
    private TagFlowLayout mTagLayout;
    private ImageView alipay, wechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        titleBar.getRightTextView()
                .setOnClickListener(view -> {
                    saveData();
                });

        jianjie = findViewById(R.id.jianjie);
        shopName = findViewById(R.id.shop_name);
        shopMobile = findViewById(R.id.contact);
        description = findViewById(R.id.description);
        zone = findViewById(R.id.zone);
        address = findViewById(R.id.address);
        mRecycler = findViewById(R.id.recycler_view);
        mTagLayout = findViewById(R.id.tag_layout);
        alipay = findViewById(R.id.alipay);
        wechat = findViewById(R.id.wechat);


        mData.add(mDrawableAdd);
        mAdapter = new ImageAdapter(getApplicationContext(), mData);

        LinearLayoutManager ms = new LinearLayoutManager(getApplicationContext());
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler.setLayoutManager(ms);
        mRecycler.setAdapter(mAdapter);

        findViewById(R.id.select_tag)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), TagActivity.class);
                    startActivityForResult(intent, 3);
                });

        //定位权限
        checkLocationPerm();

        zone.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SelectLocation01.class);
            startActivityForResult(intent, 2);
        });

        wechat.setOnClickListener(v -> {
            String[] params = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (!EasyPermissions.hasPermissions(getApplicationContext(), params)) {
                EasyPermissions.requestPermissions(CreateShopActivity.this, "需要请求必要权限！", 100, params);
            } else {
                Matisse.from(CreateShopActivity.this)
                        .choose(MimeType.ofImage())//图片类型
                        .countable(true)//true:选中后显示数字;false:选中后显示对号
                        .capture(true)
                        .maxSelectable(1)
                        .captureStrategy(new CaptureStrategy(true,
                                "com.qing.tewang.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new PicassoEngine())//图片加载引擎
                        .forResult(100);
            }
        });

        alipay.setOnClickListener(v -> {
            String[] params = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (!EasyPermissions.hasPermissions(getApplicationContext(), params)) {
                EasyPermissions.requestPermissions(CreateShopActivity.this, "需要请求必要权限！", 100, params);
            } else {
                Matisse.from(CreateShopActivity.this)
                        .choose(MimeType.ofImage())//图片类型
                        .countable(true)//true:选中后显示数字;false:选中后显示对号
                        .capture(true)
                        .maxSelectable(1)
                        .captureStrategy(new CaptureStrategy(true,
                                "com.qing.tewang.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new PicassoEngine())//图片加载引擎
                        .forResult(200);
            }
        });

    }


    private void checkLocationPerm() {

        String[] params = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};


        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "申请运行所必须的权限！", 101, params);
        } else {
            startLocation();
        }
    }

    private void startLocation() {


        if (AMapUtil.isOPen(getApplicationContext())) {
            AMapUtil.getInstance()
                    .setLocationListener(map -> {
                        if (map != null) {
                            lat = map.getLatitude();
                            lng = map.getLongitude();
                            AMapUtil.getInstance().stopLocation();

                            //获取城市ID
                            APIWrapper.getLocation(lat, lng)
                                    .subscribe(new SimpleObserver<CommonData<Location>>(getActivity()) {
                                        @Override
                                        public void onNext(CommonData<Location> data) {
                                            if (data.getErrno() == 0) {
                                                Location location = data.getData();
                                                location.setLat(lat);
                                                location.setLng(lng);
                                                SPUtils.saveLocation(getApplicationContext(), location);
                                            }
                                        }
                                    });


                        }
                    })
                    .startLocation();
        } else {
            AMapUtil.openGPS(getApplicationContext());
        }


    }

    private double lat, lng;


    private List<String> uploadImages = new ArrayList<>();

    private void saveData() {

        String sName = shopName.getText().toString();
        String sJianjie = jianjie.getText().toString();
        String sShopMobile = shopMobile.getText().toString();
        String sDes = description.getText().toString();
        String sZone = zone.getText().toString();
        String sStreet = address.getText().toString();
//        if (lng == 0 || lat == 0) {
//            showToast("还未获得您的定位！");
//            return;
//        }

        if (TextUtils.isEmpty(sName)) {
            showToast("店铺名称不能为空！");
            return;
        }
        if (TextUtils.isEmpty(sJianjie)) {
            showToast("店铺简介不能为空！");
            return;
        }
        if (TextUtils.isEmpty(sShopMobile)) {
            showToast("店铺联系方式不能为空！");
            return;
        }
        if (TextUtils.isEmpty(sDes)) {
            showToast("店铺描述不能为空！");
            return;
        }
        if (TextUtils.isEmpty(sZone)) {
            showToast("必须选择区域");
            return;
        }
        if (TextUtils.isEmpty(sStreet)) {
            showToast("请填写街道信息");
            return;
        }
        if (uploadImages.size() == 0) {
            showToast("必须上传店铺图片！");
            return;
        }
        List<String> tags = new ArrayList<>();
        if (mTagLayout.getSelectedList().size() > 0) {
            tags = Stream.of(mTagLayout.getSelectedList())
                    .map(index -> mTagList.get(index))
                    .collect(Collectors.toList());
        }

        JsonObject object = new JsonObject();
        if (!TextUtils.isEmpty(mWechatPath)) {
            object.addProperty("wechat", mWechatPath);
        }
        if (!TextUtils.isEmpty(mAliPath)) {
            object.addProperty("alipay", mAliPath);
        }


        APIWrapper.updateShopDetail(sName, sShopMobile, sJianjie,
                sDes, sZone, sStreet, uploadImages, tags, object.toString())
                .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                    @Override
                    public void onNext(CommonData commonData) {
                        if (commonData.getErrno() == 0) {
                            showToast("创建成功！");
                            User user = SPUtils.getUser(getApplicationContext());
                            user.setLevel("3");
                            user.setLevel_name("商户");
                            SPUtils.clearUser(getApplicationContext());
                            SPUtils.saveUser(getApplicationContext(), user);
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            showToast(commonData.getMsg());
                        }
                    }
                });


    }

    private List<String> mData = new ArrayList<>();


    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHold> {
        private List<String> data;
        private Context mContext;

        public ImageAdapter(Context context, List<String> data) {
            this.data = data;
            this.mContext = context;
        }

        @Override
        public ImageAdapter.ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_edit_pic, parent, false);
            return new ImageAdapter.ViewHold(view);
        }

        @Override
        public void onBindViewHolder(ImageAdapter.ViewHold holder, final int position) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    DisplayUtils.getInstance().getWidth(getApplication()) / 6
                    , DisplayUtils.getInstance().getWidth(getApplication()) / 6);
            params.setMargins(16, 1, 16, 1);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            holder.imageView.setLayoutParams(params);
            if (!data.get(position).startsWith("drawable://")) {
                holder.deleteView.setVisibility(View.VISIBLE);
                if (data.get(position).startsWith("http://")
                        || data.get(position).startsWith("https://")) {
                    ImageUtils.load(getApplicationContext(), data.get(position), holder.imageView);
                } else {
                    ImageUtils.load(getApplication(), "file://" + data.get(position), holder.imageView);
                }


                holder.imageView.setOnClickListener(view -> {

                });
            } else {
                Resources res = getResources();
                int resId = res.getIdentifier(data.get(position).replace("drawable://", ""), "mipmap", getPackageName());
                holder.imageView.setImageResource(resId);
                holder.deleteView.setVisibility(View.GONE);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] params = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        if (!EasyPermissions.hasPermissions(getApplicationContext(), params)) {
                            EasyPermissions.requestPermissions(CreateShopActivity.this, "需要请求必要权限！", 100, params);
                        } else {
                            Matisse.from(CreateShopActivity.this)
                                    .choose(MimeType.ofImage())//图片类型
                                    .countable(true)//true:选中后显示数字;false:选中后显示对号
                                    .capture(true)
                                    .maxSelectable(9)
                                    .captureStrategy(new CaptureStrategy(true,
                                            "com.qing.tewang.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                    .imageEngine(new PicassoEngine())//图片加载引擎
                                    .forResult(0);
                        }
                    }
                });
            }
            holder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    uploadImages.remove(position - 1);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHold extends RecyclerView.ViewHolder {
            ImageView deleteView, imageView;
            RelativeLayout itemLayout;

            ViewHold(View itemView) {
                super(itemView);
                deleteView = itemView.findViewById(R.id.delete_view);
                imageView = itemView.findViewById(R.id.image_view);
                itemLayout = itemView.findViewById(R.id.item_layout);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Matisse.from(CreateShopActivity.this)
                .choose(MimeType.ofImage())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .capture(true)
                .maxSelectable(9)
                .captureStrategy(new CaptureStrategy(true,
                        "com.qing.tewang.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new PicassoEngine())//图片加载引擎
                .forResult(0);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AlertDialog.Builder(getActivity(), R.style.ColorDialog)
                    .setTitle("权限申请")
                    .setMessage("申请运行所必须的权限！")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .create().show();
        }
    }


    private ImageAdapter mAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            List<String> paths = Matisse.obtainPathResult(data);
            mData.addAll(paths);
            mAdapter.notifyDataSetChanged();
            //上传
            uploadImages(paths)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleDialogObserver<List<String>>(getActivity()) {
                        @Override
                        public void onNext(List<String> imgs) {
                            uploadImages.addAll(imgs);
                        }
                    });

        } else if (requestCode == 2 && data != null) {
            String locationZone;
            if (data.getStringExtra("province").equals(data.getStringExtra("city"))) {
                locationZone = data.getStringExtra("province") +
                        data.getStringExtra("county");
            } else {
                locationZone = data.getStringExtra("province") +
                        data.getStringExtra("city") +
                        data.getStringExtra("county");
            }
            zone.setText(locationZone);
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            mTagList = data.getStringArrayListExtra("data");
            setTag();
        } else if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            List<String> paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0) {
                ImageUtils.load(getApplicationContext(), "file://" + paths.get(0), alipay);
                APIWrapper.updateCommonFile(paths.get(0))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SimpleDialogObserver<CommonData<JsonObject>>(getActivity()) {
                            @Override
                            public void onNext(CommonData<JsonObject> commonData) {
                                if (commonData.getErrno() == 0) {
                                    JsonObject result = commonData.getData();
                                    mAliPath = result.get("url").getAsString();
                                }
                            }
                        });
            }
        } else if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            List<String> paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0) {
                ImageUtils.load(getApplicationContext(), "file://" + paths.get(0), wechat);
                APIWrapper.updateCommonFile(paths.get(0))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SimpleDialogObserver<CommonData<JsonObject>>(getActivity()) {
                            @Override
                            public void onNext(CommonData<JsonObject> commonData) {
                                if (commonData.getErrno() == 0) {
                                    JsonObject result = commonData.getData();
                                    mWechatPath = result.get("url").getAsString();
                                }
                            }
                        });
            }
        }

    }

    private String mAliPath, mWechatPath;

    private void setTag() {

        TagAdapter mAdapter = new TagAdapter<String>(mTagList) {

            @Override
            public View getView(final FlowLayout parent, int position, final String text) {
                View view = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.item_flow_text, parent, false);
                TextView tv = view.findViewById(R.id.text);
                tv.setText(text);
                return view;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                view.setBackground(getResources().getDrawable(R.drawable.checked_bg));

            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                view.setBackground(getResources().getDrawable(R.drawable.normal_bg));
            }
        };

        mAdapter.setSelectedList(IntStream
                .range(0, mTagList.size())
                .boxed()
                .collect(Collectors.toSet()));
        mTagLayout.setAdapter(mAdapter);
    }


    private List<String> mTagList = new ArrayList<>();


    private Observable<List<String>> uploadImages(List<String> images) {
        return Observable.just(images)
                .map(new Function<List<String>, List<String>>() {
                    @Override
                    public List<String> apply(List<String> images) throws Exception {
                        final List<String> ids = new ArrayList<>();
                        //上传图片
                        for (String temp : images) {
                            CommonData<JsonObject> data = APIWrapper.updateCommonFile(temp).toFuture().get();
                            if (data.getErrno() == 0) {
                                JsonObject result = data.getData();
                                ids.add(result.get("url").getAsString());
                            }
                        }
                        return ids;
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapUtil.getInstance().stopLocation();
        AMapUtil.getInstance().destroyLocation();
    }
}
