package com.cvte.defenceareademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.cvte.defencearea.DFMUtils;
import com.cvte.defencearea.DefenceAreaCallback;
import com.cvte.defencearea.DefenceAreaManager;
import com.cvte.defencearea.OnBuzzerListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.input_num)
    EditText mInputNum;

    @BindView(R.id.input_type)
    EditText mInputType;

    @BindView(R.id.open_all)
    View mBtnOpenAll;

    @BindView(R.id.close_all)
    View mBtnCloseAll;

    @BindView(R.id.open)
    View mBtnOpen;

    @BindView(R.id.close)
    View mBtnClose;

    @BindView(R.id.query_enabled)
    View mBtnQueryEnabled;

    @BindView(R.id.query_state)
    View mBtnQueryState;

    @BindView(R.id.set)
    View mBtnSet;

    @BindView(R.id.play)
    View mBtnPlay;

    @BindView(R.id.stop)
    View mBtnStop;

    @BindView(R.id.list)
    ListView mListView;
    ArrayAdapter<String> mAdapter;

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    DefenceAreaManager mDefenceAreaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //==================测试防区功能==================//
        mDefenceAreaManager = DFMUtils.getSystemService(this);
        mDefenceAreaManager.addDefenceAreaCallback(mCallback);
        mDefenceAreaManager.addOnBuzzerListener(mListener);

        initViews();
    }

    private DefenceAreaCallback mCallback = new DefenceAreaCallback() {
        @Override
        public void onAlarmHappened(int allStatus, long alarmTime) {
            // 按2位1防区读取数据
            for (int i = 0; i < 8; i++) {
                int status = DFMUtils.readStatusBinary(allStatus, i);
                if (status != 0) {
                    mAdapter.add("防区" + i + " 触发于" + mFormat.format(new Date(alarmTime)));
                }
            }
        }
    };

    private OnBuzzerListener mListener = new OnBuzzerListener() {
        @Override
        public void onStart() {
            mAdapter.add("蜂鸣器打开了");
        }

        @Override
        public void onStop() {
            mAdapter.add("蜂鸣器关闭了");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDefenceAreaManager.removeDefenceAreaCallback(mCallback);
        mDefenceAreaManager.removeOnBuzzerListener(mListener);
    }

    private void initViews() {
        mBtnOpenAll.setOnClickListener(this);
        mBtnCloseAll.setOnClickListener(this);
        mBtnOpen.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mBtnQueryEnabled.setOnClickListener(this);
        mBtnQueryState.setOnClickListener(this);
        mBtnSet.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);

        mListView.setAdapter(mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1));
    }

    @Override
    public void onClick(View v) {
        int num = 0;
        int type = 0;
        try {
            num = Integer.parseInt(mInputNum.getText().toString());
        } catch (Exception ignored) {
        }
        try {
            type = Integer.parseInt(mInputType.getText().toString());
        } catch (Exception ignored) {
        }
        if (v.getId() == R.id.open_all) {
            mDefenceAreaManager.enableAllAlarm();
            mAdapter.add("打开所有防区");
        } else if (v.getId() == R.id.close_all) {
            mDefenceAreaManager.disableAllAlarm();
            mAdapter.add("关闭所有防区");
        } else if (v.getId() == R.id.open) {
            mDefenceAreaManager.enableAlarm(num);
            mAdapter.add("打开防区" + num);
        } else if (v.getId() == R.id.close) {
            mDefenceAreaManager.disableAlarm(num);
            mAdapter.add("关闭防区" + num);
        } else if (v.getId() == R.id.query_enabled) {
            mAdapter.add("查询防区" + num + " 是否打开:" + (mDefenceAreaManager.checkAlarmIsEnabled(num) ? "enabled" : "disabled"));
        } else if (v.getId() == R.id.query_state) {
            mAdapter.add("查询防区" + num + " 状态:" + (mDefenceAreaManager.checkAlarmStatus(num) ? "1" : "0"));
        } else if (v.getId() == R.id.set) {
            mDefenceAreaManager.setAlarmType(num, type);
            mAdapter.add("设置防区" + num + " 类型:" + (type == 0 ? "开路报警" : "短路报警"));
        } else if (v.getId() == R.id.play) {
            mDefenceAreaManager.playBuzzer();
            mAdapter.add("打开蜂鸣器");
        } else if (v.getId() == R.id.stop) {
            mDefenceAreaManager.stopBuzzer();
            mAdapter.add("关闭蜂鸣器");
        }
    }
}
