package t.app.info.activitys;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.utils.app.ClickUtils;
import dev.utils.app.PermissionUtils;
import dev.utils.app.assist.manager.ActivityManager;
import dev.utils.app.toast.ToastUtils;
import t.app.info.R;
import t.app.info.base.BaseApplication;
import t.app.info.base.BaseFragment;
import t.app.info.beans.AppInfoBean;
import t.app.info.fragments.AppListFragment;
import t.app.info.fragments.DeviceInfoFragment;
import t.app.info.fragments.QueryApkFragment;
import t.app.info.fragments.ScreenInfoFragment;
import t.app.info.fragments.SettingFragment;
import t.app.info.utils.ProUtils;
import t.app.info.utils.QuerySDCardUtils;
import t.app.info.utils.config.NotifyConstants;

/**
 * detail: 首页
 * Created by Ttt
 */
public class MainActivity extends AppCompatActivity {

    // 上下文
    private Context mContext;
    // Fragment管理
    private FragmentManager fgManager;
    // 判断当前的 Fragment 索引
    private int mFragmentPos = -1;
    // 当前Menu 索引
    private static int mMenuPos = -1;
    /** Fragments **/
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    // ==== View ====
    @BindView(R.id.am_toolbar)
    Toolbar am_toolbar;
    @BindView(R.id.am_drawer_layout)
    DrawerLayout am_drawer_layout;
    @BindView(R.id.am_nav_view)
    NavigationView am_nav_view;
    @BindView(R.id.am_top_btn)
    FloatingActionButton am_top_btn;
    // 获取搜索View
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置View
        setContentView(R.layout.activity_main);
        // 初始化View
        ButterKnife.bind(this);
        // 初始化上下文
        mContext = this;
        // 初始化操作
        initOperate();
        // 初始化事件
        initListeners();

