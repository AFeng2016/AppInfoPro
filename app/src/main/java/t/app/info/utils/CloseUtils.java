package t.app.info.utils;

import java.io.Closeable;

/**
 * detail: 关闭工具类 - （关闭IO流等）
 * Created by Blankj
 * http://blankj.com
 * Update to Ttt
 */
public final class CloseUtils {

    private CloseUtils() {
    }

    /**
     * 关闭 IO
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 安静关闭 IO
     * @param closeables closeables
     */
    public static void closeIOQuietly(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
