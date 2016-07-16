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
 * ���浽������
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
		// ��ʼ��һ����Ŀ¼
		File diskFile = getdDiskCacheDir(mContext, "bitmap");
		if (!diskFile.exists()) {
			diskFile.mkdirs();
		}
		try {
			if (getUsableSpace(diskFile) > MAX_SIZE) {
				// ��ʼ������
				mDiskCache = DiskLruCache.open(diskFile, 1, 1, MAX_SIZE);
				System.out.println("�����ɹ�����");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����������д�뵽�����У�Ȼ��Ӵ����ж�ȡ
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
		//ֻҪ�����˾Ϳ���ȡ����
		return getBitmapFromDiskCache(url, reqWidth, reqHeight);
	}

	/**
	 * �Ӵ�����ȡ������
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
				// ����ͼƬѹ��
				bitmap = BitmapCompress.decodeBitmapFromFd(inputStream.getFD(),
						reqWidth, reqHeight);
				// ��ͼƬ���õ��ڴ���
				if (bitmap != null) {
					//System.out.println("���ͼƬ���ڴ���------");
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
	 * �������罫��д��ָ������
	 */
	private boolean fromUrlToStream(OutputStream outputStream, String url) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		HttpURLConnection conn = null;
		try {
			URL mUrl = new URL(url);
			conn = (HttpURLConnection) mUrl.openConnection();
			InputStream inputStream = conn.getInputStream();
			// ������
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
	 * ��ȡ�ļ�Ŀ¼
	 * 
	 * @param ctx
	 * @param name
	 * @return
	 */
	private File getdDiskCacheDir(Context ctx, String name) {
		String path = "";
		// ��sd������ʱ
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			path = ctx.getExternalCacheDir().getPath();
			System.out.println("path-------" + path);
		} else {
			// �õ��ֻ��Ļ���·��
			path = ctx.getCacheDir().getPath();
		}
		return new File(path + "/" + name);
	}

	/**
	 * ��ȡ�����ÿռ�
	 * 
	 * @param path
	 * @return
	 */
	public long getUsableSpace(File path) {
		if (Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			return path.getUsableSpace();
		}
		final StatFs stats = new StatFs(path.getPath());
		System.out.println("�����ÿ�===" + (long) stats.getBlockSize()
				* (long) stats.getAvailableBlocks());
		return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	}
}
