package com.example.zhbj.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	/**
	 * ����MD5����
	 * @param password
	 * @return
	 */
	public static String encoding(String password) {
		try {
			// �õ�MD5�㷨
			MessageDigest md = MessageDigest.getInstance("md5");
			// ��ʼ����
			byte[] digest = md.digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			// �����������
			for (byte b : digest) {
				int i = b & 0xff;// ��ȡ�ֽڵĵͰ�λ��Чֵ
				String hexString = Integer.toHexString(i);// ������תΪ16����

				if (hexString.length() < 2) {
					hexString = "0" + hexString;// �����1λ�Ļ�,��0
				}

				sb.append(hexString);
			}
			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			// �Ҳ��������㷨
			e.printStackTrace();
		}
		return "";
	}
}
