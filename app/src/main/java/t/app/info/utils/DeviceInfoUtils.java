package t.app.info.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * detail: 设备信息工具类
 * Created by Ttt
 */
public class DeviceInfoUtils {

    /**
     * 获取设备信息
     * @param dInfoMaps 传入设备信息传出HashMap
     */
    public static void getDeviceInfo(HashMap<String, String> dInfoMaps) {
        // 获取设备信息类的所有申明的字段,即包括public、private和proteced， 但是不包括父类的申明字段。
        Field[] fields = Build.class.getDeclaredFields();
        // 遍历字段
        for (Field field : fields) {
            try {
                // 取消java的权限控制检查
                field.setAccessible(true);

                if (field.getName().toLowerCase().equals("SUPPORTED_ABIS".toLowerCase())) {
                    Object object = field.get(null);
                    // 判断是否数组
                    if (object instanceof String[]) {
                        // 获取类型对应字段的数据，并保存
                        dInfoMaps.put(field.getName().toLowerCase(), Arrays.toString((String[]) object));
                        continue;
                    }
                }
                // 获取类型对应字段的数据，并保存
                dInfoMaps.put(field.getName().toLowerCase(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
    }

    /**
     * BASEBAND-VER
     * 基带版本
     * return String
     */
    public static String getBaseband_Ver() {
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            Version = (String) result;
        } catch (Exception e) {
        }
        return Version;
    }

    /**
     * CORE-VER
     * 内核版本
     * return String
     */
    public static String getLinuxCore_Ver() {
        String kernelVersion = "";
        try {
            Process process = null;
            try {
                process = Runtime.getRuntime().exec("cat /proc/version");
            } catch (Exception e) {
                e.printStackTrace();
            }
            InputStream outs = process.getInputStream();
            InputStreamReader isrout = new InputStreamReader(outs);
            BufferedReader brout = new BufferedReader(isrout, 8 * 1024);

            String result = "";
            String line;
            try {
                while ((line = brout.readLine()) != null) {
                    result += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (result != "") {
                    String Keyword = "version ";
                    int index = result.indexOf(Keyword);
                    line = result.substring(index + Keyword.length());
                    index = line.indexOf(" ");
                    kernelVersion = line.substring(0, index);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

    /**
     * 获取CPU 信息
     * @return
     */
    public static String getCpuInfo() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取 CPU 最大频率（单位KHZ）
     * @return
     */
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
            result = Formatter.formatFileSize(DevUtils.getContext(), Long.parseLong(result.trim()) * 1024) + " Hz";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "unknown";
        }
        return result;
    }

    /**
     * 获取 CPU 最小频率（单位KHZ）
     * @return
     */
    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
            result = Formatter.formatFileSize(DevUtils.getContext(), Long.parseLong(result.trim()) * 1024) + " Hz";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "unknown";
        }
        return result;
    }

    /**
     * 实时获取 CPU 当前频率（单位KHZ）
     * @return
     */
    public static String getCurCpuFreq() {
        String result = "";
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = Formatter.formatFileSize(DevUtils.getContext(), Long.parseLong(text.trim()) * 1024) + " Hz";
        } catch (Exception e) {
            e.printStackTrace();
            result = "unknown";
        }
        return result;
    }

    /**
     * 获取 CPU 几核
     * @return
     */
    public static int getCPUNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 获取屏幕分辨率
     * @return
     */
    public static String getScreenSize() {
        try {
            // 获取分辨率
            int[] whArys = getScreenWidthHeight();
            // 返回分辨率信息
            return whArys[1] + "x" + whArys[0];
        } catch (Exception e){
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * 获取屏幕英寸
     * @return
     */
    public static String getScreenSizeOfDevice() {
        // https://blog.csdn.net/lincyang/article/details/42679589
        try {
            Point point = new Point();
            WindowManager windowManager = (WindowManager) DevUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getRealSize(point);
            DisplayMetrics dm = DevUtils.getContext().getResources().getDisplayMetrics();
            double x = Math.pow(point.x/ dm.xdpi, 2);
            double y = Math.pow(point.y / dm.ydpi, 2);
            double screenInches = Math.sqrt(x + y);
            // 转换大小
            DecimalFormat df = new DecimalFormat("#.0");
            return df.format(screenInches);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * 通过上下文获取屏幕宽度高度
     * @return int[] 0 = 宽度，1 = 高度
     */
    public static int[] getScreenWidthHeight() {
        try {
            WindowManager windowManager = (WindowManager) DevUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                DisplayMetrics dMetrics = DevUtils.getContext().getResources().getDisplayMetrics();
                return new int[] { dMetrics.widthPixels, dMetrics.heightPixels };
            }
            Point point = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                windowManager.getDefaultDisplay().getRealSize(point);
            } else {
                windowManager.getDefaultDisplay().getSize(point);
            }
            return new int[] { point.x, point.y };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[] { 0, 0 };
    }

    // =

    /**
     * 获得 SD 卡总大小
     * @return
     */
    public static String getSDTotalSize() {
        try {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return Formatter.formatFileSize(DevUtils.getContext(), blockSize * totalBlocks);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * 获得 SD 卡剩余容量，即可用大小
     * @return
     */
    public static String getSDAvailableSize() {
        try {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return Formatter.formatFileSize(DevUtils.getContext(), blockSize * availableBlocks);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * 获得机身内存总大小
     * @return
     */
    public static String getRomTotalSize() {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return Formatter.formatFileSize(DevUtils.getContext(), blockSize * totalBlocks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * 获得机身可用内存
     * @return
     */
    public static String getRomAvailableSize() {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return Formatter.formatFileSize(DevUtils.getContext(), blockSize * availableBlocks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    // 内存信息文件地址
    private static final String MEM_INFO_PATH = "/proc/meminfo";
    // 获取内存总大小
    public static final String MEMTOTAL = "MemTotal";
    // 获取可用内存
    public static final String MEMAVAILABLE = "MemAvailable";

    /**
     * 获取总内存大小
     * @return
     */
    public static String getTotalMemory() {
        return getMemInfoIype(MEMTOTAL);
    }

    /**
     * 获取可用内存大小
     * @return
     */
    public static String getMemoryAvailable() {
        return getMemInfoIype(MEMAVAILABLE);
    }

    /**
     * 得到 type info
     * @param type
     * @return
     */
    public static String getMemInfoIype(String type) {
        try {
            FileReader fileReader = new FileReader(MEM_INFO_PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader, 4 * 1024);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                if (str.contains(type)) {
                    break;
                }
            }
            bufferedReader.close();
            /* \\s表示   空格,回车,换行等空白符,
            +号表示一个或多个的意思     */
            String[] array = str.split("\\s+");
            // 获得系统总内存，单位是KB，乘以1024转换为Byte
            long length = Long.valueOf(array[1]).longValue() * 1024;
            return android.text.format.Formatter.formatFileSize(DevUtils.getContext(), length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
