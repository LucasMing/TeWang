package com.qing.tewang.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.app.ExitAppUtils;
import com.qing.tewang.fragment.ActivityFragment;
import com.qing.tewang.fragment.BaseFragment;
import com.qing.tewang.fragment.MessageFragment;
import com.qing.tewang.fragment.MyFragment;
import com.qing.tewang.fragment.NewVoiceFragment;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.Location;
import com.qing.tewang.model.MessageResult;
import com.qing.tewang.model.User;
import com.qing.tewang.util.AMapUtil;
import com.qing.tewang.util.DisplayUtils;
import com.qing.tewang.util.SPUtils;
import com.roughike.bottombar.BadgeContainer;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.tencent.bugly.beta.Beta;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private int mCurrentTab = -1;
    private TextView mMoney, mNumber;
    private View mRedPacket;
    private BottomBar mBottomBar;


    private List<Class<? extends BaseFragment>> fragmentClasses =
            Arrays.asList(NewVoiceFragment.class, MessageFragment.class, MyFragment.class, ActivityFragment.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_main);
        Beta.checkUpgrade();


        mBottomBar = findViewById(R.id.bottomBar);
        mBottomBar.setActiveTabColor(getResources().getColor(R.color.main_blue));
        setImageSize(mBottomBar);

        mRedPacket = findViewById(R.id.red_packet);
        mMoney = findViewById(R.id.money);
        mNumber = findViewById(R.id.number);

        setMoneyView();

        mFragmentManager = getSupportFragmentManager();


        for (int i = 0; i < fragmentClasses.size(); i++) {
            if (mFragmentManager.findFragmentByTag(i + "") == null) {
                try {
                    mFragments.add(i, fragmentClasses.get(i).newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mFragments.add(i, mFragmentManager.findFragmentByTag(i + ""));
            }
            //先重置所有fragment的状态为隐藏，彻底解决重叠问题
            if (mFragments.get(i).isAdded()) {
                mFragmentManager.beginTransaction()
                        .hide(mFragments.get(i))
                        .commitAllowingStateLoss();
            }
        }


        mBottomBar.setOnTabSelectListener(tabId -> {
            if (tabId == R.id.tab_location) {
                showTab(0);
            } else if (tabId == R.id.tab_message) {
                if (SPUtils.isLogin(getApplicationContext())) {
                    showTab(1);
                } else {
                    mBottomBar.selectTabAtPosition(0);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            } else if (tabId == R.id.tab_me) {

                if (SPUtils.isLogin(getApplicationContext())) {
                    showTab(2);
                } else {
                    mBottomBar.selectTabAtPosition(0);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            } else if (tabId == R.id.tab_record) {
                showTab(3);
            }
        });

        showTab(mCurrentTab);
        checkPerm();

    }

    private void loadData() {
        if(SPUtils.isLogin(getApplicationContext())){
            APIWrapper.getUnReadMessage()
                    .subscribe(new SimpleObserver<CommonData<MessageResult>>(getActivity()) {
                        @Override
                        public void onNext(CommonData<MessageResult> data) {

                            if (data.getErrno() == 0 && data.getData() != null) {
                                int systemNum = data.getData().getUnReadNum();
                                int packetNum = data.getData().getUnReadRedPacketNum();
                                if ((systemNum + packetNum) > 0) {
                                    if (mMsgView == null) {
                                        showBadge();
                                    } else {
                                        mMsgView.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    if (mMsgView != null) {
                                        mMsgView.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    });
        }


    }

    private View mMsgView;

    private void showBadge() {
        BottomBarTab msgTab = findViewById(R.id.tab_message);
        mMsgView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_badge, null);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mMsgView.setLayoutParams(params);


        ViewGroup tabContainer = (ViewGroup) msgTab.getParent();
        tabContainer.removeView(msgTab);

        final BadgeContainer badgeContainer = new BadgeContainer(getApplicationContext());
        badgeContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        badgeContainer.addView(msgTab);
        badgeContainer.addView(mMsgView);

        tabContainer.addView(badgeContainer, 2);

        badgeContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                badgeContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                AppCompatImageView iconView = msgTab.findViewById(R.id.bb_bottom_bar_icon);
                ViewGroup.LayoutParams params = mMsgView.getLayoutParams();

                int size = Math.max(mMsgView.getWidth(), mMsgView.getHeight());
                float xOffset = (float) (iconView.getWidth() / 1.5);

                mMsgView.setX(iconView.getX() + xOffset);
                mMsgView.setTranslationY(16);

                if (params.width != size || params.height != size) {
                    params.width = size;
                    params.height = size;
                    mMsgView.setLayoutParams(params);
                }
            }
        });
    }


    private void setImageSize(View bottomBar) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                DisplayUtils.getInstance().dp2px(getApplicationContext(), 34),
                DisplayUtils.getInstance().dp2px(getApplicationContext(), 34));

        AppCompatImageView location = bottomBar.findViewById(R.id.tab_location).findViewById(R.id.bb_bottom_bar_icon);
        location.setLayoutParams(params);
        AppCompatImageView record = bottomBar.findViewById(R.id.tab_record).findViewById(R.id.bb_bottom_bar_icon);
        record.setLayoutParams(params);
        AppCompatImageView message = bottomBar.findViewById(R.id.tab_message).findViewById(R.id.bb_bottom_bar_icon);
        message.setLayoutParams(params);
        AppCompatImageView me = bottomBar.findViewById(R.id.tab_me).findViewById(R.id.bb_bottom_bar_icon);
        me.setLayoutParams(params);

    }


    public void showTab(int index) {
        if (mCurrentTab == index)
            return;
        Fragment f = mFragments.get(index);
        if (mCurrentTab != -1)
            mFragments.get(mCurrentTab).onPause();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        mCurrentTab = index;

        if (f.isAdded()) {
            f.onResume();
        } else {
            ft.add(R.id.contentContainer, f, index + "");
        }

        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            if (index == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
        }
        ft.commitAllowingStateLoss();
    }


    private static boolean isExit;
    private final Handler mHandler = new MyHandler(this);


    private static class MyHandler extends Handler {
        private final WeakReference<Activity> reference;

        private MyHandler(Activity activity) {
            this.reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (reference.get() != null) {
                isExit = false;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final int ACCESS_COARSE_LOCATION = 101;


    private void startLocation() {
        AMapUtil map = AMapUtil.getInstance();
        map.setLocationListener(location -> {
            if (location != null) {
                double lng = location.getLongitude();
                double lat = location.getLatitude();
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
                                } else {
                                    showToast(data.getMsg());
                                }
                            }
                        });

                map.stopLocation();
                map.destroyLocation();
            } else {
                showToast("请打开定位！");
            }
        });
        map.startLocation();
    }


    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(),
                    "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            ExitAppUtils.getInstance().exit();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapUtil.getInstance().destroyLocation();
        ExitAppUtils.getInstance().exit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 0 || requestCode == 1) && mFragments != null && mFragments.size() > 2) {
            mFragments.get(2).onActivityResult(requestCode, resultCode, data);
        }
    }


    public void setMoneyView() {
        if (!SPUtils.isLogin(getApplicationContext())) {
            mRedPacket.setVisibility(View.GONE);
        } else {
            User user = SPUtils.getUser(getApplicationContext());
            mMoney.setText(String.format(Locale.CHINA, "￥%.2f",
                    Integer.parseInt(user.getBalance()) / 100.f));
            mNumber.setText(user.getRed_packet_num() + "个");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMoneyView();
        loadData();
    }


    public void checkPerm() {

        String[] params = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};


        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "申请运行所必须的权限！", ACCESS_COARSE_LOCATION, params);
        } else {
            startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startLocation();
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
                            finish();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .create().show();

        } else {
            finish();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Exception e) {
            savedInstanceState = null;
        }
    }




}
