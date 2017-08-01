package com.alivc.utilssample;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    ScreenOffReceiver mScreenOffReceiver = new ScreenOffReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter mScreenOffFilter = new IntentFilter();
        mScreenOffFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenOffReceiver, mScreenOffFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}