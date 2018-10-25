package t.app.info.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.utils.app.assist.manager.ActivityManager;
import dev.utils.app.share.SharedUtils;
import dev.utils.app.toast.ToastUtils;
import t.app.info.R;
import t.app.info.base.BaseApplication;
import t.app.info.base.BaseFragment;
import t.app.info.base.observer.DevObserverNotify;
import t.app.info.dialogs.AppSortDialog;
import t.app.info.dialogs.QuerySuffixDialog;
import t.app.info.utils.ProUtils;
import t.app.info.utils.QuerySuffixUtils;
import t.app.info.utils.config.KeyConstants;
import t.app.info.utils.config.NotifyConstants;

/**
 * detail: 设置信息 - Fragment
 * Created by Ttt
 */
public class SettingFragment extends BaseFragment {

    // ===== View =====
    @BindView(R.id.fs_appsort_linear)
    LinearLayout fs_appsort_linear;
    @BindView(R.id.fs_appsort_tv)
    TextView fs_appsort_tv;
    @BindView(R.id.fs_scanapk_linear)
    LinearLayout fs_scanapk_linear;
    @BindView(R.id.fs_reset_linear)
    LinearLayout fs_reset_linear;
    // ======== 其他对象 ========
    // 获取排序数据
    private String[] appSortArys;

    /**
     * 获取对象,并且设置数据
     */
    public static BaseFragment getInstance(){
        SettingFragment bFragment = new SettingFragment();
        return bFragment;
    }

    // ==

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rView != null){
            ViewGroup parent = (ViewGroup) rView.getParent();
            // 删除以及在显示的View,防止切回来不加载,一边空白
            if (parent != null) {
                parent.removeView(rView);
            }
            return rView;
        }
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mContext = view.getContext();
        // 保存View
        rView = view;
        // 初始化View
        ButterKnife.bind(this, view);
        // ====
        initViews();
        initValues();
        initListeners();
        initOtherOperate();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            onPause();
            onStop();
        } else {
            onStart();
            onResume();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(this.isHidden()){
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.isHidden()){
            return;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销观察者模式
        BaseApplication.sDevObservableNotify.unregisterObserver(TAG);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
        }
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void initValues() {
        super.initValues();
        // 获取数据
        appSortArys = mContext.getResources().getStringArray(R.array.appSortArys);
        // 进行排序
        selectAppSort();

    }

    @Override
    public void initListeners() {
        super.initListeners();
        // 注册观察者模式
        BaseApplication.sDevObservableNotify.registerObserver(TAG, new DevObserverNotify(getActivity()) {
            @Override
            public void onNotify(int nType, Object... args) {
                switch (nType){
                    case NotifyConstants.H_APP_SORT_NOTIFY:
                        // 发送通知
                        vHandler.sendEmptyMessage(nType);
                        break;
                }
            }
        });
        // 点击排序
        fs_appsort_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示Dialog
                new AppSortDialog(mContext).showDialog();
            }
        });
        // 设置扫描APK后缀字段
        fs_scanapk_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示Dialog
                new QuerySuffixDialog(mContext).showDialog();
            }
        });
        // 恢复默认设置
        fs_reset_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 保存索引
                SharedUtils.put(KeyConstants.KEY_APP_SORT, 0);
                // 清空后缀
                QuerySuffixUtils.reset();
                // 通知刷新
                vHandler.sendEmptyMessage(NotifyConstants.H_APP_SORT_NOTIFY);
                // 进行提示
                ToastUtils.showShort(mContext, R.string.reset_desetting_suc);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // ==

    /** View 操作Handler */
    Handler vHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 如果页面已经关闭,则不进行处理
            if (ActivityManager.isFinishingCtx(mContext)){
                return;
            }
            // 判断通知类型
            switch (msg.what){
                case NotifyConstants.H_APP_SORT_NOTIFY:
                    // 重置清空数据
                    ProUtils.reset();
                    // 进行排序
                    selectAppSort();
                    break;
            }
        }
    };

    // == 外部开放方法 ==

    /** 选择App 排序 */
    private void selectAppSort(){
        // 更新文案
        fs_appsort_tv.setText(appSortArys[ProUtils.getAppSortType()]);
    }
}
