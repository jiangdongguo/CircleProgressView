package com.jiangdg.connectbutton;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.jiangdg.circleprogressview.CircleProgressView;

public class MainActivity extends AppCompatActivity {
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
                    mProgressView.setConnectState(CircleProgressView.STAE_DOING);
                    break;
                case WHAT_RUNNING:
                    mProgressView.setProgressVaule(progress);
                    break;
                case WHAT_STOP:
                    mProgressView.setConnectState(CircleProgressView.STAE_DONE);
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
        mProgressView1 = (CircleProgressView) findViewById(R.id.progressView1);
        mProgressView2 = (CircleProgressView) findViewById(R.id.progressView2);

        mProgressView1.setOnViewClickListener(new CircleProgressView.OnViewClickListener() {
            @Override
            public void onViewClick() {
                mProgressView1.setConnectState(CircleProgressView.STAE_DOING);
                mProgressView1.setTotalSize(100);
                mProgressView1.setShowTextTipFlag(true);
                mProgressView1.setProgressVaule(i);
                i++;
            }
        });

        mProgressView.setOnViewClickListener(new CircleProgressView.OnViewClickListener() {
            @Override
            public void onViewClick() {
                mProgressView.setConnectState(CircleProgressView.STAE_DOING);
                mProgressView.setProgressVaule(CircleProgressView.NONE);

                mProgressView1.setConnectState(CircleProgressView.STAE_DOING);
                mProgressView1.setTotalSize(100);
                mProgressView1.setShowTextTipFlag(true);
                mProgressView1.setProgressVaule(30);

                mProgressView2.setConnectState(CircleProgressView.STAE_DONE);
            }
        });

        mProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mHandler.sendEmptyMessage(WHAT_START);
//                        while (progress<100){
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            progress++;
//                            mHandler.sendEmptyMessage(WHAT_RUNNING);
//                            if(progress == 100){
//                                progress = 0;
//                                mHandler.sendEmptyMessage(WHAT_STOP);
//                                break;
//                            }
//                        }
//                    }
//                }).start();
            }
        });
    }
}
