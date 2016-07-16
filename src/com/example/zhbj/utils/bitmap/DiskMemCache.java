package com.example.zhbj.utils.bitmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.zhbj.utils.DiskLruCache;
import com.example.zhbj.utils.DiskLruCache.Editor;
import com.example.zhbj.utils.DiskLruCache.Snapshot;
import com.example.zhbj.utils.MD5Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StatFs;
import android.os.Build.VERSION_CODES;
import android.os.Environment;

/**
 * 缓存到磁盘中
 * 
 * @author Administrator
 * 
 */
public class DiskMemCache {
	Context mContext;
	private final static long MAX_SIZE = 1024 * 1024 * 5;
	private final static int IO_SIZE = 1024 * 8;

	private DiskLruCache mDiskCache;
	private MemoryCache mMemoryCache;

	public DiskMemCache(Context ctx, MemoryCache memoryCache) {
		mMemoryCache = memoryCache;
		mContext = ctx;
		init();
	}

	public void init() {
		// 初始化一个根目录
		File diskFile = getdDiskCacheDir(mContext, "bitmap");
		if (!diskFile.exists()) {
			diskFile.mkdirs();
		}
		try {
			if (getUsableSpace(diskFile) > MAX_SIZE) {
				// 初始化对象
				mDiskCache = DiskLruCache.open(diskFile, 1, 1, MAX_SIZE);
				System.out.println("创建成功！！");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将网络数据写入到磁盘中，然后从磁盘中读取
	 * 
	 * @param url
	 */
	public Bitmap setBitmapFromHttpToDisk(String url, int reqWidth,
			int reqHeight) {
		if (mDiskCache == null) {
			return null;
		}
		try {
			Editor editor = mDiskCache.edit(MD5Utils.encoding(url));
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				if (fromUrlToStream(outputStream, url)) {
					editor.commit();
				} else {
					editor.abort();
				}
				mDiskCache.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//只要存入了就可以取出来
		return getBitmapFromDiskCache(url, reqWidth, reqHeight);
	}

	/**
	 * 从磁盘中取出数据
	 * 
	 * @param url
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public Bitmap getBitmapFromDiskCache(String url, int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
		if (mDiskCache == null) {
			return null;
		}
		try {
			Snapshot snapshot = mDiskCache.get(MD5Utils.encoding(url));
			if (snapshot != null) {
				FileInputStream inputStream = (FileInputStream) snapshot
						.getInputStream(0);
				// 根据图片压缩
				bitmap = BitmapCompress.decodeBitmapFromFd(inputStream.getFD(),
						reqWidth, reqHeight);
				// 将图片设置到内存中
				if (bitmap != null) {
					//System.out.println("添加图片到内存中------");
					mMemoryCache.setBitmapToMemCache(MD5Utils.encoding(url),
							bitmap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 访问网络将流写入指定对象
	 */
	private boolean fromUrlToStream(OutputStream outputStream, String url) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		HttpURLConnection conn = null;
		try {
			URL mUrl = new URL(url);
			conn = (HttpURLConnection) mUrl.openConnection();
			InputStream inputStream = conn.getInputStream();
			// 缓存流
			bis = new BufferedInputStream(inputStream, IO_SIZE);
			bos = new BufferedOutputStream(outputStream, IO_SIZE);
			int len = 0;
			while ((len = bis.read()) != -1) {
				bos.write(len);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (bis != null || bos != null) {
				try {
					bis.close();
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 获取文件目录
	 * 
	 * @param ctx
	 * @param name
	 * @return
	 */
	private File getdDiskCacheDir(Context ctx, String name) {
		String path = "";
		// 当sd卡挂载时
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			path = ctx.getExternalCacheDir().getPath();
			System.out.println("path-------" + path);
		} else {
			// 拿到手机的缓存路径
			path = ctx.getCacheDir().getPath();
		}
		return new File(path + "/" + name);
	}

	/**
	 * 获取最大可用空间
	 * 
	 * @param path
	 * @return
	 */
	public long getUsableSpace(File path) {
		if (Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			return path.getUsableSpace();
		}
		final StatFs stats = new StatFs(path.getPath());
		System.out.println("最大可用块===" + (long) stats.getBlockSize()
				* (long) stats.getAvailableBlocks());
		return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	}
}
