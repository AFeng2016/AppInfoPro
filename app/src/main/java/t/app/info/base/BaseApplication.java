package t.app.info.base;

import android.app.Application;
import android.os.StrictMode;

import t.app.info.base.observer.DevObservableNotify;
import t.app.info.utils.DevUtils;

/**
 * detail: BaseApplication
 * Created by Ttt
 */
public class BaseApplication extends Application {

    /** 全局观察者模式 */
    public static final DevObservableNotify sDevObservableNotify = new DevObservableNotify();

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化全局上下文
        DevUtils.init(getApplicationContext());
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
