package t.app.info.utils;

import com.google.gson.Gson;

import java.util.LinkedHashMap;

import t.app.info.utils.config.KeyConstants;
import t.app.info.utils.share.SharedUtils;

/**
 * detail: 搜索后缀 工具类
 * Created by Ttt
 */
public final class QuerySuffixUtils {

    /** 重置处理 */
    public static void reset(){
        // 清空配置
        SharedUtils.remove(KeyConstants.KEY_QUERY_SUFFIX);
    }

    /**
     * 获取默认配置
     * @return
     */
    private static String getDfConfig(){
        LinkedHashMap<String, String> mMaps = new LinkedHashMap<>();
        mMaps.put("apk", "apk");
        mMaps.put("apk.1", "apk.1");
        return new Gson().toJson(mMaps);
    }

    /**
     * 获取搜索后缀
     * @return
     */
    public static String getQuerySuffixStr(){
        // 获取配置
        String config = SharedUtils.getString(KeyConstants.KEY_QUERY_SUFFIX);
        // 如果为null, 返回默认配置
        if (config == null){
            return getDfConfig();
        }
        return config;
    }

    /**
     * 获取搜索后缀 LinkedHashMap
     * @return
     */
    public static LinkedHashMap<String, String> getQuerySuffixMap(){
        LinkedHashMap<String, String> maps = new LinkedHashMap<>();
        try {
            // 保存新的配置信息
            maps.putAll(new Gson().fromJson(getQuerySuffixStr(), maps.getClass()));
        } catch (Exception e){
            e.printStackTrace();
        }
        return maps;
    }

    /**
     * 刷新配置
     * @param maps
     */
    public static void refConfig(LinkedHashMap<String, String> maps){
        try {
            SharedUtils.put(KeyConstants.KEY_QUERY_SUFFIX, new Gson().toJson(maps));
        } catch (Exception e){
        }
    }

    /**
     * 获取搜索过滤后缀
     * @return
     */
    public static String[] getFilterSuffixs(){
        try {
            // 进行过滤返回
            return getQuerySuffixMap().keySet().toArray(new String[]{});
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
