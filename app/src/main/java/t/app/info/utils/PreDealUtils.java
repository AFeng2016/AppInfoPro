package t.app.info.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.PopupWindow;

/**
 * detail: 预处理操作工具类
 * Created by Ttt
 */
public class PreDealUtils {

    // 日志TAG
    private static final String TAG = "PreDealUtils";

    // == 页面判断处理 ==

    /**
     * 判断页面是否关闭
     * @param activity
     * @return
     */
    public static boolean isFinishing(Activity activity){
        if (activity != null){
            return activity.isFinishing();
        }
        return false;
    }

    /**
     * 判断页面是否关闭
     * @param mContext
     * @return
     */
    public static boolean isFinishingCtx(Context mContext){
        if (mContext != null){
            try {
                return ((Activity) mContext).isFinishing();
            } catch (Exception e){
            }
        }
        return false;
    }

    // ======== Dialog 相关 ========

    /**
     * 创建Dialog
     * @param mContext
     * @param title
     * @param content
     * @return
     */
    public static ProgressDialog creDialog(Context mContext, String title, String content){
        return creDialog(mContext, title, content, false);// 不可以使用返回键
    }

    /**
     * 创建Dialog
     * @param mContext
     * @param title
     * @param content
     * @param isCancel
     * @return
     */
    public static ProgressDialog creDialog(Context mContext, String title, String content, boolean isCancel){
        ProgressDialog progressDialog = ProgressDialog.show(mContext, title, content);
        progressDialog.setCancelable(isCancel);
        return progressDialog;
    }

    /**
     * 创建Dialog
     * @param mContext
     * @param title
     * @param content
     * @return
     */
    public static ProgressDialog createDialog(Context mContext, String title, String content){
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(title);
        progressDialog.setMessage(content);
        progressDialog.setCancelable(false); // 不可以使用返回键
        return progressDialog;
    }


    /**
     * 关闭Dialog
     * @param dialog
     */
    public static void closeDialog(Dialog dialog){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    /**
     * 关闭多个Dialog
     * @param dialogs
     */
    public static void closeDialogs(Dialog... dialogs){
        if (dialogs != null && dialogs.length != 0){
            for (int i = 0, c = dialogs.length; i < c; i++){
                // 获取Dialog
                Dialog dialog = dialogs[i];
                // 关闭Dialog
                if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }
    }

    /**
     * 关闭PopupWindow
     * @param popupWindow
     */
    public static void closePopupWindow(PopupWindow popupWindow){
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    /**
     * 关闭多个PopupWindow
     * @param popupWindows
     */
    public static void closePopupWindows(PopupWindow... popupWindows){
        if (popupWindows != null && popupWindows.length != 0){
            for (int i = 0, c = popupWindows.length; i < c; i++){
                // 获取Dialog
                PopupWindow popupWindow = popupWindows[i];
                // 关闭Dialog
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        }
    }

    /**
     * 转换SDK版本
     * @param sdkVersion
     * @return
     */
    public static String convertSDKVersion(int sdkVersion){
        // https://www.cnblogs.com/maogefff/p/7819076.html
        switch (sdkVersion){
            case 1:
                return "Android 1.0";
            case 2:
                return "Android 1.1";
            case 3:
                return "Android 1.5";
            case 4:
                return "Android 1.6";
            case 5:
                return "Android 2.0";
            case 6:
                return "Android 2.0.1";
            case 7:
                return "Android 2.1.x";
            case 8:
                return "Android 2.2.x";
            case 9:
                return "Android 2.3.0-2";
            case 10:
                return "Android 2.3.3-4";
            case 11:
                return "Android 3.0.x";
            case 12:
                return "Android 3.1.x";
            case 13:
                return "Android 3.2";
            case 14:
                return "Android 4.0.0-2";
            case 15:
                return "Android 4.0.3-4";
            case 16:
                return "Android 4.1.x";
            case 17:
                return "Android 4.2.x";
            case 18:
                return "Android 4.3";
            case 19:
                return "Android 4.4";
            case 20:
                return "Android 4.4W";
            case 21:
                return "Android 5.0";
            case 22:
                return "Android 5.1";
            case 23:
                return "Android 6.0";
            case 24:
                return "Android 7.0";
            case 25:
                return "Android 7.1.1";
            case 26:
                return "Android 8.0";
        }
        return "unknown";
    }
}
