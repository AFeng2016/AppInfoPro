package t.app.info.utils;

import android.view.View;
import android.widget.ImageView;

/**
 * detail: View 操作相关工具类
 * Created by Ttt
 */
public final class ViewUtils {

    private ViewUtils() {
    }

    /**
     * 判断View 是否为null
     * @param view
     * @return
     */
    public static boolean isEmpty(View view){
        return view == null;
    }

    /**
     * 判断View 是否为null
     * @param views
     * @return
     */
    public static boolean isEmpty(View... views){
        if (views != null && views.length != 0){
            for (int i = 0, len = views.length; i < len; i++){
                View view = views[i];
                if (view == null){
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 判断View 是否显示
     * @param view
     * @return
     */
    public static boolean isVisibility(View view){
        return isVisibility(view, true);
    }

    /**
     * 判断View 是否显示
     * @param view
     * @param isDf
     * @return
     */
    public static boolean isVisibility(View view, boolean isDf){
        if (view != null){
            // 判断是否显示
            return (view.getVisibility() == View.VISIBLE);
        }
        // 出现意外返回默认值
        return isDf;
    }

    /**
     * 判断View 是否显示
     * @param views
     * @return
     */
    public static boolean isVisibilitys(View... views){
        if (views != null && views.length != 0){
            for (int i = 0, len = views.length; i < len; i++){
                View view = views[i];
                if (view != null && view.getVisibility() == View.VISIBLE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断View 是否隐藏占位
     * @param view
     * @return
     */
    public static boolean isVisibilityIN(View view){
        return isVisibilityIN(view, false);
    }

    /**
     * 判断View 是否隐藏占位
     * @param view
     * @param isDf
     * @return
     */
    public static boolean isVisibilityIN(View view, boolean isDf){
        if (view != null){
            // 判断是否显示
            return (view.getVisibility() == View.INVISIBLE);
        }
        // 出现意外返回默认值
        return isDf;
    }

    /**
     * 判断View 是否隐藏
     * @param view
     * @return
     */
    public static boolean isVisibilityGone(View view){
        return isVisibilityGone(view, false);
    }

    /**
     * 判断View 是否隐藏
     * @param view
     * @param isDf
     * @return
     */
    public static boolean isVisibilityGone(View view, boolean isDf){
        if (view != null){
            // 判断是否显示
            return (view.getVisibility() == View.GONE);
        }
        // 出现意外返回默认值
        return isDf;
    }

    // ==

    /**
     * 获取显示的状态 (View.VISIBLE : View.GONE)
     * @param isVisibility
     * @return
     */
    public static int getVisibility(boolean isVisibility){
        return isVisibility ? View.VISIBLE : View.GONE;
    }

    /**
     * 获取显示的状态 (View.VISIBLE : View.INVISIBLE)
     * @param isVisibility
     * @return
     */
    public static int getVisibilityIN(boolean isVisibility){
        return isVisibility ? View.VISIBLE : View.INVISIBLE;
    }

    // --

    /**
     * 设置View显示状态
     * @param isVisibility
     * @param view
     */
    public static void setVisibility(boolean isVisibility, View view){
        if (view != null){
            view.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置View显示的状态
     * @param isVisibility
     * @param view
     */
    public static void setVisibility(int isVisibility, View view){
        if (view != null){
            view.setVisibility(isVisibility);
        }
    }

    // --

    /**
     * 设置View 显示的状态
     * @param isVisibility
     * @param views
     */
    public static void setVisibilitys(boolean isVisibility, View... views){
        setVisibilitys(getVisibility(isVisibility), views);
    }

    /**
     * 设置View 显示的状态
     * @param isVisibility
     * @param views
     */
    public static void setVisibilitys(int isVisibility, View... views){
        if (views != null && views.length != 0){
            for (int i = 0, len = views.length; i < len; i++){
                View view = views[i];
                if (view != null){
                    view.setVisibility(isVisibility);
                }
            }
        }
    }

    /**
     * 切换View 显示的状态
     * @param view
     * @param views
     */
    public static void toggleVisibilitys(View view, View... views){
        if (view != null){
            view.setVisibility(View.VISIBLE);
        }
        setVisibilitys(View.GONE, views);
    }

    /**
     * 切换View 显示的状态
     * @param viewArys
     * @param views
     */
    public static void toggleVisibilitys(View[] viewArys, View... views){
        toggleVisibilitys(viewArys, View.GONE, views);
    }

    /**
     * 切换View 显示的状态
     * @param viewArys
     * @param status
     * @param views
     */
    public static void toggleVisibilitys(View[] viewArys, int status, View... views){
        // 默认前面显示
        setVisibilitys(View.VISIBLE, viewArys);
        // 更具状态处理
        setVisibilitys(status, views);
    }

    // ==

    /**
     * 切换View状态
     * @param isChange 是否改变
     * @param isVisibility 是否显示
     * @param view 需要判断的View
     * @return
     */
    public static boolean toogleView(boolean isChange, int isVisibility, View view){
        if (isChange && view != null){
            view.setVisibility(isVisibility);
        }
        return isChange;
    }

    // --

    /**
     * 设置View 图片资源
     * @param draw
     * @param views
     */
    public static void setViewImageRes(int draw, ImageView... views){
        setViewImageRes(draw, View.VISIBLE, views);
    }

    /**
     * 设置View 图片资源
     * @param draw
     * @param isVisibility
     * @param views
     */
    public static void setViewImageRes(int draw, int isVisibility, ImageView... views){
        if (views != null && views.length != 0){
            for (int i = 0, len = views.length; i < len; i++){
                ImageView view = views[i];
                if (view != null){
                    try {
                        // 设置背景
                        view.setImageResource(draw);
                        // 是否显示
                        view.setVisibility(isVisibility);
                    } catch (Exception e){
                    }
                }
            }
        }
    }

    // == 初始化View操作等 ==

    /**
     * 初始化View
     * @param view
     * @param id
     * @param <T>
     * @return
     */
    public static <T extends View> T findViewById(View view, int id) {
        return view.findViewById(id);
    }
}
