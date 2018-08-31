package t.app.info.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * detail: dp，px，sp转换、View获取宽高等
 * Created by Ttt
 */
public final class SizeUtils {

	private SizeUtils() {
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @param dpValue
	 */
	public static float dipConvertPx(float dpValue) {
		try {
			float scale = DevUtils.getContext().getResources().getDisplayMetrics().density;
			return (float) (dpValue * scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1f;
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * @param pxValue
	 */
	public static int pxConvertDip(float pxValue) {
		try {
			float scale = DevUtils.getContext().getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 * @param pxValue
	 */
	public static int pxConvertSp(float pxValue) {
		try {
			float scale = DevUtils.getContext().getResources().getDisplayMetrics().scaledDensity;
			return (int) (pxValue / scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px
	 * @param spValue
	 */
	public static float spConvertPx(float spValue) {
		try {
			float scale = DevUtils.getContext().getResources().getDisplayMetrics().scaledDensity;
			return (float) (spValue * scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1f;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 第二种
	 * @param dpValue
	 */
	public static int dipConvertPx2(float dpValue) {
		try {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, DevUtils.getContext().getResources().getDisplayMetrics());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px 第二种
	 * @param spValue
	 */
	public static int spConvertPx2(float spValue) {
		try {
			// android.util.TypedValue
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, DevUtils.getContext().getResources().getDisplayMetrics());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 各种单位转换 - 该方法存在于 TypedValue
	 * @param unit 单位
	 * @param value 值
	 * @param metrics DisplayMetrics
	 * @return 转换结果
	 */
	public static float applyDimension(final int unit, final float value, final DisplayMetrics metrics) {
		switch (unit) {
			case TypedValue.COMPLEX_UNIT_PX:
				return value;
			case TypedValue.COMPLEX_UNIT_DIP:
				return value * metrics.density;
			case TypedValue.COMPLEX_UNIT_SP:
				return value * metrics.scaledDensity;
			case TypedValue.COMPLEX_UNIT_PT:
				return value * metrics.xdpi * (1.0f / 72);
			case TypedValue.COMPLEX_UNIT_IN:
				return value * metrics.xdpi;
			case TypedValue.COMPLEX_UNIT_MM:
				return value * metrics.xdpi * (1.0f / 25.4f);
		}
		return -1;
	}

	// ==

	/**
	 * 在 onCreate 中获取视图的尺寸 - 需回调 onGetSizeListener 接口，在 onGetSize 中获取 view 宽高
	 * 用法示例如下所示
	 * SizeUtils.forceGetViewSize(view, new SizeUtils.onGetSizeListener() {
	 *     Override
	 *     public void onGetSize(final View view) {
	 *         view.getWidth();
	 *     }
	 * });
	 * @param view     视图
	 * @param listener 监听器
	 */
	public static void forceGetViewSize(final View view, final onGetSizeListener listener) {
		view.post(new Runnable() {
			@Override
			public void run() {
				if (listener != null) {
					listener.onGetSize(view);
				}
			}
		});
	}

	/** 获取到 View 尺寸的监听 */
	public interface onGetSizeListener {
		void onGetSize(View view);
	}

	// ==

	/**
	 * 测量视图尺寸
	 * @param view 视图
	 * @return arr[0]: 视图宽度, arr[1]: 视图高度
	 */
	public static int[] measureView(final View view) {
		try {
			ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
			if (layoutParams == null) {
				layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, layoutParams.width);
			int lpHeight = layoutParams.height;
			int heightSpec;
			if (lpHeight > 0) {
				heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
			} else {
				heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			}
			view.measure(widthSpec, heightSpec);
			return new int[]{ view.getMeasuredWidth(), view.getMeasuredHeight() };
		} catch (Exception e){
			return new int[] { 0, 0 };
		}
	}

	/**
	 * 获取测量视图宽度
	 * @param view 视图
	 * @return 视图宽度
	 */
	public static int getMeasuredWidth(final View view) {
		return measureView(view)[0];
	}

	/**
	 * 获取测量视图高度
	 * @param view 视图
	 * @return 视图高度
	 */
	public static int getMeasuredHeight(final View view) {
		return measureView(view)[1];
	}
}