        // Toolbar
        // https://blog.csdn.net/carlos1992/article/details/50707695
        // https://www.cnblogs.com/mjsn/p/6150824.html
        // 刷新Menu
        // https://blog.csdn.net/luohaowang320/article/details/38556383
        // 去掉阴影
        // https://blog.csdn.net/dreamsever/article/details/52672739
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 重置处理
        ProUtils.reset();
        // 销毁搜索线程资源
        setSearchRunnStatus(true);
    }

    @Override
    public void onBackPressed() {
        // 如果显示了侧边栏, 则关闭
        if (am_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            am_drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            // 判断是否显示, 是的话则关闭
            if (searchView != null && !searchView.isIconified()){
                searchView.setQuery("", false); // 如果不增加，则会清空内容先
                searchView.setIconified(true);
                return;
            }
            // 判断是否首页 - 我的应用, 不是则切换回来
            if (mFragmentPos != 0){
                toggleFragment(0);
                // 设置选中第一个
                am_nav_view.setCheckedItem(R.id.nav_user_apps);
                // 设置文案
                am_toolbar.setTitle(R.string.user_apps);
                return;
            }
            // 判断是否双击退出
            if (ClickUtils.isFastDoubleClick("quit")){
                //// 回到桌面 - 需要这句，下面 onBackPressed 则不需要
                //moveTaskToBack(true);
                super.onBackPressed();
            } else {
                ToastUtils.showShort(mContext, R.string.clickReturn);
                return;
            }
        }
    }

    // ==

    /** 初始化操作 */
    private void initOperate(){
        // 重置处理
        ProUtils.reset();
        // 销毁搜索线程资源
        setSearchRunnStatus(false);
        // 初始化 Fragment 集
        initFragments();
        // 设置文案
        am_toolbar.setTitle(R.string.user_apps);
        // 设置侧边栏
        setSupportActionBar(am_toolbar);
        // 设置切换动画事件等
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, am_drawer_layout, am_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        am_drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        // 默认选中第一个
        am_nav_view.setCheckedItem(R.id.nav_user_apps);
        // 延迟请求权限
        vHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 请求权限
                requestPermission();
            }
        }, 1000);
    }

    /** 初始化事件 */
    private void initListeners(){
        // 设置点击处理 = 双击Title
        am_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断是否双击
                if (ClickUtils.isFastDoubleClick(mFragmentPos + "")){
                    try {
                        // 双击回到顶部
                        mFragments.get(mFragmentPos).onScrollTop();
                    } catch (Exception e){
                    }
                }
            }
        });
        // 设置Item 点击事件
        am_nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_user_apps:
                        toggleFragment(0);
                        // 设置文案
                        am_toolbar.setTitle(R.string.user_apps);
                        break;
                    case R.id.nav_system_apps:
                        toggleFragment(1);
                        // 设置文案
                        am_toolbar.setTitle(R.string.system_apps);
                        break;
                    case R.id.nav_phone_info:
                        toggleFragment(2);
                        // 设置文案
                        am_toolbar.setTitle(R.string.phone_info);
                        break;
                    case R.id.nav_screen_info:
                        toggleFragment(3);
                        // 设置文案
                        am_toolbar.setTitle(R.string.screen_info);
                        break;
                    case R.id.nav_query_apk:
                        toggleFragment(4);
                        // 设置文案
                        am_toolbar.setTitle(R.string.query_apk);
                        break;
                    case R.id.nav_setting:
                        toggleFragment(5);
                        // 设置文案
                        am_toolbar.setTitle(R.string.setting);
                        break;
                }
                am_drawer_layout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        // 设置点击事件 = 回到顶部
        am_top_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 回到顶部
                    mFragments.get(mFragmentPos).onScrollTop();
                } catch (Exception e){
                }
            }
        });
    }

    /** 初始化 Fragment 集 */
    private void initFragments(){
        // 添加用户
        mFragments.add(AppListFragment.getInstance(AppInfoBean.AppType.USER));
        // 添加系统应用
        mFragments.add(AppListFragment.getInstance(AppInfoBean.AppType.SYSTEM));
//        // 添加全部应用
//        mFragments.add(AppListFragment.getInstance(AppInfoBean.AppType.ALL));
        // 添加手机信息
        mFragments.add(DeviceInfoFragment.getInstance());
        // 添加屏幕信息
        mFragments.add(ScreenInfoFragment.getInstance());
        // 添加扫描APK
        mFragments.add(QueryApkFragment.getInstance());
        // 添加设置
        mFragments.add(SettingFragment.getInstance());
        // --
        // 得到Fragment管理器
        fgManager = getSupportFragmentManager();
        // 初始化添加对应的布局
        FragmentTransaction fragmentTransaction = fgManager.beginTransaction();
        // 初始化 Fragment 集
        for (int i = 0, len = mFragments.size(); i < len; i++){
            // 添加到集合中
            fragmentTransaction.add(R.id.am_linear, mFragments.get(i), i + "");
            // 隐藏布局
            fragmentTransaction.hide(mFragments.get(i));
        }
        // 提交保存
        fragmentTransaction.commit();
        // 默认显示第一个
        toggleFragment(0);
    }

    /**
     * 切换 Fragment 处理
     * @param mPos
     */
    private void toggleFragment(int mPos) {
        // 判断是否想等
        if (mPos != mFragmentPos){
            // 初始化添加对应的布局
            FragmentTransaction fragmentTransaction = fgManager.beginTransaction();
            // 判断准备显示的 Fragment
            BaseFragment fragment = mFragments.get(mPos);
            // 如果默认未初始化, 则直接显示
            if (mFragmentPos < 0){
                fragmentTransaction.show(fragment).commit();
            } else {
                fragmentTransaction.hide(mFragments.get(mFragmentPos)).show(fragment).commit();
            }
            // 重新保存索引
            mFragmentPos = mPos;
            // 保存新的索引
            mMenuPos = mPos;
            // 切换改变处理
            toggleChange();
        }
    }

    /**
     * 切换改变处理
     */
    private void toggleChange(){
        switch (mFragmentPos){
            case 0: // 我的应用
            case 1: // 系统应用
            case 4: // 扫描APK
                am_top_btn.setVisibility(View.VISIBLE);
                break;
            case 2: // 手机信息
            case 3: // 屏幕信息
            case 5: // 设置
            default:
                am_top_btn.setVisibility(View.GONE);
                break;
        }
        //通知系统更新菜单
        supportInvalidateOptionsMenu();
        // 发送通知
        BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_TOGGLE_FRAGMENT_NOTIFY);
    }

    /**
     * 获取当前 Menu 索引
     * @return
     */
    public static int getMenuPos(){
        return mMenuPos;
    }

    // == Menu ==

    @Override // 默认创建Menu显示
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu_apps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // 准备显示Menu
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        switch (mFragmentPos){
            case 0: // 我的应用
            case 1: // 系统应用
            case 4: // 扫描APK
                getMenuInflater().inflate(R.menu.bar_menu_apps, menu);
                // 初始化搜索操作
                initSearchOperate(menu);
                break;
            case 2: // 手机信息
            case 3: // 屏幕信息
                getMenuInflater().inflate(R.menu.bar_menu_device, menu);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.bma_refresh: // 刷新
                switch (mFragmentPos){
                    case 0: // 手机应用
                        ProUtils.clearAppData(AppInfoBean.AppType.USER);
                        break;
                    case 1: // 系统应用
                        ProUtils.clearAppData(AppInfoBean.AppType.SYSTEM);
                        break;
                    case 4: // 扫描APK
                        // 清空数据
                        QuerySDCardUtils.getInstance().reset();
                        break;
                }
                // 进行通知刷新
                BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_REFRESH_NOTIFY, mFragmentPos);
                break;
            case R.id.bmd_export_item: // 导出
                // 需要的权限
                String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                // 判断是否存在读写权限
                if(ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    // 发出通知
                    BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_EXPORT_DEVICE_MSG_NOTIFY);
                } else {
                    PermissionUtils.permission(permission).callBack(new PermissionUtils.PermissionCallBack() {
                        @Override
                        public void onGranted(PermissionUtils permissionUtils) {
                            // 发出通知
                            BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_EXPORT_DEVICE_MSG_NOTIFY);
                        }

                        @Override
                        public void onDenied(PermissionUtils permissionUtils) {
                            // 提示导出失败
                            ToastUtils.showShort(mContext, R.string.export_fail);
                        }
                    }).request();
                }
                break;
        }
        return true;
    }

    // ==

    /**
     * 初始化搜索操作
     * @param menu
     */
    private void initSearchOperate(Menu menu){
        // https://www.jianshu.com/p/16f9e995e454
        // https://www.cnblogs.com/tianzhijiexian/p/4226675.html
        // https://www.jianshu.com/p/7c1e78e91506
        // 获取搜索Item
        MenuItem searchItem = menu.findItem(R.id.bma_search);
        // 初始化搜索View
        searchView = (SearchView) searchItem.getActionView();
        // 默认提示
        searchView.setQueryHint(getString(R.string.input_packname_aname_query));
//        // 初始化事件
//        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) { // 展开
//                ToastUtils.showShort(mContext, "展开");
//                // 销毁搜索线程资源
//                setSearchRunnStatus(true);
//                // 发送通知
//                BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_SEARCH_EXPAND);
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) { // 合并
//                ToastUtils.showShort(mContext, "合并");
//                // 销毁搜索线程资源
//                setSearchRunnStatus(true);
//                // 发送通知
//                BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_SEARCH_COLLAPSE);
//                return true;
//            }
//        });
        // 搜索框展开时后面叉叉按钮的点击事件
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // 销毁搜索线程资源
                setSearchRunnStatus(true);
                // 发送通知
                BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_SEARCH_COLLAPSE);
                return false;
            }
        });
        // 搜索图标按钮(打开搜索框的按钮)的点击事件
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 销毁搜索线程资源
                setSearchRunnStatus(true);
                // 发送通知
                BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_SEARCH_EXPAND);
            }
        });
        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override // 当点击搜索按钮时触发该方法
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override // 当搜索内容改变时触发该方法
            public boolean onQueryTextChange(String newText) {
                // 开始进行搜索 - 节省不必要的操作,使用Handler + Runnable,减少搜索处理的次数
                vHandler.removeCallbacks(getSearchRunnable());
                vHandler.postDelayed(getSearchRunnable(), 250);
                return false;
            }
        });
    }

    // ============= 搜索操作 ==============
    /** 搜索线程*/
    private static Runnable searchRunn = null;

    /**
     * 设置搜索线程状态
     * @param isDestroy
     */
    private void setSearchRunnStatus(boolean isDestroy) {
        // 表示属于销毁,则移除之前的任务
        if (isDestroy) {
            // 退出页面,则停止操作
            vHandler.removeCallbacks(getSearchRunnable());
        } else { // 非销毁 - 初始化
            searchRunn = null;
        }
    }

    /** 获取搜索线程 */
    private Runnable getSearchRunnable() {
        if (searchRunn == null) {
            searchRunn = new Runnable() {
                @Override
                public void run() {
                    // 延迟触发
                    vHandler.sendEmptyMessage(NotifyConstants.H_SEARCH_INPUT_CONTENT);
                }
            };
        }
        return searchRunn;
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
                // 搜索输入的内容
                case NotifyConstants.H_SEARCH_INPUT_CONTENT:
                    try {
                        // 发送通知
                        BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_SEARCH_INPUT_CONTENT, searchView.getQuery().toString());
                    } catch (Exception e){
                    }
                    break;
            }
        }
    };

    /** 请求权限 */
    private void requestPermission(){
        // 需要的权限
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        // 判断是否存在读写权限
        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) { // 已经存在权限
        } else {
            // 请求权限
            PermissionUtils.permission(permission).request();
        }
    }

}
