package com.wutianlong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener {

    private Intent intent = null;

    //    摇一摇权限不能忘
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_yaoyiyao).setOnClickListener(this);
        findViewById(R.id.main_yaoyiyao1).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_yaoyiyao:
                //普通
                intent = new Intent(this, TestSensorActivity.class);
                startActivity(intent);
                break;
            case R.id.main_yaoyiyao1:
                //优化：避免了多次发送信息
                intent = new Intent(this, OptimizeSensorActivity.class);
                startActivity(intent);
                break;
        }
    }
}
