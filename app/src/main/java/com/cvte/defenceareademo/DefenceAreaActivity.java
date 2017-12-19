package com.cvte.defenceareademo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cvte.defencearea.DFMUtils;
import com.cvte.defencearea.DefenceAreaCallback;
import com.cvte.defencearea.DefenceAreaManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO Description
 *
 * @author laizhenqi
 * @since 2017/11/27
 */
public class DefenceAreaActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.open_all)
    Button mOpenAll;

    @BindView(R.id.close_all)
    Button mCloseAll;

    @BindView(R.id.list)
    ListView mListView;
    ArrayAdapter<String> mAdapter;

    @BindView(R.id.exit)
    View mBtnExit;

    private int[] ids = new int[]{
            R.id.dal0, R.id.dal1, R.id.dal2, R.id.dal3, R.id.dal4, R.id.dal5, R.id.dal6, R.id.dal7
    };

    private DefenceAreaLayout[] mLayouts = new DefenceAreaLayout[ids.length];

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    DefenceAreaManager mDefenceAreaManager;

    private int[] statusCode = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defence_area);
        ButterKnife.bind(this);

        //==================测试防区功能==================//
        mDefenceAreaManager = DFMUtils.getSystemService(this);

        initViews();
        mDefenceAreaManager.addDefenceAreaCallback(mCallback);

    }

    private void initViews() {
        mOpenAll.setOnClickListener(this);
        mCloseAll.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
        mListView.setAdapter(mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1));

        for (int i = 0; i < ids.length; i++) {
            mLayouts[i] = (DefenceAreaLayout) findViewById(ids[i]);
            final int num = i;
            if (mLayouts[i].getState()) {
                mDefenceAreaManager.enableAlarm(num);
            }
            mDefenceAreaManager.setAlarmType(num, mLayouts[i].getType());
            statusCode[i] = mDefenceAreaManager.checkAlarmStatus(i) ? 1 : 0;
            Log.i("Test", "defencearea " + i + " : " + statusCode[i]);
            mLayouts[i].setOnSwitcherListener(new DefenceAreaLayout.OnSwitcherListener() {
                @Override
                public void onSwitched(boolean flag) {
                    if (flag) {
                        mDefenceAreaManager.enableAlarm(num);
                        mAdapter.add("打开 " + mLayouts[num].getName());
                    } else {
                        mDefenceAreaManager.disableAlarm(num);
                        mAdapter.add("关闭 " + mLayouts[num].getName());
                    }
                }
            });
            mLayouts[i].setOnModeSwitchListener(new DefenceAreaLayout.OnModeSwitchListener() {
                @Override
                public void onModeSwitched(int type) {
                    mDefenceAreaManager.setAlarmType(num, type);
                    mAdapter.add("设置 " + mLayouts[num].getName() + " 类型 " + (type == 0 ? "开路报警" : "闭路报警"));
                }
            });
        }
    }

    private void checkAllStatus() {
        for (int i = 0; i < ids.length; i++) {
            final DefenceAreaLayout dal = (DefenceAreaLayout) findViewById(ids[i]);
            if (dal.getState()) {
                mDefenceAreaManager.enableAlarm(i);
            }
            mDefenceAreaManager.setAlarmType(i, dal.getType());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            list.add(mAdapter.getItem(i));
        }
        outState.putStringArrayList("info", list);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("info") && mAdapter != null) {
            ArrayList<String> list = savedInstanceState.getStringArrayList("info");
            if (list != null) mAdapter.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDefenceAreaManager.removeDefenceAreaCallback(mCallback);
    }

    private DefenceAreaCallback mCallback = new DefenceAreaCallback() {
        @Override
        public void onAlarmHappened(int allStatus, long alarmTime) {
            // 按2位1防区读取数据
            for (int i = 0; i < 8; i++) {
                int status = DFMUtils.readStatusBinary(allStatus, i);
                // 读取当前状态，如果和之前的不一致，且为1，则触发
                if (statusCode[i] != status && status == 1) {
                    mHandler.sendEmptyMessage(i * 10 + 1);
                    mAdapter.add("防区" + i + " 触发于" + mFormat.format(new Date(alarmTime)));
                }
                statusCode[i] = status;
                if (mLayouts[i].isLightEnabled() && status == 0) {
                    // 一秒后关闭
                    mHandler.sendEmptyMessageDelayed(i * 10, 1000);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open_all) {
            mDefenceAreaManager.enableAllAlarm();
            mAdapter.add("打开所有防区");
            checkAllStatus();
        } else if (v.getId() == R.id.close_all) {
            mDefenceAreaManager.disableAllAlarm();
            mAdapter.add("关闭所有防区");
            checkAllStatus();
        } else if(v.getId() == R.id.exit) {
            finish();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            int num = what / 10;
            int opt = what % 10;
            if (opt == 0) {
                //1亮灯，0就代表1秒后关闭
                mLayouts[num].enableLight(false);
            } else {
                removeMessages(what - 1);
                mLayouts[num].enableLight(true);
            }
        }
    };
}
