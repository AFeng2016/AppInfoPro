package t.app.info.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import t.app.info.R;
import t.app.info.activitys.MainActivity;
import t.app.info.adapters.AppListAdapter;
import t.app.info.base.BaseApplication;
import t.app.info.base.BaseFragment;
import t.app.info.base.observer.DevObserverNotify;
import t.app.info.beans.AppInfoBean;
import t.app.info.utils.DevCommonUtils;
import t.app.info.utils.PreDealUtils;
import t.app.info.utils.ProUtils;
import t.app.info.utils.config.NotifyConstants;
import t.app.info.widgets.StateLayout;

/**
 * detail: App 列表 - Fragment
 * Created by Ttt
 */
public class AppListFragment extends BaseFragment {

    // ===== View =====
    @BindView(R.id.fal_recycleview)
    RecyclerView fal_recycleview;
    @BindView(R.id.fal_statelayout)
    StateLayout fal_statelayout;
    // ======== 其他对象 ========
    // App 列表类型
    private AppInfoBean.AppType mAppType = AppInfoBean.AppType.ALL;
    // 适配器
    private AppListAdapter mAppListAdapter;

    /**
     * 获取对象,并且设置数据
     * @param appType
     * @return
     */
    public static BaseFragment getInstance(AppInfoBean.AppType appType){
        AppListFragment bFragment = new AppListFragment();
        bFragment.mAppType = appType;
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
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);
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
        // 获取列表
        vHandler.sendEmptyMessage(NotifyConstants.H_QUERY_APPLIST_END_NOTIFY);
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
        try {
            // 注销观察者模式
            BaseApplication.sDevObservableNotify.unregisterObserver(TAG + mAppType.name());
        } catch (Exception e){
        }
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
        // 初始化适配器并绑定
        mAppListAdapter = new AppListAdapter(getActivity());
        fal_recycleview.setAdapter(mAppListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        fal_recycleview.setLayoutManager(manager);
    }

