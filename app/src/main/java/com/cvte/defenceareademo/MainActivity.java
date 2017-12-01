package com.cvte.defenceareademo;

import android.content.Intent;
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

    @BindView(R.id.defence_area)
    View mBtnDefenceArea;

    @BindView(R.id.buzzer)
    View mBtnBuzzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        mBtnDefenceArea.setOnClickListener(this);
        mBtnBuzzer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.defence_area) {
            Intent act = new Intent(this, DefenceAreaActivity.class);
            startActivity(act);
        } else if(v.getId() == R.id.buzzer) {
            Intent act = new Intent(this, BuzzerActivity.class);
            startActivity(act);
        }
    }
}
