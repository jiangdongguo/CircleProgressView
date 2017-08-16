package com.jiangdg.connectbutton;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.jiangdg.circleprogressview.CircleProgressView;

public class MainActivity extends AppCompatActivity {
    private final int WHAT_START = 0x00;
    private final int WHAT_STOP = 0x01;
    private final int WHAT_RUNNING = 0x02;
    private CircleProgressView mProgressView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressView = (CircleProgressView) findViewById(R.id.progressView);
//        mProgressView.setTotalSize(100);
//        mProgressView.setShowTextTipFlag(true);

        mProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressView.setConnectState(CircleProgressView.STAE_DOING);
                mProgressView.setProgressVaule(CircleProgressView.NONE);
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