    @Override
    public void initListeners() {
        super.initListeners();
        // 注册观察者模式
        BaseApplication.sDevObservableNotify.registerObserver(TAG + mAppType.name(), new DevObserverNotify(getActivity()) {
            @Override
            public void onNotify(int nType, Object... args) {
                switch (nType){
                    case NotifyConstants.H_REFRESH_NOTIFY:
                        // 获取类型
                        AppInfoBean.AppType refType = AppInfoBean.AppType.ALL;
                        // 根据索引判断
                        switch (MainActivity.getMenuPos()){
                            case 0:
                                refType = AppInfoBean.AppType.USER;
                                break;
                            case 1:
                                refType = AppInfoBean.AppType.SYSTEM;
                                break;
                        }
                        // 类型相同才处理
                        if (mAppType != null && mAppType == refType) {
                            vHandler.sendEmptyMessage(NotifyConstants.H_REFRESH_NOTIFY);
                            // 发送通知
                            vHandler.sendEmptyMessage(NotifyConstants.H_QUERY_APPLIST_END_NOTIFY);
                        }
                        break;
                    case NotifyConstants.H_QUERY_APPLIST_END_NOTIFY:
                        // 发送通知
                        vHandler.sendEmptyMessage(nType);
                        break;
                    case NotifyConstants.FOR_R_APP_UNINSTALL:
                        try {
                            // 获取包名
                            String packName = (String) args[0];
                            // 属于用户类型
                            if (mAppType == AppInfoBean.AppType.USER){
                                // 进行获取
                                ArrayList<AppInfoBean> lists = ProUtils.getAppLists(mAppType);
                                // 防止为null
                                if (lists == null){
                                    return;
                                }
                                // 循环判断移除
                                for (int i = 0, len = lists.size(); i < len; i++){
                                    if (lists.get(i).getAppPackName().equals(packName)){
                                        AppInfoBean appInfoBean = lists.remove(i); // 删除并返回
                                        listSearchs.remove(appInfoBean); // 删除搜索的数据源
                                        break;
                                    }
                                }
                                // 保存新的数据
                                ProUtils.sMapAppInfos.put(mAppType, lists);
                                // 发送通知
                                vHandler.sendEmptyMessage(NotifyConstants.H_QUERY_APPLIST_END_NOTIFY);
                            }
                        } catch (Exception e){
                        }
                        break;
                    /** 切换Fragment 通知 */
                    case NotifyConstants.H_TOGGLE_FRAGMENT_NOTIFY:
                        // 合并表示不属于搜索
                        isSearch = false;
                        // 清空数据
                        listSearchs.clear();
                        break;
                    /** 搜索合并通知 */
                    case NotifyConstants.H_SEARCH_COLLAPSE:
                        // 合并表示不属于搜索
                        isSearch = false;
                        // 发送通知
                        vHandler.sendEmptyMessage(NotifyConstants.H_QUERY_APPLIST_END_NOTIFY);
                        break;
                    /** 搜索展开通知 */
                    case NotifyConstants.H_SEARCH_EXPAND:
                        // 展开表示属于搜索
                        isSearch = true;
                        // 删除旧的数据
                        listSearchs.clear();
                        break;
                    /** 搜索输入内容通知 */
                    case NotifyConstants.H_SEARCH_INPUT_CONTENT:
                        // 获取类型
                        AppInfoBean.AppType notifyType = AppInfoBean.AppType.ALL;
                        // 根据索引判断
                        switch (MainActivity.getMenuPos()){
                            case 0:
                                notifyType = AppInfoBean.AppType.USER;
                                break;
                            case 1:
                                notifyType = AppInfoBean.AppType.SYSTEM;
                                break;
                        }
                        // 类型相同才处理
                        if (mAppType != null && mAppType == notifyType) {
                            try {
                                // 删除旧的数据
                                listSearchs.clear();
                                // 进行筛选处理
                                filterAppList(ProUtils.getAppLists(mAppType), listSearchs, (String) args[0]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what = NotifyConstants.H_QUERY_APPLIST_END_NOTIFY;
                            msg.obj = args[0];
                            // 发送通知
                            vHandler.sendMessage(msg);
                        }
                        break;
                }
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
            if (PreDealUtils.isFinishingCtx(mContext)){
                return;
            }
            // 判断通知类型
            switch (msg.what){
                case NotifyConstants.H_REFRESH_NOTIFY: // 刷新通知
                    mAppListAdapter.clearData();
                    break;
                case NotifyConstants.H_QUERY_APPLIST_END_NOTIFY:
                    // 判断是否搜索
                    if (isSearch){
                        // 判断是否存在数据
                        if (listSearchs.size() != 0){
                            // 刷新状态
                            fal_statelayout.setState(StateLayout.State.QUERY_END, DevCommonUtils.length(listSearchs));
                            // 判断根据操作类型判断
                            mAppListAdapter.setListDatas(listSearchs);
                        } else { // 判断是否输入内容
                            // 判断是否存在内容
                            String searchContent = "";
                            if (msg.obj != null){
                                try {
                                    searchContent = (String) msg.obj;
                                } catch (Exception e){
                                }
                            }
                            // 判断是否为null
                            boolean isEmpty = TextUtils.isEmpty(searchContent);
                            // 显示提示
                            if (isEmpty){
                                // 获取数据源
                                ArrayList<AppInfoBean> listApps = ProUtils.getAppLists(mAppType);
                                // 判断是否存在数据
                                int size = DevCommonUtils.length(listApps, -1);
                                // 刷新状态
                                fal_statelayout.setState(((size == -1) ? StateLayout.State.REFRESH : StateLayout.State.QUERY_END), size);
                                // 判断根据操作类型判断
                                mAppListAdapter.setListDatas(listApps);
                            } else {
                                // 设置搜索没数据提示
                                fal_statelayout.setStateToSearchNoData(searchContent);
                                // 刷新数据
                                mAppListAdapter.setListDatas(new ArrayList<AppInfoBean>());
                            }
                        }
                    } else {
                        // 获取数据源
                        ArrayList<AppInfoBean> listApps = ProUtils.getAppLists(mAppType);
                        // 判断是否存在数据
                        int size = DevCommonUtils.length(listApps, -1);
                        // 刷新状态
                        fal_statelayout.setState(((size == -1) ? StateLayout.State.REFRESH : StateLayout.State.QUERY_END), size);
                        // 判断根据操作类型判断
                        mAppListAdapter.setListDatas(listApps);
                    }
                    break;
            }
        }
    };

    // == 外部开放方法 ==

    /**
     * 滑动到顶部
     */
    public void onScrollTop(){
        if (fal_recycleview != null){
            fal_recycleview.scrollToPosition(0);
        }
    }

    // = 搜索相关 =
    // 判断是否搜索
    private boolean isSearch = false;
    // 搜索数据源
    private ArrayList<AppInfoBean> listSearchs = new ArrayList<>();


    /**
     * 筛选群组列表数据
     * @param listDatas 数据源
     * @param listSearchs 筛选结果
     * @param sContent 筛选关键字
     */
    private int filterAppList(List<AppInfoBean> listDatas, List<AppInfoBean> listSearchs, String sContent){
        // 数据总数
        int size = 0;
        // 防止数据为null
        if (listDatas != null){
            // 保存临时数据 - 主要是预防搜索途中,进行加载，导致遍历List中数据源改变，导致抛出异常 ConcurrentModificationException
            ArrayList<AppInfoBean> listTemps = new ArrayList<>(listDatas);
            // 进行遍历临时数据源
            for (int i = 0, count = listTemps.size(); i < count; i++) {
                // 获取单独的实体类
                AppInfoBean appInfoBean = listTemps.get(i);
                // 判断是否包含
                if (DevCommonUtils.isContains(true, sContent, appInfoBean.getAppName(), appInfoBean.getAppPackName())){
                    // 保存数据
                    listSearchs.add(appInfoBean);
                    // 进行累加
                    size++;
                }
            }
        }
        return size;
    }
}
