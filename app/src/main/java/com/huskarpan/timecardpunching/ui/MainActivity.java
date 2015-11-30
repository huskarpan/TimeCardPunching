package com.huskarpan.timecardpunching.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huskarpan.common.view.TopBar;
import com.huskarpan.timecardpunching.R;
import com.huskarpan.timecardpunching.bll.OneDayPunchingInfo;
import com.huskarpan.timecardpunching.bll.PunchingBll;
import com.huskarpan.timecardpunching.entities.PunchingInfoDetail;
import com.lidroid.xutils.util.LogUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Huskar on 2015/9/22.
 *
 * @description 打卡主界面
 */
public class MainActivity extends AppCompatActivity {


    private TopBar mTopBar;
    private RecyclerView mPunchingInfoDetails;
    private PunchingInfoDetailAdapter mDataAdapter;
    private OneDayPunchingInfo todayPunchingInfo;
    private PunchingBll mPunchingBll;
    private List<PunchingInfoDetail> details;
    private FloatingActionButton fabPunching;
    private TextView tvCurrentDate;
    private TextView tvPunchingStatus;
    private TextView tvPunchingTotalHours;
    private NumberFormat totalHourFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        updateView();
    }

    private void initView() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#220000FF"));
        mTopBar = (TopBar) findViewById(R.id.tb_bar);
        mTopBar.setOnRightButtonOnClickListener(new TopBar.IRightButtonOnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_enter, R.anim.activity_left_exit);
            }
        });

        tvCurrentDate = (TextView) findViewById(R.id.tv_currentDate);
        tvPunchingStatus = (TextView) findViewById(R.id.tv_punching_status);
        tvPunchingTotalHours = (TextView) findViewById(R.id.tv_punching_total_hours);

        mPunchingInfoDetails = (RecyclerView) findViewById(R.id.rv_punchingInfoDetails);
        initData();

        mDataAdapter = new PunchingInfoDetailAdapter(this, details);
        mDataAdapter.setOnItemClickListener(new PunchingInfoDetailAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View itemView, int position) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mPunchingInfoDetails.setAdapter(mDataAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPunchingInfoDetails.setLayoutManager(linearLayoutManager);

        mPunchingInfoDetails.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mPunchingInfoDetails.setItemAnimator(new DefaultItemAnimator());

        fabPunching = (FloatingActionButton) findViewById(R.id.fab_punching);
        fabPunching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PunchingInfoDetail detail = mPunchingBll.addPunchingInfoDetail(
                            todayPunchingInfo.getRecord().getId(), new Date());
                    todayPunchingInfo.getDetails().add(detail);
                    mDataAdapter.Add(detail);
                    mPunchingInfoDetails.smoothScrollToPosition(mDataAdapter.getItemCount()-1);
                    if (detail.getRecordType() == PunchingInfoDetail.RECORD_TYPE_OFF_DUTY) {
                        todayPunchingInfo = mPunchingBll.getOneDayPunchingInfo(
                                new Date(todayPunchingInfo.getRecord().date));
                    } else {
                        todayPunchingInfo.getRecord().setTotalHours(
                                mPunchingBll.calculateTotalHours(
                                        todayPunchingInfo.getRecord().date,todayPunchingInfo.getDetails()));
                    }
                    updateView();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void initData() {
        totalHourFormat = NumberFormat.getNumberInstance();
        totalHourFormat.setMaximumFractionDigits(2);
        mPunchingBll = new PunchingBll();
        todayPunchingInfo = mPunchingBll.getTodayPunchingInfo();
        details = new ArrayList<PunchingInfoDetail>();
        for (PunchingInfoDetail detail : todayPunchingInfo.getDetails()) {
            details.add(detail);
        }
    }

    private void updateView(){
        tvCurrentDate.setText(todayPunchingInfo.getRecord().getDateFormat(this));
        boolean isOnDuty = mPunchingBll.isOnDuty(todayPunchingInfo.getDetails());
        tvPunchingStatus.setText(getString(isOnDuty ? R.string.punching_status_on_duty : R.string.punching_status_off_duty));
        double totalHours = todayPunchingInfo.getRecord().getTotalHours();
        LogUtils.d("totalHours:"+totalHours);
        tvPunchingTotalHours.setText(totalHours == 0 ? "0" : totalHourFormat.format(totalHours));
        fabPunching.setImageResource(isOnDuty ? R.mipmap.ic_stop_white_24dp : R.mipmap.ic_play_circle_outline_white_24dp);
//        fabPunching.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
        int backgroundTintColor=getResources().getColor(isOnDuty?R.color.color_accent:R.color.color_accent2);
        fabPunching.setBackgroundTintList(ColorStateList.valueOf(backgroundTintColor));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_add) {
            LogUtils.d("add");
        }else if (id == R.id.action_delete) {
            mDataAdapter.Delete(0);
        }

        return super.onOptionsItemSelected(item);
    }


}
