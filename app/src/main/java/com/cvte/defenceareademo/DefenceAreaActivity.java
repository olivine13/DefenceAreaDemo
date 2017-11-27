package com.cvte.defenceareademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cvte.defencearea.DFMUtils;
import com.cvte.defencearea.DefenceAreaCallback;
import com.cvte.defencearea.DefenceAreaManager;

import java.text.SimpleDateFormat;
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
public class DefenceAreaActivity extends Activity {

    @BindView(R.id.list)
    ListView mListView;
    ArrayAdapter<String> mAdapter;

    private int[] ids = new int[]{
            R.id.dal1, R.id.dal2, R.id.dal3, R.id.dal4, R.id.dal5, R.id.dal6, R.id.dal7, R.id.dal8
    };

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    DefenceAreaManager mDefenceAreaManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defence_area);
        ButterKnife.bind(this);

        //==================测试防区功能==================//
        mDefenceAreaManager = DFMUtils.getSystemService(this);
        mDefenceAreaManager.addDefenceAreaCallback(mCallback);

        initViews();
    }

    private void initViews() {
        mListView.setAdapter(mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1));

        for (int i = 0; i < ids.length; i++) {
            final DefenceAreaLayout dal = (DefenceAreaLayout) findViewById(ids[i]);
            final int num = i;
            dal.enable(mDefenceAreaManager.checkAlarmIsEnabled(num));
            dal.setOnSwitcherListener(new DefenceAreaLayout.OnSwitcherListener() {
                @Override
                public void onSwitched(boolean flag) {
                    if (flag) {
                        mDefenceAreaManager.enableAlarm(num);
                        mAdapter.add("打开 " + dal.getName());
                    } else {
                        mDefenceAreaManager.disableAlarm(num);
                        mAdapter.add("关闭 " + dal.getName());
                    }
                }
            });
            dal.setOnModeSwitchListener(new DefenceAreaLayout.OnModeSwitchListener() {
                @Override
                public void onModeSwitched(int type) {
                    mDefenceAreaManager.setAlarmType(num, type);
                    mAdapter.add("设置 " + dal.getName() + " 类型 " + (type == 0 ? "开路报警" : "闭路报警"));
                }
            });
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
                if (status != 0) {
                    mAdapter.add("防区" + i + " 触发于" + mFormat.format(new Date(alarmTime)));
                }
            }
        }
    };
}
