package com.cvte.defenceareademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cvte.defencearea.DFMUtils;
import com.cvte.defencearea.DefenceAreaManager;

import butterknife.BindView;

/**
 * TODO Description
 *
 * @author laizhenqi
 * @since 2017/11/27
 */
public class BuzzerActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.play)
    View mBtnPlay;

    @BindView(R.id.stop)
    View mBtnStop;

    DefenceAreaManager mDefenceAreaManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzzer);

        mDefenceAreaManager = DFMUtils.getSystemService(this);
        mBtnPlay.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDefenceAreaManager.stopBuzzer();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play) {
            mDefenceAreaManager.playBuzzer();
        } else if (v.getId() == R.id.stop) {
            mDefenceAreaManager.stopBuzzer();
        }
    }
}
