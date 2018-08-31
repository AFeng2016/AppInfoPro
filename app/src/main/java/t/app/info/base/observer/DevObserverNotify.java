package t.app.info.base.observer;

import android.app.Activity;

import java.util.UUID;

/**
 * detail: 观察者通知方法抽象类
 * Created by Ttt
 */
public abstract class DevObserverNotify extends IObserverMethod {

	// 标记id - 一定程度上唯一
	private int markId;
	// 标记TAG
	private Object tag;
	// 特殊持有保存的参数
	private Object holdObj;
	// 当前Activity
	private Activity activity;

	public DevObserverNotify(){
		this(null);
	}

	/**
	 * 初始化构造函数
	 * @param activity
	 */
	public DevObserverNotify(Activity activity){
		// 保存数据
		this.markId = UUID.randomUUID().hashCode();
		// 保存当前Activity
		this.activity = activity;
	}

	// ======== GET/SET 方法  =========

	/**
	 * 获取标记Id
	 * @return
	 */
	public int getMarkId() {
		return markId;
	}

	/**
	 * 获取标记Tag
	 * @return
	 */
	public Object getTag() {
		return tag;
	}

	/**
	 * 获取持有的对象
	 * @return
	 */
	public Object getHoldObj() {
		return holdObj;
	}

	/**
	 * 获取对应的Activity
	 * @return
	 */
	public Activity getActivity() {
		return activity;
	}

	// -

	/**
	 * 设置Tag
	 * @param tag
	 */
	public DevObserverNotify setTag(Object tag) {
		this.tag = tag;
		return this;
	}

	/**
	 * 设置持有对象
	 * @param holdObj
	 */
	public DevObserverNotify setHoldObj(Object holdObj) {
		this.holdObj = holdObj;
		return this;
	}

}