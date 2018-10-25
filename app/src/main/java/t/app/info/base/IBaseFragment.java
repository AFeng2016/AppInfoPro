package t.app.info.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import dev.utils.app.logger.DevLogger;

/**
 * detail: Fragment 基类控制方法
 * Created by Ttt
 */
public abstract class IBaseFragment extends Fragment {

    // =============  其他变量相关  ===============
    /** 日志Tag */
    protected String TAG = "IBaseFragment";
    /** 上下文 */
    protected Context mContext = null;
    /** 临时View,防止切回页面出现不刷新问题 */
    protected View rView;
    /** 当前页面是否可见(生命周期) */
    private boolean isFragmentVisible = true;
    /** 整个项目 Dialog - 便于控制 */
    public Dialog dialog;
    /** 整个项目 PopupWindow - 便于控制 */
    public PopupWindow popupWindow;
    // ============= 内部生命周期  ==============

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        printLogPri("onAttach");
        // 保存上下文
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        printLogPri("onDetach");
    }

    // --

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printLogPri("onCreate");
        try {
            TAG = this.getClass().getSimpleName();
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        printLogPri("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        printLogPri("onHiddenChanged - hidden : " + hidden);
        // 判断Fragment 是否可见
        isFragmentVisible = !hidden;
    }

    @Override
    public void onStart() {
        super.onStart();
        printLogPri("onStart");
        // 标记属于可见
        isFragmentVisible = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        printLogPri("onResume");
        // 标记属于可见
        isFragmentVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        printLogPri("onPause");
        // 标记属于不可见
        isFragmentVisible = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        printLogPri("onStop");
        // 标记属于不可见
        isFragmentVisible = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        printLogPri("onDestroy");
        // 标记属于不可见
        isFragmentVisible = false;
    }

    // ====== 可选择实现 ===============

    /** 初始化全部Views */
    public void initViews(){
        printLogPri("initViews()");
    }
    /** 初始化全部参数，配置等 */
    public void initValues(){
        printLogPri("initValues()");
    }
    /** 初始化绑定事件等 */
    public void initListeners(){
        printLogPri("initListeners()");
    }
    /** 初始化其他操作 - 便于拓展(并非一定使用,预留可以统一) */
    public void initOtherOperate(){
        printLogPri("initOtherOperate()");
    }

    // =========== 正常打印Log ==============

    /**
     * 统一打印日志 - 私有,内部封装调用
     * @param message 打印内容
     */
    private final void printLogPri(String message) {
        DevLogger.dTag(TAG, "%s -> %s", new Object[] { TAG, message });
    }

    // ============= 对外提供方法 ============
    /**
     * 判断Activity 是否可见状态
     * @return
     */
    protected final boolean isFragmentVisible(){
        return isFragmentVisible;
    }
}


