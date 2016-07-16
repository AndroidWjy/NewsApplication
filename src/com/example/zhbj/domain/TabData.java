package com.example.zhbj.domain;

import java.util.ArrayList;

/**
 * @author Administrator ҳǩ����
 */
public class TabData {
	public int retcode;

	public TabDataLsit data;

	public class TabDataLsit {
		public String more;
		public ArrayList<NewsList> news;
		public ArrayList<topNewsList> topnews;
		public String title;

		@Override
		public String toString() {
			return "TabData [news=" + news + ", topNews=" + topnews
					+ ", title=" + title + "]";
		}

	}

	/**
	 * @author Administrator �����б�
	 */
	public class NewsList {
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;

		@Override
		public String toString() {
			return "NewsList [title=" + title + "]";
		}

	}

	/**
	 * @author Administrator ͷ������
	 */
	public class topNewsList {
		public String id;
		public String topimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;

		@Override
		public String toString() {
			return "topNewsList [title=" + title + "]";
		}

	}

	@Override
	public String toString() {
		return "NewsTabData [data=" + data + "]";
	}

}
