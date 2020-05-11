package com.qing.tewang.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.User;
import com.qing.tewang.ui.CollectActivity;
import com.qing.tewang.ui.CreateShopActivity;
import com.qing.tewang.ui.ManagerActivity;
import com.qing.tewang.ui.MoneyActivity;
import com.qing.tewang.ui.SettingActivity;
import com.qing.tewang.ui.UpdateActivity;
import com.qing.tewang.ui.UserInfoActivity;
import com.qing.tewang.util.FileUtils;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.LogUtils;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.widget.StrokeColorText;
import com.tencent.bugly.beta.Beta;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

public class MyFragment extends BaseFragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private ImageView mAvatarView;
    private TextView mUserName;
    private TextView mMoneyText, mMobile;
    private StrokeColorText mLevelName;
    private SwipeRefreshLayout swipeView;
    private View managerView, updateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);

        mAvatarView = rootView.findViewById(R.id.user_avatar);
        mUserName = rootView.findViewById(R.id.user_name);
        mMoneyText = rootView.findViewById(R.id.my_money);
        mLevelName = rootView.findViewById(R.id.level_name);
        mMobile = rootView.findViewById(R.id.mobile);
        swipeView = rootView.findViewById(R.id.swipe_view);
        managerView = rootView.findViewById(R.id.manager);
        updateView = rootView.findViewById(R.id.my_update);

        setUser();

        rootView.findViewById(R.id.money_layout).setOnClickListener(this);
        updateView.setOnClickListener(this);
        rootView.findViewById(R.id.my_collect).setOnClickListener(this);
        rootView.findViewById(R.id.setting).setOnClickListener(this);
        rootView.findViewById(R.id.head).setOnClickListener(this);
        rootView.findViewById(R.id.updating).setOnClickListener(this);
        mAvatarView.setOnClickListener(this);
        managerView.setOnClickListener(this);

        swipeView.setOnRefreshListener(this::updateUser);

        return rootView;
    }

    private void updateUser() {
        APIWrapper.updateUser()
                .subscribe(new SimpleObserver<CommonData<User>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<User> data) {
                        swipeView.setRefreshing(false);
                        if (data.getErrno() == 0) {
                            User user = data.getData();
                            if (user != null) {
                                SPUtils.clearUser(getContext());
                                SPUtils.saveUser(getContext(), user);
                                setUser();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        swipeView.setRefreshing(false);
                    }
                });
    }

    private void setUser() {
        User user = SPUtils.getUser(getContext());
        if (user != null) {
            mUserName.setText(user.getName());
            mLevelName.setText(user.getLevel_name());
            mMobile.setText(user.getMobile());
            if (!TextUtils.isEmpty(user.getAvatar())) {
                ImageUtils.load(getContext(), user.getAvatar(), mAvatarView);
            }
            if (user.getLevel().equals("3")) {//商户
                updateView.setVisibility(View.GONE);
                managerView.setVisibility(View.VISIBLE);
            } else if (user.getLevel().equals("4")) {
                //判断是否有商店

            } else {
                managerView.setVisibility(View.GONE);
                updateView.setVisibility(View.VISIBLE);
            }
        } else {
            managerView.setVisibility(View.GONE);
            updateView.setVisibility(View.VISIBLE);
        }
    }

    private Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.money_layout:
                intent = new Intent(getContext(), MoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.my_collect:
                intent = new Intent(getContext(), CollectActivity.class);
                startActivity(intent);
                break;
            case R.id.my_update:
                Intent intent = new Intent(getContext(), CreateShopActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.head:
                intent = new Intent(getContext(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.user_avatar:
                String[] params = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (!EasyPermissions.hasPermissions(getContext(), params)) {
                    EasyPermissions.requestPermissions(getActivity(), "需要请求必要权限！", 100, params);
                } else {
                    Matisse.from(getActivity())
                            .choose(MimeType.ofImage())//图片类型
                            .countable(true)//true:选中后显示数字;false:选中后显示对号
                            .capture(true)
                            .maxSelectable(1)
                            .captureStrategy(new CaptureStrategy(true,
                                    "com.qing.tewang.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                            .imageEngine(new PicassoEngine())//图片加载引擎
                            .forResult(0);
                }
                break;
            case R.id.manager:
                intent = new Intent(getContext(), ManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.updating:
                Beta.checkUpgrade();
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Matisse.from(getActivity())
                .choose(MimeType.ofImage())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .capture(true)
                .maxSelectable(1)
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
                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .create().show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = Matisse.obtainResult(data).get(0);
            Uri destinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), "SampleCropImage.jpeg"));
            UCrop.of(uri, destinationUri)
                    .withAspectRatio(16, 16)
                    .withMaxResultSize(400, 400)
                    .start(getActivity(), 1);
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri resultUri = UCrop.getOutput(data);
            String avatarPath = FileUtils.getImagePath(resultUri);
            if (!TextUtils.isEmpty(avatarPath)) {
                mAvatarView.setImageBitmap(BitmapFactory.decodeFile(FileUtils.getImagePath(resultUri)));
                //上传
                APIWrapper.updateAvatar(avatarPath)
                        .subscribe(new SimpleDialogObserver<CommonData<JsonObject>>(getActivity()) {
                            @Override
                            public void onNext(CommonData<JsonObject> data) {
                                if (data.getErrno() == 0) {
                                    User user = SPUtils.getUser(getContext());
                                    JsonObject json = data.getData();
                                    String avatar = json.get("url").getAsString();
                                    user.setAvatar(avatar);
                                    SPUtils.clearUser(getContext());
                                    SPUtils.saveUser(getContext(), user);
                                }
                            }
                        });
            }

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (SPUtils.isLogin(getContext())) {
            setUser();
        }
    }
}
