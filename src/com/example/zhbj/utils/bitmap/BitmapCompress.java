package com.example.zhbj.utils.bitmap;

 import java.io.FileDescriptor;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
/**
 * ͼƬѹ��
 * @author Administrator
 *
 */
public class BitmapCompress {
	/**
	 * ������Դ�ļ�ѹ��
	 * 
	 * @return
	 */
	public static Bitmap decodeBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// ��ȡ�߽�
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);

	}

	/**
	 * �����ļ�����ѹ��
	 * 
	 * @return
	 */
	public static Bitmap decodeBitmapFromFd(FileDescriptor fd, int reqWidth,
			int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// ��ȡ�߽�
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fd, null, options);
		options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFileDescriptor(fd, null, options);
	}

	/**
	 * ���������
	 * 
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateSampleSize(Options options, int reqWidth,
			int reqHeight) {
		if (reqWidth == 0 || reqHeight == 0) {
			return 1;
		}
		// �õ�ͼƬ�߿�
		final int height = options.outHeight;
		final int width = options.outWidth;
		int sampleSize = 1;
		//���߶ȴ�������߶�
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			//ͬʱ���㼴��
			while (halfHeight / sampleSize >= reqHeight
					&& halfWidth / sampleSize >= reqWidth) {
				sampleSize *= 2;
			}
		}
		return sampleSize;
	}
}
