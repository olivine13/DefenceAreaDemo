## 八方区魔镜API #
### 1.1介绍 #
本api仅运行在部分支持防区报警功能的型号上
开发者可通过添加jar库进行相关功能开发

### 1.2用法 #

当前防区号范围为0~7

#### 1.2.1 初始化 #
在AndroidManifest.xml文件中添加权限

`<uses-permission android:name="android.permission.DEFENCE_AREA_ADMIN"/>`

获取防区管理对象
```
DefenceAreaManager mDefenceAreaManager;

@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    mDefenceAreaManager = DFMUtils.getSystemService(this);
    ...
}
```

#### 1.2.2 打开\关闭防区警报 #
打开所有防区报警


`mDefenceAreaManager.enableAllAlarm()`


关闭所有防区报警
`mDefenceAreaManager.disableAllAlarm()`
打开

`mDefenceAreaManager.enableAlarm(int alarmNum)`

关闭
`mDefenceAreaManager.disableAlarm(int alarmNum)`

#### 1.2.3 检查防区警报状态开关状态 #

`mDefenceAreaManager.checkAlarmIsEnabled(int alarmNum)`

#### 1.2.4 设置防区警报类型 #

`mDefenceAreaManager.setAlarmType(int alarmNum, int alarmType)` 

#### 1.2.5 监听防区警报是否触发 #

添加监听

`mDefenceAreaManager.addDefenceAreaCallback(DefenceAreaCallback callback)`

移除监听

`mDefenceAreaManager.removeDefenceAreaCallback(DefenceAreaCallback callback)`

防区回调接口

```
public interface DefenceAreaCallback {

    /**
     * 当有防区触发时，在此处处理响应事件
     * @param allStatus 当前防区状态
     * @param alarmTime 触发时间
     */
    void onAlarmHappened(int allStatus, long alarmTime);
}
```

收到allStatus后，使用`DFMUtil.readStatusBinary(allStatus, alarmNum)` 读取当前alarmNum状态，如未0代表未报警，1代表报警

```
// 遍历0-7读取状态
for (int i = 0; i < 8; i++) {
    int status = DFMUtils.readStatusBinary(allStatus, i);
    if (status != 0) {
        Log.d("防区" + i + " 触发");
    }
}
```

#### 1.2.6 检查防区报警状态 #

`mDefenceAreaManager.checkAlarmStatus(int alarmNum)`

#### 1.2.7 蜂鸣器开关 #

打开`mDefenceAreaManager.play()`


关闭`mDefenceAreaManager.stop()`

注册蜂鸣器监听
```
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
mDefenceAreaManager.addOnBuzzerListener(mListener);

```

移除监听
`mDefenceAreaManager.removeOnBuzzerListener(mListener)`


