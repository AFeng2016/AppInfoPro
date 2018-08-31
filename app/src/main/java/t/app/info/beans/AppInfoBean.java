package t.app.info.beans;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import t.app.info.utils.DevUtils;
import t.app.info.utils.FileUtils;

/**
 * detail: app 信息实体类
 * Created by Ttt
 */
public class AppInfoBean {

    // https://my.oschina.net/orgsky/blog/368768

    // app 包名
    private String appPackName;
    // app 名
    private String appName;
    // app 图标
    private transient Drawable appIcon;
    // App 类型
    private AppType appType;
    // 获取版本号
    private int versionCode;
    // 获取版本名
    private String versionName;
    // app 首次安装时间
    private long firstInstallTime;
    // 获取最后一次更新时间
    private long lastUpdateTime;
    // 获取 app 地址
    private String sourceDir;
    // APK 大小
    private long apkSize;

    /**
     * 初始化 App 信息实体类
     * @param apkUri
     */
    public static AppInfoBean obtain(String apkUri){
        try {
            // https://blog.csdn.net/sljjyy/article/details/17370665
            PackageManager pManager = DevUtils.getApplication().getPackageManager();
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkUri, PackageManager.GET_ACTIVITIES);
            // = 设置 apk 位置信息 =
            ApplicationInfo appInfo = pInfo.applicationInfo;
            /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = apkUri;
            appInfo.publicSourceDir = apkUri;
            return new AppInfoBean(pInfo, pManager);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化 App 信息实体类
     * @param pInfo
     * @param pManager
     */
    public AppInfoBean(PackageInfo pInfo, PackageManager pManager){
        // app 包名
        appPackName = pInfo.applicationInfo.packageName;
        // app 名
        appName = pManager.getApplicationLabel(pInfo.applicationInfo).toString();
        // app 图标
        appIcon = pManager.getApplicationIcon(pInfo.applicationInfo);
        // 获取App 类型
        appType = AppInfoBean.getAppType(pInfo);
        // 获取版本号
        versionCode = pInfo.versionCode;
        // 获取版本名
        versionName = pInfo.versionName;
        // app 首次安装时间
        firstInstallTime = pInfo.firstInstallTime;
        // 获取最后一次更新时间
        lastUpdateTime = pInfo.lastUpdateTime;
        // 获取 app 地址
        sourceDir = pInfo.applicationInfo.sourceDir;
        // 获取 APK 大小
        apkSize = FileUtils.getFileLength(sourceDir);
    }

    /**
     * 获取App 包名
     * @return
     */
    public String getAppPackName() {
        return appPackName;
    }

    /**
     * 获取App 名
     * @return
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 获取App 图标
     * @return
     */
    public Drawable getAppIcon() {
        return appIcon;
    }

    /**
     * 获取 App 类型
     * @return
     */
    public AppType getAppType() {
        return appType;
    }

    // =

    /** App 类型 */
    public enum AppType {

        USER, // 用户 App

        SYSTEM, // 系统 App

        ALL, // 全部 App
    }

    /**
     * 获取App 类型
     * @param pInfo
     * @return
     */
    public static AppType getAppType(PackageInfo pInfo){
        if (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo)){
            return AppType.USER;
        }
        return AppType.SYSTEM;
    }

    /**
     * 表示系统程序
     * @param pInfo
     * @return
     */
    public static boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    /**
     * 表示系统程序被手动更新后，也成为第三方应用程序
     * @param pInfo
     * @return
     */
    public static boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public long getFirstInstallTime() {
        return firstInstallTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public long getApkSize() {
        return apkSize;
    }
}
