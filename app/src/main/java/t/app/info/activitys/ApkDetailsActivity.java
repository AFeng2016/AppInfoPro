package t.app.info.activitys;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import t.app.info.R;
import t.app.info.base.BaseApplication;
import t.app.info.base.observer.DevObserverNotify;
import t.app.info.beans.AppInfoBean;
import t.app.info.beans.KeyValueBean;
import t.app.info.beans.item.ApkInfoItem;
import t.app.info.utils.AppUtils;
import t.app.info.utils.ClipboardUtils;
import t.app.info.utils.FileUtils;
import t.app.info.utils.PreDealUtils;
import t.app.info.utils.ToastUtils;
import t.app.info.utils.config.KeyConstants;
import t.app.info.utils.config.NotifyConstants;
import t.app.info.utils.config.ProConstants;

/**
 * detail: Apk 详情页面(不一定已安装)
 * Created by Ttt
 */
public class ApkDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    // 上下文
    private Context mContext;
    // apk 信息 Item
    private ApkInfoItem apkInfoItem;
    // ==== View ====
    @BindView(R.id.apd_toolbar)
    Toolbar apd_toolbar;
    @BindView(R.id.apd_app_igview)
    ImageView apd_app_igview;
    @BindView(R.id.apd_name_tv)
    TextView apd_name_tv;
    @BindView(R.id.apd_vname_tv)
    TextView apd_vname_tv;
    @BindView(R.id.apd_params_linear)
    LinearLayout apd_params_linear;
    @BindView(R.id.apd_install_apk_tv)
    TextView apd_install_apk_tv;
    @BindView(R.id.apd_delete_apk_tv)
    TextView apd_delete_apk_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置View
        setContentView(R.layout.activity_apk_details);
        // 初始化View
        ButterKnife.bind(this);
        // 初始化上下文
        mContext = this;
        // 初始化操作
        initOperate();
        // 初始化事件
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销观察者模式
        BaseApplication.sDevObservableNotify.unregisterObserver(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.apd_install_apk_tv: // 安装应用
                // 文件存在处理
                if (FileUtils.isFileExists(apkInfoItem.getApkUri())){
                    // 安装apk
                    AppUtils.installApp(apkInfoItem.getApkUri(), "t.app.info.fileprovider");
                } else {
                    ToastUtils.showShort(this, R.string.file_not_exist);
                }

                break;
            case R.id.apd_delete_apk_tv: // 删除apk文件
                // 文件存在处理
                if (FileUtils.isFileExists(apkInfoItem.getApkUri())){
                    // 删除文件通知
                    BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_DELETE_APK_FILE_NOTIFY, apkInfoItem.getApkUri());
                }
                // 删除文件
                FileUtils.deleteFile(apkInfoItem.getApkUri());
                // 提示删除成功
                ToastUtils.showShort(mContext, R.string.delete_suc);
                break;
        }
    }

    // ==

    /** 初始化操作 */
    private void initOperate(){
        try {
            // 解析获取数据
            apkInfoItem = ApkInfoItem.obtain(getIntent().getStringExtra(KeyConstants.KEY_APK_URI));
        } catch (Exception e){
            e.printStackTrace();
        }
        if (apkInfoItem == null){
            // 提示获取失败
            ToastUtils.showShort(mContext, R.string.get_apkinfo_fail);
            finish(); // 销毁页面
            return;
        }
        // 刷新数据
        refData();
        // == 处理 ActionBar
        // https://blog.csdn.net/andygo_520/article/details/51439688
        // https://blog.csdn.net/zouchengxufei/article/details/51199922
        setSupportActionBar(apd_toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            // 给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 对应ActionBar.DISPLAY_SHOW_TITLE
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /** 初始化事件 */
    private void initListeners(){
        // 安装应用
        apd_install_apk_tv.setOnClickListener(this);
        // 删除apk文件
        apd_delete_apk_tv.setOnClickListener(this);
        // 设置点击事件
        apd_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭页面
                finish();
            }
        });
        // 注册观察者模式
        BaseApplication.sDevObservableNotify.registerObserver(this, new DevObserverNotify(this) {
            @Override
            public void onNotify(int nType, Object... args) {
                switch (nType){
                    case NotifyConstants.H_EXPORT_APP_MSG_NOTIFY:
                        // 发送通知
                        vHandler.sendEmptyMessage(nType);
                        break;
                }
            }
        });
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
            // 操作结果
            boolean result = false;
            // 操作提示
            String tips = "";
            // 获取文件名
            String fileName = "";
            // 判断通知类型
            switch (msg.what){
                case NotifyConstants.H_EXPORT_APP_MSG_NOTIFY: // 导出app信息
                    // 获取文件名 应用名_包名_版本名.txt
                    fileName = "apkFile_" + apkInfoItem.getAppInfoBean().getAppName() + "_" + apkInfoItem.getAppInfoBean().getAppPackName() + "_" + apkInfoItem.getAppInfoBean().getVersionName() + ".txt";
                    // 导出数据
                    result = FileUtils.saveFile(ProConstants.EXPORT_APK_MSG_PATH, fileName, apkInfoItem.toString());
                    // 获取提示内容
                    tips = mContext.getString(result ? R.string.export_suc : R.string.export_fail);
                    // 判断结果
                    if (result){
                        // 拼接保存路径
                        tips += " " + ProConstants.EXPORT_APK_MSG_PATH + fileName;
                    }
                    // 提示结果
                    ToastUtils.showShort(mContext, tips);
                    break;
            }
        }
    };

    /**
     * 刷新数据
     */
    private void refData(){
        // https://blog.csdn.net/ruingman/article/details/51347650

        // 获取app信息
        AppInfoBean appInfoBean = apkInfoItem.getAppInfoBean();
        // 设置 app 图标
        apd_app_igview.setImageDrawable(appInfoBean.getAppIcon());
        // 设置 app 名
        apd_name_tv.setText(appInfoBean.getAppName());
        // 设置 app 版本名
        apd_vname_tv.setText(appInfoBean.getVersionName());
        // 初始化View
        forViews();
    }

    /** 循环遍历添加 View 数据 */
    private void forViews(){
        // 清空旧的View
        apd_params_linear.removeAllViews();
        // 数据源
        ArrayList<KeyValueBean> lists = apkInfoItem.getListKeyValues();
        // LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(this);
        // 遍历添加
        for (int i = 0, len = lists.size(); i < len; i++){
            // 获取Item
            final KeyValueBean keyValueBean = lists.get(i);
            // 初始化数据源
            View itemView = inflater.inflate(R.layout.item_app_details, null, false);
            // 初始化View
            LinearLayout iad_linear = (LinearLayout) itemView.findViewById(R.id.iad_linear);
            TextView iad_key_tv = (TextView) itemView.findViewById(R.id.iad_key_tv);
            TextView iad_value_tv = (TextView) itemView.findViewById(R.id.iad_value_tv);
            // 设置值
            iad_key_tv.setText(keyValueBean.getKey());
            iad_value_tv.setText(keyValueBean.getValue());
            // 设置点击事件
            iad_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 复制的内容
                    String txt = keyValueBean.toString();
                    // 复制到剪切板
                    ClipboardUtils.copyText(txt);
                    // 进行提示
                    ToastUtils.showShort(mContext, mContext.getString(R.string.copy_suc) + " -> " + txt);
                }
            });
            // 添加View
            apd_params_linear.addView(itemView);
        }
    }

    // ==

    @Override // 默认创建Menu显示
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu_apk_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 需要的权限
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        // 判断点击按钮
        switch (item.getItemId()) {
            case R.id.bmpi_export_apk_msg: // 导出Apk信息
                // 判断是否存在读写权限
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    // 发出通知
                    BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_EXPORT_APP_MSG_NOTIFY);
                } else {
                    PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, new String[]{permission}, new PermissionsResultAction() {
                        @Override
                        public void onGranted() {
                            // 发出通知
                            BaseApplication.sDevObservableNotify.onNotify(NotifyConstants.H_EXPORT_APP_MSG_NOTIFY);
                        }

                        @Override
                        public void onDenied(String permission) {
                            // 提示导出失败
                            ToastUtils.showShort(mContext, R.string.export_fail);
                        }
                    });
                }
                break;
        }
        return true;
    }
}
