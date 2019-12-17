package com.retrohttp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.httplibrary.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtil.setDebug(true);
        LogUtil.i("=======我是通讯log=========");
    }

}
