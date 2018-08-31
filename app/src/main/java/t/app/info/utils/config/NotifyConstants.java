package t.app.info.utils.config;

/**
 * detail: 通知参数
 * Created by Ttt
 */
public final class NotifyConstants {

    public static final int BASE = 1000;

    /** 查询 App 列表结束通知 */
    public static final int H_QUERY_APPLIST_END_NOTIFY = BASE + 1;
    /** 查询 手机参数 结束通知 */
    public static final int H_QUERY_DEVICE_INFO_END_NOTIFY = BASE + 2;
    /** 导出设备信息通知 */
    public static final int H_EXPORT_DEVICE_MSG_NOTIFY = BASE + 3;
    /** 导出app信息通知 */
    public static final int H_EXPORT_APP_MSG_NOTIFY = BASE + 4;
    /** 导出应用apk安装包通知 */
    public static final int H_EXPORT_APP_NOTIFY = BASE + 5;
    /** 应用排序变更 */
    public static final int H_APP_SORT_NOTIFY = BASE + 6;
    /** 搜索合并通知 */
    public static final int H_SEARCH_COLLAPSE = BASE + 7;
    /** 搜索展开通知 */
    public static final int H_SEARCH_EXPAND = BASE + 8;
    /** 搜索输入内容通知 */
    public static final int H_SEARCH_INPUT_CONTENT = BASE + 9;
    /** 切换Fragment 通知 */
    public static final int H_TOGGLE_FRAGMENT_NOTIFY = BASE + 10;
    /** 搜索文件资源结束 通知 */
    public static final int H_QUERY_FILE_RES_END_NOTIFY = BASE + 11;
    /** 搜索文件资源中 通知 */
    public static final int H_QUERY_FILE_RES_ING_NOTIFY = BASE + 12;
    /** 刷新 通知 */
    public static final int H_REFRESH_NOTIFY = BASE + 13;
    /** 删除文件 通知 */
    public static final int H_DELETE_APK_FILE_NOTIFY = BASE + 14;



    /** 跳转 App 详情页面 回传 */
    public static final int FOR_R_APP_DETAILS = BASE + 300;
    /** 卸载 App 回传 */
    public static final int FOR_R_APP_UNINSTALL = BASE + 301;

}
