package t.app.info.base.observer;

import java.util.Iterator;
import java.util.Map;

/**
 * detail: 观察者模式，通知类
 * Created by Ttt
 */
public final class DevObservableNotify extends DevObservable<DevObserverNotify> {

	/**
	 * 默认通知方法(统一实现,便于统一操作)
	 * @param nType 通知类型
	 * @param args 参数
	 */
	public void onNotify(int nType, Object... args) {
		synchronized (mapObservables) {
			try {
				Iterator<Map.Entry<Object, DevObserverNotify>> iterator = mapObservables.entrySet().iterator();
				while (iterator.hasNext()){
					Map.Entry<Object, DevObserverNotify> entry = iterator.next();
					DevObserverNotify value = entry.getValue();
					if (value != null){
						value.onNotify(nType, args);
					}
				}
			} catch (Exception e){
			}
		}
	}
}
