package t.app.info.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * detail: 软键盘相关辅助类
 * Created by Ttt
 */
public final class KeyBoardUtils {

	private KeyBoardUtils() {
	}

	/** 默认延迟时间 */
	private static int DELAY_MILLIS = 300;
	/** 键盘显示 */
	public static final int KEYBOARD_DISPLAY = 930;
	/** 键盘隐藏 */
	public static final int KEYBOARD_HIDE = 931;


	/**
	 * 避免输入法面板遮挡 manifest.xml 中 activity 中设置
	 * android:windowSoftInputMode="adjustPan"
	 * android:windowSoftInputMode="adjustUnspecified|stateHidden"
	 */

	// == ----------------------------------------- ==

	/**
	 * 设置延迟时间
	 * @param delayMillis
	 */
	public static void setDelayMillis(int delayMillis) {
		DELAY_MILLIS = delayMillis;
	}

	// ================
	// == 打开软键盘 ==
	// ================
	
	/**
	 * 打开软键盘
	 * @param mEditText 输入框
	 */
	public static void openKeyboard(EditText mEditText) {
		if (mEditText != null) {
			try {
				InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 打开软键盘
	 * @param mEditText
	 * @param vHandler
	 */
	public static void openKeyboard(final EditText mEditText, Handler vHandler){
		openKeyboard(mEditText, vHandler, DELAY_MILLIS);
	}

	/**
	 * 打开软键盘
	 * @param mEditText
	 * @param vHandler
	 * @param delayMillis
	 */
	public static void openKeyboard(final EditText mEditText, Handler vHandler, int delayMillis){
		if (vHandler != null && mEditText != null){
			// 延迟打开
			vHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						mEditText.requestFocus();
						mEditText.setSelection(mEditText.getText().toString().length());
					} catch (Exception e){
					}
					openKeyboard(mEditText);
				}
			}, delayMillis);
		}
	}

	// -

	/**
	 * 打开软键盘
	 */
	public static void openKeyboard() {
		try {
			InputMethodManager imm = (InputMethodManager) DevUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
	}

	/**
	 * 打开软键盘
	 * @param vHandler
	 */
	public static void openKeyboard(Handler vHandler){
		openKeyboard(vHandler, DELAY_MILLIS);
	}

	/**
	 * 打开软键盘
	 * @param vHandler
	 * @param delayMillis
	 */
	public static void openKeyboard(Handler vHandler, int delayMillis){
		if (vHandler != null && DevUtils.getContext() != null){
			// 延迟打开
			vHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					openKeyboard();
				}
			}, delayMillis);
		}
	}

	// ================
	// == 关闭软键盘 ==
	// ================

	/**
	 * 关闭软键盘
	 * @param mEditText 输入框
	 */
	public static void closeKeyboard(EditText mEditText) {
		if (mEditText != null) {
			try {
				InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 关闭软键盘
	 */
	public static void closeKeyboard() {
		if (DevUtils.getContext() != null) {
			try {
				InputMethodManager imm = (InputMethodManager) DevUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 关闭软键盘
	 * @param mActivity 当前页面
	 */
	public static void closeKeyboard(Activity mActivity) {
		if (mActivity != null) {
			try {
				InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mActivity.getWindow().peekDecorView().getWindowToken(), 0);
				//imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 关闭dialog中打开的键盘
	 * @param mDialog
	 */
	public static void closeKeyboard(Dialog mDialog) {
		if (mDialog != null) {
			try {
				InputMethodManager imm = (InputMethodManager) mDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mDialog.getWindow().peekDecorView().getWindowToken(), 0);
				//imm.hideSoftInputFromWindow(mDialog.getCurrentFocus().getWindowToken(), 0);
			} catch (Exception e) {
			}
		}
	}

	// ==

	/**
	 * 关闭软键盘 - 特殊处理
	 * @param mEditText
	 * @param mDialog
	 */
	public static void closeKeyBoardSpecial(EditText mEditText, Dialog mDialog){
		try {
			// 关闭输入法
			closeKeyboard();
			// 关闭输入法
			closeKeyboard(mEditText);
			// 关闭输入法
			closeKeyboard(mDialog);
		} catch (Exception e){
		}
	}

	public static void closeKeyBoardSpecial(final EditText mEditText, final Dialog mDialog, Handler vHandler){
		closeKeyBoardSpecial(mEditText, mDialog, vHandler, DELAY_MILLIS);
	}

	/**
	 * 关闭软键盘 - 特殊处理(两个都关闭)
	 * @param mEditText
	 * @param mDialog
	 * @param vHandler
	 * @param delayMillis
	 */
	public static void closeKeyBoardSpecial(final EditText mEditText, final Dialog mDialog, Handler vHandler, int delayMillis){
		if (vHandler != null){
			// 延迟打开
			vHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					closeKeyBoardSpecial(mEditText, mDialog);
				}
			}, delayMillis);
		}
	}

	// -

	public static void closeKeyboard(final EditText mEditText, Handler vHandler){
		closeKeyboard(mEditText, vHandler, DELAY_MILLIS);
	}

	/**
	 * 关闭软键盘
	 * @param mEditText
	 * @param vHandler
	 * @param delayMillis
	 */
	public static void closeKeyboard(final EditText mEditText, Handler vHandler, int delayMillis){
		if (vHandler != null && mEditText != null){
			// 延迟打开
			vHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					closeKeyboard(mEditText);
				}
			}, delayMillis);
		}
	}

	public static void closeKeyboard(Handler vHandler){
		closeKeyboard(vHandler, DELAY_MILLIS);
	}

	/**
	 * 关闭软键盘
	 * @param vHandler
	 * @param delayMillis
	 */
	public static void closeKeyboard(Handler vHandler, int delayMillis){
		if (vHandler != null && DevUtils.getContext() != null){
			// 延迟打开
			vHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					closeKeyboard();
				}
			}, delayMillis);
		}
	}

	public static void closeKeyboard(final Activity mActivity, Handler vHandler){
		closeKeyboard(mActivity, vHandler, DELAY_MILLIS);
	}

	/**
	 * 关闭软键盘
	 * @param mActivity
	 * @param vHandler
	 * @param delayMillis
	 */
	public static void closeKeyboard(final Activity mActivity, Handler vHandler, int delayMillis){
		if (vHandler != null && mActivity != null){
			// 延迟打开
			vHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					closeKeyboard(mActivity);
				}
			}, delayMillis);
		}
	}

	public static void closeKeyboard(final Dialog mDialog, Handler vHandler){
		closeKeyboard(mDialog, vHandler, DELAY_MILLIS);
	}

	/**
	 * 关闭软键盘
	 * @param mDialog
	 * @param vHandler
	 * @param delayMillis
	 */
	public static void closeKeyboard(final Dialog mDialog, Handler vHandler, int delayMillis){
		if (vHandler != null && mDialog != null){
			// 延迟打开
			vHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					closeKeyboard(mDialog);
				}
			}, delayMillis);
		}
	}
	
	// == ----------------------------------------- ==

	// 下面暂时无法使用，缺少判断键盘是否显示，否则和自动切换无区别
	// InputMethodManager.isActive()   (无法获取)
	// Activity.getWindow().getAttributes().softInputMode  (有些版本可以，不适用)
	// ==----==

	/**
	 * 自动切换键盘状态，如果键盘显示了则隐藏，隐藏着显示
	 */
	public static void toggleKeyboard() {
		// 程序启动后，自动弹出软键盘，可以通过设置一个时间函数来实现，不能再onCreate里写
		try {
			InputMethodManager imm = (InputMethodManager) DevUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		} catch (Exception e) {
		}
	}

	// ==========  点击非EditText 则隐藏输入法  ===============

	/**
	 * 某个View里面的子View的View判断
	 * @param view
	 */
	public static void judgeView(View view, final Activity activity) {
		if (!(view instanceof EditText)) {
			view.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					closeKeyboard(activity);
					return false;
				}
			});
		}
		// --
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View innerView = ((ViewGroup) view).getChildAt(i);
				judgeView(innerView, activity);
			}
		}
	}

	// ==========  输入法隐藏显示  ===============

	/**
	 * 判断软键盘是否可见
	 * @param activity
	 * @return true : 可见, false : 不可见
	 */
	public static boolean isSoftInputVisible(final Activity activity) {
		return isSoftInputVisible(activity, 200);
	}

	/**
	 * 判断软键盘是否可见
	 * @param activity
	 * @param minHeightOfSoftInput 软键盘最小高度
	 * @return true : 可见, false : 不可见
	 */
	public static boolean isSoftInputVisible(final Activity activity, final int minHeightOfSoftInput) {
		return getContentViewInvisibleHeight(activity) >= minHeightOfSoftInput;
	}

	/**
	 * 计算View的宽度高度
	 * @param activity
	 * @return
	 */
	private static int getContentViewInvisibleHeight(final Activity activity) {
		try {
			final View contentView = activity.findViewById(android.R.id.content);
			Rect rect = new Rect();
			contentView.getWindowVisibleDisplayFrame(rect);
			return contentView.getRootView().getHeight() - rect.height();
		} catch (Exception e){
			return 0;
		}
	}

	/**
	 * 注册软键盘改变监听器
	 * @param activity
	 * @param listener listener
	 */
	public static void registerSoftInputChangedListener(final Activity activity, final OnSoftInputChangedListener listener) {
		try {
			// 获取根View
			final View contentView = activity.findViewById(android.R.id.content);
			// 添加事件
			contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if (listener != null) {
						// 获取高度
						int height = getContentViewInvisibleHeight(activity);
						// 判断是否相同
						listener.onSoftInputChanged(height >= 200, height);
					}
				}
			});
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 注册软键盘改变监听器
	 * @param activity
	 * @param listener listener
	 */
	public static void registerSoftInputChangedListener2(final Activity activity, final OnSoftInputChangedListener listener) {
		final View decorView = activity.getWindow().getDecorView();
		decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (listener != null){
					try {
						Rect rect = new Rect();
						decorView.getWindowVisibleDisplayFrame(rect);
						// 计算出可见屏幕的高度
						int displayHight = rect.bottom - rect.top;
						// 获得屏幕整体的高度
						int hight = decorView.getHeight();
						// 获得键盘高度
						int keyboardHeight = hight - displayHight;
						// 计算一定比例
						boolean visible = (double) displayHight / hight < 0.8;
						// 判断是否显示
						listener.onSoftInputChanged(visible, keyboardHeight);
					} catch (Exception e){
					}
				}
			}
		});
	}

	/** 输入法弹出、隐藏改变事件 */
	public interface OnSoftInputChangedListener {

		void onSoftInputChanged(boolean visible, int height);
	}

	// ==

	/**
	 * 修复软键盘内存泄漏 在 Activity.onDestroy() 中使用
	 * @param mContext
	 */
	public static void fixSoftInputLeaks(final Context mContext) {
		if (mContext == null) return;
		try {
			InputMethodManager imm = (InputMethodManager) DevUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			String[] strArr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
			for (int i = 0; i < 3; i++) {
				try {
					Field declaredField = imm.getClass().getDeclaredField(strArr[i]);
					if (declaredField == null) continue;
					if (!declaredField.isAccessible()) {
						declaredField.setAccessible(true);
					}
					Object obj = declaredField.get(imm);
					if (obj == null || !(obj instanceof View)) continue;
					View view = (View) obj;
					if (view.getContext() == mContext) {
						declaredField.set(imm, null);
					} else {
						return;
					}
				} catch (Throwable th) {
				}
			}
		} catch (Exception e){
		}
	}

	// =============== 输入法Key Listener 快捷处理  ==========================

	/**
	 * 限制只能输入字母和数字，默认弹出英文输入法
	 * @return
	 */
	public static DigitsKeyListener getNumberAndEnglishKeyListener() {
		/** 限制只能输入字母和数字，默认弹出英文输入法 */
		DigitsKeyListener digitsKeyListener = new DigitsKeyListener() {
			@Override
			public int getInputType() {
				return InputType.TYPE_TEXT_VARIATION_PASSWORD;
			}

			@Override
			protected char[] getAcceptedChars() {
				char[] data = "qwertyuioplkjhgfdsazxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM1234567890".toCharArray();
				return data;
			}
		};
		return digitsKeyListener;
	}

	/**
	 * 限制只能输入数字，默认弹出数字列表
	 * @return
	 */
	public static DigitsKeyListener getNumberKeyListener() {
		/** 限制只能输入数字，默认弹出数字列表 */
		DigitsKeyListener digitsKeyListener = new DigitsKeyListener() {
			@Override
			public int getInputType() {
				return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
			}

			@Override
			protected char[] getAcceptedChars() {
				char[] data = "1234567890".toCharArray();
				return data;
			}
		};
		return digitsKeyListener;
	}
}
