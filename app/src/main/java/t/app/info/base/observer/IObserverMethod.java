package t.app.info.base.observer;

/**
 * detail: 观察者监听通知方法
 * Created by Ttt
 */
public abstract class IObserverMethod {

    /**
     * 默认通知方法(统一实现,便于统一操作)
     * @param nType 通知类型
     * @param args 参数
     */
    public abstract void onNotify(int nType, Object... args);

    /**
     * 网络状态改变通知
     * @param nType 通知类型
     * @param args 参数
     */
    public void onNetworkState(int nType, Object... args){
    }
}
