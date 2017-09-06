package com.jiangdg.connectbutton;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.jiangdg.circleprogressview.CircleProgressView;

public class MainActivity extends Activity {
    private final int WHAT_START = 0x00;
    private final int WHAT_STOP = 0x01;
    private final int WHAT_RUNNING = 0x02;
    private CircleProgressView mProgressView;
    private CircleProgressView mProgressView1;
    private CircleProgressView mProgressView2;
    private int progress;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case WHAT_START:
                    mProgressView.setConnectState(CircleProgressView.STATE_DOING);
                    break;
                case WHAT_RUNNING:
                    mProgressView.setProgressVaule(progress);
                    break;
                case WHAT_STOP:
                    mProgressView.setConnectState(CircleProgressView.STATE_DONE);
                    break;
            }
        }
    };
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressView = (CircleProgressView) findViewById(R.id.progressView);
        mProgressView.setShowTextTipFlag(true);
        mProgressView.setTotalSize(100);
        mProgressView1 = (CircleProgressView) findViewById(R.id.progressView1);
        mProgressView2 = (CircleProgressView) findViewById(R.id.progressView2);


        mProgressView.setOnViewClickListener(new CircleProgressView.OnViewClickListener() {
            @Override
            public void onViewClick() {
                // 第3个
                mProgressView2.setConnectState(CircleProgressView.STATE_DONE);
                // 第2个
                mProgressView1.setConnectState(CircleProgressView.STATE_DOING);
                mProgressView1.setProgressVaule(CircleProgressView.NONE);
                // 第1个
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(WHAT_START);
                        while (progress<100){
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progress++;
                            mHandler.sendEmptyMessage(WHAT_RUNNING);
                            if(progress == 100){
                                progress = 0;
                                mHandler.sendEmptyMessage(WHAT_STOP);
                                break;
                            }
                        }
                    }
                }).start();
            }
        });
    }
}
