package com.alivc.utilssample;

import android.os.Bundle;
import android.view.WindowManager;

import com.alivc.utilssample.widgets.UnderView;

//锁屏页面
public class LockScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉系统锁屏页面
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        //页面在锁屏时能显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_lock_screen);

        ((UnderView) findViewById(R.id.underView)).bindView(findViewById(R.id.tv), this);
    }
}
