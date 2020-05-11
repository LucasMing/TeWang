package com.qing.tewang.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.HomeAd;
import com.qing.tewang.model.MessageResult;
import com.qing.tewang.model.User;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.widget.SmartTitleBar;
import com.superluo.textbannerlibrary.TextBannerView;
import com.tencent.bugly.beta.Beta;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private DrawerLayout mDrawer;
    private ImageView mViewAvatar;
    private TextView mTextName;

    private View mMessageTip;
    private TextBannerView mTextBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Beta.checkUpgrade();

        mTextName = findViewById(R.id.user_name);
        mViewAvatar = findViewById(R.id.user_avatar);

        findViewById(R.id.user_edit)
                .setOnClickListener(this);
        findViewById(R.id.account)
                .setOnClickListener(this);
        findViewById(R.id.update)
                .setOnClickListener(this);
        findViewById(R.id.collect)
                .setOnClickListener(this);
        findViewById(R.id.setting)
                .setOnClickListener(this);

        mDrawer = findViewById(R.id.drawer_layout);
        mTextBanner = findViewById(R.id.tv_banner);


        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> mDrawer.openDrawer(Gravity.LEFT));


        titleBar.findViewById(R.id.right_iv)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                    startActivity(intent);
                });

        mMessageTip = titleBar.findViewById(R.id.message_tip);

        checkPerm();

        getHomeAd();
        getUnReadMessage();

        initUser();
    }

    private static final int ACCESS_COARSE_LOCATION = 101;

    private void checkPerm() {

        String[] params = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO};


        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "申请运行所必须的权限！", ACCESS_COARSE_LOCATION, params);
        } else {
            startLocation();
        }

        startLocation();
    }

    private void startLocation() {

    }

    private void getHomeAd() {
//        APIWrapper.getHomeAd()
//                .subscribe(new SimpleObserver<CommonData<List<HomeAd>>>(getActivity()) {
//                    @Override
//                    public void onNext(CommonData<List<HomeAd>> data) {
//                        if (data.getErrno() == 0) {
//                            List<HomeAd> ads = data.getData();
//                            if (ads != null && ads.size() > 0) {
//                                List<String> temps = ads.stream()
//                                        .map(HomeAd::getTitle)
//                                        .collect(Collectors.toList());
//                                mTextBanner.setDatas(temps);
//                                mTextBanner.startViewAnimator();
//                                mTextBanner.setItemOnClickListener(new ITextBannerItemClickListener() {
//                                    @Override
//                                    public void onItemClick(String data, int position) {
//
//                                        HomeAd ad = ads.get(position);
//                                        if (!TextUtils.isEmpty(ad.getVoice_sn())) {
//                                            Intent intent = new Intent(getApplicationContext(), VoiceDetailActivity.class);
//                                            intent.putExtra("sn", ad.getVoice_sn());
//                                            startActivity(intent);
//                                        } else if (!TextUtils.isEmpty(ad.getUrl())) {
//                                            Intent intent = new Intent(getApplicationContext(), WebActivity.class);
//                                            intent.putExtra("title", ad.getTitle());
//                                            intent.putExtra("url", ad.getUrl());
//                                            startActivity(intent);
//                                        }
//
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });

    }

    private void getUnReadMessage() {
        APIWrapper.getUnReadMessage()
                .subscribe(new SimpleObserver<CommonData<MessageResult>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<MessageResult> data) {

                        if (data.getErrno() == 0) {
                            if (data.getData().getUnReadNum() > 0) {
                                mMessageTip.setVisibility(View.VISIBLE);
                            } else {
                                mMessageTip.setVisibility(View.GONE);
                            }
                        }
                    }
                });

    }


    @Override
    protected void onResume() {
        super.onResume();
        getUnReadMessage();
        mTextBanner.startViewAnimator();
    }

    private void initUser() {
        User user = SPUtils.getUser(getApplicationContext());
        if (user == null || TextUtils.isEmpty(user.getToken())) {
            SPUtils.clearUser(getApplication());
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            mTextName.setText(user.getName());
            if (!TextUtils.isEmpty(user.getAvatar())) {
                ImageUtils.load(getApplicationContext(), user.getAvatar(), mViewAvatar);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void closeDrawer() {
        mDrawer.closeDrawer(GravityCompat.START);
    }

    private Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_edit:

                break;
            case R.id.user_avatar:

                break;
            case R.id.update:
                intent = new Intent(getApplicationContext(), UpdateActivity.class);
                startActivity(intent);

                break;
            case R.id.setting:
                intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.account:
                intent = new Intent(getApplicationContext(), MoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.collect:
                intent = new Intent(getApplicationContext(), CollectActivity.class);
                startActivity(intent);
                break;

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mTextBanner.stopViewAnimator();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
