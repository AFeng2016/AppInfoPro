package t.app.info.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import t.app.info.R;
import t.app.info.adapters.DeviceInfoAdapter;
import t.app.info.base.BaseApplication;
import t.app.info.base.BaseFragment;
import t.app.info.base.observer.DevObserverNotify;
import t.app.info.beans.DeviceInfoBean;
import t.app.info.beans.item.DeviceInfoItem;
import t.app.info.utils.DeviceInfoUtils;
import t.app.info.utils.FileUtils;
import t.app.info.utils.PreDealUtils;
import t.app.info.utils.ScreenUtils;
import t.app.info.utils.SizeUtils;
import t.app.info.utils.ToastUtils;
import t.app.info.utils.config.NotifyConstants;
import t.app.info.utils.config.ProConstants;

/**
 * detail: 屏幕信息 - Fragment
 * Created by Ttt
 */
public class ScreenInfoFragment extends BaseFragment {

    // ===== View =====
    @BindView(R.id.fsi_recycleview)
    RecyclerView fsi_recycleview;
    // ======== 其他对象 ========
    // 适配器
    private DeviceInfoAdapter mDeviceInfoAdapter;
    // 获取设备信息
    private ArrayList<DeviceInfoItem> mListDeviceInfos = new ArrayList<>();

    /**
     * 获取对象,并且设置数据
     */
    public static BaseFragment getInstance(){
        ScreenInfoFragment bFragment = new ScreenInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_screen_info, container, false);
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
        // 发送请求获取
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取设备信息
                getDeviceInfos();
            }
        }).start();
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
        // 初始化适配器并绑定
        mDeviceInfoAdapter = new DeviceInfoAdapter(getActivity());
        fsi_recycleview.setAdapter(mDeviceInfoAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        fsi_recycleview.setLayoutManager(manager);
    }

    @Override
    public void initListeners() {
        super.initListeners();
        // 注册观察者模式
        BaseApplication.sDevObservableNotify.registerObserver(TAG, new DevObserverNotify(getActivity()) {
            @Override
            public void onNotify(int nType, Object... args) {
                switch (nType){
                    case NotifyConstants.H_EXPORT_DEVICE_MSG_NOTIFY:
                        // 发送通知
                        vHandler.sendEmptyMessage(nType);
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
                case NotifyConstants.H_QUERY_DEVICE_INFO_END_NOTIFY:
                    // 刷新适配器
                    mDeviceInfoAdapter.setListDatas(mListDeviceInfos);
                    break;
                case NotifyConstants.H_EXPORT_DEVICE_MSG_NOTIFY:
                    // 导出数据
                    boolean result = FileUtils.saveFile(ProConstants.EXPORT_PATH, "screeninfo.txt", DeviceInfoBean.obtain(mListDeviceInfos));
                    // 获取提示内容
                    String tips = mContext.getString(result ? R.string.export_suc : R.string.export_fail);
                    // 判断结果
                    if (result){
                        // 拼接保存路径
                        tips += " " + ProConstants.EXPORT_PATH + "screeninfo.txt";
                    }
                    // 提示结果
                    ToastUtils.showShort(mContext, tips);
                    break;
            }
        }
    };

    // == 外部开放方法 ==

    /**
     * 滑动到顶部
     */
    public void onScrollTop(){
        if (fsi_recycleview != null){
            fsi_recycleview.scrollToPosition(0);
        }
    }

    /** 获取手机屏幕信息 */
    private void getDeviceInfos(){
        // 获取手机尺寸
        // https://blog.csdn.net/lincyang/article/details/42679589

        // 设备信息
        HashMap<String, String> mapDeviceInfos = new HashMap<>();
        // 进行初始化获取
        DeviceInfoUtils.getDeviceInfo(mapDeviceInfos);
        mListDeviceInfos.clear();
        // 获取屏幕尺寸(英寸)
        mListDeviceInfos.add(new DeviceInfoItem(R.string.screen, DeviceInfoUtils.getScreenSizeOfDevice() + ""));
        // 获取屏幕分辨率
        mListDeviceInfos.add(new DeviceInfoItem(R.string.screen_size, DeviceInfoUtils.getScreenSize() + ""));
        // 获取屏幕高度
        mListDeviceInfos.add(new DeviceInfoItem(R.string.height_pixels, ScreenUtils.getScreenHeight() + ""));
        // 获取屏幕宽度
        mListDeviceInfos.add(new DeviceInfoItem(R.string.width_pixels, ScreenUtils.getScreenWidth() + ""));
        // 获取 X轴 dpi
        mListDeviceInfos.add(new DeviceInfoItem(R.string.xdpi, ScreenUtils.getXDpi() + ""));
        // 获取 Y轴 dpi
        mListDeviceInfos.add(new DeviceInfoItem(R.string.ydpi, ScreenUtils.getYDpi() + ""));
        // 获取屏幕密度
        mListDeviceInfos.add(new DeviceInfoItem(R.string.density, ScreenUtils.getDensity() + ""));
        // 获取屏幕密度Dpi
        mListDeviceInfos.add(new DeviceInfoItem(R.string.density_dpi, ScreenUtils.getDensityDpi() + ""));
        // 获取屏幕缩放密度
        mListDeviceInfos.add(new DeviceInfoItem(R.string.scaled_density, ScreenUtils.getScaledDensity() + ""));
        // 获取高度 dpi 基准
        mListDeviceInfos.add(new DeviceInfoItem(R.string.height_dpi, ScreenUtils.getHeightDpi() + ""));
        // 获取宽度 dpi 基准
        mListDeviceInfos.add(new DeviceInfoItem(R.string.width_dpi, ScreenUtils.getWidthDpi() + ""));
        // =
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1dp=" + SizeUtils.dipConvertPx(1f) + "px");
        stringBuilder.append(", 1sp=" + SizeUtils.spConvertPx(1f) + "px");
        // 转换 dpi
        mListDeviceInfos.add(new DeviceInfoItem(R.string.convert_dpi, stringBuilder.toString()));

        // 发送通知
        vHandler.sendEmptyMessage(NotifyConstants.H_QUERY_DEVICE_INFO_END_NOTIFY);
    }
}
