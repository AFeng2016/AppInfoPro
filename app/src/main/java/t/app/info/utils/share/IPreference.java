package t.app.info.utils.share;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * detail: SharedPreferences 操作接口
 * Created by Ttt
 * http://www.jianshu.com/p/fcd75a324c35
 */
public interface IPreference {

    /** 枚举: 存储或取出的数据类型 */
    enum DataType {
        INTEGER, LONG, BOOLEAN, FLOAT, STRING, STRING_SET
    }

    // ==

    /**
     * 保存一个数据
     * @param key
     * @param value
     */
    <T> void put(String key, T value);

    /**
     * 保存一个Map集合(只能是 Integer,Long,Boolean,Float,String,Set)
     * @param map
     */
    <T> void putAll(Map<String, T> map);

    /**
     * 保存一个List集合
     * @param key
     * @param list
     */
    void putAll(String key, List<String> list);

    /**
     * 保存一个List集合,并且自定义保存顺序
     * @param key
     * @param list
     * @param comparator
     */
    void putAll(String key, List<String> list, Comparator<String> comparator);

    /**
     * 根据key取出一个数据
     * @param key
     */
    <T> T get(String key, DataType type);

    /**
     * 取出全部数据
     * @return
     */
    Map<String, ?> getAll();

    /**
     * 取出一个List集合
     * @param key
     * @return
     */
    List<String> getAll(String key);

    /**
     * 移除一个数据
     * @param key
     * @return
     */
    void remove(String key);

    /**
     * 移除一个集合的数据
     * @param keys
     * @return
     */
    void removeAll(List<String> keys);

    /**
     * 移除一个数组的数据
     * @param keys
     * @return
     */
    void removeAll(String[] keys);

    /**
     * 是否存在key
     * @return
     */
    boolean contains(String key);

    /**
     * 清除全部数据
     */
    void clear();

    // ==
    /**
     * 获取int类型的数据
     * @return
     */
    int getInt(String key);

    /**
     * 获取Float类型的数据
     * @param key
     * @return
     */
    float getFloat(String key);

    /**
     * 获取long类型的数据
     * @param key
     * @return
     */
    long getLong(String key);


    /**
     * 获取boolean类型的数据
     * @param key
     * @return
     */
    boolean getBoolean(String key);

    /**
     * 获取String类型的数据
     * @param key
     * @return
     */
    String getString(String key);

    /**
     * 获取Set类型的数据
     * @param key
     * @return
     */
    Set<String> getSet(String key);
}
