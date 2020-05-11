package com.qing.tewang.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qing.tewang.R;
import com.qing.tewang.adapter.ScrollPagerAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.util.FileUtils;
import com.qing.tewang.widget.ScrollViewPager;
import com.qing.tewang.widget.SmartTitleBar;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;


public class PhotoViewActivity extends BaseActivity {

    private int position;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);
        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());
        TextView rightText = titleBar.getRightTextView();


        ScrollViewPager viewPager = findViewById(R.id.photo_scroll);


        ArrayList<View> views = new ArrayList<>();
        ArrayList<String> urlStr = getIntent().getStringArrayListExtra("urls");
        position = getIntent().getIntExtra("position", 0);

        rightText.setVisibility(View.VISIBLE);
        rightText.setText("保存");
        rightText.setOnClickListener(v -> {
            APIWrapper.downloadFile(urlStr.get(position))
                    .subscribe(new SimpleObserver<ResponseBody>(getActivity()) {
                        @Override
                        public void onNext(ResponseBody responseBody) {
                            //系统相册目录
                            String galleryPath = Environment.getExternalStorageDirectory()
                                    + File.separator + Environment.DIRECTORY_DCIM
                                    + File.separator + "Camera" + File.separator;
                            File dir = new File(galleryPath);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }

                            File file = new File(galleryPath, System.currentTimeMillis() + ".jpg");
                            FileUtils.writeResponseBodyToDisk(responseBody, file);

                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri uri = Uri.fromFile(file);
                            intent.setData(uri);
                            sendBroadcast(intent);
                            showToast("保存成功！");
                        }
                    });
        });


        if (urlStr != null) {
            for (int i = 0; i < urlStr.size(); i++) {
                View v = View.inflate(this, R.layout.item_photoview, null);
                views.add(v);
            }
            ScrollPagerAdapter adapter = new ScrollPagerAdapter(PhotoViewActivity.this, views, urlStr);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);
        } else
            finish();
    }


}
