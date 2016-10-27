package com.wutianlong;

import android.app.Activity;
import android.app.Service;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;



public class OptimizeSensorActivity extends Activity implements SensorEventListener {

    /** 传感器管理类 */
    private SensorManager sensorManager;
    /** 震动器 */
    private Vibrator vibrator;

    /** 摇一摇动画 */
    private AnimationDrawable animationDrawable;
    private ImageView animation_yaoyiyao;

    //手机状态时候在摇动中
    private boolean isShake = false;

    //开始摇动
    private static final int SHAKE_START = 1;
    //结束摇动
    private static final int SHAKE_END = 2;

    /** 处理每次摇动结束的事件 */
    private Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHAKE_START:

                    isShake = true;//摇动中
                    startAnimation();

                    break;
                case SHAKE_END://摇动结束

                    isShake = false;
                    stopAnimation();
                    Toast.makeText(OptimizeSensorActivity.this, "检测到摇晃，执行你想要的操作！", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yaoyiyao_test1_layout);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        animation_yaoyiyao= (ImageView) findViewById(R.id.yaoyiyao_test1_img);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册监听器
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        // values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = sensorEvent.values;
        Log.e("YaoYiYao", "values[0]:" + values[0] + "      values[1]:" + values[0] + "     values[2]" + values[0]);
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {

            if ((Math.abs(values[0]) + Math.abs(values[1])+ Math
                    .abs(values[2])) > 18) {
                // 摇动手机后，再伴随震动提示~~
                if(!isShake){

                    vibrator.vibrate(new long[]{200, 300, 200, 300, 200}, -1);
                    //发送一个空消息，告诉handler已经开始摇动了
                    handler.sendEmptyMessage(SHAKE_START);

                    //间隔1.2s,发送一个空消息，告诉handler已经结束摇动了//避免了多次请求处理
                    handler.sendEmptyMessageDelayed(SHAKE_END, 1200);
                }

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // 当传感器精度改变时回调该方法，Do nothing.
    }
    //启动摇一摇的动画
    public void startAnimation() {
        animation_yaoyiyao.setImageResource(R.drawable.animation_yaoyiyao_item);
        animationDrawable = (AnimationDrawable) animation_yaoyiyao.getDrawable();
        animationDrawable.start();

    }

    //停止摇一摇的动画
    public void stopAnimation() {
        animationDrawable = (AnimationDrawable) animation_yaoyiyao.getDrawable();
        if(animationDrawable!=null&&animationDrawable.isRunning()){
            animationDrawable.stop();
        }

    }
}
