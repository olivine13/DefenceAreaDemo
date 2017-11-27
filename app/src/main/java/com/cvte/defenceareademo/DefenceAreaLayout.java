package com.cvte.defenceareademo;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO Description
 *
 * @author laizhenqi
 * @since 2017/11/27
 */
public class DefenceAreaLayout extends LinearLayout implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    interface OnSwitcherListener {
        void onSwitched(boolean flag);
    }

    interface OnModeSwitchListener {
        void onModeSwitched(int type);
    }

    @BindView(R.id.name)
    TextView mTitle;

    @BindView(R.id.switcher)
    Switch mSwitch;

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.radioButton_open)
    RadioButton mBtnOpen;
    @BindView(R.id.radioButton_close)
    RadioButton mBtnClose;

    String mName = "防区";

    private OnSwitcherListener mOnSwitcherListener;
    private OnModeSwitchListener mOnModeSwitchListener;

    public DefenceAreaLayout(Context context) {
        this(context, null);
    }

    public DefenceAreaLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefenceAreaLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_defence_area, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DefenceAreaLayout, 0, 0);
        mName = a.getString(R.styleable.DefenceAreaLayout_defenceAreaName);
        a.recycle();
    }

    public String getName() {
        return mName;
    }

    public void enable(boolean flag) {
        mSwitch.setChecked(flag);
    }

    public void setOnSwitcherListener(OnSwitcherListener onSwitcherListener) {
        mOnSwitcherListener = onSwitcherListener;
    }

    public void setOnModeSwitchListener(OnModeSwitchListener onModeSwitchListener) {
        mOnModeSwitchListener = onModeSwitchListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        mTitle.setText(mName);

        mSwitch.setOnCheckedChangeListener(this);

        mRadioGroup.check(R.id.radioButton_open);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mOnSwitcherListener != null) {
            mOnSwitcherListener.onSwitched(isChecked);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mOnModeSwitchListener != null) {
            mOnModeSwitchListener.onModeSwitched(checkedId == R.id.radioButton_open ? 0 : 1);
        }
    }
}
