package com.dgy.countdowntime;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Timestamp;
import java.util.Date;

import me.dgy.countdown.CountDownTimerView;

public class MainActivity extends AppCompatActivity {

    private CountDownTimerView countDownTimerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countDownTimerView= (CountDownTimerView) findViewById(R.id.countdown);
        countDownTimerView.setTextColor(Color.GREEN);
        long nd = 1000*24*60*60;//一天的毫秒数
        countDownTimerView.setTime(new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()+nd*2));
        countDownTimerView.start();
    }
}
