package com.example.zhbj.domain;

import java.util.ArrayList;

/**
 * @author Administrator 对网络数据进行封装，方便Gson进行解析
 */
public class NewsData {
	public int retcode;
	public ArrayList<NewsMenuData> data;

	@Override
	public String toString() {
		return "NewsData [retcode=" + retcode + ", data=" + data + "]";
	}

	/**
	 * 侧边栏数据
	 */
	public class NewsMenuData {
		public String id;
		public String title;
		public int type;
		public String url;
		public ArrayList<NewsTabData> children;

		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", type=" + type + ", url="
					+ url + ", children=" + children + "]";
		}

	}

	/**
	 * 滚动条数据
	 */
	public class NewsTabData {
		public String id;
		public String title;
		public int type;
		public String url;

		@Override
		public String toString() {
			return "NewsTabData [title=" + title + ", type=" + type + ", url="
					+ url + "]";
		}

	}

}
