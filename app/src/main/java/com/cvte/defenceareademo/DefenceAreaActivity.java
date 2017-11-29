package com.cvte.defenceareademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private int[] ids = new int[]{
            R.id.dal0, R.id.dal1, R.id.dal2, R.id.dal3, R.id.dal4, R.id.dal5, R.id.dal6, R.id.dal7
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
        mOpenAll.setOnClickListener(this);
        mCloseAll.setOnClickListener(this);
        mListView.setAdapter(mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1));

        for (int i = 0; i < ids.length; i++) {
            final DefenceAreaLayout dal = (DefenceAreaLayout) findViewById(ids[i]);
            final int num = i;
            if(dal.getState()) {
                mDefenceAreaManager.enableAlarm(num);
            }
            mDefenceAreaManager.setAlarmType(num, dal.getType());
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

    private void checkAllStatus() {
        for (int i = 0; i < ids.length; i++) {
            final DefenceAreaLayout dal = (DefenceAreaLayout) findViewById(ids[i]);
            if(dal.getState()) {
                mDefenceAreaManager.enableAlarm(i);
            }
            mDefenceAreaManager.setAlarmType(i, dal.getType());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0 ; i < mAdapter.getCount(); i++) {
            list.add(mAdapter.getItem(i));
        }
        outState.putStringArrayList("info", list);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if( savedInstanceState !=null && savedInstanceState.containsKey("info") && mAdapter!=null ) {
            ArrayList<String> list = savedInstanceState.getStringArrayList("info");
            if(list!=null) mAdapter.addAll(list);
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
                if (status != 0) {
                    mAdapter.add("防区" + i + " 触发于" + mFormat.format(new Date(alarmTime)));
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
        }
    }
}
