package com.cvte.defencearea;

public abstract class DefenceAreaManager {

    public abstract boolean enableAllAlarm();

    public abstract boolean disableAllAlarm();

    public abstract boolean enableAlarm(int alarmNum);

    public abstract boolean disableAlarm(int alarmNum);

    public abstract boolean checkAlarmIsEnabled(int alarmNum);

    public abstract boolean checkAlarmStatus(int alarmNum);

    public abstract boolean setAlarmType(int alarmNum, int alarmType);

    public abstract void addDefenceAreaCallback(DefenceAreaCallback listener);

    public abstract void removeDefenceAreaCallback(DefenceAreaCallback listener);

}