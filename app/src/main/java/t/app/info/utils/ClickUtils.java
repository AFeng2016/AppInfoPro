package t.app.info.utils;

import java.util.HashMap;

/**
 * detail: 点击工具类
 * Created by Ttt
 */
public final class ClickUtils {

    private ClickUtils(){
    }

    // 上一次点击的标识id = viewId 等
    private static int lastTagId = -1;
    /** 上次点击时间 */
    private static long lastClickTime = 0l; // 局限性是, 全局统一事件，如果上次点击后，立刻点击其他就无法点
    /** 默认间隔时间 */
    private static long DF_DIFF = 1000l; // 点击间隔1秒内
    /** 配置数据 */
    private static final HashMap<String, Long> mapConfig = new HashMap<>();
    /** 点击记录数据 */
    private static final HashMap<String, Long> mapRecords = new HashMap<>();

    // ===

    /**
     * 判断两次点击的间隔 小于默认间隔时间(1秒), 则认为是多次无效点击
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DF_DIFF);
    }

    /**
     * 判断两次点击的间隔 小于默认间隔时间(1秒), 则认为是多次无效点击
     * @param tagId
     * @return
     */
    public static boolean isFastDoubleClick(int tagId) {
        return isFastDoubleClick(tagId, DF_DIFF);
    }

    /**
     * 判断两次点击的间隔 小于间隔时间(diff), 则认为是多次无效点击
     * @param tagId
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int tagId, long diff) {
        long cTime = System.currentTimeMillis();
        long dTime = cTime - lastClickTime;
        // 判断时间是否超过
        if (lastTagId == tagId && lastClickTime > 0 && dTime < diff) {
            return true;
        }
        lastTagId = tagId;
        lastClickTime = cTime;
        return false;
    }

    // ===

    /**
     * 判断两次点击的间隔(根据默认Tag判断) 小于指定间隔时间, 则认为是多次无效点击
     * @param tag
     * @return
     */
    public static boolean isFastDoubleClick(String tag){
        // 获取配置时间
        Long config_time = mapConfig.get(tag);
        // 如果等于null, 则传入默认时间
        if (config_time == null){
            return isFastDoubleClick(tag, DF_DIFF);
        }
        return isFastDoubleClick(tag, config_time);
    }

    /**
     * 判断两次点击的间隔 小于间隔时间(diff), 则认为是多次无效点击
     * @param tag
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(String tag, long diff){
        // 获取上次点击的时间
        Long lastTime = mapRecords.get(tag);
        if (lastTime == null){
            lastTime = 0l;
        }
        long cTime = System.currentTimeMillis();
        long dTime = cTime - lastTime;
        // 判断时间是否超过
        if (lastTime > 0 && dTime < diff) {
            return true;
        }
        // 保存上次点击时间
        mapRecords.put(tag, cTime);
        return false;
    }

    // ===

    /**
     * 判断两次点击的间隔(根据默认Tag判断) 小于指定间隔时间, 则认为是多次无效点击
     * @param obj
     * @return
     */
    public static boolean isFastDoubleClick(Object obj){
        // 获取TAG
        String tag = ((obj != null) ? ("obj_" + obj.hashCode()) : "obj_null");
        // 获取配置时间
        Long config_time = mapConfig.get(tag);
        // 如果等于null, 则传入默认时间
        if (config_time == null){
            return isFastDoubleClick(tag, DF_DIFF);
        }
        return isFastDoubleClick(tag, config_time);
    }

    /**
     * 判断两次点击的间隔 小于间隔时间(diff), 则认为是多次无效点击
     * @param obj
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(Object obj, long diff){
        // 获取TAG
        String tag = ((obj != null) ? ("obj_" + obj.hashCode()) : "obj_null");
        // 获取上次点击的时间
        Long lastTime = mapRecords.get(tag);
        if (lastTime == null){
            lastTime = 0l;
        }
        long cTime = System.currentTimeMillis();
        long dTime = cTime - lastTime;
        // 判断时间是否超过
        if (lastTime > 0 && dTime < diff) {
            return true;
        }
        // 保存上次点击时间
        mapRecords.put(tag, cTime);
        return false;
    }

    // ===

    /**
     * 初始化配置信息
     * @param mapConfig
     */
    public static void initConfig(HashMap<String, Object> mapConfig){
        if (mapConfig != null){
            mapConfig.putAll(mapConfig);
        }
    }

    /**
     * 添加配置信息
     * @param key
     * @param val
     */
    public static void putConfig(String key, Long val){
        mapConfig.put(key, val);
    }

    /**
     * 移除配置信息
     * @param key
     */
    public static void removeConfig(String key){
        mapConfig.remove(key);
    }
}
