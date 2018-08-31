package t.app.info.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import t.app.info.utils.share.SharedUtils;

/**
 * detail: 开发工具类
 * Created by Ttt
 */
public final class DevUtils {

    /** 禁止构造对象,保证只有一个实例 */
    private DevUtils() {
    }

    // ---

    /** 全局 Application 对象 */
    private static Application sApplication;
    /** 全局上下文 - getApplicationContext() */
    private static Context sContext;
    /** 获取当前线程,主要判断是否属于主线程 */
    private static Thread sUiThread;
    /** 全局Handler,便于子线程快捷操作等 */
    private static Handler sHandler;

    /**
     * 默认初始化方法 - 必须调用 - Application.onCreate 中调用
     * @param mContext 上下文
     */
    public static void init(Context mContext) {
        // 设置全局上下文
        initContext(mContext);
        // 初始化全局 Application
        initApplication(mContext);
        // 初始化Shared 工具类
        SharedUtils.init(mContext);
        // 保存当前线程信息
        sUiThread = Thread.currentThread();
        // 初始化全局Handler - 主线程
        sHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 初始化全局上下文
     * @param mContext
     */
    private static void initContext(Context mContext) {
        // 如果为null, 才进行判断处理
        if (DevUtils.sContext == null){
            // 防止传进来的为null
            if (mContext == null) {
                return;
            }
            DevUtils.sContext = mContext.getApplicationContext();
        }
    }

    /**
     * 初始化全局 Application
     * @param mContext
     */
    private static void initApplication(Context mContext) {
        // 如果为null, 才进行判断处理
        if (DevUtils.sApplication == null){
            if (mContext == null){
                return;
            }
            Application mApplication = null;
            try {
                mApplication = (Application) mContext.getApplicationContext();
            } catch (Exception e){
            }
            // 防止传进来的为null
            if (mApplication == null) {
                return;
            }
            DevUtils.sApplication = mApplication;
        }
    }

    /**
     * 获取全局上下文
     * @return
     */
    public static Context getContext() {
        return DevUtils.sContext;
    }

    /**
     * 获取上下文(判断null,视情况返回全局上下文)
     * @param mContext
     */
    public static Context getContext(Context mContext) {
        // 进行判断
        if (mContext != null){
            return mContext;
        }
        return DevUtils.sContext;
    }

    /**
     * 获取全局 Application
     * @return
     */
    public static Application getApplication(){
        return DevUtils.sApplication;
    }

    /**
     * 获取Handler
     * @return
     */
    public static Handler getHandler(){
        if (sHandler == null){
            // 初始化全局Handler - 主线程
            sHandler = new Handler(Looper.getMainLooper()); //Looper.myLooper();
        }
        return sHandler;
    }

    /**
     * 执行UI 线程任务 =>  Activity 的 runOnUiThread(Runnable)
     * @param action 若当前非UI线程则切换到UI线程执行
     */
    public static void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != sUiThread) {
            sHandler.post(action);
        } else {
            action.run();
        }
    }

    /**
     * 执行UI 线程任务 => 延时执行
     * @param action
     * @param delayMillis
     */
    public static void runOnUiThread(Runnable action, long delayMillis){
        sHandler.postDelayed(action, delayMillis);
    }
}
