package me.dgy.countdown;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义时分秒倒计时
 * create by dgy on 2017年12月15日 11:08:59
 */

@SuppressLint("HandlerLeak")
public class CountDownTimerView extends LinearLayout {

    // 天
    private TextView tv_day;
    // 小时
    private TextView tv_hour;
    // 分钟
    private TextView tv_min;
    // 秒
    private TextView tv_sec;

    private TextView tv_day_link,tv_hour_link,tv_min_link,tv_sec_link;

    private ViewGroup parentview;

    private Context context;
    private int day;
    private int hour;
    private int min;
    private int sec;
    // 计时器
    private Timer timer;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        };
    };

    private int textColor;
    private float textSize;
    private String dayLink,hourLink,minuteLink,secondLink;//时分秒中间连接字符
    private String endTips;

    public CountDownTimerView(Context context) {
        this(context, null, 0);
    }

    public CountDownTimerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public CountDownTimerView(Context context,AttributeSet attrs ,int defstylesAttr){
        super(context,attrs,defstylesAttr);

        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.parentview= (ViewGroup) inflater.inflate(R.layout.view_countdowntimer, this);
        tv_hour = (TextView) parentview.findViewById(R.id.tv_hour);
        tv_day = (TextView) parentview.findViewById(R.id.tv_day);
        tv_min = (TextView) parentview.findViewById(R.id.tv_min);
        tv_sec = (TextView) parentview.findViewById(R.id.tv_sec);
        tv_day_link= (TextView) parentview.findViewById(R.id.tv_day_link);
        tv_hour_link= (TextView) parentview.findViewById(R.id.tv_hour_link);
        tv_min_link= (TextView) parentview.findViewById(R.id.tv_min_link);
        tv_sec_link= (TextView) parentview.findViewById(R.id.tv_sec_link);

        /**
         *
         */
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.CountDownTimerViewLayout, defstylesAttr, 0);

        textColor=t.getColor(R.styleable.CountDownTimerViewLayout_text_color, 0);
        textSize=t.getDimension(R.styleable.CountDownTimerViewLayout_text_size,30)/2;
        dayLink=t.getString(R.styleable.CountDownTimerViewLayout_day_link);
        if (dayLink==null){
            dayLink=":";
        }
        hourLink=t.getString(R.styleable.CountDownTimerViewLayout_hour_link);
        if (hourLink==null){
            hourLink=":";
        }
        minuteLink=t.getString(R.styleable.CountDownTimerViewLayout_minute_link);
        if (minuteLink==null){
            minuteLink=":";
        }
        secondLink=t.getString(R.styleable.CountDownTimerViewLayout_second_link);
        if (secondLink==null){
            secondLink="";
        }
        endTips=t.getString(R.styleable.CountDownTimerViewLayout_end_tips);
        if (endTips==null){
            endTips="";
        }
        setLinkTexts();
        setTextColorandSize();

    }

    /**
     * @Description 设置连接字符
     */
    private void setLinkTexts(){
        tv_day_link.setText(dayLink);
        tv_hour_link.setText(hourLink);
        tv_min_link.setText(minuteLink);
        tv_sec_link.setText(secondLink);
    }
    /**
     * @Description 设置所有文字字体和颜色
     */
    private void setTextColorandSize(){
        LinearLayout linearLayout= (LinearLayout) parentview.getChildAt(0);
        for(int i=0;i<linearLayout.getChildCount();i++){
            TextView text= (TextView) linearLayout.getChildAt(i);
            if (textColor!=0) {
                text.setTextColor(textColor);
            }
            text.setTextSize(textSize);
        }
    }
    /**
     * 
     * @Description: 开始计时
     * @param
     * @return void
     * @throws
     */
    public void start() {

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    /**
     * 
     * @Description: 停止计时
     * @param
     * @return void
     * @throws
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void setTime(long tiem) {
        //按照传入的格式生成一个simpledateformate对象
        long nd = 1000*24*60*60;//一天的毫秒数
        long nh = 1000*60*60;//一小时的毫秒数
        long nm = 1000*60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {
//获得两个时间的毫秒时间差异
        int day = (int) (tiem/nd);//计算差多少天
        int hour = (int) (tiem%nd/nh);//计算差多少小时
        int min = (int) (tiem%nd%nh/nm);//计算差多少分钟
        int sec = (int) (tiem%nd%nh%nm/ns);//计算差多少秒
        setTime(day, hour, min, sec);

    }

    /**
     * @throws Exception
     * 
     * @Description: 设置倒计时的时长
     * @param
     * @return void
     * @throws
     */
    public void setTime(int day, int hour, int min, int sec) {
        if (hour >= 24 || min >= 60 || sec >= 60 || day < 0
                || hour < 0 || min < 0 || sec < 0) {
            throw new RuntimeException(
                    "Time format is error,please check out your code");
        }
        // day 的十位数
        this.day = day;

        this.hour= hour;

        this.min = min ;

        this.sec = sec ;
        // 第个time 进行初始化
        timeClean();

    }

    /**
     * 根据两个时间判断相差彬设置值
     * @param startime
     * @param endtime
     */
    public void setTime(Timestamp startime,Timestamp endtime){
        long time;
        if (startime.getTime()>endtime.getTime()){
            time=0;
        }else if(endtime.getTime()>startime.getTime()){
            time=endtime.getTime()-startime.getTime();
        }else{
            time=0;
        }
        setTime(time);
    }

    private void setEndTips() {
        if (endTips == null || endTips.equals("")) {
            tv_day.setText("00");
            tv_hour.setText("00");
            tv_min.setText("00");
            tv_sec.setText("00");
            setLinkTexts();
        } else {
            tv_day.setText(endTips);
            tv_hour.setText("");
            tv_min.setText("");
            tv_sec.setText("");
            tv_day_link.setText("");
            tv_hour_link.setText("");
            tv_min_link.setText("");
            tv_sec_link.setText("");
        }
    }

    private void timeClean() {
        tv_day.setText((day<10?("0"+day):day)+ "");
        tv_hour.setText((hour<10?("0"+hour):hour) + "");
        tv_min.setText((min<10?("0"+min):min) + "");
        tv_sec.setText((sec<10?("0"+sec):sec)  + "");
        setLinkTexts();
    }

    /**
     * 
     * @Description: 倒计时
     * @param
     * @return boolean
     * @throws
     */
    public Boolean countDown() {
        if (isCarry(tv_sec)){
            if (isCarry(tv_min)){
                if (isCarryHour(tv_hour)){
                    if (isCarryDay(tv_day)){
                        setEndTips();
                        stop();
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @Description: 判断分秒数是否可再减
     * @param
     * @return boolean
     * @throws
     */
    private boolean isCarry(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 59;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time<10?("0"+time):time + "");
            return false;
        }

    }

    /**
     *
     * @Description: 判断小时是否可再减
     * @param
     * @return boolean
     * @throws
     */
    private boolean isCarryHour(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 23;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time<10?("0"+time):time + "");
            return false;
        }

    }

    /**
     * 判断天是否可再减
     * 
     * @param
     * @return boolean
     * @throws
     */
    private boolean isCarryDay(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 0;
            tv.setText(time + "0");
            return true;
        } else {
            tv.setText(time<10?("0"+time):time + "");
            return false;
        }

    }


    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        setTextColorandSize();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        setTextColorandSize();
    }

    public String getDayLink() {
        return dayLink;
    }

    public void setDayLink(String dayLink) {
        this.dayLink = dayLink;
        setLinkTexts();
    }

    public String getHourLink() {
        return hourLink;
    }

    public void setHourLink(String hourLink) {
        this.hourLink = hourLink;
        setLinkTexts();
    }

    public String getMinuteLink() {
        return minuteLink;
    }

    public void setMinuteLink(String minuteLink) {
        this.minuteLink = minuteLink;
        setLinkTexts();
    }

    public String getSecondLink() {
        return secondLink;
    }

    public void setSecondLink(String secondLink) {
        this.secondLink = secondLink;
        setLinkTexts();
    }
}
