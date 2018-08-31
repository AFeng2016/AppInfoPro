package t.app.info.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * detail: App（Android 工具类）
 * Created by Ttt
 */
public final class AppUtils {

	private AppUtils() {
	}
    
    /**
	 * 通过上下文获取 WindowManager
	 * @return
	 */
	public static WindowManager getWindowManager() {
		try {
			return (WindowManager) DevUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
		} catch (Exception e) {
		}
		return null;
	}

//	/**
//	 * 通过上下文获取 DisplayMetrics (获取关于显示的通用信息，如显示大小，分辨率和字体)
//	 * @return
//	 */
//	public static DisplayMetrics getDisplayMetrics() {
//		try {
//			WindowManager wManager = (WindowManager) DevUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
//			if (wManager != null) {
//				DisplayMetrics dMetrics = new DisplayMetrics();
//				wManager.getDefaultDisplay().getMetrics(dMetrics);
//				return dMetrics;
//			}
//		} catch (Exception e) {
//		}
//		return null;
//	}

	/**
	 * 获取 Manifest Meta Data
	 * @param metaKey
	 * @return
	 */
	public static String getMetaData(String metaKey) {
		try {
			ApplicationInfo appInfo = DevUtils.getContext().getPackageManager().getApplicationInfo(DevUtils.getContext().getPackageName(), PackageManager.GET_META_DATA);
			String data = appInfo.metaData.getString(metaKey);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// == 快捷获取方法 ==

	/**
	 * 获取View
	 * @param resource
	 * @return
	 */
	public static View getView(@LayoutRes int resource){
		return getView(DevUtils.getContext(), resource);
	}

	/**
	 * 获取View
	 * @param mContext
	 * @param resource
	 * @return
	 */
	public static View getView(Context mContext, @LayoutRes int resource){
		try {
			return ((LayoutInflater) DevUtils.getContext(mContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, null);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Resources getResources() {
		try {
			return DevUtils.getContext().getResources();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String getString(@StringRes int id) {
		try {
			return DevUtils.getContext().getResources().getString(id);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Resources.Theme getTheme() {
		try {
			return DevUtils.getContext().getTheme();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static AssetManager getAssets() {
		try {
			return DevUtils.getContext().getAssets();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Drawable getDrawable(@DrawableRes int id) {
		try {
			return ContextCompat.getDrawable(DevUtils.getContext(), id);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static int getColor( @ColorRes int id) {
		try {
			return ContextCompat.getColor(DevUtils.getContext(), id);
		} catch (Exception e){
			e.printStackTrace();
		}
		return -1;
	}

	public static <T> T getSystemService(String name){
		try {
			return (T) DevUtils.getContext().getSystemService(name);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static PackageManager getPackageManager(){
		try {
			return DevUtils.getContext().getPackageManager();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Configuration getConfiguration() {
		try {
			return DevUtils.getContext().getResources().getConfiguration();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static DisplayMetrics getDisplayMetrics() {
		try {
			return DevUtils.getContext().getResources().getDisplayMetrics();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static ContentResolver getContentResolver() {
		try {
			return DevUtils.getContext().getContentResolver();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取app的图标
	 * @return
	 */
	public static Drawable getAppIcon() {
		return getAppIcon(DevUtils.getContext().getPackageName());
	}

	/**
	 * 获取app的图标
	 * @param packageName
	 * @return
	 */
	public static Drawable getAppIcon(final String packageName) {
		if (isSpace(packageName)) return null;
		try {
			PackageManager pm = DevUtils.getContext().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? null : pi.applicationInfo.loadIcon(pm);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取app 包名
	 * @return
	 */
	public static String getAppPackageName() {
		return DevUtils.getContext().getPackageName();
	}

	/**
	 * 获取app 名
	 * @return
	 */
	public static String getAppName() {
		return getAppName(DevUtils.getContext().getPackageName());
	}

	/**
	 * 获取app 名
	 * @param packageName
	 * @return
	 */
	public static String getAppName(final String packageName) {
		if (isSpace(packageName)) return null;
		try {
			PackageManager pm = DevUtils.getContext().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取app版本名 - 对外显示
	 * @return
	 */
	public static String getAppVersionName() {
		return getAppVersionName(DevUtils.getContext().getPackageName());
	}

	/**
	 * 获取app版本名 - 对外显示
	 * @param packageName The name of the package.
	 * @return
	 */
	public static String getAppVersionName(final String packageName) {
		if (isSpace(packageName)) return null;
		try {
			PackageInfo pi = DevUtils.getContext().getPackageManager().getPackageInfo(packageName, 0);
			return pi == null ? null : pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取app版本号 - 内部判断
	 * @return
	 */
	public static int getAppVersionCode() {
		return getAppVersionCode(DevUtils.getContext().getPackageName());
	}

	/**
	 * 获取app版本号 - 内部判断
	 * @param packageName The name of the package.
	 * @return
	 */
	public static int getAppVersionCode(final String packageName) {
		if (isSpace(packageName)) return -1;
		try {
			PackageInfo pi = DevUtils.getContext().getPackageManager().getPackageInfo(packageName, 0);
			return pi == null ? -1 : pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	// =

 	/**
 	 * 对内设置指定语言 (app 多语言,单独改变app语言)
 	 * @param locale
 	 */
 	public static void setLanguage(Locale locale) {
 		try {
 			// 获得res资源对象
 			Resources resources = DevUtils.getContext().getResources();
 			// 获得设置对象
 			Configuration config = resources.getConfiguration();
 			// 获得屏幕参数：主要是分辨率，像素等。
 			DisplayMetrics dm = resources.getDisplayMetrics();
 			// 语言
 			config.locale = locale;
 			// 更新语言
 			resources.updateConfiguration(config, dm);
 		} catch (Exception e) {
 		}
 	}

 	// =

	/**
	 * 安装 App（支持 8.0）的意图
	 * @param filePath The path of file.
	 * @param authority 7.0 及以上安装需要传入清单文件中的<provider>}的 authorities 属性
	 * @return 是否可以跳转
	 */
	public static boolean installApp(final String filePath, final String authority) {
		return installApp(getFileByPath(filePath), authority);
	}

	/**
	 * 安装 App(支持 8.0)的意图
	 * @param file The file.
	 * @param authority 7.0 及以上安装需要传入清单文件中的<provider>}的 authorities 属性
	 * @return 是否可以跳转
	 */
	public static boolean installApp(final File file, final String authority) {
		if (!isFileExists(file)) return false;
		try {
			DevUtils.getContext().startActivity(IntentUtils.getInstallAppIntent(file, authority, true));
		} catch (Exception e){
			return false;
		}
		return true;
	}

	/**
	 * 安装 App(支持 8.0)的意图 - 回传
	 * @param activity
	 * @param filePath
	 * @param authority
	 * @param requestCode
	 * @return
	 */
	public static boolean installApp(final Activity activity, final String filePath, final String authority, final int requestCode) {
		return installApp(activity, getFileByPath(filePath), authority, requestCode);
	}

	/**
	 * 安装 App(支持 8.0)的意图 - 回传
	 * @param activity
	 * @param file
	 * @param authority
	 * @param requestCode
	 * @return
	 */
	public static boolean installApp(final Activity activity, final File file, final String authority, final int requestCode) {
		if (!isFileExists(file)) return false;
		try {
			activity.startActivityForResult(IntentUtils.getInstallAppIntent(file, authority), requestCode);
		} catch (Exception e){
			return false;
		}
		return true;
	}

	/**
	 * 卸载 App
	 * @param packageName
	 * @return 卸载 App 的意图
	 * @param
	 */
	public static boolean uninstallApp(final String packageName) {
		if (isSpace(packageName)) return false;
		try {
			DevUtils.getContext().startActivity(IntentUtils.getUninstallAppIntent(packageName, true));
		} catch (Exception e){
			return false;
		}
		return true;
	}

	/**
	 * 卸载 App
	 * @param activity
	 * @param packageName
	 * @param requestCode
	 */
	public static boolean uninstallApp(final Activity activity, final String packageName, final int requestCode) {
		if (isSpace(packageName)) return false;
		try {
			activity.startActivityForResult(IntentUtils.getUninstallAppIntent(packageName), requestCode);
		} catch (Exception e){
			return false;
		}
		return true;
	}

	/**
	 * 判断是否安装了应用
	 * @param action The Intent action, such as ACTION_VIEW.
	 * @param category The desired category.
	 * @return true : yes, false : no
	 */
	public static boolean isAppInstalled(@NonNull final String action, @NonNull  final String category) {
		try {
			Intent intent = new Intent(action);
			intent.addCategory(category);
			PackageManager pm = DevUtils.getContext().getPackageManager();
			ResolveInfo info = pm.resolveActivity(intent, 0);
			return info != null;
		} catch (Exception e){
			return false;
		}
	}

	/**
	 * 判断是否安装了应用
	 * @param packageName
	 * @return true : yes, false : no
	 */
	public static boolean isAppInstalled(@NonNull final String packageName) {
		return !isSpace(packageName) && IntentUtils.getLaunchAppIntent(packageName) != null;
	}

	/**
	 * 判断是否安装指定包名的APP
	 * @param mContext 上下文
	 * @param packageName 包路径
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean isInstalledApp(Context mContext, String packageName) {
		if (packageName == null || "".equals(packageName)) {
			return false;
		}
		try {
			ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断是否app 是否debug模式
	 * @return true : yes, false : no
	 */
	public static boolean isAppDebug() {
		return isAppDebug(DevUtils.getContext().getPackageName());
	}

	/**
	 * 判断是否app 是否debug模式
	 * @param packageName
	 * @return true : yes, false : no
	 */
	public static boolean isAppDebug(final String packageName) {
		if (isSpace(packageName)) return false;
		try {
			ApplicationInfo ai = DevUtils.getContext().getPackageManager().getApplicationInfo(packageName, 0);
			return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断app 是否系统app
	 * @return true : yes, false : no
	 */
	public static boolean isAppSystem() {
		return isAppSystem(DevUtils.getContext().getPackageName());
	}

	/**
	 * 判断app 是否系统app
	 * @param packageName
	 * @return true : yes, false : no
	 */
	public static boolean isAppSystem(final String packageName) {
		if (isSpace(packageName)) return false;
		try {
			ApplicationInfo ai = DevUtils.getContext().getPackageManager().getApplicationInfo(packageName, 0);
			return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断app 是否在前台
	 * @return true : yes, false : no
	 */
	public static boolean isAppForeground() {
		try {
			ActivityManager manager = (ActivityManager) DevUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> info = manager.getRunningAppProcesses();
			if (info == null || info.size() == 0) return false;
			for (ActivityManager.RunningAppProcessInfo aInfo : info) {
				if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					return aInfo.processName.equals(DevUtils.getContext().getPackageName());
				}
			}
		} catch (Exception e){
		}
		return false;
	}

	/**
	 * 打开app
	 * @param packageName
	 */
	public static boolean launchApp(final String packageName) {
		if (isSpace(packageName)) return false;
		try {
			DevUtils.getContext().startActivity(IntentUtils.getLaunchAppIntent(packageName, true));
			return true;
		} catch (Exception e){
		}
		return false;
	}

	/**
	 * 打开app, 并且回传
	 * @param activity    The activity.
	 * @param packageName
	 * @param requestCode
	 */
	public static boolean launchApp(final Activity activity, final String packageName, final int requestCode) {
		if (isSpace(packageName)) return false;
		try {
			activity.startActivityForResult(IntentUtils.getLaunchAppIntent(packageName), requestCode);
			return true;
		} catch (Exception e){
		}
		return false;
	}

	/** 跳转到 专门的APP 设置详情页面 */
	public static boolean launchAppDetailsSettings() {
		return launchAppDetailsSettings(DevUtils.getContext().getPackageName());
	}

	/**
	 * 跳转到 专门的APP 设置详情页面
	 * @param packageName
	 */
	public static boolean launchAppDetailsSettings(final String packageName) {
		if (isSpace(packageName)) return false;
		try {
			DevUtils.getContext().startActivity(IntentUtils.getLaunchAppDetailsSettingsIntent(packageName, true));
			return true;
		} catch (Exception e){
		}
		return false;
	}

	/**
     * 跳转到 专门的APP 应用商城详情页面
	 * @param marketPkg
     * @return
     */
	public static boolean launchAppDetails(final String marketPkg) {
		return launchAppDetails(DevUtils.getContext().getPackageName(), marketPkg);
	}
	
	/**
	 * 跳转到 专门的APP 应用商城详情页面
	 * @param marketPkg
	 * @param marketPkg
	 * @return
	 */
	public static boolean launchAppDetails(final String packageName, final String marketPkg) {
		if (isSpace(packageName)) return false;
		try {
			DevUtils.getContext().startActivity(IntentUtils.getlaunchAppDetailIntent(packageName, marketPkg, true));
			return true;
		} catch (Exception e){
		}
		return false;
	}

	/**
	 * 获取app 路径 /data/data/包名/.apk
	 * @return
	 */
	public static String getAppPath() {
		return getAppPath(DevUtils.getContext().getPackageName());
	}

	/**
	 * 获取app 路径 /data/data/包名/.apk
	 * @param packageName
	 * @return
	 */
	public static String getAppPath(final String packageName) {
		if (isSpace(packageName)) return null;
		try {
			PackageManager pm = DevUtils.getContext().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? null : pi.applicationInfo.sourceDir;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ==

	/**
	 * 获取app 签名
	 * @return
	 */
	public static Signature[] getAppSignature() {
		return getAppSignature(DevUtils.getContext().getPackageName());
	}

	/**
	 * 获取app 签名
	 * @param packageName
	 * @return
	 */
	public static Signature[] getAppSignature(final String packageName) {
		if (isSpace(packageName)) return null;
		try {
			PackageManager pm = DevUtils.getContext().getPackageManager();
			@SuppressLint("PackageManagerGetSignatures")
			PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			return pi == null ? null : pi.signatures;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取 app sha1值
	 * @return
	 */
	public static String getAppSignatureSHA1() {
		return getAppSignatureSHA1(DevUtils.getContext().getPackageName());
	}

	/**
	 * 获取 app sha1值
	 * @param packageName
	 * @return
	 */
	public static String getAppSignatureSHA1(@NonNull final String packageName) {
		Signature[] signature = getAppSignature(packageName);
		if (signature == null || signature.length <= 0) return null;
		return encryptSHA1ToString(signature[0].toByteArray()).replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
	}

	/**
	 * 获取 APP 信息
	 * @return
	 */
	public static AppInfo getAppInfo() {
		return getAppInfo(DevUtils.getContext().getPackageName());
	}

	/**
	 * 获取 APP 信息
	 * @param packageName
	 * @return 当前应用的 AppInfo
	 */
	public static AppInfo getAppInfo(final String packageName) {
		try {
			PackageManager pm = DevUtils.getContext().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return getBean(pm, pi);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取全部安装的 APP 信息
	 * @return
	 */
	public static List<AppInfo> getAppsInfo() {
		List<AppInfo> list = new ArrayList<>();
		try {
			PackageManager pm = DevUtils.getContext().getPackageManager();
			List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
			for (PackageInfo pi : installedPackages) {
				AppInfo ai = getBean(pm, pi);
				if (ai == null) continue;
				list.add(ai);
			}
		} catch (Exception e){
		}
		return list;
	}

	/**
	 * 转换APP 信息实体类
	 * @param pm
	 * @param pi
	 * @return
	 */
	private static AppInfo getBean(final PackageManager pm, final PackageInfo pi) {
		if (pm == null || pi == null) return null;
		try {
			ApplicationInfo ai = pi.applicationInfo;
			String packageName = pi.packageName;
			String name = ai.loadLabel(pm).toString();
			Drawable icon = ai.loadIcon(pm);
			String packagePath = ai.sourceDir;
			String versionName = pi.versionName;
			int versionCode = pi.versionCode;
			boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
			return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
		} catch (Exception e){
		}
		return null;
	}

	// ==

	/**
	 * 检查是否存在某个文件
	 * @param file 文件路径
	 * @return 是否存在文件
	 */
	private static boolean isFileExists(final File file){
		return file != null && file.exists();
	}

	/**
	 * 获取文件
	 * @param filePath
	 * @return
	 */
	private static File getFileByPath(final String filePath){
		return new File(filePath != null ? filePath : null);
	}

	/**
	 * 判断字符串是否为 null 或全为空白字符
	 * @param str 待校验字符串
	 * @return
	 */
	private static boolean isSpace(final String str) {
		if (str == null) return true;
		for (int i = 0, len = str.length(); i < len; ++i) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断设备是否 root
	 * @return
	 */
	private static boolean isDeviceRooted() {
		String su = "su";
		String[] locations = { "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
				"/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/" };
		for (String location : locations) {
			if (new File(location + su).exists()) {
				return true;
			}
		}
		return false;
	}

	// 十六进制大写转换
	private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	 * SHA1加密
	 * @param data
	 * @return
	 */
	private static String encryptSHA1ToString(final byte[] data) {
		return bytes2HexString(encryptSHA1(data));
	}

	/**
	 * SHA1加密
	 * @param data
	 * @return
	 */
	private static byte[] encryptSHA1(final byte[] data) {
		if (data == null || data.length <= 0) return null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(data);
			return md.digest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 数据加密转换16进制
	 * @param bytes
	 * @return
	 */
	private static String bytes2HexString(final byte[] bytes) {
		if (bytes == null) return null;
		int len = bytes.length;
		if (len <= 0) return null;
		char[] ret = new char[len << 1];
		for (int i = 0, j = 0; i < len; i++) {
			ret[j++] = HEX_DIGITS[bytes[i] >>> 4 & 0x0f];
			ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
		}
		return new String(ret);
	}

	/**
	 * APP 信息实体类
	 * name of package
	 * icon
	 * name
	 * path of package
	 * version name
	 * version code
	 * is system
	 */
	public static class AppInfo {

		private String   packageName;
		private String   name;
		private Drawable icon;
		private String   packagePath;
		private String   versionName;
		private int      versionCode;
		private boolean  isSystem;

		public Drawable getIcon() {
			return icon;
		}

		public void setIcon(final Drawable icon) {
			this.icon = icon;
		}

		public boolean isSystem() {
			return isSystem;
		}

		public void setSystem(final boolean isSystem) {
			this.isSystem = isSystem;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(final String packageName) {
			this.packageName = packageName;
		}

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public String getPackagePath() {
			return packagePath;
		}

		public void setPackagePath(final String packagePath) {
			this.packagePath = packagePath;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(final int versionCode) {
			this.versionCode = versionCode;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(final String versionName) {
			this.versionName = versionName;
		}

		public AppInfo(String packageName, String name, Drawable icon, String packagePath, String versionName, int versionCode, boolean isSystem) {
			this.setName(name);
			this.setIcon(icon);
			this.setPackageName(packageName);
			this.setPackagePath(packagePath);
			this.setVersionName(versionName);
			this.setVersionCode(versionCode);
			this.setSystem(isSystem);
		}

		@Override
		public String toString() {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("pkg name: " + getPackageName());
			stringBuffer.append("app icon: " + getIcon());
			stringBuffer.append("app name: " + getName());
			stringBuffer.append("app path: " + getPackagePath());
			stringBuffer.append("app verName: " + getVersionName());
			stringBuffer.append("app verCode: " + getVersionCode());
			stringBuffer.append("app system: " + isSystem());
			return stringBuffer.toString();
		}
	}

	// == 其他功能 ==

	/**
	 * 启动本地应用打开PDF
	 * @param mContext 上下文
	 * @param filePath 文件路径
	 */
	public static boolean openPDFFile(Context mContext, String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				Uri path = Uri.fromFile(file);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(path, "application/pdf");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent);
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 启动本地应用打开PDF
	 * @param mContext 上下文
	 * @param filePath 文件路径
	 */
	public static boolean openWordFile(Context mContext, String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				Uri path = Uri.fromFile(file);
				Intent intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(path, "application/msword");
				mContext.startActivity(intent);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 调用WPS打开office文档
	 * @param mContext 上下文
	 * @param filePath 文件路径
	 */
	public static boolean openOfficeByWPS(Context mContext, String filePath) {
		try {
			// 检查是否安装WPS
			String wpsPackageEng = "cn.wps.moffice_eng";// 普通版与英文版一样
			// String wpsActivity = "cn.wps.moffice.documentmanager.PreStartActivity";
			String wpsActivity2 = "cn.wps.moffice.documentmanager.PreStartActivity2";// 默认第三方程序启动

			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setClassName(wpsPackageEng, wpsActivity2);

			Uri uri = Uri.fromFile(new File(filePath));
			intent.setData(uri);
			mContext.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
