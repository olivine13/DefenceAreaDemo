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

    private String mName = "防区";
    private boolean mState = true;
    private int mType = 0;

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
        mState = a.getBoolean(R.styleable.DefenceAreaLayout_open, true);
        mType = a.getInt(R.styleable.DefenceAreaLayout_type, 0);
        a.recycle();
    }

//    @Nullable
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable state = super.onSaveInstanceState();
//        SavedState ss = new SavedState(state);
//        ss.name = mName;
//        ss.state = mState;
//        ss.type = mType;
//        return ss;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        if( !(state instanceof SavedState)) {
//            super.onRestoreInstanceState(state);
//            return;
//        }
//        SavedState ss = (SavedState)state;
//        super.onRestoreInstanceState(ss.getSuperState());
//
//        mName = ss.name;
//        mState = ss.state;
//        mType = ss.type;
//    }

    public String getName() {
        return mName;
    }

    public boolean getState() {
        return mState;
    }

    public int getType() {
        return mType;
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
        mSwitch.setChecked(mState);

        mRadioGroup.check(mType == 0 ? R.id.radioButton_open : R.id.radioButton_close);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mOnSwitcherListener != null) {
            mState = isChecked;
            mOnSwitcherListener.onSwitched(isChecked);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mOnModeSwitchListener != null) {
            mType = checkedId == R.id.radioButton_open ? 0 : 1;
            mOnModeSwitchListener.onModeSwitched(mType);
        }
    }

//    static class SavedState extends BaseSavedState {
//        String name;
//        boolean state;
//        int type;
//
//        SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        private SavedState(Parcel in) {
//            super(in);
//            this.name = in.readString();
//            this.state = in.readInt() == 1;
//            this.type = in.readInt();
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeString(this.name);
//            out.writeInt(this.state ? 1 : 0);
//            out.writeInt(this.type);
//        }
//
//        //required field that makes Parcelables from a Parcel
//        public static final Parcelable.Creator<SavedState> CREATOR =
//                new Parcelable.Creator<SavedState>() {
//                    public SavedState createFromParcel(Parcel in) {
//                        return new SavedState(in);
//                    }
//                    public SavedState[] newArray(int size) {
//                        return new SavedState[size];
//                    }
//                };
//    }
}
