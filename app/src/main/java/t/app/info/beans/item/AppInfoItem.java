package t.app.info.beans.item;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.text.format.Formatter;

import com.google.gson.GsonBuilder;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import t.app.info.R;
import t.app.info.beans.AppInfoBean;
import t.app.info.beans.KeyValueBean;
import t.app.info.utils.AppCommonUtils;
import t.app.info.utils.DevUtils;
import t.app.info.utils.FileUtils;
import t.app.info.utils.PreDealUtils;
import t.app.info.utils.SignaturesUtils;

/**
 * detail: App 信息Item
 * Created by Ttt
 */
public final class AppInfoItem {

    // App 信息实体类
    private AppInfoBean appInfoBean;
    // App 参数集
    private ArrayList<KeyValueBean> listKeyValues = new ArrayList<>();

    private AppInfoItem(){
    }

    public static AppInfoItem obtain(String packName) throws Exception{
        // 如果包名为null, 则直接不处理
        if (TextUtils.isEmpty(packName)){
            return null;
        }
        // 获取上下文
        Context mContext = DevUtils.getContext();
        // 初始化包管理类
        PackageManager pManager = mContext.getPackageManager();
        // 获取对应的PackageInfo(原始的PackageInfo 获取 signatures 等于null,需要这样获取)
        PackageInfo pInfo = pManager.getPackageInfo(packName, PackageManager.GET_SIGNATURES); // 64
        // 初始化实体类
        AppInfoItem appInfoItem = new AppInfoItem();
        // 获取app 信息
        appInfoItem.appInfoBean = new AppInfoBean(pInfo, pManager);
        // == 获取 ==
        // 格式化日期
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // ===========
        // app 签名MD5
        String md5 = SignaturesUtils.signatureMD5(pInfo.signatures);
        // app SHA1
        String sha1 = SignaturesUtils.signatureSHA1(pInfo.signatures);
        // app SHA256
        String sha256 = SignaturesUtils.signatureSHA256(pInfo.signatures);
        // app 首次安装时间
        String firstInstallTime = dFormat.format(pInfo.firstInstallTime);
        // 获取最后一次更新时间
        String lastUpdateTime = dFormat.format(pInfo.lastUpdateTime);
        // app 最低支持版本
        int minSdkVersion = -1;
        // 属于7.0以上才有的方法
        if (AppCommonUtils.isN()){
            minSdkVersion = pInfo.applicationInfo.minSdkVersion;
        }
        // app 兼容sdk版本
        int targetSdkVersion = pInfo.applicationInfo.targetSdkVersion;
        // 获取 app 安装包大小
        String apkLength = Formatter.formatFileSize(DevUtils.getContext(), FileUtils.getFileLength(appInfoItem.appInfoBean.getSourceDir()));
        // 获取证书对象
        X509Certificate cert = SignaturesUtils.getX509Certificate(pInfo.signatures);
        // 设置有效期
        StringBuilder sbEffective = new StringBuilder();
        sbEffective.append(dFormat.format(cert.getNotBefore()));
        sbEffective.append(" " + mContext.getString(R.string.to) + " "); // 至
        sbEffective.append(dFormat.format(cert.getNotAfter()));
        sbEffective.append("\n\n");
        sbEffective.append(cert.getNotBefore());
        sbEffective.append(" " + mContext.getString(R.string.to) + " ");
        sbEffective.append(cert.getNotAfter());
        // 获取有效期
        String effective = sbEffective.toString();
        // 证书是否过期 true = 过期,false = 未过期
        boolean isEffective = false;
        try {
            cert.checkValidity();
            // CertificateExpiredException - 如果证书已过期。
            // CertificateNotYetValidException - 如果证书不再有效。
        } catch (CertificateExpiredException ce) {
            isEffective = true;
        } catch (CertificateNotYetValidException ce) {
            isEffective = true;
        }
        // 判断是否过期
        String isEffectiveState = isEffective ? mContext.getString(R.string.overdue_hint_s) : mContext.getString(R.string.notoverdue_hint_s);
        // 证书发布方
        String principal = cert.getIssuerX500Principal().toString();
        // 证书版本号
        String version = cert.getVersion() + "";
        // 证书算法名称
        String sigalgname = cert.getSigAlgName();
        // 证书算法OID
        String sigalgoid = cert.getSigAlgOID();
        // 证书机器码
        String serialnumber = cert.getSerialNumber().toString();
        // 证书 DER编码
        String dercode = SignaturesUtils.toHexString(cert.getTBSCertificate());

        // ================
        // === 保存集合 ===
        // ================
        // app 包名
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.packname_hint_s, appInfoItem.appInfoBean.getAppPackName()));
        // app 签名MD5
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.md5_hint_s, md5));
        // app 版本号 - 主要用于app内部版本判断 int 类型
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.version_code_hint_s, appInfoItem.appInfoBean.getVersionCode() + ""));
        // app 版本名 - 主要用于对用户显示版本信息
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.version_name_hint_s, appInfoItem.appInfoBean.getVersionName()));
        // app SHA1
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.sha1_hint_s, sha1));
        // app SHA256.
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.sha256_hint_s, sha256));
        // app 首次安装时间
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.first_install_time_hint_s, firstInstallTime));
        // 获取最后一次更新时间
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.last_update_time_hint_s, lastUpdateTime));
        // app 最低支持版本
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.minsdkversion_hint_s, minSdkVersion + " ( " + PreDealUtils.convertSDKVersion(minSdkVersion) + "+ )"));
        // app 兼容sdk版本
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.targetsdkversion_hint_s, targetSdkVersion + " ( " + PreDealUtils.convertSDKVersion(targetSdkVersion) + "+ )"));
        // 获取 apk 大小
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.apk_length_hint_s, apkLength));
        // 获取有效期
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.effective_hint_s, effective));
        // 判断是否过期
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.iseffective_hint_s, isEffectiveState));
        // 证书发布方
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.principal_hint_s, principal));
        // 证书版本号
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.version_hint_s, version));
        // 证书算法名称
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.sigalgname_hint_s, sigalgname));
        // 证书算法OID
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.sigalgoid_hint_s, sigalgoid));
        // 证书机器码
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.dercode_hint_s, serialnumber));
        // 证书 DER编码
        appInfoItem.listKeyValues.add(KeyValueBean.get(R.string.serialnumber_hint_s, dercode));

        // 返回实体类
        return appInfoItem;
    }

    public AppInfoBean getAppInfoBean() {
        return appInfoBean;
    }

    public ArrayList<KeyValueBean> getListKeyValues() {
        return listKeyValues;
    }

    @Override
    public String toString() {
        try {
            // 返回 JSON格式数据 - 格式化
            return new GsonBuilder().setPrettyPrinting().create().toJson(this);
        } catch (Exception e){
        }
        return null;
    }
}
