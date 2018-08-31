package t.app.info.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * detail: SD卡相关辅助类
 * Created by Ttt
 */
public final class SDCardUtils {

	private SDCardUtils() {
	}
    
	// == ----------------------------------------- ==
    
	/**
	 * 判断SDCard是否正常挂载
	 * @return
	 */
	public static boolean isSDCardEnable() {
		// android.os.Environment
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取SD卡路径（File对象）
	 * @return
	 */
	public static File getSDCardFile() {
		return Environment.getExternalStorageDirectory();
	}
	
	/**
	 * 获取SD卡路径（无添加  -> / -> File.separator）
	 * @return
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	// ==

	/**
	 * 判断 SD 卡是否可用
	 * @return true : 可用, false : 不可用
	 */
	public static boolean isSDCardEnablePath() {
		return !getSDCardPaths().isEmpty();
	}

	/**
	 * 获取 SD 卡路径
	 * @param removable true : 外置 SD 卡, false : 内置 SD 卡
	 * @return SD 卡路径
	 */
	@SuppressWarnings("TryWithIdenticalCatches")
	public static List<String> getSDCardPaths(final boolean removable) {
		List<String> listPaths = new ArrayList<>();
		try {
			StorageManager storageManager = (StorageManager) DevUtils.getContext().getSystemService(Context.STORAGE_SERVICE);
			Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
			Method getVolumeList = StorageManager.class.getMethod("getVolumeList");
			Method getPath = storageVolumeClazz.getMethod("getPath");
			Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
			Object result = getVolumeList.invoke(storageManager);
			final int length = Array.getLength(result);
			for (int i = 0; i < length; i++) {
				Object storageVolumeElement = Array.get(result, i);
				String path = (String) getPath.invoke(storageVolumeElement);
				boolean res = (Boolean) isRemovable.invoke(storageVolumeElement);
				if (removable == res) {
					listPaths.add(path);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPaths;
	}

	/**
	 * 获取 SD 卡路径
	 * @return SD 卡路径
	 */
	@SuppressWarnings("TryWithIdenticalCatches")
	public static List<String> getSDCardPaths() {
		List<String> listPaths = new ArrayList<>();
		try {
			StorageManager storageManager = (StorageManager) DevUtils.getContext().getSystemService(Context.STORAGE_SERVICE);
			Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
			getVolumePathsMethod.setAccessible(true);
			Object invoke = getVolumePathsMethod.invoke(storageManager);
			listPaths = Arrays.asList((String[]) invoke);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPaths;
	}

	// ==

	/**
	 * 返回对应路径的空间总大小
	 * @param path
	 * @return
	 */
	public static long getAllBlockSize(String path) {
		try {
			// 获取路径的存储空间信息
			StatFs statFs = new StatFs(path);
			// 获取单个数据块的大小(Byte) 
			long blockSize = statFs.getBlockSize();
			// 获取数据块的数量
			long blockCount = statFs.getBlockCount();
			// 返回空间总大小
			return (blockCount * blockSize);
		} catch (Exception e) {
		}
		return -1l;
	}
	
	/**
	 * 返回对应路径的空闲空间(byte 字节大小)
	 * @param path
	 * @return
	 */
	public static long getAvailableBlocks(String path) {
		try {
			// 获取路径的存储空间信息
			StatFs statFs = new StatFs(path);
			// 获取单个数据块的大小(Byte) 
			long blockSize = statFs.getBlockSize();
			// 空闲的数据块的数量
			long freeBlocks = statFs.getAvailableBlocks();
			// 返回空闲空间
			return (freeBlocks * blockSize);
		} catch (Exception e) {
		}
		return -1l;
	}
	
	
	/**
	 * 返回对应路径,已使用的空间大小
	 * @param path
	 * @return
	 */
	public static long getAlreadyBlock(String path) {
		try {
			// 获取路径的存储空间信息
			StatFs statFs = new StatFs(path);
			// 获取单个数据块的大小(Byte) 
			long blockSize = statFs.getBlockSize();
			// 获取数据块的数量
			long blockCount = statFs.getBlockCount();
			// 空闲的数据块的数量
			long freeBlocks = statFs.getAvailableBlocks();
			// 返回空间总大小
			return ((blockCount - freeBlocks)* blockSize);
		} catch (Exception e) {
		}
		return -1l;
	}
	
	/**
	 * 返回对应路径的空间大小信息
	 * @return 返回数据，0 = 总空间大小，1 = 空闲控件大小 ， 2 = 已使用空间大小
	 */
	public static long[] getBlockSizeInfos(String path) {
		try {
			// 获取路径的存储空间信息
			StatFs statFs = new StatFs(path);
			// 获取单个数据块的大小(Byte) 
			long blockSize = statFs.getBlockSize();
			// 获取数据块的数量
			long blockCount = statFs.getBlockCount();
			// 空闲的数据块的数量
			long freeBlocks = statFs.getAvailableBlocks();
			// 计算空间大小信息
			long[] blocks = new long[3];
			blocks[0] = blockSize * blockCount;
			blocks[1] = blockSize * freeBlocks;
			blocks[2] = ((blockCount - freeBlocks)* blockSize);
			// 返回空间大小信息
			return blocks;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取缓存地址
	 * @return
	 */
	public static String getDiskCacheDir() {
		String cachePath;
		if (isSDCardEnable()) { // 判断SDCard是否挂载
			cachePath = DevUtils.getContext().getExternalCacheDir().getPath();
		} else {
			cachePath = DevUtils.getContext().getCacheDir().getPath();
		}
		// 防止不存在目录文件，自动创建
		FileUtils.createFolder(cachePath);
		// 返回文件存储地址
		return cachePath;
	}

	/**
	 * 获取缓存资源地址
	 * @param fPath 文件地址
	 * @return
	 */
	public static File getCacheFile(String fPath) {
		return new File(getCachePath(fPath));
	}

	/**
	 * 获取缓存资源地址
	 * @param fPath 文件地址
	 * @return
	 */
	public static String getCachePath(String fPath){
		// 获取缓存地址
		String cachePath = new File(getDiskCacheDir(), fPath).getAbsolutePath();
		// 防止不存在目录文件，自动创建
		FileUtils.createFolder(cachePath);
		// 返回头像地址
		return cachePath;
	}
}

