package t.app.info.base.observer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import dev.utils.app.logger.DevLogger;

/**
 * detail: 观察者抽象类
 * Created by Ttt
 */
public abstract class DevObservable<T> {

	// 日志 TAG
	private final String TAG = DevObservable.class.getSimpleName();
	/** 观察者集合 */
	protected final HashMap<Object, T> mapObservables = new HashMap<>(50);
	/** 观察者 Key 集合 - 有序 */
	private final LinkedHashMap<Object, Integer> linkedKeys = new LinkedHashMap<>(50);

	/**
	 * 添加观察者
	 * @param obj
	 * @param observer
	 */
	public final void registerObserver(final Object obj, final T observer) {
		// 防止对象为null
		if (obj != null && observer != null) {
			synchronized (mapObservables) {
				linkedKeys.remove(obj); // 移除旧的 key, 防止存在
				linkedKeys.put(obj, null); // 保存新的
				mapObservables.put(obj, observer);
			}
		}
	}

	/**
	 * 根据 key 注销观察者监听
	 * @param obj
	 */
	public final void unregisterObserver(final Object obj){
		// 防止对象为null
		if (obj != null){
			synchronized (mapObservables){
				linkedKeys.remove(obj); // 移除旧的 key
				mapObservables.remove(obj);
			}
		}
	}

	/**
	 * 根据 value 注销观察者监听
	 * @param observer
	 */
	public final void unregisterObserverValue(final T observer) {
		// 防止对象为null
		if (observer != null) {
			synchronized (mapObservables) {
				try {
					Iterator<Map.Entry<Object, T>> iterator = mapObservables.entrySet().iterator();
					while (iterator.hasNext()){
						Map.Entry<Object, T> entry = iterator.next();
						T value = entry.getValue();
						if (value != null && value == observer){
							linkedKeys.remove(entry.getKey()); // 移除旧的 key
							iterator.remove(); // 进行移除
							return;
						}
					}
				} catch (Exception e){
				}
			}
		}
	}

	/**
	 * 移除全部观察者
	 */
	public final void unregisterAll() {
		synchronized (mapObservables) {
			linkedKeys.clear();
			mapObservables.clear();
		}
	}

	// =

	/**
	 * 获取监听的观察者总数
	 * @return
	 */
	public final int getObservableSize(){
		return mapObservables.size();
	}

	/**
	 * 获取最先监听的观察者
	 * @return
	 */
	public final T getStackHead(){
		synchronized (mapObservables) {
			try {
				// 获取Key
				Object key = getHead(linkedKeys).getKey();
				// 获取最先绑定的观察者
				return mapObservables.get(key);
			} catch (Exception e) {
				DevLogger.eTag(TAG, e, "getStackHead");
			}
		}
		return null;
	}

	/**
	 * 获取最尾监听的观察者
	 * @return
	 */
	public final T getStackTail(){
		synchronized (mapObservables) {
			try {
				// 获取Key
				Object key = getTailByReflection(linkedKeys).getKey();
				// 获取最后绑定的观察者
				return mapObservables.get(key);
			} catch (Exception e) {
				DevLogger.eTag(TAG, e, "getStackTail");
			}
		}
		return null;
	}

	// ==

	/**
	 * 获取第一位
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	private <K, V> Map.Entry<K, V> getHead(LinkedHashMap<K, V> map) {
		return map.entrySet().iterator().next();
	}

	/**
	 * 通过反射获取最后一位
	 * http://bookshadow.com/weblog/2016/10/27/java-linked-hash-map-get-first-and-get-last/
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private <K, V> Map.Entry<K, V> getTailByReflection(LinkedHashMap<K, V> map)
			throws NoSuchFieldException, IllegalAccessException {
		Field tail = map.getClass().getDeclaredField("tail");
		tail.setAccessible(true);
		return (Map.Entry<K, V>) tail.get(map);
	}

	/**
	 * 获取最后一位(遍历)
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	private <K, V> Map.Entry<K, V> getTail(LinkedHashMap<K, V> map) {
		Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
		Map.Entry<K, V> tail = null;
		while (iterator.hasNext()) {
			tail = iterator.next();
		}
		return tail;
	}
}